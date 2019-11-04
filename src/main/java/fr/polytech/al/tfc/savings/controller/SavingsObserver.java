package fr.polytech.al.tfc.savings.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SavingsObserver {

    private final double interest = 1.1;
    @Autowired
    private AccountRepository accountRepository;

    public void computeSavings() {
        for (Account account : accountRepository.findAllByAccountType(AccountType.SAVINGS)) {
            account.setMoney((int) Math.round(account.getMoney() * interest));
            accountRepository.save(account);
        }
    }
}
