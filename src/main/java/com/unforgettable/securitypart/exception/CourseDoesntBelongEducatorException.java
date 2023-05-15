package com.unforgettable.securitypart.exception;

public class CourseDoesntBelongEducatorException extends RuntimeException{
    public CourseDoesntBelongEducatorException(String message) {
        super(message);
    }
}
