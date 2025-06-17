package org.ExpenseTracker.utils;

import org.ExpenseTracker.data.models.*;
import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mapper {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String convertToLowerCase(String input) {
        if (input != null) {
            return input.toLowerCase();
        }
        return null;
    }

    public static String toSentenceCase(String input) {
        if (input == null || input.trim().isEmpty()) {
            return input;
        }
        String[] words = input.trim().split("\\s+");
        String result = "";
        for (int i = 0; i < words.length; i++) {
            if (!words[i].isEmpty()) {
                String word = words[i].toLowerCase();
                result += Character.toUpperCase(word.charAt(0)) + word.substring(1);
                if (i < words.length - 1) {
                    result += " ";
                }
            }
        }
        return result;
    }


    public static void mapRegisterRequestToUser(RegisterRequest request, User user) {
        user.setUsername(toSentenceCase(request.getUsername()));
        user.setEmail(convertToLowerCase(request.getEmail()));
        user.setPassword(convertToLowerCase(request.getPassword()));
    }


    public static RegisterResponse toRegisterResponse(User user) {
        RegisterResponse response = new RegisterResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(convertToLowerCase(user.getEmail()));
        return response;
    }


    public static LoginResponse toLoginResponse(User user) {
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(convertToLowerCase(user.getEmail()));
        return response;
    }

    public static void mapCreateExpenseRequestToExpense(CreateExpenseRequest request, String userId, Expense expense) {
        expense.setUserId(userId);
        expense.setAmount(request.getAmount());
        expense.setDate(request.getDate());
        expense.setCategory(toSentenceCase(request.getCategory()));
        expense.setDescription(toSentenceCase(request.getDescription()));
    }


    public static CreateExpenseResponse toCreateExpenseResponse(Expense expense) {
        if (expense == null) return null;
        CreateExpenseResponse response = new CreateExpenseResponse();
        response.setId(expense.getId());
        response.setUserId(expense.getUserId());
        response.setAmount(expense.getAmount());
        response.setDate(expense.getDate());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setUpdatedAt(expense.getUpdatedAt());
        return response;
    }


    public static SearchExpensesResponse toSearchExpensesResponse(Expense expense) {
        if (expense == null) return null;
        SearchExpensesResponse response = new SearchExpensesResponse();
        response.setId(expense.getId());
        response.setUserId(expense.getUserId());
        response.setAmount(expense.getAmount());
        response.setDate(expense.getDate());
        response.setCategory(expense.getCategory());
        response.setDescription(expense.getDescription());
        response.setUpdatedAt(expense.getUpdatedAt());
        return response;
    }


    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime != null) {
            return dateTime.format(DATE_TIME_FORMATTER);
        }
        return null;
    }
}