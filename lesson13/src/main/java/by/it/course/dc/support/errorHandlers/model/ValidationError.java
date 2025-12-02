package by.it.course.dc.support.errorHandlers.model;

public record ValidationError(
        String object,
        String field,
        String message) {
}