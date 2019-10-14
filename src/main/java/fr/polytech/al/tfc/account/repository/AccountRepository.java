package fr.polytech.al.tfc.account.repository;

import fr.polytech.al.tfc.account.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends MongoRepository<Transaction, String> {

    Transaction findBySource(String source);
    Transaction findByReceiver(String receiver);
}
