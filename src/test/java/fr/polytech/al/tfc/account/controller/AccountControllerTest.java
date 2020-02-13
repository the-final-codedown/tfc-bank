package fr.polytech.al.tfc.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Cap;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    private ProfileRepository profileRepository;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Account account;

    //set up is testing saveAccount
    @Before
    public void setUp() throws Exception {
        account = new Account(800,AccountType.CHECK);
        accountRepository.save(account);
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
        MvcResult mvcResult = this.mockMvc.perform(get("/accounts/" + account.getAccountId() + "/cap")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        Cap cap = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Cap.class);
        assertEquals(300, cap.getAmountSlidingWindow().intValue());
        assertEquals(800, cap.getMoney().intValue());
    }
    @Test
    public void createAccountForProfile() throws Exception {
        final String emailTest = "createAccountForProfile";
        Profile profile = new Profile(emailTest);
        profileRepository.save(profile);
        Assert.assertEquals(0, profile.getAccounts().size());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("money", 800);
        jsonObject.addProperty("accountType", AccountType.CHECK.name());

        mockMvc.perform(post("/accounts/" + emailTest + "/accounts")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk());
        profile = profileRepository.findByEmail(emailTest).get();
        Assert.assertEquals(1, profile.getAccounts().size());
    }

}