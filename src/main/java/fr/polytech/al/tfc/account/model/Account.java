package fr.polytech.al.tfc.account.model;

import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
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
    private float money;

    private float amountSlidingWindow = 300;

    /**
     * One week window
     */
    private List<Transaction> transactionsWindow;
    private List<Transaction> transactions;

    public Account(String accountId, @NonNull float money) {
        this.accountId = accountId;
        this.money = money;
        this.transactionsWindow = new ArrayList<>();
        this.transactions = new ArrayList<>();
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
