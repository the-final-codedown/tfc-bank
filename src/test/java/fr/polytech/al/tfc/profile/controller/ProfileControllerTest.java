package fr.polytech.al.tfc.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import fr.polytech.al.tfc.account.model.AccountDTO;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.junit.After;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {



    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileByEmail() throws Exception {
        final String emailTest = "test";
        Profile profile = new Profile(emailTest);
        profileRepository.save(profile);

        MvcResult mvcResult1 = mockMvc.perform(get("/profiles/" + emailTest))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profileToTest = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(),Profile.class);
        assertEquals(profile,profileToTest);

        MvcResult mvcResult2 = mockMvc.perform(get("/profiles/notfound"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void saveProfile() throws Exception {
        final String emailTest = "saveProfile";
        Profile profile = new Profile(emailTest);
        mockMvc.perform(post("/profiles")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(emailTest))
                .andExpect(status().isOk());

        assertEquals(profile,profileRepository.findProfileByEmail(emailTest).get());
    }

    @Test
    public void createAccountForProfile() throws Exception {
        final String emailTest = "createAccountForProfile";
        Profile profile = new Profile(emailTest);
        profileRepository.save(profile);
        assertEquals(0,profile.getAccounts().size());

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("money",800);
        jsonObject.addProperty("accountType", AccountType.CHECKACCOUNT.name());

        mockMvc.perform(post("/profiles/"+emailTest+"/accounts")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonObject.toString()))
                .andExpect(status().isOk());
        profile = profileRepository.findProfileByEmail(emailTest).get();
        assertEquals(1,profile.getAccounts().size());
    }
}