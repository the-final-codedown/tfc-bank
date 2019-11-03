package fr.polytech.al.tfc.profile.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountDTO;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.profile.model.Profile;
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

    @Autowired
    public ProfileController(ProfileRepository profileRepository, AccountRepository accountRepository) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping("/{email}")
    public ResponseEntity<Profile> getProfileByEmail(@PathVariable(value = "email") String email) {
        Optional<Profile> profile = profileRepository.findProfileByEmail(email);
        return profile
                .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Profile> saveProfile(@RequestBody String email) {
        Profile profile = new Profile(email);
        profileRepository.save(profile);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PostMapping("/{email}/accounts")
    public ResponseEntity<Account> createAccountForProfile(@PathVariable(value = "email") String email, @RequestBody AccountDTO accountDTO) {
        Account account = new Account(accountDTO);
        accountRepository.save(account);
        Optional<Profile> profile = profileRepository.findProfileByEmail(email);
        if (profile.isPresent()) {
            profile.get().addAccount(account);
            profileRepository.save(profile.get());
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
