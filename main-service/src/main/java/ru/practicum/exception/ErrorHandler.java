package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.category.controller.AdminController;
import ru.practicum.category.controller.PublicController;
import ru.practicum.compilation.controller.AdminCompilationController;
import ru.practicum.compilation.controller.PublicCompilationController;
import ru.practicum.event.controller.EventAdminController;
import ru.practicum.event.controller.EventPrivateController;
import ru.practicum.event.controller.EventPublicController;
import ru.practicum.request.controller.RequestController;
import ru.practicum.user.controller.UserController;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice(assignableTypes = {
        UserController.class,
        AdminController.class,
        PublicController.class,
        RequestController.class,
        EventAdminController.class,
        EventPublicController.class,
        EventPrivateController.class,
        AdminCompilationController.class,
        PublicCompilationController.class
})
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handle(final NotFoundException e) {
        return new ErrorResponse("Not found error: ", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handle(final ConflictException e) {
        return new ErrorResponse("Conflict error: ", e.getMessage());
    }

    @ExceptionHandler({ConstraintViolationException.class,
                        MethodArgumentNotValidException.class,
                        MissingPathVariableException.class,
                        BadRequestException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handle(Exception e) throws Exception {
        if (e instanceof ConstraintViolationException ||
                e instanceof  MethodArgumentNotValidException ||
                e instanceof MissingPathVariableException ||
                e instanceof BadRequestException) {
            return new ErrorResponse("Validation error: ", e.getMessage());
        }
        throw e;
    }

}
