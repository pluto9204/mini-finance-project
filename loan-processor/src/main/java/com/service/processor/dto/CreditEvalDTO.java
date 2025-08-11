package com.service.processor.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditEvalDTO {
	private String registerNo;
	private String sex;
	private String name;
	private String phoneNumber;
}
