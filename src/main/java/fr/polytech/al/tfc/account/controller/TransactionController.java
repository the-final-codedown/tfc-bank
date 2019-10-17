package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.exception.NoExistingAccountException;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController("transaction")
public class TransactionController {
    final
    TransactionRepository transactionRepository;
    final AccountRepository accountRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;

    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Transaction> addTransaction(String source, String destination, float value, LocalDateTime date) {
        Optional<Account> optionalAccount = accountRepository.findById(source);
        if (optionalAccount.isPresent()) {
            Transaction transaction = Transaction.builder()
                    .source(source)
                    .receiver(destination)
                    .value(value)
                    .date(date)
                    .build();
            transactionRepository.save(transaction);
            Account account = optionalAccount.get();
            account.setMoney(account.getMoney() - value);
            account.addTransaction(transaction);
            accountRepository.save(account);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            throw new NoExistingAccountException();
        }
    }
}
