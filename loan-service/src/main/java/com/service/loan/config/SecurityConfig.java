package com.service.loan.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.service.common.filter.JwtAuthenticationFilter;
import com.service.common.util.JwtUtil;

import lombok.RequiredArgsConstructor;

@Configuration //Spring 설정 클래스
@EnableWebSecurity //Spring Security의 기본 웹 보안 기능을 활성화
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtUtil jwtUtil;
	private final RedisTemplate<String, Object> redisTemplate;
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        		.cors(Customizer.withDefaults())	//CORS 설정 적용
                .csrf(csrf -> csrf.disable()) //CSRF 보호 비활성화
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/loan/apply/**").permitAll() // 인증 없이 접근 허용
                                .anyRequest().authenticated() // ⬅️ 나머지는 인증 필요
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtUtil, redisTemplate), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000")); // 프론트엔드 주소
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 자격 증명 허용 (쿠키 등)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
