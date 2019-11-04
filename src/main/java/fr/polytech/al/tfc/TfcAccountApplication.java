package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.BankAccount;
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
    public void run(String... args) {
        if (accountRepository.findAll().size() == 0) {
            accountRepository.save(new Account(300, AccountType.CHECK));
            accountRepository.save(new Account(500, AccountType.CHECK));
            accountRepository.save(new Account(1000, AccountType.CHECK));
        }
        if (!accountRepository.existsById("bank")) {
            accountRepository.save(new BankAccount());
        }
    }
}
