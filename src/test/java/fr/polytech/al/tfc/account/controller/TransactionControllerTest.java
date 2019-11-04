package fr.polytech.al.tfc.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private TransactionController transactionController;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final String idAccount1 = "idAccount1";
    private Account account1;
    private final String idAccount2 = "idAccount2";
    private Account account2;

    @Test
    @DirtiesContext
    public void contextLoads() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Before
    public void setUp() throws Exception {
        account1 = new Account(idAccount1, 300, AccountType.CHECK);
        account2 = new Account(idAccount2, 300,AccountType.CHECK);

        accountRepository.save(account1);
        accountRepository.save(account2);
    }

    @Test
    public void addTransaction() throws Exception {
        LocalDateTime transactionDate = LocalDateTime.now();
        Integer transactionAmount = 29;
        JsonObject transactionJsonObject = new JsonObject();
        transactionJsonObject.addProperty("source", idAccount1);
        transactionJsonObject.addProperty("receiver", idAccount2);
        transactionJsonObject.addProperty("amount", transactionAmount);
        transactionJsonObject.addProperty("date", String.valueOf(transactionDate));
        MvcResult mvcResult = mockMvc.perform(post("/transactions")
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
                .content(transactionJsonObject.toString())).andReturn();
        Transaction transaction = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Transaction.class);
        assertEquals(idAccount2, transaction.getReceiver());
        assertEquals(idAccount1, transaction.getSource());
        assertEquals(transactionDate, transaction.getDate());
        assertEquals(transactionAmount, transaction.getAmount());

        List<Transaction> transactionList = transactionRepository.findAllBySourceAndReceiverAndDate(idAccount2, idAccount1, transactionDate);
        for (Transaction transaction1 : transactionList) {
            assertEquals(idAccount1, transaction1.getSource());
            assertEquals(idAccount2, transaction1.getReceiver());
            assertEquals(transactionDate, transaction1.getDate());
            assertEquals(transactionAmount, transaction1.getAmount());
        }
    }
}