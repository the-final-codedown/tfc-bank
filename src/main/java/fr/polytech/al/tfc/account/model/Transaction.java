package fr.polytech.al.tfc.account.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class Transaction {

    @Id
    private String id;

    @NonNull
    private String source;

    @NonNull
    private String receiver;

    @NonNull
    private Integer value;

    @NonNull
    private LocalDateTime date;

    public Transaction() {

    }

    public Transaction(@NonNull String source, @NonNull String receiver, @NonNull Integer value, @NonNull LocalDateTime date) {
        this.source = source;
        this.receiver = receiver;
        this.value = value;
        this.date = date;
    }
}
