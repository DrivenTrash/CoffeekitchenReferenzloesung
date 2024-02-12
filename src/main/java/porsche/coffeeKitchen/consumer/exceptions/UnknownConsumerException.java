package porsche.coffeeKitchen.consumer.exceptions;

public class UnknownConsumerException extends Exception{
    public UnknownConsumerException(String errorMessage){
        super(errorMessage);
    }
}