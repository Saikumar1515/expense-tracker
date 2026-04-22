package org.expensetracker.controller;

import jakarta.validation.Valid;
import org.expensetracker.dto.ExpenseRequest;
import org.expensetracker.entity.Expense;
import org.expensetracker.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @PostMapping
    public ResponseEntity<Expense> createExpense(
            @Valid @RequestBody ExpenseRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String key) {

        if (key == null || key.isEmpty()) {
            key = String.valueOf(System.currentTimeMillis());
        }

        Expense expense = service.createExpense(request, key);
        return ResponseEntity.status(201).body(expense);
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "date_desc") String sort) {

        return ResponseEntity.ok(service.getExpenses(category, sort));
    }
}