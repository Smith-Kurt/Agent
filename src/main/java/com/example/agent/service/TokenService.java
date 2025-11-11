package com.example.agent.service;

public interface TokenService {
	String generateToken(Long userId);
	Long getUserIdByToken(String token);
	void revokeToken(String token);
}


