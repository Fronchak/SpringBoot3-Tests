package com.fronchak.petshopv1.services;

import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fronchak.petshopv1.dtos.user.TokenOutputDTO;
import com.fronchak.petshopv1.dtos.user.UserLoginDTO;
import com.fronchak.petshopv1.dtos.user.UserRegisterDTO;
import com.fronchak.petshopv1.entities.User;
import com.fronchak.petshopv1.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AuthService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Transactional
	public TokenOutputDTO register(UserRegisterDTO inputDTO) {
		User user = new User();
		user.setEmail(inputDTO.getEmail());
		user.setPassword(passwordEncoder.encode(inputDTO.getPassword()));
		user = userRepository.save(user);
		HashMap<String, Object> map = new HashMap<>();
		map.put("roles", user.getRoles().stream().map((role) -> role.getAuthority()).collect(Collectors.toList()));
		String token = jwtService.generateToken(map, user);
		return new TokenOutputDTO(token);
	}
	
	@Transactional(readOnly = true)
	public TokenOutputDTO login(UserLoginDTO dto) {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
		User user = userRepository.findByEmail(dto.getEmail())
				.orElseThrow(() -> new EntityNotFoundException("User not found"));
		HashMap<String, Object> map = new HashMap<>();
		map.put("roles", user.getRoles().stream().map((role) -> role.getAuthority()).collect(Collectors.toList()));
		String token = jwtService.generateToken(map, user);
		return new TokenOutputDTO(token);
	}
}
