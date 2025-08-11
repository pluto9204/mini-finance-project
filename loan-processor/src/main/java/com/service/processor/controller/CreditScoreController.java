package com.service.processor.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.processor.dto.CreditEvalDTO;
import com.service.processor.service.CreditScoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/credit")
public class CreditScoreController {
	
	private final CreditScoreService creditScoreService; 
	
	//신용평가 전문 처리
	@PostMapping("/evaluateCreditScore")
	public String evaluateCredit(@RequestBody String requestStr) {
		
		if(requestStr == null || requestStr.length() != 48) {
			return "요청부에러";
		}
		
		String loanId = requestStr.substring(0, 12).trim();
		String registerNo = requestStr.substring(12, 25).trim();
		String sex = requestStr.substring(25, 26).trim();
		String name = requestStr.substring(26, 35).trim();
		String phoneNumber = requestStr.substring(35,48).trim();
		
		CreditEvalDTO creditEvalDTO =  new CreditEvalDTO();
		creditEvalDTO.setName(name);
		creditEvalDTO.setPhoneNumber(phoneNumber);
		creditEvalDTO.setRegisterNo(registerNo);
		creditEvalDTO.setSex(sex);
		
		Map<String, Object> resMap = creditScoreService.evaluateCredit(creditEvalDTO);
		
		String creditScore = resMap.get("creditScore").toString();
		String creditGrade = resMap.get("creditGrade").toString();
		String creditProvider = resMap.get("creditProvider").toString();
		String viewDate = resMap.get("viewDate").toString();
		
		return buildResponseFixedLength(creditScore, creditGrade, creditProvider, viewDate);
	}
	
	private String buildResponseFixedLength(String creditScore, String creditGrade, String creditProvider, String viewDate) {
	    return String.format(
	        "%2s%-05d%-6s%-10s%-8s",
	        "RS",
	        creditScore,
	        creditGrade,
	        creditProvider,
	        viewDate
	    );
	}
}
