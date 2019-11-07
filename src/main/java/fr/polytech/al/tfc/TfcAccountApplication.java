package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.BankAccount;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.profile.business.ProfileBusiness;
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

    @Autowired
    private ProfileBusiness profileBusiness;

    public static void main(String[] args) {
        SpringApplication.run(TfcAccountApplication.class, args);
    }

    @Override
    public void run(String... args) {
        if (profileRepository.findAll().isEmpty() && accountRepository.findAll().isEmpty()) {
            createAccount("mathieu@email", 300);
            createAccount("florian@email", 500);
            createAccount("theos@email", 1000);
            createAccount("gregoire@email", 10000);
        }
        if (!profileRepository.existsById("bank") && !accountRepository.existsById("bank")) {
            Profile bank = new Profile("bank");
            Account bankAccount = new BankAccount();
            accountRepository.save(bankAccount);
            bank.addAccount(bankAccount.getAccountId());
            profileRepository.save(bank);
        }
    }

    private void createAccount(String email, int amount) {
        profileBusiness.saveProfileWithAccount(new Profile(email), new Account(amount, AccountType.CHECK));
    }
}
