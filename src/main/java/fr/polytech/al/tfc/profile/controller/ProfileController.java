package fr.polytech.al.tfc.profile.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountDTO;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.profile.business.ProfileBusiness;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.model.ProfileDTO;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileRepository profileRepository;

    private final AccountRepository accountRepository;

    private final ProfileBusiness profileBusiness;

    @Autowired
    public ProfileController(ProfileRepository profileRepository, AccountRepository accountRepository, ProfileBusiness profileBusiness) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
        this.profileBusiness = profileBusiness;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfileByEmail(@PathVariable(value = "email") String email) {
        Optional<Profile> profile = profileRepository.findByEmail(email);
        return profile
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Profile> saveProfile(@RequestBody ProfileDTO profileDTO) {
        Profile profile = new Profile(profileDTO.getEmail());
        profileRepository.save(profile);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PostMapping("/{email}/accounts")
    public ResponseEntity<Account> createAccountForProfile(@PathVariable(value = "email") String email, @RequestBody AccountDTO accountDTO) {
        Optional<Profile> optionalProfile = profileRepository.findByEmail(email);
        if (optionalProfile.isPresent()) {
            Account account = new Account(accountDTO);
            profileBusiness.saveProfileWithAccount(optionalProfile.get(), account);
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
