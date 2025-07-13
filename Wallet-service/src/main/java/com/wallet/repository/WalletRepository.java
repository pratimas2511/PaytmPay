package com.wallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wallet.model.Wallet;
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Integer> {
	
	Optional<Wallet> findByUsername(String username);

}
