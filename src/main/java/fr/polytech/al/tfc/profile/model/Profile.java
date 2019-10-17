package fr.polytech.al.tfc.profile.model;

import fr.polytech.al.tfc.account.model.Account;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Profile {

    @Id
    private String id;

    @NonNull
    private String email;

    private List<Account> accounts = new ArrayList<>();

}
