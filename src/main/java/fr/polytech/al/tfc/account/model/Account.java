package fr.polytech.al.tfc.account.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Account {

    private static final Integer amountSlidingWindow = 300;

    @Id
    private String accountId;

    @NonNull
    private Integer money;

    private Integer lastWindow = 0;

    @NonNull
    private AccountType accountType;

    public Account(String accountId, @NonNull Integer money, AccountType accountType) {
        this.accountType = accountType;
        this.accountId = accountId;
        this.money = money;
    }

    public Account(AccountDTO accountDTO) {
        this.accountType = accountDTO.getAccountType();
        this.money = accountDTO.getMoney();
    }

    public Integer getAmountSlidingWindow() {
        return amountSlidingWindow;
    }

    public void processTransaction(Transaction transaction) {
        this.setMoney(this.money -= transaction.getAmount());
    }

}
