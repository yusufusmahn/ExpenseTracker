package org.ExpenseTracker.data.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document("expenses")
public class Expense {
    @Id
    private String id;
    private double amount;
    private LocalDate date;
    private String category;
    private String description;
    private String userId;
}