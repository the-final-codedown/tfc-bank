package fr.polytech.al.tfc.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Account {

    @Id
    private String accountId;

    @NonNull
    private Integer money;

    private Integer amountSlidingWindow = 300;

    @NonNull
    private AccountType accountType;

    /**
     * One week window
     */
    private List<Transaction> transactionsWindow = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public Account(String accountId, @NonNull Integer money, AccountType accountType) {
        this.accountType = accountType;
        this.accountId = accountId;
        this.money = money;
    }

    public Account(AccountDTO accountDTO) {
        this.accountType = accountDTO.getAccountType();
        this.money = accountDTO.getMoney();
    }

    public void addTransactionWindow(Transaction transactionWindow) {
        this.transactionsWindow.add(transactionWindow);
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public void addPayment(Transaction transaction) {
        addTransaction(transaction);
        addTransactionWindow(transaction);
    }

    public void processTransaction(Transaction transaction, boolean updateSlidingWindow) {
        this.setMoney(this.money -= transaction.getAmount());
        if (updateSlidingWindow) {
            this.amountSlidingWindow -= transaction.getAmount();
            this.addPayment(transaction);
        } else {
            this.addTransaction(transaction);
        }
    }

}
