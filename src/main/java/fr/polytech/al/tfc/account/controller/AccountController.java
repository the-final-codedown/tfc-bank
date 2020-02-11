package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountDTO;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Cap;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }


    @GetMapping("/{accountType}/accounts")
    public ResponseEntity<List<Account>> viewAccounts(@PathVariable(value = "accountType") AccountType accountType) {
        List<Account> accounts = accountRepository.findAllByAccountType(accountType);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> viewAccount(@PathVariable(value = "id") String id) {
        Optional<Account> account = accountRepository.findById(id);
        return account
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/cap")
    public ResponseEntity<Cap> getCap(@PathVariable(value = "id") String id) {
        System.out.println("Fetching account with id " + id);
        Optional<Account> account = accountRepository.findById(id);
        return account
                .map(value -> new ResponseEntity<>(new Cap(value.getMoney(), value.getAmountSlidingWindow()), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
