package com.service.notification.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="tb_loan_result")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    @Column(name = "user_id", length = 20)
    private String userId;

    @Column(name = "loan_amount", length = 20)
    private Integer amount;

    @Column(name = "result", length = 20)
    private String result;

    @Column(name = "reason", length = 20)
    private String reason;

    @Column(name = "created_at", length = 30)
    private LocalDateTime createdAt;

    @PrePersist
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }
}
