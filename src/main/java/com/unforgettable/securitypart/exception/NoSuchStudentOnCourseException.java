package com.unforgettable.securitypart.exception;

public class NoSuchStudentOnCourseException extends RuntimeException{
    public NoSuchStudentOnCourseException(String message) {
        super(message);
    }
}
