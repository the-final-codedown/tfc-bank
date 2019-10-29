package fr.polytech.al.tfc.profile.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfileControllerTest {

    private final String emailTest = "test";
    private Profile profile;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() throws Exception {
        profile = new Profile(emailTest);
        profileRepository.save(profile);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileByEmail() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/profiles/" + emailTest))
                .andExpect(status().isOk())
                .andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        Profile profileToTest = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),Profile.class);
        assertEquals(profile,profileToTest);

     //   assertEquals(HttpStatus.NO_CONTENT,profileController.getProfileByEmail("FAKE_EMAIL").getStatusCode());
    }

    @Test
    public void saveProfile() {
        //assertEquals(HttpStatus.OK,profileController.saveProfile(emailTest).getStatusCode());
    }
}