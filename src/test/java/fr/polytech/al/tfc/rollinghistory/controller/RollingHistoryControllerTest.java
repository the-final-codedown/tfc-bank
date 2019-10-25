package fr.polytech.al.tfc.rollinghistory.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class RollingHistoryControllerTest {

    private String idAccount1 = "idAccount1";
    private String idAccount2 = "idAccount2";
    private RollingHistoryController rollingHistoryController;

    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private TransactionRepository transactionRepository;

    @Before
    public void setUp() throws Exception {
        List<Account> accounts = new ArrayList();
        Transaction transaction = new Transaction(idAccount1,idAccount2,29,LocalDateTime.now());
        Account account = new Account(idAccount1,300);

        account.addTransaction(transaction);
        accounts.add(account);

        account = new Account(idAccount2,300);
        account.addTransaction(transaction);
        accounts.add(account);

        Mockito.when(accountRepository.findAll()).thenReturn(accounts);
        //Mockito.when(accountRepository.save(account));
        rollingHistoryController = new RollingHistoryController(transactionRepository, accountRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void processHistory() {

    }
}