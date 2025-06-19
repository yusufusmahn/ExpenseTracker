package org.ExpenseTracker.exceptions;

public class UserNotFoundException extends ExpenseTrackerException {
    public UserNotFoundException(String message) {
        super(message);
    }
}