package org.ExpenseTracker.dtos.responses;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateExpenseResponse {
    private String id;
    private String userId; 
    private double amount;
    private LocalDateTime date;
    private String category;
    private String description;
    private LocalDateTime updatedAt;
}