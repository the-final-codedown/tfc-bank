package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dump")
public class PrettyDump {

    private ProfileRepository profileRepository;
    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    @Autowired
    public PrettyDump(ProfileRepository profileRepository, TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.profileRepository = profileRepository;
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @GetMapping
    public ResponseEntity<String> start() {
        StringBuilder result = new StringBuilder();
        for (Profile p : profileRepository.findAll()) {
            result.append(p.getEmail()).append("\t").append(p.getAccounts().size()).append(" Accounts\n");
            for (Account account : accountRepository.findAllById(p.getAccounts())) {
                List<Transaction> transactions = transactionRepository.findAllBySourceOrReceiver(account.getAccountId(), account.getAccountId())
                        .stream()
                        .sorted(Collections.reverseOrder(Comparator.comparing(Transaction::getDate)))
                        .collect(Collectors.toList());
                result.append("id : ").append(account.getAccountId()).append("\t Money : ").append(account.getMoney()).append("\t transaction : ").append(transactions.size()).append("\n");
                int i = 0;
                for (Transaction t : transactions) {
                    result.append("\t id : ");
                    if (t.getSource().equals(account.getAccountId())) {
                        result.append(accountRepository.findById(t.getReceiver()).get().getAccountId()).append(" -");
                    } else {
                        result.append(accountRepository.findById(t.getSource()).get().getAccountId()).append(" +");
                    }
                    result.append(t.getAmount()).append("\n");
                    if (i++ > 10) {
                        break;
                    }
                }
            }
            result.append("\n");
        }
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}
