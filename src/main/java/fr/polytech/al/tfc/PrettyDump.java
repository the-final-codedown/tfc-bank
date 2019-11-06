package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dump")
public class PrettyDump {

    private ProfileRepository profileRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public PrettyDump(ProfileRepository profileRepository, TransactionRepository transactionRepository) {
        this.profileRepository = profileRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping
    public ResponseEntity<String> start() {
        StringBuilder result = new StringBuilder();
        for (Profile p : profileRepository.findAll()) {
            result.append(p.getEmail()).append("\t").append(p.getAccounts().size()).append(" Accounts\n");
            for (Account account : p.getAccounts()) {
                List<Transaction> transactions = transactionRepository.findAllBySourceOrReceiver(account.getAccountId(), account.getAccountId());
                result.append("id : ").append(account.getAccountId()).append("\t Money : ").append(account.getMoney()).append("\t transaction : ").append(transactions.size()).append("\n");
                for (Transaction t : transactions) {
                    if (t.getSource().equals(account.getAccountId()))
                        result.append("\t id : ").append(t.getId()).append(" +").append(t.getAmount()).append("\n");
                    else
                        result.append("\t id : ").append(t.getId()).append(" -").append(t.getAmount()).append("\n");
                }

            }
        }
        return new ResponseEntity<>(result.toString(), HttpStatus.OK);
    }
}
