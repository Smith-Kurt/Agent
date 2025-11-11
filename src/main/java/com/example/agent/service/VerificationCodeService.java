package com.example.agent.service;

public interface VerificationCodeService {
	void sendRegistrationCode(String phone);
	boolean verifyRegistrationCode(String phone, String code);
	void sendLoginCode(String phone);
	boolean verifyLoginCode(String phone, String code);
}


