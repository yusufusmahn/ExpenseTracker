package org.ExpenseTracker.dtos.requests;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateExpenseRequest {
    private double amount;
    private LocalDateTime date;
    private String category;
    private String description;
}