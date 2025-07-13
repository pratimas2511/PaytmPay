package com.wallet.controller;

import java.util.Collections;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import com.wallet.util.JwtUtil;

@RestController
@RequestMapping("/wallet")
public class WalletController {

	@Autowired
	private  WalletRepository repo;
	@Autowired
	private  JwtUtil jwtUtil;

	/* ---------- POST /wallet/topup ---------- */
	@Transactional
	@PostMapping("/topup")
	public ResponseEntity<?> topUp(HttpServletRequest request,
								   @RequestParam double amount) {

		String user = extractUser(request);
		if (amount <= 0)
			return ResponseEntity.badRequest().body("Amount must be positive");

		Wallet w = repo.findByUsername(user)
				.orElseGet(() -> repo.save(new Wallet(0, user, 0.0)));

		w.setAmount(w.getAmount() + amount);
		return ResponseEntity.ok(repo.save(w));
	}

	/* ---------- POST /wallet/transfer ---------- */
	@Transactional
	@PostMapping("/transfer")
	public ResponseEntity<?> transfer(HttpServletRequest request,
									  @RequestParam String to,
									  @RequestParam double amount) {

		String from = extractUser(request);
		if (from.equals(to))
			return ResponseEntity.badRequest().body("Sender and receiver cannot be the same");
		if (amount <= 0)
			return ResponseEntity.badRequest().body("Amount must be positive");

		Wallet sender = repo.findByUsername(from)
				.orElseThrow(() -> new IllegalStateException("Sender wallet not found"));
		if (sender.getAmount() < amount)
			return ResponseEntity.badRequest().body("Insufficient funds");

		Wallet receiver = repo.findByUsername(to)
				.orElseGet(() -> repo.save(new Wallet(0, to, 0.0)));

		sender.setAmount(sender.getAmount() - amount);
		receiver.setAmount(receiver.getAmount() + amount);

		return okBalance(sender);
	}

	/* ---------- GET /wallet/balance ---------- */
	@GetMapping("/balance")
	public ResponseEntity<?> balance(HttpServletRequest request) {
		String user = extractUser(request);
		Wallet w = repo.findByUsername(user)
				.orElseGet(() -> repo.save(new Wallet(0, user, 0.0)));
		return okBalance(w);
	}

	/* ---------- helpers ---------- */
	private String extractUser(HttpServletRequest req) {
		String h = req.getHeader("Authorization");
		if (h == null || !h.startsWith("Bearer ")) throw new RuntimeException("Missing token");
		String user = jwtUtil.validateAndExtractUsername(h.substring(7));
		if (user == null) throw new RuntimeException("Invalid token");
		return user;
	}

	private ResponseEntity<?> okBalance(Wallet w) {
		return ResponseEntity.ok(Collections.singletonMap("balance", w.getAmount()));
	}
}
