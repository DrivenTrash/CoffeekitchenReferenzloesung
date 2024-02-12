package porsche.coffeeKitchen.consumer.exceptions;

public class InsufficientCreditException extends Exception{
    public InsufficientCreditException(String errorMessage) {
        super(errorMessage);
    }
}