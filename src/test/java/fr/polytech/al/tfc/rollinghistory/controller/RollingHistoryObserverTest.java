package fr.polytech.al.tfc.rollinghistory.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.polytech.al.tfc.account.model.Account;
import fr.polytech.al.tfc.account.model.AccountType;
import fr.polytech.al.tfc.account.model.Transaction;
import fr.polytech.al.tfc.account.repository.AccountRepository;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RollingHistoryObserverTest {

    private String idAccount1 = "idAccount1";
    private String idAccount2 = "idAccount2";

    @MockBean
    KafkaConsumer<String,String> kafkaConsumer;
    @MockBean
    KafkaProducer<String,String> kafkaProducer;

    @Autowired
    private RollingHistoryObserver rollingHistoryController;

    @Autowired
    private AccountRepository accountRepository;

    @Before
    public void setUp() {
        kafkaConsumer = mock(KafkaConsumer.class);
        kafkaProducer = mock(KafkaProducer.class);
        Transaction transaction1 = new Transaction(idAccount1, idAccount2, 29, LocalDateTime.now());
        Transaction transaction2 = new Transaction(idAccount1, idAccount2, 29, LocalDateTime.now().minusDays(7));
        Account account = new Account(idAccount1, 300, AccountType.CHECK);

        account.addTransactionWindow(transaction1);
        account.addTransactionWindow(transaction2);
        accountRepository.save(account);

        account = new Account(idAccount2, 300, AccountType.CHECK);
        account.addTransactionWindow(transaction1);
        account.addTransactionWindow(transaction2);
        accountRepository.save(account);

    }

    @Test
    public void processHistory() throws JsonProcessingException {
        Optional<Account> account1 = accountRepository.findById(idAccount1);
        assertTrue(account1.isPresent());
        assertEquals(2, account1.get().getTransactionsWindow().size());

        rollingHistoryController.processHistory();

        account1 = accountRepository.findById(idAccount1);
        assertTrue(account1.isPresent());
        assertEquals(1, account1.get().getTransactionsWindow().size());

        Optional<Account> account2 = accountRepository.findById(idAccount2);
        assertTrue(account2.isPresent());
        assertEquals(0, account2.get().getTransactionsWindow().size());
    }

}