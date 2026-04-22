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

    public Expense createExpense(ExpenseRequest request, String key) {

        return repository.findByIdempotencyKey(key)
                .orElseGet(() -> {
                    Expense e = new Expense();
                    e.setAmount(request.getAmount());
                    e.setCategory(request.getCategory());
                    e.setDescription(request.getDescription());
                    e.setDate(request.getDate());
                    e.setCreatedAt(LocalDateTime.now());
                    e.setIdempotencyKey(key);
                    return repository.save(e);
                });
    }

    public List<Expense> getExpenses(String category, String sort) {

        boolean asc = "asc".equalsIgnoreCase(sort);

        List<Expense> list;

        if (category != null && !category.isEmpty()) {
            list = repository.findByCategoryIgnoreCase(category);
        } else {
            list = asc
                    ? repository.findAllByOrderByDateAsc()
                    : repository.findAllByOrderByDateDesc();
        }

        return list;
    }
}