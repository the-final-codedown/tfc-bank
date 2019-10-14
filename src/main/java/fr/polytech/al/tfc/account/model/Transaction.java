package fr.polytech.al.tfc.account.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
@Builder
public class Transaction {

    @Id
    private String id;

    @NonNull
    private String source;

    @NonNull
    private String receiver;

    @NonNull
    private float value;

    @NonNull
    private LocalDateTime date;


}
