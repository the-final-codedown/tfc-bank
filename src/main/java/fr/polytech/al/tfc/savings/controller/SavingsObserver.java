package fr.polytech.al.tfc.savings.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Component
public class SavingsObserver {

    @Autowired
    private AccountRepository accountRepository;
    private final double interest = 1.1;


    public void computeSavings(){
        for(Account account : accountRepository.findAllByAccountType(AccountType.SAVINGSACCOUNT)){
            account.setMoney( (int) Math.round(account.getMoney()*interest));
            accountRepository.save(account);
        }
    }
}
