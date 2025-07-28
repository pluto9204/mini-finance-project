package com.service.loan.apply.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLoanDTO {
	private Long loanId;
	private String userId;
	private long monthlyIncome;
	private long amount;
	private String purpose;
}
