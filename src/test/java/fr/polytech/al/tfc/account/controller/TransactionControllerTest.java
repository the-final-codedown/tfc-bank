package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class TransactionControllerTest {

    private TransactionController transactionController;

    @MockBean
    private AccountRepository accountRepository ;
    @MockBean
    private TransactionRepository transactionRepository;

    private final String idAccount1 = "idAccount1";
    private Account account1;
    private final String idAccount2 = "idAccount2";
    private Account account2;

    @Before
    public void setUp() throws Exception {
        account1 = new Account(idAccount1,300);
        account2 = new Account(idAccount2,300);

        Mockito.when(accountRepository.findById(idAccount1)).thenReturn(Optional.ofNullable(account1));
        Mockito.when(accountRepository.findById(idAccount2)).thenReturn(Optional.ofNullable(account2));

        transactionController = new TransactionController(transactionRepository,accountRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addTransaction() {
        assertEquals(HttpStatus.OK,transactionController.addTransaction(idAccount1,idAccount2,29, LocalDateTime.now()).getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT,transactionController.addTransaction("FAKE_ID",idAccount1,29, LocalDateTime.now()).getStatusCode());
    }
}