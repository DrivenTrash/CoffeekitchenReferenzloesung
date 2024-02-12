package porsche.coffeeKitchen.consumer.exceptions;

public class DuplicateConsumerException extends Exception{
    public DuplicateConsumerException(String errorMessage) {
        super(errorMessage);
    }
}

