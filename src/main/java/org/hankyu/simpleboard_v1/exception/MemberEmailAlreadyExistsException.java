package org.hankyu.simpleboard_v1.exception;

public class MemberEmailAlreadyExistsException extends RuntimeException{
    public MemberEmailAlreadyExistsException(String message) {
        super(message);
    }
}
