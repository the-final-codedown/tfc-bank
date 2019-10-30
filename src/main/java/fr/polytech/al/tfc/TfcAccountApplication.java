package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TfcAccountApplication implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;
    public static void main(String[] args) {
        SpringApplication.run(TfcAccountApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Account account = new Account(300, AccountType.CHECKACCOUNT);
        Account account1 = new Account(500,AccountType.CHECKACCOUNT);
        Account account2 = new Account(1000,AccountType.CHECKACCOUNT);
        accountRepository.save(account);
        accountRepository.save(account1);
        accountRepository.save(account2);
    }
}
