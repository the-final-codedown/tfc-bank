package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountDTO;
import fr.polytech.al.tfc.account.model.Caps;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Account> saveAccount(@RequestBody AccountDTO accountDTO) {
        Account account = new Account(accountDTO);
        accountRepository.save(account);
        return new ResponseEntity<>(account, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> viewAccount(@PathVariable(value = "id") String id) {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            return new ResponseEntity<>(account.get(), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/{id}/caps")
    public ResponseEntity<Caps> getCaps(@PathVariable(value = "id") String id) {
        Optional<Account> optionalAccount = accountRepository.findById(id);
        if(optionalAccount.isPresent()){
            Account account = optionalAccount.get();
            return new ResponseEntity<>(new Caps(account.getMoney(),account.getAmountSlidingWindow()),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>((Caps) null, HttpStatus.NOT_FOUND);
        }
    }
}
