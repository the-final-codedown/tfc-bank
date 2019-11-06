package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.BankAccount;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TfcAccountApplication implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ProfileRepository profileRepository;

    public static void main(String[] args) {
        SpringApplication.run(TfcAccountApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String emailMat = "mathieu@email";
        String emailFlo = "florian@email";
        String emailVeg = "theos@email";
        String emailGreg = "gregoire@email";
        if (profileRepository.findAll().isEmpty()) {
            Profile mathieu = new Profile(emailMat);
            Profile florian = new Profile(emailFlo);
            Profile theos = new Profile(emailVeg);
            Profile gregoire = new Profile(emailGreg);
            if (accountRepository.findAll().isEmpty()) {
                Account accountMat = new Account(300, AccountType.CHECK);
                Account accountFlo = new Account(500, AccountType.CHECK);
                Account accountGregoire = new Account(1000, AccountType.CHECK);
                Account accountTheos = new Account(10000, AccountType.CHECK);
                mathieu.addAccount(accountMat);
                gregoire.addAccount(accountGregoire);
                theos.addAccount(accountTheos);
                florian.addAccount(accountFlo);
                accountRepository.save(accountTheos);
                accountRepository.save(accountGregoire);
                accountRepository.save(accountFlo);
                accountRepository.save(accountMat);

                profileRepository.save(theos);
                profileRepository.save(mathieu);
                profileRepository.save(gregoire);
                profileRepository.save(florian);
            }
        }
        if (!accountRepository.existsById("bank")) {
            accountRepository.save(new BankAccount());
        }
    }
}
