package transactions.exceptions;

public class ItemDoesntExistException extends Exception{
    public ItemDoesntExistException(){
        super();
    }
    public ItemDoesntExistException(String message){
        super(message);
    }
}
