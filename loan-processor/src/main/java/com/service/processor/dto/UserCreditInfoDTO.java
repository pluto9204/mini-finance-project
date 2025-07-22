package com.service.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreditInfoDTO {
	private String userId;
	private int creditScore;
	private long monthlyIncome;
	private long userDebt;
}
