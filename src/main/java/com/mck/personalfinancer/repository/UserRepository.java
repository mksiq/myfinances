package com.mck.personalfinancer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mck.personalfinancer.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	
	boolean existsByEmail(String email);
}
