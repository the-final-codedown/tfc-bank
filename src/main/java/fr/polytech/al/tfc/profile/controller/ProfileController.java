package fr.polytech.al.tfc.profile.controller;

import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController(value = "profile")
public class ProfileController {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    public ProfileController(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfileByEmail(@PathVariable(value = "email") String email) {
        Optional<Profile> profile = profileRepository.findProfileByEmail(email);
        if(profile.isPresent())
            return new ResponseEntity<>(profile.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Profile> saveProfile(@RequestBody String email) {
        Profile profile = new Profile(email);
        profileRepository.save(profile);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
