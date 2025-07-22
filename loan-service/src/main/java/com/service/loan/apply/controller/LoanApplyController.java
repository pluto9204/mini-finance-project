package com.service.loan.apply.controller;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.service.loan.apply.dto.RequestLoanDTO;
import com.service.loan.apply.service.LoanApplyProducer;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/loan/apply")
public class LoanApplyController {
	
	private final LoanApplyProducer loanApplyProducer;
	
	private static final Logger logger = LoggerFactory.getLogger(LoanApplyController.class);
	
	@PostMapping("/requestLoan")
	public Map<String, Object> execute(@RequestBody RequestLoanDTO requestLoanDTO, HttpServletRequest httpServletRequest) {
		Map<String, Object> result = new HashMap<String, Object>();
		
		logger.info("Loan Apply Start" + requestLoanDTO.toString());
		
		result = loanApplyProducer.requestLoan(requestLoanDTO);

		logger.info("Loan Apply End");
		
		return result;
	}
}
