package org.ExpenseTracker.controllers;


import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;
import org.ExpenseTracker.exceptions.*;
import org.ExpenseTracker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;


//    @PostMapping("/add/{userId}")
//    public ResponseEntity<ApiResponse> addExpense(@PathVariable String userId, @RequestBody CreateExpenseRequest request) {
//        try {
//            CreateExpenseResponse response = expenseService.addExpenseResponse(request, userId);
//            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
//        } catch (ExpenseTracker e) {
//            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
//        }
//    }


    @GetMapping("/total/{userId}")
    public ResponseEntity<ApiResponse> getTotal(@PathVariable("userId") String userId) {
        try {
            double total = expenseService.calculateTotal(userId);
            return new ResponseEntity<>(new ApiResponse(total, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/byUser/{userId}")
    public ResponseEntity<ApiResponse> getExpensesByUserId(@PathVariable("userId") String userId) {
        try {
            List<SearchExpensesResponse> responses = expenseService.getExpensesByUserId(userId);
            return new ResponseEntity<>(new ApiResponse(responses, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/{expenseId}")
    public ResponseEntity<ApiResponse> updateExpense(@PathVariable("expenseId") String expenseId, @RequestBody CreateExpenseRequest request) {
        try {
            CreateExpenseResponse response = expenseService.updateExpense(expenseId, request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{expenseId}")
    public ResponseEntity<ApiResponse> deleteExpense(@PathVariable("expenseId") String expenseId) {
        try {
            boolean deleted = expenseService.deleteExpense(expenseId);
            return new ResponseEntity<>(new ApiResponse(deleted, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/search/{userId}")
    public ResponseEntity<ApiResponse> searchExpenses(
            @PathVariable("userId") String userId,
            @RequestParam(value = "category", required = false) String category){

        try {
            List<SearchExpensesResponse> responses = expenseService.searchExpenses(userId, category);
            return new ResponseEntity<>(new ApiResponse(responses, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{expenseId}")
    public ResponseEntity<ApiResponse> getExpenseById(@PathVariable("expenseId") String expenseId) {
        try {
            SearchExpensesResponse response = expenseService.getExpenseById(expenseId);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }



}