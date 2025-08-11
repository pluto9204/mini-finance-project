package com.service.processor.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.service.common.code.LoanCodeConstant.LoanApplyState;
import com.service.common.entity.LoanMasterEntity;
import com.service.common.repository.LoanMasterRepository;
import com.service.loan.apply.dto.RequestLoanDTO;
import com.service.processor.dto.UserDeptInfoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoanApplyService {
	
	private final LoanMasterRepository loanMasterRepository;
	
	private final CreditScoreTxService creditScoreTxService;
	
	@Transactional(rollbackFor = Exception.class)
	public String processLoan(RequestLoanDTO requestLoanDTO) throws Exception {
		String loanReason = "APPROVED";
		
		String loanId = String.valueOf(requestLoanDTO.getLoanId()); //대출아이디
		String userId = requestLoanDTO.getUserId();	//유저아이디
		Long amount = requestLoanDTO.getAmount();	//대출신청금액
		
		UserDeptInfoDTO userCreditInfoDTO = mockUserCreditInfo(userId);
		Long monthlyIncome = requestLoanDTO.getMonthlyIncome();	//월소득	
		Long userDebt = userCreditInfoDTO.getUserDebt();	//부채
		String loanState = LoanApplyState.APPROVED.toString();
		
		String requestStr = buildRequestFixedLength(loanId, "9201011234567", "1", "테스트", "01012341234");
	
		String responseStr = creditScoreTxService.callCreditScore(requestLoanDTO.getLoanId(), userId, requestStr);
		
		String resIdx = responseStr.substring(0, 2).trim();
		int creditScore = Integer.valueOf(responseStr.substring(2, 7).trim()) ;
		String creditGrade = responseStr.substring(7, 13).trim();
		String creditProvider = responseStr.substring(13, 23).trim();
		String viewDate = responseStr.substring(23, 31).trim();
		
		if(!"RS".equalsIgnoreCase(resIdx)) {
			throw new Exception("신용전문응답에러");
		}
		
	    if (amount > 10000) {
	    	loanReason =  "REJECTED: 신청 금액 초과";
	    	loanState = LoanApplyState.REJECT.toString();
	    }

	    if (creditScore < 600) {
	    	loanReason = "REJECTED: 신용 점수 부족";
	    	loanState = LoanApplyState.REJECT.toString();
	    }
	    
	    if("4등급".equals(creditGrade) || "5등급".equals(creditGrade)) {
	    	loanReason = "REJECTED: 신용 등급 부족";
	    	loanState = LoanApplyState.REJECT.toString();
	    }

	    if (monthlyIncome < 150) {
	    	loanReason = "REJECTED: 소득 부족";
	    	loanState = LoanApplyState.REJECT.toString();
	    }

	    double debtRatio = (double) userDebt / monthlyIncome;
	    if (debtRatio > 0.4) {
	    	loanReason = "REJECTED: 부채 비율 초과";
	    	loanState = LoanApplyState.REJECT.toString();
	    }
	    
	    LoanMasterEntity loanMasterEntity = loanMasterRepository.findById(requestLoanDTO.getLoanId())
	    	    .orElse(new LoanMasterEntity());
	    loanMasterEntity.setLoanId(requestLoanDTO.getLoanId());
	    loanMasterEntity.setCreditScore(creditScore);
	    loanMasterEntity.setMonthlyIncome(monthlyIncome);
	    loanMasterEntity.setDept(userDebt);
	    loanMasterEntity.setLoanResultReason(loanReason);
	    loanMasterEntity.setLoanState(loanState);
	    loanMasterRepository.save(loanMasterEntity);	
	    
	    return loanReason;
	}
	
	//임의 데이터
	private UserDeptInfoDTO mockUserCreditInfo(String userId) {
		Long debt = (long) 50;
	    // 실제로는 DB에서 사용자 정보 조회
	    return new UserDeptInfoDTO(userId, debt); // 신용점수, 월 소득, 기존 부채
	}
	
	private String buildRequestFixedLength(String loanId, String registerNo, String sex, String name, String phoneNumber) {
	    return String.format("%2s%-10s%-13s%-1s%-10s%-12s",
    		"RQ",
    		loanId,
    		registerNo,
    		sex,
    		name,
    		phoneNumber
	    );	
	}
}
