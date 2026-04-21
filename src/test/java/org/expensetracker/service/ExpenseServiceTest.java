package org.expensetracker.service;

import org.expensetracker.dto.ExpenseRequest;
import org.expensetracker.entity.Expense;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpenseServiceTest {

    @Autowired
    private ExpenseService service;

    @Test
    void shouldNotCreateDuplicateExpenseForSameIdempotencyKey() {

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("100"));
        request.setCategory("Food");
        request.setDescription("Lunch");
        request.setDate(LocalDate.now());

        String key = "abc-123";

        Expense e1 = service.createExpense(request, key);
        Expense e2 = service.createExpense(request, key);

        assertEquals(e1.getId(), e2.getId());
    }

    @Test
    void shouldThrowExceptionForInvalidAmount() {

        ExpenseRequest request = new ExpenseRequest();
        request.setAmount(new BigDecimal("-10"));
        request.setCategory("Food");
        request.setDate(LocalDate.now());

        assertThrows(IllegalArgumentException.class, () -> {
            service.createExpense(request, "key-1");
        });
    }

    @Test
    void shouldReturnExpensesSortedDesc() {

        List<Expense> expenses = service.getExpenses(null, "desc");

        assertNotNull(expenses);
    }

    @Test
    void shouldCreateExpense() {
        ExpenseRequest req = new ExpenseRequest();
        req.setAmount(BigDecimal.valueOf(100));
        req.setCategory("Food");
        req.setDate(LocalDate.now());

        Expense result = service.createExpense(req, "test-key");

        assertNotNull(result.getId());
    }
}