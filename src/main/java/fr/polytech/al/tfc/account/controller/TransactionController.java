package fr.polytech.al.tfc.account.controller;

import fr.polytech.al.tfc.account.exception.NoExistingAccountException;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController("transaction")
public class TransactionController {
    final TransactionRepository transactionRepository;
    final AccountRepository accountRepository;

    @Autowired
    public TransactionController(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity<Transaction> addTransaction(String source, String destination, float value, LocalDateTime date) {
        Optional<Account> optionalSourceAccount = accountRepository.findById(source);
        Optional<Account> optionalDestinationAccount = accountRepository.findById(destination);
        if (optionalSourceAccount.isPresent() && optionalDestinationAccount.isPresent()) {
            Transaction transaction = new Transaction(source,destination,value,date);
            Transaction oppositeTransaction = getOppositeTransaction(source,destination,value,date);
            transactionRepository.save(transaction);
            transactionRepository.save(oppositeTransaction);
            Account sourceAccount = optionalSourceAccount.get();
            sourceAccount.setMoney(sourceAccount.getMoney() - value);
            sourceAccount.setAmountSlidingWindow(sourceAccount.getAmountSlidingWindow() - value);
            sourceAccount.addPayment(transaction);
            accountRepository.save(sourceAccount);

            Account destinationAccount = optionalDestinationAccount.get();
            destinationAccount.setMoney(destinationAccount.getMoney() + value);
            destinationAccount.addTransaction(transaction);
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            //throw new NoExistingAccountException();
        }
    }
    public Transaction getOppositeTransaction(String source,String destination,float value,LocalDateTime localDateTime){
        return new Transaction(destination,source,value,localDateTime);
    }
}
