package com.service.notification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.service.notification.dto.LoanResultEntity;


public interface LoanResultRepository extends JpaRepository<LoanResultEntity, String>  {

}
