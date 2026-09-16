package com.businesshub.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.businesshub.dto.LoginRequestDTO;
import com.businesshub.dto.LoginResponseDTO;
import com.businesshub.dto.UserRequestDTO;
import com.businesshub.dto.UserResponseDTO;
import com.businesshub.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping
	public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO dto) {

		UserResponseDTO response = userService.createUser(dto);

		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {

		LoginResponseDTO response = userService.login(dto);

		return ResponseEntity.ok(response);
	}
}