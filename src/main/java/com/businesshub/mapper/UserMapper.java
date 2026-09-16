package com.businesshub.mapper;

import com.businesshub.dto.UserRequestDTO;
import com.businesshub.dto.UserResponseDTO;
import com.businesshub.entity.UserEntity;

public class UserMapper {

	public static UserEntity toEntity(UserRequestDTO dto) {

		UserEntity user = new UserEntity();

		user.setName(dto.getName());
		user.setEmail(dto.getEmail());
		user.setPassword(dto.getPassword());
		user.setRole("USER");

		return user;
	}

	public static UserResponseDTO toResponseDTO(UserEntity user) {

		UserResponseDTO dto = new UserResponseDTO();

		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setEmail(user.getEmail());

		return dto;
	}
}