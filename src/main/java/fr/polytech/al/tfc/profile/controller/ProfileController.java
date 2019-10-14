package fr.polytech.al.tfc.profile.controller;


import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("profile")
public class ProfileController {

    ProfileRepository profileRepository;

    @GetMapping
    public ResponseEntity<Profile> getProfileByEmail(@PathVariable(value = "email") String email) {
        Profile profile = profileRepository.findProfileByEmail(email);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Profile> saveProfile(@RequestBody String body) {
        Profile profile = new Profile(body);
        profileRepository.save(profile);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
