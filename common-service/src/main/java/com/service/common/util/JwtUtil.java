package com.service.common.util;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;


@Component
public class JwtUtil {
	
	//토큰의 secret 키
	@Value("${jwt.secret}")
	private String secret;

	//토큰 만료시간 설정
    @Value("${jwt.expiration}")
    private long expiration;

    // 기존 토큰 생성 메서드
    public String generateToken(String userId) {
        return Jwts.builder()
            .setSubject(userId) // 토큰의 사용자 ID 설정
            .setIssuedAt(new Date()) // 토큰 발급 시간
            .setExpiration(new Date(System.currentTimeMillis() + expiration)) // 만료 시간 설정
            .signWith(SignatureAlgorithm.HS256, secret) // HMAC SHA-256 알고리즘 + secret 키로 서명
            .compact(); // 최종적으로 문자열 형태의 JWT 생성
    }
    
    // 토큰에서 사용자 ID(Subject) 추출
    public String getUserIdFromToken(String token) {
        return getClaims(token).getBody().getSubject();
    }
    
    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
        	/*
        	 * SignatureException : JWT의 서명이 유효하지 않은 경우 발생합니다.
        	 * MalformedJwtException : JWT 형식이 잘못되어 파싱할 수 없을 때 발생합니다.
        	 * UnsupportedJwtException : 지원하지 않는 JWT 형식이거나 기능을 사용할 경우 발생합니다.
        	 * IllegalArgumentException : null 또는 빈 문자열 토큰을 파싱하려 할 때 발생합니다.
        	 */
            System.out.println("Invalid JWT signature or malformed token");
        } catch (ExpiredJwtException e) {
        	//ExpiredJwtException : 토큰의 유효 시간이 만료된 경우 발생합니다.
            System.out.println("Expired JWT token");
        }
        return false;
    }
    
    public Date getExpiration(String token) {
        return ((Claims) getClaims(token)).getExpiration();
    }

    // 토큰에서 Claims 추출 (내부용)
	private Jws<Claims> getClaims(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .parseClaimsJws(token);
    }
}
