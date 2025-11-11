package com.example.agent.dto;

import lombok.Data;

@Data
public class LoginPasswordRequest {
	private String usernameOrPhone; //body为usernameOrPhone
	private String password;
}


