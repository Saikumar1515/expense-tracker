package org.expensetracker.repository;

import org.expensetracker.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    //  Idempotency
    Optional<Expense> findByIdempotencyKey(String key);

    // Filter by category (case-insensitive) + sorting
    List<Expense> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    List<Expense> findByCategoryIgnoreCaseOrderByDateAsc(String category);

    // Get all expenses (sorted)
    List<Expense> findAllByOrderByDateDesc();

    List<Expense> findAllByOrderByDateAsc();
}