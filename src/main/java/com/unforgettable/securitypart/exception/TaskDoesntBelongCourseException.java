package com.unforgettable.securitypart.exception;

public class TaskDoesntBelongCourseException extends RuntimeException{
    public TaskDoesntBelongCourseException(String message) {
        super(message);
    }
}
