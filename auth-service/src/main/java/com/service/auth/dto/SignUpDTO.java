package com.service.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
	private String id;
	private String password;
	private String username;
	private String email;
	private String phonenumber;
	

}
