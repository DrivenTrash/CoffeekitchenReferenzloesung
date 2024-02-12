package porsche.coffeeKitchen.consumer;

import porsche.coffeeKitchen.consumer.exceptions.DuplicateConsumerException;
import porsche.coffeeKitchen.consumer.exceptions.InsufficientCreditException;
import porsche.coffeeKitchen.consumer.exceptions.UnknownConsumerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumerService {

    private final ConsumerRepository consumerRepository;

    @Autowired
    public ConsumerService(ConsumerRepository consumerRepository) {
        this.consumerRepository = consumerRepository;
    }

    public Consumer addNewConsumer(Consumer consumer) throws DuplicateConsumerException {
        Optional<Consumer> consumerOptional = consumerRepository.findConsumerByName(consumer.getName());
        if (consumerOptional.isPresent()) {
            throw new DuplicateConsumerException("Consumer bereits angelegt");
        }
        consumerRepository.save(consumer);
        return consumer;
    }

    public List<Consumer> getAll() {
        return consumerRepository.findAll();
    }

    public Consumer getConsumer(Consumer consumer) throws UnknownConsumerException {
        Optional<Consumer> optionalConsumer = consumerRepository.findConsumerByName(consumer.getName());
        if (optionalConsumer.isPresent()) {
            return optionalConsumer.get();
        } else {
            throw new UnknownConsumerException(consumer.getName() + " ist nicht bekannt.");
        }
    }

    @Transactional
    public void drinkCoffee(Consumer consumer) throws UnknownConsumerException, InsufficientCreditException {
        Optional<Consumer> consumerOptional = consumerRepository.findConsumerByName((consumer.getName()));
        if (consumerOptional.isEmpty()) {
            throw new UnknownConsumerException(consumer.getName() + " ist nicht bekannt.");
        } else if (consumerOptional.get().getCredit().compareTo(BigDecimal.valueOf(1.35)) < 0) {
            throw new InsufficientCreditException("Kein ausreichendes Guthaben");
        } else {
            Consumer consumer1 = consumerOptional.get();
            consumer1.setCredit(consumer1.getCredit().subtract(BigDecimal.valueOf(1.35)));
            consumer1.setCount(consumer1.getCount() + 1);
        }
    }

    @Transactional
    public void addCredit(Consumer consumer, Integer credit) throws UnknownConsumerException {
        Optional<Consumer> consumerOptional = consumerRepository.findConsumerByName((consumer.getName()));
        if (consumerOptional.isEmpty()) {
            throw new UnknownConsumerException(consumer.getName() + " ist nicht bekannt.");
        } else {
            Consumer consumer1 = consumerOptional.get();
            consumer1.setCredit(consumer1.getCredit().add(BigDecimal.valueOf(credit)));
        }
    }
}
