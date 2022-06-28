package org.hankyu.simpleboard_v1.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hankyu.simpleboard_v1.dto.response.Response;
import org.hankyu.simpleboard_v1.exception.*;
import org.hankyu.simpleboard_v1.exception.type.ExceptionType;
import org.hankyu.simpleboard_v1.handler.ResponseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.hankyu.simpleboard_v1.exception.type.ExceptionType.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdvice {

    private final ResponseHandler responseHandler;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response exception(Exception e) {
        log.error("e = {}", e.getMessage());
        return getFailureResponse(EXCEPTION);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Response accessDeniedException() {
        return getFailureResponse(ACCESS_DENIED_EXCEPTION);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response bindException(BindException e) {
        return getFailureResponse(BIND_EXCEPTION, e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(LoginFailureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response loginFailureException() {
        return getFailureResponse(LOGIN_FAILURE_EXCEPTION);
    }

    @ExceptionHandler(MemberEmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberEmailAlreadyExistsException(MemberEmailAlreadyExistsException e) {
        return getFailureResponse(MEMBER_EMAIL_ALREADY_EXISTS_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(MemberNicknameAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response memberNicknameAlreadyExistsException(MemberNicknameAlreadyExistsException e) {
        return getFailureResponse(MEMBER_NICKNAME_ALREADY_EXISTS_EXCEPTION, e.getMessage());
    }

    @ExceptionHandler(MemberNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response memberNotFoundException() {
        return getFailureResponse(MEMBER_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response roleNotFoundException() {
        return getFailureResponse(ROLE_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response categoryNotFoundException() {
        return getFailureResponse(CATEGORY_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(CannotConvertNestedStructureException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Response cannotConvertNestedStructureException(CannotConvertNestedStructureException e) {
        log.error("e = {}", e.getMessage());
        return getFailureResponse(CANNOT_CONVERT_NESTED_STRUCTURE_EXCEPTION);
    }

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response postNotFoundException() {
        return getFailureResponse(POST_NOT_FOUND_EXCEPTION);
    }

    @ExceptionHandler(UnsupportedImageFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response unsupportedImageFormatException() {
        return getFailureResponse(UNSUPPORTED_IMAGE_FORMAT_EXCEPTION);
    }

    @ExceptionHandler(FileUploadFailureException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response fileUploadFailureException(FileUploadFailureException e) {
        log.error("e = {}", e.getMessage());
        return getFailureResponse(FILE_UPLOAD_FAILURE_EXCEPTION);
    }

    private Response getFailureResponse(ExceptionType exceptionType) {
        return responseHandler.getFailureResponse(exceptionType);
    }

    private Response getFailureResponse(ExceptionType exceptionType, Object... args) {
        return responseHandler.getFailureResponse(exceptionType, args);
    }
}
