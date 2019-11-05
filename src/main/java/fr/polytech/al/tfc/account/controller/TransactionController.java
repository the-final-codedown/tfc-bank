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

    @Autowired // TODO search if optional ?
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

            // TODO needed?
            Transaction oppositeTransaction = new Transaction(transaction);
            transactionRepository.save(oppositeTransaction);

            Account sourceAccount = optionalSourceAccount.get();
            sourceAccount.processTransaction(transaction, true);
            accountRepository.save(sourceAccount);

            Account destinationAccount = optionalDestinationAccount.get();
            destinationAccount.processTransaction(transaction, false);
            accountRepository.save(destinationAccount);

            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
