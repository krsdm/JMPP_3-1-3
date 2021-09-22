package krsdm.springbootcrud.restExсeptions;

public class NoSuchUserExeption extends RuntimeException {
    public NoSuchUserExeption(String message) {
        super(message);
    }
}
