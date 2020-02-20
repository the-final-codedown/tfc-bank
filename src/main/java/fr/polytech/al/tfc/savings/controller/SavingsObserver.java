package fr.polytech.al.tfc.savings.controller;

import fr.polytech.al.tfc.account.business.TransactionBusiness;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class SavingsObserver {

    private final double interest = 0.1;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionBusiness transactionBusiness;

    public void computeSavings() throws Exception {
        for (Account account : accountRepository.findAllByAccountType(AccountType.SAVINGS)) {
            String source = "bank";
            Optional<Account> bankOpt = accountRepository.findById(source);
            Account bankAccount = null;
            if(bankOpt.isPresent())
                bankAccount = bankOpt.get();
            else
                throw new Exception("Bank not found");
            int value = (int) Math.round(account.getMoney() * interest);
            Transaction transaction = new Transaction(source,account.getAccountId(),value, LocalDateTime.now());
            transactionBusiness.processTransaction(transaction,bankAccount,account);

        }
    }
}
