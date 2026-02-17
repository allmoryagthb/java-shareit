package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExceptionTest {

    @Test
    void conflictExceptionTest() {
        var exc = new ConflictException("message");
        assertEquals("message", exc.getMessage());
    }

    @Test
    void entityNotFoundExceptionTest() {
        var exc = new EntityNotFoundException("message");
        assertEquals("message", exc.getMessage());
    }

    @Test
    void internalServerExceptionTest() {
        var exc = new InternalServerException("message");
        assertEquals("message", exc.getMessage());
    }

    @Test
    void validationExceptionTest() {
        var exc = new ValidationException("message");
        assertEquals("message", exc.getMessage());
    }
}
