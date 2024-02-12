package porsche.coffeeKitchen.consumer;

import porsche.coffeeKitchen.consumer.exceptions.DuplicateConsumerException;
import porsche.coffeeKitchen.consumer.exceptions.InsufficientCreditException;
import porsche.coffeeKitchen.consumer.exceptions.UnknownConsumerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
public class ConsumerController{

    private final ConsumerService consumerService;

    @Autowired
    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    // Alle Gets
    //Liste aller Consumer in der Datenbank
    @GetMapping(path = "consumer")
    public List<Consumer> getAll() {
        return consumerService.getAll();
    }

    //Daten eines einzelnen Consumers
    @GetMapping(path = "consumer/:{consumerName}")
    public Consumer getConsumer (@PathVariable String consumerName){
        try {
            Consumer consumer = new Consumer(consumerName);
            return consumerService.getConsumer(consumer);
        } catch (UnknownConsumerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such consumer in the database");
        }
    }

    // Alle Posts
    //Neuen Consumer anlegen
    @PostMapping(path = "consumer/:{consumerName}")
    public Consumer addNewConsumer (@PathVariable String consumerName) {
        try {
            Consumer newConsumer = new Consumer(consumerName);
            return consumerService.addNewConsumer(newConsumer);
        } catch (DuplicateConsumerException exception) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Consumer already exists");
        }
    }

    // Alle puts
    //drinkCoffee
    @PutMapping(path = "consumer/:{consumerName}")
    public Consumer drinkCoffee(@PathVariable String consumerName) {
        try {
            Consumer consumer = new Consumer(consumerName);
            consumerService.drinkCoffee(consumer);
            return consumerService.getConsumer(consumer);
        }
        catch (UnknownConsumerException | InsufficientCreditException exception) {
            if (exception.getClass().equals(UnknownConsumerException.class))
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such consumer in the database");
            else
                throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED, "No sufficient credit available");

        }
    }
    //Alte Variante, die nicht funktioniert und mich abfuckt, weil ich nicht weiß, warum nicht. Wird behalten, falls ich das irgendwann mal klären will/kann
//    @PutMapping(path = "putTest")
//    public Consumer putTest(@RequestBody @NonNull String consumerName){
//        consumerService.putTest(consumerName);
//        return consumerService.getConsumer(consumer);
//    }

    //addCredit
    @PutMapping(path = "consumer/:{consumerName}/:{credit}")
    public Consumer addCredit(@PathVariable String consumerName, @PathVariable Integer credit) {
        try {
            Consumer consumer = new Consumer(consumerName);
            consumerService.addCredit(consumer, credit);
            return consumerService.getConsumer(consumer);
        } catch (UnknownConsumerException exception) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such consumer in the database");
        }
    }
}
