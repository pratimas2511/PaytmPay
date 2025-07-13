package com.tx.controller;

import jakarta.servlet.http.HttpServletRequest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tx.service.TransactionService;
import com.tx.repo.TransactionRepo;

@RestController
@RequestMapping("/transaction")

public class TransactionController {

    @Autowired
    private  TransactionService service;
    @Autowired
    private  TransactionRepo repo;

    /* base URL of wallet‑service, injected from properties or env */
    @Value("${wallet.base-url}")
    private String walletBaseUrl;

    /* -------- POST /transaction/send -------- */
    @PostMapping("/send")
    public ResponseEntity<?> sendMoney(HttpServletRequest request,
                                       @RequestParam String to,
                                       @RequestParam double amount) {

        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Amount must be positive");
        }
        if (request.getUserPrincipal().getName().equals(to)) {
            return ResponseEntity.badRequest().body("Sender and receiver cannot be the same");
        }

        service.executeTransfer(request, to, amount, walletBaseUrl);
        return ResponseEntity.ok("Transfer successful");
    }

    /* -------- GET /transaction/history -------- */
    @GetMapping("/history")
    public ResponseEntity<?> history(HttpServletRequest request) {
        String user = request.getUserPrincipal().getName();
        return ResponseEntity.ok(repo.findBySender(user));
    }
}
