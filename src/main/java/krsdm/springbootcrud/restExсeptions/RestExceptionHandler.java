package krsdm.springbootcrud.restExсeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BadRequest> exceptionHandler(NoSuchUserExeption exception) {
        BadRequest badRequest = new BadRequest();
        badRequest.setInfo(exception.getMessage());
        return new ResponseEntity<>(badRequest, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<BadRequest> exceptionHandler(Exception exception) {
        BadRequest badRequest = new BadRequest();
        badRequest.setInfo(exception.getMessage());
        return new ResponseEntity<>(badRequest, HttpStatus.BAD_REQUEST);
    }

}
