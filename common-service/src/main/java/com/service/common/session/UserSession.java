package com.service.common.session;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSession implements Serializable {
	private String id;
	private String username;
	private String email;
	private String phonenumber;
}
