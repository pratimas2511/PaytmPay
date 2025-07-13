package com.tx.service;


import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import com.tx.model.Transaction;
import com.tx.repo.TransactionRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service

public class TransactionService {

    @Autowired
    private  RestTemplate restTemplate;
    @Autowired
    private  TransactionRepo repo;

    /** Executes transfer via wallet‑service then records it locally. */
    @Transactional
    public void executeTransfer(HttpServletRequest request,
                                String toUser,
                                double amount,
                                String walletServiceBaseUrl) {

        /* ---------- 1. forward call to wallet‑service ---------- */
        String url = walletServiceBaseUrl + "/wallet/transfer"
                + "?to=" + toUser
                + "&amount=" + amount;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", request.getHeader("Authorization"));
        restTemplate.postForEntity(url, new HttpEntity<>(null, headers), Void.class);

        /* ---------- 2. record transaction locally ---------- */
        String fromUser = extractUser(request);          // helper below
        Transaction tx = new Transaction(0, fromUser, toUser, amount, LocalDateTime.now());
        repo.save(tx);
    }

    /* ===== helper: extract username from JWT (simple parse) ===== */
    private String extractUser(HttpServletRequest request) {
        return request.getUserPrincipal().getName();     // Spring sets it if security is configured
    }
}

