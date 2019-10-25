package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class AccountControllerTest {

    private AccountController accountController;
    @MockBean
    private AccountRepository accountRepository;

    private Account account;
    private final String idAccount1 = "idAccount1";
    private final String idAccount2 = "idAccount2";

    @Before
    public void setUp() throws Exception {
        account = new Account(idAccount1,300);
        Optional opAccount = Optional.ofNullable(account);
        Mockito.when(accountRepository.findById(idAccount1)).thenReturn(opAccount);
        Mockito.when(accountRepository.findById(idAccount2)).thenReturn(Optional.empty());
        accountController = new AccountController(accountRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveAccount() {
    }

    @Test
    public void viewAccount() {
        ResponseEntity<Account> res =  accountController.viewAccount(idAccount1);
        assertEquals(HttpStatus.OK,res.getStatusCode());
        assertEquals(account,res.getBody());
        res =  accountController.viewAccount(idAccount2);
        assertEquals(HttpStatus.NO_CONTENT,res.getStatusCode());
    }
}