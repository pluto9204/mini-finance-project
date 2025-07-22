package com.service.auth.service;

import java.lang.System.Logger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.service.auth.dto.LoginDTO;
import com.service.auth.dto.SignUpDTO;
import com.service.auth.dto.UserEntity;
import com.service.auth.repository.UserRepository;
import com.service.auth.util.StringUtil;
import com.service.common.session.UserSession;
import com.service.common.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    
    private final RedisTemplate redisTemplate;
    
    private final PasswordEncoder passwordEncoder;
    
    private final JwtUtil jwtUtil;

    public Map<String, Object> signUp(SignUpDTO req) {
        Map<String, Object> resultMap = new HashMap<>();

        try {
	        String id = req.getId();
	        String username = req.getUsername();
	        String password = req.getPassword();
	        String email = req.getEmail();
	        String phone_number = req.getPhonenumber();
	        
	        String err_msg = "";
	        if(StringUtil.isEmpty(id)) { err_msg = "아이디를 입력해 주세요."; } 
	        else if (StringUtil.isEmpty(username)) { err_msg = "이름를 입력해 주세요."; }
	        else if (StringUtil.isEmpty(password)) { err_msg = "패스워드를 입력해 주세요."; }
	        else if (StringUtil.isEmpty(email)) { err_msg = "이메일를 입력해 주세요."; }
	        else if (StringUtil.isEmpty(phone_number)) { err_msg = "휴대폰 번호를 입력해 주세요."; }
	        if(!err_msg.equals("")) {
	        	return buildResponse("01", err_msg);
	        }

	        if (userRepository.findById(id).isPresent()) {
	        	return buildResponse("02", "이미 있는 사용자입니다.");
	        } else {
	            String passwordEnc = passwordEncoder.encode(password);
	            userRepository.save(new UserEntity(id, passwordEnc, username, email, phone_number));

	        	return buildResponse("00", "회원가입 완료.");
	        }
        } catch(Exception e) {
        	e.printStackTrace();
        	return buildResponse("99", "시스템 에러 발생");
        }
    }

    public Map<String, Object> login(LoginDTO req) {
        Map<String, Object> resultMap = new HashMap<>();

        String id = req.getId();
        String password = req.getPassword();

        // ✅ 1. 파라미터 유효성 검증
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(password)) {
            return buildResponse("01", "아이디 또는 비밀번호를 입력해주세요.");
        }

        try {
            // ✅ 2. 사용자 조회
            Optional<UserEntity> userOpt = userRepository.findById(id);
            if (userOpt.isEmpty()) {
                return buildResponse("02", "존재하지 않는 아이디입니다.");
            }

            UserEntity user = userOpt.get();

            // ✅ 3. 비밀번호 검증
            if (!passwordEncoder.matches(password, user.getPassword())) {
                return buildResponse("03", "패스워드가 일치하지 않습니다.");
            }

            // ✅ 4. JWT 발급
            String token = jwtUtil.generateToken(user.getId());

            // ✅ 5. Redis 세션 저장 (스레드 안전)
            UserSession userSession = new UserSession();
            userSession.setId(id);
            userSession.setUsername(user.getUsername());
            userSession.setEmail(user.getEmail());
            userSession.setPhonenumber(user.getPhone_number());

            redisTemplate.opsForValue().set("user:" + id, userSession, 30, TimeUnit.MINUTES);

            // ✅ 6. 성공 응답
            resultMap.put("res_code", "00");
            resultMap.put("res_message", "로그인 되었습니다.");
            resultMap.put("token", token);
            resultMap.put("user_name", user.getUsername());
            return resultMap;

        } catch (Exception e) {
            // ✅ 7. 예외 처리
            e.printStackTrace();
            return buildResponse("99", "시스템 오류가 발생했습니다.");
        }
    }
    
    public Map<String, Object> logout(HttpServletRequest req) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
    	
    	String token = parseToken(req);
    	if(token != null && jwtUtil.validateToken(token)) {
    		Date expiration = jwtUtil.getExpiration(token);
        	long now = System.currentTimeMillis();
        	long remainingTime = expiration.getTime() - now;
        	
        	redisTemplate.opsForValue().set("blacklist:" + token, "logout", remainingTime, TimeUnit.MINUTES);
        	
        	return buildResponse("00", "로그아웃되었습니다.");
    	} else {
    		return buildResponse("01", "유효하지 않은 토큰입니다.");
    	}
    	
    }
    
    private Map<String, Object> buildResponse(String resCode, String resMessage) {
        Map<String, Object> response = new HashMap<>();
        response.put("res_code", resCode);
        response.put("res_message", resMessage);
        return response;
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