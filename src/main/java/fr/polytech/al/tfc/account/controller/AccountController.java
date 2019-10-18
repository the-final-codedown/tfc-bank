package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("account")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody int money) {
        Account account = Account.builder()
                .money(money)
                .build();
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Account> viewAccount(@PathVariable String id){
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

    }
}
