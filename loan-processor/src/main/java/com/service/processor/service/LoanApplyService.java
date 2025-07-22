package com.service.processor.service;

import org.springframework.stereotype.Service;

import com.service.loan.apply.dto.RequestLoanDTO;
import com.service.processor.dto.UserCreditInfoDTO;

@Service
public class LoanApplyService {
	
	
	public String processLoan(RequestLoanDTO requestLoanDTO) {
		String userId = requestLoanDTO.getUserId();	//유저아이디
		Long amount = requestLoanDTO.getAmount();	//대출신청금액
		String purpose = requestLoanDTO.getPurpose();	//대출목적
		
		UserCreditInfoDTO userCreditInfoDTO = mockUserCreditInfo(userId);	
		int creditScore = userCreditInfoDTO.getCreditScore();	//신용점수
		long monthlyIncome = userCreditInfoDTO.getMonthlyIncome();	//월소득	
		long userDebt = userCreditInfoDTO.getUserDebt();	//부채
		
	    if (amount > 10000) {
	        return "REJECTED: 신청 금액 초과";
	    }

	    if (creditScore < 600) {
	        return "REJECTED: 신용 점수 부족";
	    }

	    if (monthlyIncome < 150) {
	        return "REJECTED: 소득 부족";
	    }

	    double debtRatio = (double) userDebt / monthlyIncome;
	    if (debtRatio > 0.4) {
	        return "REJECTED: 부채 비율 초과";
	    }
	    
	    return "APPROVED";
	}
	
	//임의 데이터
	private UserCreditInfoDTO mockUserCreditInfo(String userId) {
	    // 실제로는 DB에서 사용자 정보 조회
	    return new UserCreditInfoDTO(userId, 650, 200, 50); // 신용점수, 월 소득, 기존 부채
	}
}
