package com.service.auth.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.auth.dto.LoginDTO;
import com.service.auth.dto.SignUpDTO;
import com.service.auth.repository.UserRepository;
import com.service.auth.service.AuthService;
import com.service.common.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(HttpServletRequest request, @RequestBody SignUpDTO req){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = authService.signUp(req);
		
		return ResponseEntity.ok(resultMap);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(HttpServletRequest request, @RequestBody LoginDTO req){
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = authService.login(req);
		
		return ResponseEntity.ok(resultMap);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap = authService.logout(request);
		
		return ResponseEntity.ok(resultMap);
	}
}
