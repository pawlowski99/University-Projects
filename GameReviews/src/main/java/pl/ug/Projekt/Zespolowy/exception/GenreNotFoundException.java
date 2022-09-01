package pl.ug.Projekt.Zespolowy.exception;

public class GenreNotFoundException extends RuntimeException{
    public GenreNotFoundException(Long id){
        super("Could not find genre " + id);
    }

}
