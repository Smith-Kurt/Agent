package com.example.agent.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.agent.entity.User;
import com.example.agent.mapper.UserMapper;
import com.example.agent.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
	@Override
	public User getByPhone(String phone) {
		return lambdaQuery().eq(User::getPhone, phone).one();
	}

	@Override
	public User getByUsername(String username) {
		return lambdaQuery().eq(User::getUsername, username).one();
	}

	@Override
	public User getByUsernameOrPhone(String usernameOrPhone) {
		LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
		wrapper.eq(User::getUsername, usernameOrPhone).or().eq(User::getPhone, usernameOrPhone);
		return getOne(wrapper);
	}
}


