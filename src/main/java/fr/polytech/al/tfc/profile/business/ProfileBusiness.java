package fr.polytech.al.tfc.profile.business;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.stereotype.Component;

@Component
public class ProfileBusiness {

    private final ProfileRepository profileRepository;

    private final AccountRepository accountRepository;

    public ProfileBusiness(ProfileRepository profileRepository, AccountRepository accountRepository) {
        this.profileRepository = profileRepository;
        this.accountRepository = accountRepository;
    }
    public void saveProfileWithAccount(Profile profile, Account account) {
        accountRepository.save(account);
        profile.addAccount(account.getAccountId());
        profileRepository.save(profile);
    }
}
