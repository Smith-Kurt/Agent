package com.example.agent.dto;

import lombok.Data;

@Data
public class RegisterConfirmRequest {
	private String phone;
	private String username;
	private String password;
	private String code;
}


