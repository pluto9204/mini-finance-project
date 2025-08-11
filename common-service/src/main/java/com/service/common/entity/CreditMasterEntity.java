package com.service.common.entity;

import java.time.LocalDateTime;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="tb_credit_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditMasterEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "credit_id", nullable = false)
	private Long creditId;
	
	@Column(name = "loan_id")
    private Long loanId;
	
    @Column(name = "user_id", length = 20)
    private String userId;
    
    @Lob
    @Column(name = "request_payload", columnDefinition = "CLOB")
    private String requestPayload;

    @Lob
    @Column(name = "response_payload", columnDefinition = "CLOB")
    private String responsePayload;
    
    @Column(name = "request_timstamp")
    @CreationTimestamp
    private LocalDateTime requestTimestamp;
    
    @Column(name = "response_timstamp")
    private LocalDateTime reponseTimestamp;
}
