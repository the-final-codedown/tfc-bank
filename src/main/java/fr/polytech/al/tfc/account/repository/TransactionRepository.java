package fr.polytech.al.tfc.account.repository;

import fr.polytech.al.tfc.account.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {

    Transaction findBySource(String source);

    Transaction findByReceiver(String receiver);

    List<Transaction> findAllByDateAfter(LocalDateTime localDateTime);

    List<Transaction> findAllBySourceAndReceiverAndDate(String source, String receiver, LocalDateTime date);
}
