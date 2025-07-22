package com.service.auth.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "tb_login_user")
@NoArgsConstructor
@AllArgsConstructor
public class LoginEntity {

	@Id
	@Column(name = "user_id", nullable = false, length = 20)
	private String id;

	@Column(name = "user_password", nullable = false, length = 20)
	private String password;
}
