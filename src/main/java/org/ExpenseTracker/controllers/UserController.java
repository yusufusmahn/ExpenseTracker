package org.ExpenseTracker.controllers;

import org.ExpenseTracker.dtos.requests.*;
import org.ExpenseTracker.dtos.responses.*;
import org.ExpenseTracker.exceptions.*;
import org.ExpenseTracker.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@RequestBody RegisterRequest request) {
        try {
            RegisterResponse response = userService.registerUser(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.CREATED);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = userService.login(request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/{email}/expenses")
    public ResponseEntity<ApiResponse> addExpense(@PathVariable("email") String email, @RequestBody CreateExpenseRequest request) {
        try {
            CreateExpenseResponse response = userService.addExpense(email, request);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/{email}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable("email") String email) {
        try {
            LoginResponse response = userService.getUserByEmail(email);
            return new ResponseEntity<>(new ApiResponse(response, true), HttpStatus.OK);
        } catch (ExpenseTrackerException e) {
            return new ResponseEntity<>(new ApiResponse(e.getMessage(), false), HttpStatus.NOT_FOUND);
        }
    }
}