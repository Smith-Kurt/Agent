package com.example.agent.service.impl;

import com.example.agent.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
	private static final String TOKEN_PREFIX = "auth:token:";
	private static final Duration TOKEN_TTL = Duration.ofHours(72);
	private final StringRedisTemplate stringRedisTemplate;

	@Override
	public String generateToken(Long userId) {
		String token = UUID.randomUUID().toString().replace("-", "");
		stringRedisTemplate.opsForValue().set(TOKEN_PREFIX + token, String.valueOf(userId), TOKEN_TTL);
		return token;
	}

	@Override
	public Long getUserIdByToken(String token) {
		String val = stringRedisTemplate.opsForValue().get(TOKEN_PREFIX + token);
		if (val == null) {
			return null;
		}
		try {
			return Long.parseLong(val);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	@Override
	public void revokeToken(String token) {
		stringRedisTemplate.delete(TOKEN_PREFIX + token);
	}
}


