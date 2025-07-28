package com.service.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_loan_master")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanMasterEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "user_id", length = 20)
    private String userId;

    @Column(name = "loan_state", length = 10)
    private String loanState;
    
    @Column(name = "loan_Result_Reason", length = 20)
    private String loanResultReason;

    @Column(name = "purpose", length = 20)
    private String purpose;

    @Column(name = "monthly_income", length = 20)
    private Long monthlyIncome;
    
    @Column(name = "credit_score", length = 10)
    private int creditScore;

    @Column(name = "dept", length = 20)
    private Long dept;
    
    @Column(name = "loan_amount", length = 20)
    private Long loanAmount;

    @Column(name = "created_at", length = 30)
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

}
