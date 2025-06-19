package org.ExpenseTracker.services;

import org.ExpenseTracker.data.repositories.*;
import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;
import org.ExpenseTracker.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }


    @Test
    void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Testuser");
        request.setEmail("TEST@GMAIL.COM");
        request.setPassword("password");

        RegisterResponse response = userService.registerUser(request);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("Testuser", response.getUsername());
        assertEquals("test@gmail.com", response.getEmail());
        assertNotNull(userRepository.findByEmail("test@gmail.com"));
    }

    @Test
    void testRegisterUser_DuplicateEmail() {
        RegisterRequest request1 = new RegisterRequest();
        request1.setUsername("user1");
        request1.setEmail("duplicate@gmail.com");
        request1.setPassword("pass");
        userService.registerUser(request1);

        RegisterRequest request2 = new RegisterRequest();
        request2.setUsername("user2");
        request2.setEmail("DUPLICATE@GMAIL.COM");
        request2.setPassword("password");

        assertThrows(InvalidUserException.class, () -> userService.registerUser(request2));
    }

    @Test
    void testLogin_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password");
        userService.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("password");

        LoginResponse response = userService.login(loginRequest);

        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
    }

    @Test
    void testLogin_InvalidPassword() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password");
        userService.registerUser(registerRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@gmail.com");
        loginRequest.setPassword("wrongpass");

        assertThrows(InvalidUserException.class, () -> userService.login(loginRequest));
    }

    @Test
    void testAddExpense_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password");
        userService.registerUser(registerRequest);

        CreateExpenseRequest expenseRequest = new CreateExpenseRequest();
        expenseRequest.setAmount(100.0);
        expenseRequest.setDate(LocalDateTime.now().plusDays(1));
        expenseRequest.setCategory("Food");
        expenseRequest.setDescription("Dinner");

        CreateExpenseResponse response = userService.addExpense("test@gmail.com", expenseRequest);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("test@gmail.com", userRepository.findById(response.getUserId()).get().getEmail());
    }

    @Test
    void testAddExpense_UserNotFound() {
        CreateExpenseRequest expenseRequest = new CreateExpenseRequest();
        expenseRequest.setAmount(100.0);
        expenseRequest.setDate(LocalDateTime.now().plusDays(1));
        expenseRequest.setCategory("Food");
        expenseRequest.setDescription("Dinner");

        assertThrows(UserNotFoundException.class, () -> userService.addExpense("nonexistent@gmail.com", expenseRequest));
    }

    @Test
    void testGetUserByEmail_Success() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@gmail.com");
        registerRequest.setPassword("password");
        userService.registerUser(registerRequest);

        LoginResponse response = userService.getUserByEmail("test@gmail.com");

        assertNotNull(response);
        assertEquals("test@gmail.com", response.getEmail());
    }

    @Test
    void testGetUserByEmail_NotFound() {
        assertThrows(UserNotFoundException.class, () -> userService.getUserByEmail("nonexistent@gmail.com"));
    }
}