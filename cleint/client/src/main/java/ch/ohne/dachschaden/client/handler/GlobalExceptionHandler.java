package ch.ohne.dachschaden.client.handler;

import ch.ohne.dachschaden.client.exceptions.AddressNotFoundException;
import ch.ohne.dachschaden.client.exceptions.ExternalGeoServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AddressNotFoundException.class)
    ProblemDetail handleAddressNotFound(AddressNotFoundException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setDetail(ex.getMessage());
        return pd;
    }

    @ExceptionHandler(ExternalGeoServiceException.class)
    ProblemDetail handleExternal(ExternalGeoServiceException ex) {
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        pd.setDetail(ex.getMessage());
        return pd;
    }
}
