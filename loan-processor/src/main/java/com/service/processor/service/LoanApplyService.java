package com.service.processor.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.common.code.LoanCodeConstant.LoanApplyState;
import com.service.common.entity.LoanMasterEntity;
import com.service.common.repository.LoanMasterRepository;
import com.service.loan.apply.dto.RequestLoanDTO;
import com.service.processor.dto.UserCreditInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanApplyService {
	
	private final LoanMasterRepository loanMasterRepository;
	
	@Transactional
	public String processLoan(RequestLoanDTO requestLoanDTO) {
		String userId = requestLoanDTO.getUserId();	//유저아이디
		Long amount = requestLoanDTO.getAmount();	//대출신청금액
		
		UserCreditInfoDTO userCreditInfoDTO = mockUserCreditInfo(userId);	
		int creditScore = userCreditInfoDTO.getCreditScore();	//신용점수
		Long monthlyIncome = requestLoanDTO.getMonthlyIncome();	//월소득	
		Long userDebt = userCreditInfoDTO.getUserDebt();	//부채
		String loanReason = "APPROVED";
		String LoanState = LoanApplyState.APPROVED.toString();
		
	    if (amount > 10000) {
	    	loanReason =  "REJECTED: 신청 금액 초과";
	    	LoanState = LoanApplyState.REJECT.toString();
	    }

	    if (creditScore < 600) {
	    	loanReason = "REJECTED: 신용 점수 부족";
	    	LoanState = LoanApplyState.REJECT.toString();
	    }

	    if (monthlyIncome < 150) {
	    	loanReason = "REJECTED: 소득 부족";
	    	LoanState = LoanApplyState.REJECT.toString();
	    }

	    double debtRatio = (double) userDebt / monthlyIncome;
	    if (debtRatio > 0.4) {
	    	loanReason = "REJECTED: 부채 비율 초과";
	    	LoanState = LoanApplyState.REJECT.toString();
	    }
	    
	    LoanMasterEntity loanMasterEntity = new LoanMasterEntity();
	    loanMasterEntity.setLoanId(requestLoanDTO.getLoanId());
	    loanMasterEntity.setCreditScore(creditScore);
	    loanMasterEntity.setMonthlyIncome(monthlyIncome);
	    loanMasterEntity.setDept(userDebt);
	    loanMasterEntity.setLoanResultReason(loanReason);
	    loanMasterEntity.setLoanState(LoanState);
	    loanMasterRepository.save(loanMasterEntity);
	    
	    return loanReason;
	}
	
	//임의 데이터
	private UserCreditInfoDTO mockUserCreditInfo(String userId) {
		int creditScore = 200;
		Long debt = (long) 50;
	    // 실제로는 DB에서 사용자 정보 조회
	    return new UserCreditInfoDTO(userId, creditScore, debt); // 신용점수, 월 소득, 기존 부채
	}
}
