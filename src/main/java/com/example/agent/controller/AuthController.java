package com.example.agent.controller;

import com.example.agent.common.ApiResponse;
import com.example.agent.dto.*;
import com.example.agent.entity.LoginLog;
import com.example.agent.entity.User;
import com.example.agent.service.LoginLogService;
import com.example.agent.service.TokenService;
import com.example.agent.service.UserService;
import com.example.agent.service.VerificationCodeService;
import com.example.agent.util.IpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

	private final UserService userService;
	private final LoginLogService loginLogService;
	private final VerificationCodeService verificationCodeService;
	private final TokenService tokenService;
	private final PasswordEncoder passwordEncoder;

	@PostMapping("/register/code")
	public ApiResponse<Void> sendRegisterCode(@RequestBody RegisterCodeRequest req) {
		if (req == null || !StringUtils.hasText(req.getPhone())) {
			return ApiResponse.fail("手机号不能为空");
		}
		User exists = userService.getByPhone(req.getPhone());
		if (exists != null) {
			return ApiResponse.fail("该手机号已注册");
		}
		verificationCodeService.sendRegistrationCode(req.getPhone());
		return ApiResponse.okMessage("验证码已发送（控制台打印）");
	}

	@PostMapping("/register")
	public ApiResponse<Void> register(@RequestBody RegisterConfirmRequest req) {
		if (req == null || !StringUtils.hasText(req.getPhone()) || !StringUtils.hasText(req.getUsername())
			|| !StringUtils.hasText(req.getPassword()) || !StringUtils.hasText(req.getCode())) {
			return ApiResponse.fail("参数不完整");
		}
		if (!verificationCodeService.verifyRegistrationCode(req.getPhone(), req.getCode())) {
			return ApiResponse.fail("验证码错误或已过期");
		}
		if (userService.getByPhone(req.getPhone()) != null) {
			return ApiResponse.fail("该手机号已注册");
		}
		if (userService.getByUsername(req.getUsername()) != null) {
			return ApiResponse.fail("用户名已存在");
		}
		String hash = passwordEncoder.encode(req.getPassword());
		User user = User.builder()
			.username(req.getUsername())
			.passwordHash(hash)
			.status(1)
			.phone(req.getPhone())
			.role("user")
			.build();
		userService.save(user);
		return ApiResponse.okMessage("注册成功");
	}

	@PostMapping("/login/password")
	public ApiResponse<Map<String, String>> loginWithPassword(@RequestBody LoginPasswordRequest req, HttpServletRequest request) {
		if (req == null || !StringUtils.hasText(req.getUsernameOrPhone()) || !StringUtils.hasText(req.getPassword())) {
			return ApiResponse.fail("参数不完整");
		}
		User user = userService.getByUsernameOrPhone(req.getUsernameOrPhone());
		if (user == null || user.getStatus() != null && user.getStatus() == 0) {
			return ApiResponse.fail("用户不存在或已禁用");
		}
		if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
			return ApiResponse.fail("用户名或密码错误");
		}
        LoginLog loginLog = LoginLog.builder()
			.userId(user.getId())
			.ipAddress(IpUtil.getClientIp(request))
			.loginTime(LocalDateTime.now())
			.build();
		loginLogService.save(loginLog);
		String token = tokenService.generateToken(user.getId());
		return ApiResponse.ok(Map.of("token", token));
	}

	@PostMapping("/login/code/send")
	public ApiResponse<Void> sendLoginCode(@RequestBody LoginCodeSendRequest req) {
		if (req == null || !StringUtils.hasText(req.getPhone())) {
			return ApiResponse.fail("手机号不能为空");
		}
		User user = userService.getByPhone(req.getPhone());
		if (user == null) {
			return ApiResponse.fail("该手机号未注册");
		}
		verificationCodeService.sendLoginCode(req.getPhone());
		return ApiResponse.okMessage("验证码已发送（控制台打印）");
	}

	@PostMapping("/login/code/verify")
	public ApiResponse<Map<String, String>> loginWithCode(@RequestBody LoginCodeVerifyRequest req, HttpServletRequest request) {
		if (req == null || !StringUtils.hasText(req.getPhone()) || !StringUtils.hasText(req.getCode())) {
			return ApiResponse.fail("参数不完整");
		}
		User user = userService.getByPhone(req.getPhone());
		if (user == null || user.getStatus() != null && user.getStatus() == 0) {
			return ApiResponse.fail("用户不存在或已禁用");
		}
		if (!verificationCodeService.verifyLoginCode(req.getPhone(), req.getCode())) {
			return ApiResponse.fail("验证码错误或已过期");
		}
		LoginLog loginLog = LoginLog.builder()
			.userId(user.getId())
			.ipAddress(IpUtil.getClientIp(request))
			.loginTime(LocalDateTime.now())
			.build();
		loginLogService.save(loginLog);
		String token = tokenService.generateToken(user.getId());
		return ApiResponse.ok(Map.of("token", token));
	}
}


