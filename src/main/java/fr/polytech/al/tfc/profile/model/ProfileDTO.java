package fr.polytech.al.tfc.profile.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(
        chain = true
)
public class ProfileDTO {
    private String email;
}
