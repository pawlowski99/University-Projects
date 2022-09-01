package pl.ug.Projekt.Zespolowy.exception;

public class PublisherNotFoundException extends RuntimeException{
    public PublisherNotFoundException(Long id){
        super("Could not find publisher " + id);
    }
}
