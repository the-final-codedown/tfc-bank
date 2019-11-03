package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction) {
        Optional<Account> optionalSourceAccount = accountRepository.findById(transaction.getSource());
        Optional<Account> optionalDestinationAccount = accountRepository.findById(transaction.getReceiver());
        if (optionalSourceAccount.isPresent() && optionalDestinationAccount.isPresent()) {
            transactionRepository.save(transaction);
            Transaction oppositeTransaction = new Transaction(transaction);
            transactionRepository.save(oppositeTransaction);
            Account sourceAccount = optionalSourceAccount.get();
            Account destinationAccount = optionalDestinationAccount.get();

            sourceAccount.setMoney(sourceAccount.getMoney() - transaction.getAmount());
            sourceAccount.setAmountSlidingWindow(sourceAccount.getAmountSlidingWindow() - transaction.getAmount());
            sourceAccount.addPayment(transaction);
            accountRepository.save(sourceAccount);

            destinationAccount.setMoney(destinationAccount.getMoney() + transaction.getAmount());
            destinationAccount.addTransaction(transaction);

            accountRepository.save(destinationAccount);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
