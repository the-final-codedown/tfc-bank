package fr.polytech.al.tfc.account.model;


import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@Document
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
