package org.expensetracker.controller;

import jakarta.validation.Valid;
import org.expensetracker.dto.ExpenseRequest;
import org.expensetracker.entity.Expense;
import org.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    // Create Expense
    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String key) {

        // Handle missing key (basic fallback)
        if (key == null || key.isEmpty()) {
            key = String.valueOf(System.currentTimeMillis());
        }

        Expense expense = service.createExpense(request, key);

        // 201 Created is more correct than 200
        return ResponseEntity.status(201).body(expense);
    }

    // Get Expenses (Filter + Sort)
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "desc") String sort) {

        List<Expense> expenses = service.getExpenses(category, sort);
        return ResponseEntity.ok(expenses);
    }
}