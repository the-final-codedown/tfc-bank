package fr.polytech.al.tfc.profile.model;

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

    private List<String> accounts = new ArrayList<>();

    public void addAccount(String accountId) {
        this.accounts.add(accountId);
    }

}
