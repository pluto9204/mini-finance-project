package com.service.auth.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_auth_user")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	@Id
	@Column(name = "user_id", nullable = false, length = 20)
	private String id;
	
	@Column(name = "user_password", nullable = false, length = 100)
	private String password;
	
	@Column(name = "user_name", nullable = false, length = 20)
	private String username;
	
	@Column(name = "user_email", length = 30)
	private String email;
	
	@Column(name = "user_phone_number", length = 20)
	private String phone_number;
}
