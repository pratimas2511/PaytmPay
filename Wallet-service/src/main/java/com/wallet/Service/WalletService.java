package com.wallet.Service;

import com.wallet.model.Wallet;
import com.wallet.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    @Autowired
    private WalletRepository repo;

    /** Atomic debit + credit. Rolls back on any RuntimeException. */
    @Transactional
    public void transfer(String fromUser, String toUser, double amount) {

        if (amount <= 0) throw new IllegalArgumentException("Amount must be positive");
        if (fromUser.equals(toUser)) throw new IllegalArgumentException("Sender = receiver");

        Wallet sender = repo.findByUsername(fromUser)
                .orElseThrow(() -> new IllegalStateException("Sender wallet not found"));

        if (sender.getAmount() < amount) throw new IllegalStateException("Insufficient funds");

        Wallet receiver = repo.findByUsername(toUser)
                .orElseGet(() -> repo.save(new Wallet(0, toUser, 0.0)));

        sender.setAmount(sender.getAmount() - amount);
        receiver.setAmount(receiver.getAmount() + amount);
        // JPA dirty checking will flush both updates in one commit
    }
}
