package ch.ohne.dachschaden.client.handler;

import ch.ohne.dachschaden.client.exceptions.AddressNotFoundException;
import ch.ohne.dachschaden.client.exceptions.ExternalGeoServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;

@RestControllerAdvice
class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AddressNotFoundException.class)
    ProblemDetail handleAddressNotFound(AddressNotFoundException ex) {
        log.error("Address not found", ex); // full stacktrace in logs
        var pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setDetail(ex.getMessage());
        pd.setProperty("stacktrace", getStackTrace(ex)); // optional: expose stacktrace
        return pd;
    }

    @ExceptionHandler(ExternalGeoServiceException.class)
    ProblemDetail handleExternal(ExternalGeoServiceException ex) {
        log.error("External Geo service error", ex); // full stacktrace in logs
        var pd = ProblemDetail.forStatus(HttpStatus.BAD_GATEWAY);
        pd.setDetail(ex.getMessage());
        pd.setProperty("stacktrace", getStackTrace(ex)); // optional: expose stacktrace
        return pd;
    }

    private String getStackTrace(Throwable ex) {
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
