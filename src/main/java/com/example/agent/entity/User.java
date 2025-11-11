package com.example.agent.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("`user`")
public class User {
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	private String username;

	@TableField("create_at")
	private LocalDateTime createAt;

	@TableField("update_at")
	private LocalDateTime updateAt;

	@TableField("password_hash")
	private String passwordHash;

	private Integer status;

	private String phone;

	private String email;

	private String role;
}


