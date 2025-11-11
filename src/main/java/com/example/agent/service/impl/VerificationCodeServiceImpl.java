package com.example.agent.service.impl;

import com.example.agent.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {
	private static final String REGISTER_CODE_PREFIX = "register:code:";
	private static final String LOGIN_CODE_PREFIX = "login:code:";
	private static final Duration CODE_TTL = Duration.ofMinutes(5);
	private final StringRedisTemplate stringRedisTemplate;
	private final SecureRandom secureRandom = new SecureRandom();

	@Override
	public void sendRegistrationCode(String phone) {
		String code = generateCode();
		stringRedisTemplate.opsForValue().set(REGISTER_CODE_PREFIX + phone, code, CODE_TTL);
		log.info("Registration code for phone {}: {}", phone, code);
	}

	@Override
	public boolean verifyRegistrationCode(String phone, String code) {
		String key = REGISTER_CODE_PREFIX + phone;
		String cached = stringRedisTemplate.opsForValue().get(key);
		if (cached != null && cached.equals(code)) {
			stringRedisTemplate.delete(key);
			return true;
		}
		return false;
	}

	@Override
	public void sendLoginCode(String phone) {
		String code = generateCode();
		stringRedisTemplate.opsForValue().set(LOGIN_CODE_PREFIX + phone, code, CODE_TTL);
		log.info("Login code for phone {}: {}", phone, code);
	}

	@Override
	public boolean verifyLoginCode(String phone, String code) {
		String key = LOGIN_CODE_PREFIX + phone;
		String cached = stringRedisTemplate.opsForValue().get(key);
		if (cached != null && cached.equals(code)) {
			stringRedisTemplate.delete(key);
			return true;
		}
		return false;
	}

	private String generateCode() {
		int num = secureRandom.nextInt(900000) + 100000;
		return String.valueOf(num);
	}
}


