package fr.polytech.al.tfc.rollinghistory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Cap;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.rollinghistory.producer.RollingHistoryProducer;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@EnableScheduling
@Component
public class RollingHistoryObserver {

    private final RollingHistoryProducer rollingHistoryProducer;
    private final AccountRepository accountRepository;
    private int expirationTime = 604800; // Represents 7 days in seconds

    public RollingHistoryObserver(RollingHistoryProducer rollingHistoryProducer, AccountRepository accountRepository) {
        this.rollingHistoryProducer = rollingHistoryProducer;
        this.accountRepository = accountRepository;
    }

    public int getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    /**
     * Replace IsKafkaEnable with mockedBean in test
     *
     * @throws JsonProcessingException
     */
    @Scheduled(fixedDelay = 5000)
    public void processHistory() throws JsonProcessingException {
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
                rollingHistoryProducer.sendCap(account.getAccountId(), new Cap(account.getMoney(), account.getAmountSlidingWindow()));
                transactions.removeAll(removedTransactionsWindow);
                account.setTransactionsWindow(transactions);
                accountRepository.save(account);
            }
        }
    }
}
