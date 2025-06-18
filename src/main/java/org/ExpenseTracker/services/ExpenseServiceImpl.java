package org.ExpenseTracker.services;

import org.ExpenseTracker.data.models.Expense;
import org.ExpenseTracker.data.repositories.*;
import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;
import org.ExpenseTracker.exceptions.*;
import org.ExpenseTracker.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Override
    public CreateExpenseResponse addExpenseResponse(CreateExpenseRequest request, String userId) {
        validateAddExpenseRequest(request);
        Expense expense = new Expense();
        Mapper.mapCreateExpenseRequestToExpense(request, userId, expense);
        expense.setUpdatedAt(LocalDateTime.now());
        Expense savedExpense = expenseRepository.save(expense);
        return Mapper.toCreateExpenseResponse(savedExpense);
    }

    @Override
    public List<SearchExpensesResponse> getExpensesByUserId(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new InvalidExpenseException("User ID is required");
        }
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        List<SearchExpensesResponse> responses = new ArrayList<>();
        for (Expense expense : expenses) {
            responses.add(Mapper.toSearchExpensesResponse(expense));
        }
        return responses;
    }

    @Override
    public double calculateTotal(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new InvalidExpenseException("User ID is required");
        }
        double total = 0;
        for (Expense expense : expenseRepository.findByUserId(userId)) {
            total += expense.getAmount();
        }
        return total;
    }

    @Override
    public CreateExpenseResponse updateExpense(String expenseId, CreateExpenseRequest request) {
        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new InvalidExpenseException("Expense ID is required");
        }
        Expense expense = expenseRepository.findById(expenseId).orElse(null);
        if (expense == null) {
            throw new ExpenseNotFoundException("Expense with ID " + expenseId + " not found");
        }
        validateAddExpenseRequest(request);
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(Mapper.toSentenceCase(request.getCategory()));
        expense.setDescription(Mapper.toSentenceCase(request.getDescription()));
        expense.setUpdatedAt(LocalDateTime.now());
        Expense updatedExpense = expenseRepository.save(expense);
        return Mapper.toCreateExpenseResponse(updatedExpense);
    }

    @Override
    public boolean deleteExpense(String expenseId) {
        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new InvalidExpenseException("Expense ID is required");
        }
        if (!expenseRepository.existsById(expenseId)) {
            throw new ExpenseNotFoundException("Expense with ID " + expenseId + " not found");
        }
        expenseRepository.deleteById(expenseId);
        return true;
    }

    @Override
    public List<SearchExpensesResponse> searchExpenses(String userId, String category) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new InvalidExpenseException("User ID is required");
        }
        List<Expense> expenses = expenseRepository.findByUserId(userId);
        List<SearchExpensesResponse> responses = new ArrayList<>();

        for (Expense expense : expenses) {
            boolean matches = true;

            if (category != null && !category.trim().isEmpty()) {
                matches = matches && expense.getCategory().equalsIgnoreCase(Mapper.toSentenceCase(category));
            }

            if (matches) {
                responses.add(Mapper.toSearchExpensesResponse(expense));
            }
        }

        if (responses.isEmpty()) {
            throw new ExpenseNotFoundException("No expenses found for the given criteria");
        }
        return responses;
    }


    @Override
    public SearchExpensesResponse getExpenseById(String expenseId) {
        if (expenseId == null || expenseId.trim().isEmpty()) {
            throw new InvalidExpenseException("Expense ID is required");
        }
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ExpenseNotFoundException("Expense with ID " + expenseId + " not found"));
        return Mapper.toSearchExpensesResponse(expense);
    }


    private void validateAddExpenseRequest(CreateExpenseRequest request) {
        if (request.getAmount() <= 0) {
            throw new InvalidExpenseException("Amount must be positive");
        }
        if (request.getDate() == null || request.getDate().isBefore(LocalDateTime.now())) {
            throw new InvalidExpenseException("Date and time must be now or in the future");
        }
        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            throw new InvalidExpenseException("Category is required");
        }
    }
}