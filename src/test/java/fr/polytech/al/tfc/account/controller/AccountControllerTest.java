package fr.polytech.al.tfc.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Caps;
import fr.polytech.al.tfc.account.repository.AccountRepository;
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
import springfox.documentation.spring.web.json.Json;

import static junit.framework.TestCase.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountController accountController;
    @Autowired
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    private Account account;
    @Test
    @DirtiesContext
    public void contextLoads() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
    }

    //set up is testing saveAccount
    @Before
    public void setUp() throws Exception {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("money",800);
        jsonObject.addProperty("accountType", AccountType.CHECKACCOUNT.name());

        MvcResult mvcResult = mockMvc.perform(post("/accounts")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk()).andReturn();
        account = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Account.class);
    }

    @Test
    public void viewAccount() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/accounts/" + account.getAccountId())
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Account accountRetrieved = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Account.class);
        assertEquals(300, accountRetrieved.getAmountSlidingWindow().intValue());
        assertEquals(800, accountRetrieved.getMoney().intValue());
    }

    @Test
    public void viewCapsForAnAccount() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/accounts/" + account.getAccountId() + "/caps")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
        Caps caps = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Caps.class);
        assertEquals(300, caps.getAmountSlidingWindow().intValue());
        assertEquals(800, caps.getMoney().intValue());
    }
}