package fr.polytech.al.tfc;

import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.TransactionRepository;
import fr.polytech.al.tfc.profile.model.Profile;
import fr.polytech.al.tfc.profile.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class PrettyDump {

    private ProfileRepository profileRepository;
    private TransactionRepository transactionRepository;

    @Autowired
    public PrettyDump(ProfileRepository profileRepository, TransactionRepository transactionRepository) {
        this.profileRepository = profileRepository;
        this.transactionRepository = transactionRepository;
    }

    @PreDestroy
    public void start(){
        for(Profile p : profileRepository.findAll()){
            System.out.println( p.getEmail());
            for(Account account : p.getAccounts()){
                System.out.println("id : " + account.getAccountId() + "\t Money :" + account.getMoney());
                for(Transaction t : transactionRepository.findAllBySourceOrReceiver(account.getAccountId(),account.getAccountId())){
                    if(t.getSource().equals(account.getAccountId()))
                        System.out.println("\tid : " + t.getId() + " +" + t.getAmount());
                    else
                        System.out.println("\tid : " + t.getId() + " -" + t.getAmount());
                }

            }
        }
    }
}
