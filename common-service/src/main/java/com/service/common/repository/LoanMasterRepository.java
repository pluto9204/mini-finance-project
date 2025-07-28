package com.service.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.common.entity.LoanMasterEntity;

public interface LoanMasterRepository extends JpaRepository<LoanMasterEntity, Long> {

}
