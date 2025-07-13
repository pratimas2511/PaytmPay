package com.auth.service;

import com.auth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth.repo.AuthUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	@Autowired
	private AuthUserRepository repo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}

	public UserDetails save(AuthUser authUser) {
		return repo.save(authUser);
	}

}
