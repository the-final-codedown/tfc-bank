package fr.polytech.al.tfc.profile.controller;

import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class ProfileControllerTest {

    private final String emailTest = "test@test.test";
    private Profile profile;
    private ProfileController profileController;

    @MockBean
    private ProfileRepository profileRepository;

    @Before
    public void setUp() throws Exception {
        profile = new Profile(emailTest);
        Mockito.when(profileRepository.findProfileByEmail(profile.getEmail()))
                .thenReturn(Optional.ofNullable(profile));
        profileController = new ProfileController(profileRepository);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileByEmail() {
        ResponseEntity<Profile> profileFound = profileController.getProfileByEmail(emailTest);
        assertEquals(HttpStatus.OK,profileFound.getStatusCode());
        assertEquals(profile,profileFound.getBody());

        assertEquals(HttpStatus.NO_CONTENT,profileController.getProfileByEmail("FAKE_EMAIL").getStatusCode());
    }

    @Test
    public void saveProfile() {
        assertEquals(HttpStatus.OK,profileController.saveProfile(emailTest).getStatusCode());
    }
}