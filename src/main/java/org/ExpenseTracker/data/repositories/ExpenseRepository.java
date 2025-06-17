package org.ExpenseTracker.data.repositories;

import org.ExpenseTracker.data.models.*;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ExpenseRepository extends MongoRepository<Expense, String> {
    List<Expense> findByUserId(String userId);
//    List<Expense> findByUser(User user);
}