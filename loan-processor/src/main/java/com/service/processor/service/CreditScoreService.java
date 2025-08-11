package com.service.processor.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import com.service.processor.dto.CreditEvalDTO;

@Service
public class CreditScoreService {
	
	//신용평가 모형
	public Map<String, Object> evaluateCredit(CreditEvalDTO creditEvalDTO){
		Map<String, Object> resMap = new HashMap<String, Object>();
		
		int creditScore = ThreadLocalRandom.current().nextInt(650, 850);
		String creditGrade = getGrade(creditScore);
		String creditProvider = "MockCreditService";
		
        // 현재 날짜와 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // 원하는 형식으로 포맷
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String viewDate = now.format(formatter);
        
        resMap.put("creditScore", String.valueOf(creditScore));
        resMap.put("creditGrade", creditGrade);
        resMap.put("creditProvider", creditProvider);
        resMap.put("viewDate", viewDate);
        
        return resMap;
	}
	
	private String getGrade(int creditScore) {
        if (creditScore >= 800) return "1등급";
        if (creditScore >= 740) return "2등급";
        if (creditScore >= 680) return "3등급";
        if (creditScore >= 620) return "4등급";
        return "5등급";
	}

}
