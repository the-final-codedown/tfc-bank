package fr.polytech.al.tfc.profile.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class Profile {

    @Id
    private String id;

    @NonNull
    private String email;

}
