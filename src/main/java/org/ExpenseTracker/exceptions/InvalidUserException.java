package org.ExpenseTracker.exceptions;

public class InvalidUserException extends ExpenseTrackerException {
    public InvalidUserException(String message) {
        super(message);
    }
}