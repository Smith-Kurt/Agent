package com.example.agent.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.agent.entity.User;

public interface UserService extends IService<User> {
	User getByPhone(String phone);
	User getByUsername(String username);
	User getByUsernameOrPhone(String usernameOrPhone);
}


