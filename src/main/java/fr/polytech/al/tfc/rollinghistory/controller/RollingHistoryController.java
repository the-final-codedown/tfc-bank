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
public class RollingHistoryController {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public RollingHistoryController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Scheduled(fixedDelay = 5000)
    public void processHistory() {
        //TODO improve efficiency of the algorithm
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime then = now.minusDays(7);
            List<Transaction> transactions = account.getTransactionsWindow();
            if (!transactions.isEmpty()) {
                List<Transaction> removedTransactionsWindow = transactions.stream()
                        .filter(transaction -> transaction.getDate().isBefore(then))
                        .collect(Collectors.toList());
                for (Transaction transaction : removedTransactionsWindow) {
                    account.setAmountSlidingWindow(account.getAmountSlidingWindow() + transaction.getValue());
                }
                transactions.removeAll(removedTransactionsWindow);
                account.setTransactionsWindow(transactions);
                accountRepository.save(account);
            }
        }

    }

}
