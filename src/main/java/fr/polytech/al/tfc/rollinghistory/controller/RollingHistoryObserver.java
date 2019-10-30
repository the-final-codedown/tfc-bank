package fr.polytech.al.tfc.rollinghistory.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class RollingHistoryObserver {
    private int expirationTime = 604800;
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public RollingHistoryObserver(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    @Scheduled(fixedDelay = 5000)
    public void processHistory() {
        //TODO improve efficiency of the algorithm
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime then = now.minusSeconds(expirationTime);
            List<Transaction> transactions = account.getTransactionsWindow();
            if (!transactions.isEmpty()) {
                List<Transaction> removedTransactionsWindow = transactions.stream()
                        .filter(transaction -> transaction.getDate().isBefore(then))
                        .collect(Collectors.toList());
                for (Transaction transaction : removedTransactionsWindow) {
                    account.setAmountSlidingWindow(account.getAmountSlidingWindow() + transaction.getAmount());
                }
                transactions.removeAll(removedTransactionsWindow);
                account.setTransactionsWindow(transactions);
                accountRepository.save(account);
            }
        }

    }

}
