package fr.polytech.al.tfc.rollinghistory.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RollingHistoryObserverTest {

    private String idAccount1 = "idAccount1";
    private String idAccount2 = "idAccount2";

    @Autowired
    private RollingHistoryObserver rollingHistoryController;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        Transaction transaction1 = new Transaction(idAccount1, idAccount2, 29, LocalDateTime.now());
        Transaction transaction2 = new Transaction(idAccount1, idAccount2, 29, LocalDateTime.now().minusDays(7));
        Account account = new Account(idAccount1, 300, AccountType.CHECK);

        account.addPayment(transaction1);
        account.addPayment(transaction2);
        accountRepository.save(account);

        account = new Account(idAccount2, 300, AccountType.CHECK);
        account.addTransaction(transaction1);
        account.addTransaction(transaction2);
        accountRepository.save(account);
    }

    @Test
    public void processHistory() {
        Optional<Account> account1 = accountRepository.findById(idAccount1);
        assertTrue(account1.isPresent());
        assertEquals(2, account1.get().getTransactionsWindow().size());

        rollingHistoryController.processHistory();

        account1 = accountRepository.findById(idAccount1);
        assertTrue(account1.isPresent());
        assertEquals(1, account1.get().getTransactionsWindow().size());

        Optional<Account> account2 = accountRepository.findById(idAccount2);
        assertTrue(account2.isPresent());
        assertEquals(0, account2.get().getTransactionsWindow().size());
    }

}