package org.ExpenseTracker.services;

import org.ExpenseTracker.dtos.requests.CreateExpenseRequest;
import org.ExpenseTracker.dtos.responses.*;

import java.util.List;

public interface ExpenseService {
    CreateExpenseResponse addExpenseResponse(CreateExpenseRequest request, String userId);
    List<SearchExpensesResponse> getExpensesByUserId(String userId);
    double calculateTotal(String userId);
    CreateExpenseResponse updateExpense(String expenseId, CreateExpenseRequest request);
    boolean deleteExpense(String expenseId);
    List<SearchExpensesResponse> searchExpenses(String userId, String category);
    SearchExpensesResponse getExpenseById(String expenseId);

}