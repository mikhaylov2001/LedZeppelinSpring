package by.it.course.dc.support.errorHandlers;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

import static org.springframework.http.HttpStatus.*;

@Getter
enum AppException {

    MethodArgumentNotValidException(BAD_REQUEST, 1),
    IllegalStateException(BAD_REQUEST, 2),
    InvalidDataAccessApiUsageException(FORBIDDEN, 3),
    NoSuchElementException(NOT_FOUND, 4),
    DataIntegrityViolationException(FORBIDDEN, 5),
    CassandraInvalidQueryException(BAD_REQUEST, 6),
    RuntimeException(INTERNAL_SERVER_ERROR, 99);

    private final HttpStatusCode httpStatus;
    private final int subCode;

    AppException(HttpStatusCode httpStatus, int subCode) {
        this.httpStatus = httpStatus;
        this.subCode = subCode;
    }

    int getErrorCode() {
        return httpStatus.value() * 100 + subCode;
    }

    public static AppException resolve(String simpleName) {
        try {
            return valueOf(simpleName);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace(System.err);
            return RuntimeException;
        }
    }

}
