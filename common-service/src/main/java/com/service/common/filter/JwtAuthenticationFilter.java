package com.service.common.filter;

import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.service.common.session.UserSession;
import com.service.common.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = parseToken(request); //토큰추출
		if(token != null && jwtUtil.validateToken(token)) { //토큰검증
			String isBlackList = (String) redisTemplate.opsForValue().get("blacklist:".concat(token));
			if(isBlackList != null) {
				filterChain.doFilter(request, response);
				return;
			};
			
			String userId = jwtUtil.getUserIdFromToken(token); //토큰에서 ID 추출
			String redisKey = "user:" + userId;
			
			UserSession userSession = (UserSession) redisTemplate.opsForValue().get(redisKey); //Redis에 저장된 UserSession 조회
			if(userSession != null) { //세션이 존재하면 로그인 상태 유지
				//UsernamePasswordAuthenticationToken 으로 유저 인증객체 생성
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userSession, null, null);
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				//SecurityContextHolder에 저장 -> 이후 Spring Security 로그인 상태로 인식
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}
		
		filterChain.doFilter(request, response);
	}
	
	//Authorization 헤더에서 Bearer 접두어를 제외한 순수 토큰을 파싱하는 메서드
	private String parseToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if(StringUtils.hasText(header) && header.startsWith("Bearer ")) {
			return header.substring(7);
		}
		return null;
	}

}
