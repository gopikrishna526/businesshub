package com.businesshub.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.businesshub.dto.LoginRequestDTO;
import com.businesshub.dto.LoginResponseDTO;
import com.businesshub.dto.UserRequestDTO;
import com.businesshub.dto.UserResponseDTO;
import com.businesshub.entity.UserEntity;
import com.businesshub.exception.InvalidCredentialsException;
import com.businesshub.exception.UserAlreadyExistsException;
import com.businesshub.mapper.UserMapper;
import com.businesshub.repository.UserRepository;
import com.businesshub.security.JwtService;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public UserResponseDTO createUser(UserRequestDTO dto) {

		if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("User already exists with email: " + dto.getEmail());
		}

		UserEntity user = UserMapper.toEntity(dto);

		user.setPassword(passwordEncoder.encode(dto.getPassword()));

		UserEntity savedUser = userRepository.save(user);

		return UserMapper.toResponseDTO(savedUser);
	}

	public LoginResponseDTO login(LoginRequestDTO dto) {

		Optional<UserEntity> userOptional = userRepository.findByEmail(dto.getEmail());

		if (userOptional.isEmpty()) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		UserEntity user = userOptional.get();

		if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid email or password");
		}

		String token = jwtService.generateToken(user.getEmail());

		return new LoginResponseDTO(user.getId(), user.getName(), user.getEmail(), token);
	}
}