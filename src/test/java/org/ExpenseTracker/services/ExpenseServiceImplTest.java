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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ExpenseServiceImplTest {

    @Autowired
    private ExpenseServiceImpl expenseService;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        expenseRepository.deleteAll();
        userRepository.deleteAll();

        RegisterRequest userRequest = new RegisterRequest();
        userRequest.setUsername("testuser");
        userRequest.setEmail("test@gmail.com");
        userRequest.setPassword("password123");
        userService.registerUser(userRequest);

        assertNotNull(userService.getUserByEmail("test@gmail.com"));
        LoginResponse user = userService.getUserByEmail("test@gmail.com");
        assertNotNull(user.getId());

        expenseRepository.deleteAll();
    }

    @Test
    public void testAddExpenseResponse_Success() {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(100.0);
        request.setDate(LocalDateTime.now().plusDays(1));
        request.setCategory("Food");
        request.setDescription("Dinner");

        CreateExpenseResponse response = expenseService.addExpenseResponse(request, userService.getUserByEmail("test@gmail.com").getId());

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(100.0, response.getAmount());
        assertEquals("Food", response.getCategory());
    }

    @Test
    public void testAddExpenseResponse_InvalidAmount() {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(-100.0);
        request.setDate(LocalDateTime.now().plusDays(1));
        request.setCategory("Food");
        request.setDescription("Dinner");

        assertThrows(InvalidExpenseException.class, () ->
            expenseService.addExpenseResponse(request, userService.getUserByEmail("test@gmail.com").getId()));
    }

    @Test
    public void testGetExpensesByUserId_Success() {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(100.0);
        request.setDate(LocalDateTime.now().plusDays(1));
        request.setCategory("Food");
        request.setDescription("Dinner");
        String userId = userService.getUserByEmail("test@gmail.com").getId();
        expenseService.addExpenseResponse(request, userId);

        List<SearchExpensesResponse> responses = expenseService.getExpensesByUserId(userId);

        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
        assertEquals(100.0, responses.get(0).getAmount());
    }

    @Test
    public void testGetExpensesByUserId_InvalidUserId() {
        assertThrows(InvalidExpenseException.class, () -> expenseService.getExpensesByUserId(null));
    }

    @Test
    public void testUpdateExpense_Success() {
        CreateExpenseRequest initialRequest = new CreateExpenseRequest();
        initialRequest.setAmount(100.0);
        initialRequest.setDate(LocalDateTime.now().plusDays(1));
        initialRequest.setCategory("Food");
        initialRequest.setDescription("Dinner");
        String userId = userService.getUserByEmail("test@gmail.com").getId();
        CreateExpenseResponse initialResponse = expenseService.addExpenseResponse(initialRequest, userId);
        String expenseId = initialResponse.getId();

        CreateExpenseRequest updateRequest = new CreateExpenseRequest();
        updateRequest.setAmount(200.0);
        updateRequest.setDate(LocalDateTime.now().plusDays(2));
        updateRequest.setCategory("Travel");
        updateRequest.setDescription("Trip");

        CreateExpenseResponse updatedResponse = expenseService.updateExpense(expenseId, updateRequest);

        assertNotNull(updatedResponse);
        assertEquals(200.0, updatedResponse.getAmount());
        assertEquals("Travel", updatedResponse.getCategory());
    }

    @Test
    public void testUpdateExpense_NotFound() {
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(200.0);
        request.setDate(LocalDateTime.now().plusDays(2));
        request.setCategory("Travel");
        request.setDescription("Trip");

        assertThrows(ExpenseNotFoundException.class, () -> expenseService.updateExpense("nonexistentId", request));
    }

    @Test
    public void testSearchExpenses_Success() {
        String userId = userService.getUserByEmail("test@gmail.com").getId();
        CreateExpenseRequest request1 = new CreateExpenseRequest();
        request1.setAmount(100.0);
        request1.setDate(LocalDateTime.now().plusDays(1));
        request1.setCategory("Food");
        request1.setDescription("Dinner");
        expenseService.addExpenseResponse(request1, userId);

        CreateExpenseRequest request2 = new CreateExpenseRequest();
        request2.setAmount(150.0);
        request2.setDate(LocalDateTime.now().plusDays(2));
        request2.setCategory("Travel");
        request2.setDescription("Trip");
        expenseService.addExpenseResponse(request2, userId);

        List<SearchExpensesResponse> responses = expenseService.searchExpenses(userId, "Food");

        assertEquals(1, responses.size());
        assertEquals(100.0, responses.get(0).getAmount());
        assertEquals("Food", responses.get(0).getCategory());
    }

    @Test
    public void testSearchExpenses_NoMatch() {
        String userId = userService.getUserByEmail("test@gmail.com").getId();
        CreateExpenseRequest request = new CreateExpenseRequest();
        request.setAmount(100.0);
        request.setDate(LocalDateTime.now().plusDays(1));
        request.setCategory("Food");
        request.setDescription("Dinner");
        expenseService.addExpenseResponse(request, userId);

        assertThrows(ExpenseNotFoundException.class, () -> expenseService.searchExpenses(userId, "Travel"));
    }
}