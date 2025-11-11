package com.example.agent.dto;

import lombok.Data;

@Data
public class LoginCodeVerifyRequest {
	private String phone;
	private String code;
}


