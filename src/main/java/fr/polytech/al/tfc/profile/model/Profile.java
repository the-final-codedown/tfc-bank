package fr.polytech.al.tfc.profile.model;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@RequiredArgsConstructor
public class Profile {

    @Id
    public Long id;

    @NonNull
    public String email;

}
