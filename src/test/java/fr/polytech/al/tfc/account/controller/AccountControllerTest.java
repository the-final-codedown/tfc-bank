package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    private AccountController accountController;

    private AccountRepository accountRepository;
    @Autowired
    private MockMvc mockMvc;

    private Account account;
    private final String idAccount1 = "idAccount1";
    private final String idAccount2 = "idAccount2";

    @Test
    @DirtiesContext
    public void contextLoads() {
        Assert.assertNotNull(accountController);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }
    @Before
    public void setUp() throws Exception {
        account = new Account(idAccount1,300);
        MvcResult mvcResult = mockMvc.perform(post("/accounts")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content("300"))
                .andExpect(status().isOk()).andReturn();
        mvcResult.getResponse().getContentAsString();
        accountController = new AccountController(accountRepository);
    }
/*
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void saveAccount() {
    }

    @Test
    public void viewAccount() {
        ResponseEntity<Account> res = accountController.viewAccount(idAccount1);
        assertEquals(HttpStatus.OK,res.getStatusCode());
        assertEquals(account,res.getBody());
        res =  accountController.viewAccount(idAccount2);
        assertEquals(HttpStatus.NO_CONTENT,res.getStatusCode());
    }*/
}