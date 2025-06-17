package org.ExpenseTracker.dtos.responses;

import lombok.Data;

@Data
public class RegisterResponse {
    private String id;
    private String username;
    private String email;
}