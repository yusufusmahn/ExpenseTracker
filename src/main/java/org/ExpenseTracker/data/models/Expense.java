package org.ExpenseTracker.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("expenses")
public class Expense {
    @Id
    private String id;
    private String userId;
    private double amount;
    private LocalDateTime date;
    private String category;
    private String description;
//  private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}