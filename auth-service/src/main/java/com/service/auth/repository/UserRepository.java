package com.service.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.service.auth.dto.UserEntity;


public interface UserRepository extends JpaRepository<UserEntity, String> {
}