package fr.polytech.al.tfc.account.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@Builder
public class Account {
    @Id
    private String accountId;

    @NonNull
    private float money;

    private float amountSlidingWindow;

    /**
     * One week window
     */
    private List<Transaction> transactionsWindow;
    private List<Transaction> transactions;

    private void addTransactionWindow(Transaction transactionWindow) {
        this.transactionsWindow.add(transactionWindow);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
        this.addTransactionWindow(transaction);
    }
}
