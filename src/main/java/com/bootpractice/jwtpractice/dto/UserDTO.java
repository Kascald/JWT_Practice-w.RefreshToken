package com.bootpractice.jwtpractice.dto;

import com.bootpractice.jwtpractice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter @Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class UserDTO {
	private UUID id;
	private String username;
	private String password;
	private List<String> roleList;

	public UserDTO(User user) {
		this.id = user.getId();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.roleList = user.getRoleList();
	}

}
