package fr.polytech.al.tfc.account.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    private String accountId;

    @NonNull
    private Integer money;

    private Integer amountSlidingWindow = 300;

    /**
     * One week window
     */
    private List<Transaction> transactionsWindow = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String accountId, @NonNull Integer money) {
        this.accountId = accountId;
        this.money = money;
    }

    public void addTransactionWindow(Transaction transactionWindow) {
        this.transactionsWindow.add(transactionWindow);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }
    public void addPayment(Transaction transaction){
        addTransaction(transaction);
        addTransactionWindow(transaction);
    }
}
