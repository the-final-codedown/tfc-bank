package fr.polytech.al.tfc.account.controller;


import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController("account")
public class AccountController {

    AccountRepository accountRepository;

    @PostMapping
    @ResponseBody
    public ResponseEntity<Transaction> addTransaction(String source, String destination, float value, LocalDateTime date){
        Transaction transaction = Transaction.builder()
                .source(source)
                .receiver(destination)
                .value(value)
                .date(date)
                .build();
        accountRepository.save(transaction);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }
}
