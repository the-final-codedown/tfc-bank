package fr.polytech.al.tfc.profile.model;

import fr.polytech.al.tfc.account.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
@NoArgsConstructor
@RequiredArgsConstructor
public class Profile {

    @Id
    @NonNull
    private String email;

    private List<Account> accounts = new ArrayList<>();

    public void addAccount(Account account) {
        this.accounts.add(account);
    }


}
