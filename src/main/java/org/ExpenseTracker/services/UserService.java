package org.ExpenseTracker.services;

import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;

public interface UserService {
    RegisterResponse registerUser(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    CreateExpenseResponse addExpense(String username, CreateExpenseRequest request);
    LoginResponse getUserByEmail(String email);
}