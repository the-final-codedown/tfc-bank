# tfc-bank
This service will be used to manage the profile of the user



# Kafka 
Topic : kafka-transaction  
Responsability : Receive transaction
```
public class Transaction {
    private String id;
    private String source;
    private String receiver;
    private float value;
    private LocalDateTime date;
}
```
