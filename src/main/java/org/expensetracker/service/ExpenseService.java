package org.expensetracker.service;

import org.expensetracker.dto.ExpenseRequest;
import org.expensetracker.entity.Expense;
import org.expensetracker.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    public Expense createExpense(ExpenseRequest request, String idempotencyKey) {

        //  Idempotency check
        Optional<Expense> existing = repository.findByIdempotencyKey(idempotencyKey);
        if (existing.isPresent()) {
            return existing.get();
        }

        //  Business validation
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        if (request.getDate() == null) {
            throw new IllegalArgumentException("Date is required");
        }

        //  DTO → Entity mapping
        Expense expense = new Expense();
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate());

        // System fields
        expense.setCreatedAt(LocalDateTime.now());
        expense.setIdempotencyKey(idempotencyKey);

        return repository.save(expense);
    }

    public List<Expense> getExpenses(String category, String sort) {

        //  Safe handling (avoid null pointer)
        boolean isDesc = (sort == null) || sort.equalsIgnoreCase("date_desc");

        //  Filter + Sort
        if (category != null && !category.trim().isEmpty()) {
            return isDesc
                    ? repository.findByCategoryIgnoreCaseOrderByDateDesc(category)
                    : repository.findByCategoryIgnoreCaseOrderByDateAsc(category);
        }

        //  Only Sort
        return isDesc
                ? repository.findAllByOrderByDateDesc()
                : repository.findAllByOrderByDateAsc();
    }
}