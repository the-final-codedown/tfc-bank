package fr.polytech.al.tfc.savings.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.fail;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class SavingsControllerTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private MockMvc mockMvc;

    private String accountId1 = "account1";
    private String accountId2 = "account2";
    private String accountId3 = "account3";

    @Before
    public void setUp() {
        accountRepository.save(new Account(accountId1, 500, AccountType.SAVINGS));
        accountRepository.save(new Account(accountId2, 500, AccountType.CHECK));
        accountRepository.save(new Account(accountId3, 0, AccountType.SAVINGS));
    }

    @Test
    public void startComputingSavings() throws Exception {
        mockMvc.perform(post("/savings")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        Optional<Account> optionalAccount = accountRepository.findById(accountId1);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            assertEquals(500 * 1.1, (double) account.getMoney());
        } else fail();

        assertEquals(500, (int) accountRepository.findById(accountId2).get().getMoney());
        assertEquals(0, (int) accountRepository.findById(accountId3).get().getMoney());
    }
}