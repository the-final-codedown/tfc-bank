package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.controller.TransactionController;
import fr.polytech.al.tfc.account.model.AccountDTO;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.profile.controller.ProfileController;
import fr.polytech.al.tfc.profile.model.ProfileDTO;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Ignore
public class PrettyDumpTest {

    @Autowired
    private ProfileController profileController;
    @Autowired
    private TransactionController transactionController;

    @Autowired
    private PrettyDump prettyDump;



    @Before
    public void setUp() throws Exception {
        String emailProfileAlice = "alice@alice.com";
        ProfileDTO profileDTOAlice = new ProfileDTO();
        profileDTOAlice.setEmail(emailProfileAlice);
        profileController.saveProfile(profileDTOAlice);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setMoney(500);
        accountDTO.setAccountType(AccountType.CHECK);
        String accountIdAlice1 = profileController.createAccountForProfile(emailProfileAlice, accountDTO).getBody().getAccountId();
        accountDTO = new AccountDTO();
        accountDTO.setMoney(500);
        accountDTO.setAccountType(AccountType.CHECK);
        String accountIdAlice2 = profileController.createAccountForProfile(emailProfileAlice, accountDTO).getBody().getAccountId();


        String emailProfileBob = "bob@bob.com";
        ProfileDTO profileDTOBob = new ProfileDTO();
        profileDTOBob.setEmail(emailProfileBob);
        profileController.saveProfile(profileDTOBob);
        accountDTO = new AccountDTO();
        accountDTO.setMoney(500);
        accountDTO.setAccountType(AccountType.CHECK);
        String accountIdBob1 = profileController.createAccountForProfile(emailProfileBob, accountDTO).getBody().getAccountId();
        accountDTO = new AccountDTO();
        accountDTO.setMoney(1000);
        accountDTO.setAccountType(AccountType.CHECK);
        String accountIdBob2 = profileController.createAccountForProfile(emailProfileBob, accountDTO).getBody().getAccountId();

        transactionController.addTransaction(new Transaction(accountIdAlice1, accountIdBob1, 100, LocalDateTime.now()));
        transactionController.addTransaction(new Transaction(accountIdBob1, accountIdAlice1, 100, LocalDateTime.now()));
        transactionController.addTransaction(new Transaction(accountIdAlice1, accountIdBob2, 100, LocalDateTime.now()));
        transactionController.addTransaction(new Transaction(accountIdBob2, accountIdAlice1, 100, LocalDateTime.now()));
    }

    @Test
    public void start() {
        System.out.println(prettyDump.start().getBody());
    }
}