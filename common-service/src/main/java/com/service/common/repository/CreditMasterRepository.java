package com.service.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.common.entity.CreditMasterEntity;

public interface CreditMasterRepository extends JpaRepository<CreditMasterEntity, Long>  {

}
