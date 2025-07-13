package com.auth.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.auth.model.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Integer>{
	Optional<AuthUser>findByUsername(String username);
}
