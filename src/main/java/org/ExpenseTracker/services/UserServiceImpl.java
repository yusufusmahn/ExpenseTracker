package org.ExpenseTracker.services;

import org.ExpenseTracker.data.models.*;
import org.ExpenseTracker.data.repositories.*;
import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;
import org.ExpenseTracker.exceptions.*;

import org.ExpenseTracker.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseService expenseService;

    @Override
    public RegisterResponse registerUser(RegisterRequest request) {
        validateRegisterRequest(request);

        String normalizedEmail = Mapper.convertToLowerCase(request.getEmail());
        User existingUser = userRepository.findByEmail(normalizedEmail);
        if (existingUser != null) {
            throw new InvalidUserException("Email " + normalizedEmail + " is already registered");
        }

        User user = new User();
        Mapper.mapRegisterRequestToUser(request, user);
        User savedUser = userRepository.save(user);
        return Mapper.toRegisterResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        String normalizedEmail = Mapper.convertToLowerCase(request.getEmail());
        User user = userRepository.findByEmail(normalizedEmail);
        if (user == null) {
            throw new UserNotFoundException("User with email " + normalizedEmail + " not found");
        }
        if (!user.verifyPassword(Mapper.convertToLowerCase(request.getPassword()))){
            throw new InvalidUserException("Invalid password");
        }
        return Mapper.toLoginResponse(user);
    }


    @Override
    public CreateExpenseResponse addExpense(String email, CreateExpenseRequest request) {
        String normalizedEmail = Mapper.convertToLowerCase(email);
        User user = userRepository.findByEmail(normalizedEmail);
        if (user == null) {
            throw new UserNotFoundException("User with email " + normalizedEmail + " not found");
        }
        validateAddExpenseRequest(request);
        String userId = user.getId();
        return expenseService.addExpenseResponse(request, userId);
    }


    @Override
    public LoginResponse getUserByEmail(String email) {
        String normalizedEmail = Mapper.convertToLowerCase(email);
        User user = userRepository.findByEmail(normalizedEmail);
        if (user == null) {
            throw new UserNotFoundException("User with email " + normalizedEmail + " not found");
        }
        return Mapper.toLoginResponse(user);
    }


    private void validateRegisterRequest(RegisterRequest request) {
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new InvalidUserException("Username is required");
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new InvalidUserException("Email is required");
        }
        if (request.getPassword() == null || request.getPassword().trim().length() < 6) {
            throw new InvalidUserException("Password must be at least 6 characters");
        }
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