import React, { useState } from "react";
import { createExpense } from "../services/api";

function ExpenseForm({ onSuccess }) {
  const [form, setForm] = useState({
    amount: "",
    category: "",
    description: "",
    date: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    //  Validation
    if (!form.amount || Number(form.amount) <= 0) {
      setError("Amount must be greater than 0");
      return;
    }

    if (!form.category.trim()) {
      setError("Category is required");
      return;
    }

    if (!form.date) {
      setError("Date is required");
      return;
    }

    setLoading(true);
    setError("");

    try {
      await createExpense({
        ...form,
        amount: Number(form.amount), // ensure number
        category: form.category.trim(),
        description: form.description.trim(),
      });

      // Reset form
      setForm({ amount: "", category: "", description: "", date: "" });

      onSuccess();
    } catch (err) {
      setError("Failed to save expense. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        placeholder="Amount"
        value={form.amount}
        onChange={(e) => setForm({ ...form, amount: e.target.value })}
      />

      <input
        placeholder="Category"
        value={form.category}
        onChange={(e) => setForm({ ...form, category: e.target.value })}
      />

      <input
        placeholder="Description"
        value={form.description}
        onChange={(e) => setForm({ ...form, description: e.target.value })}
      />

      <input
        type="date"
        value={form.date}
        onChange={(e) => setForm({ ...form, date: e.target.value })}
      />

      <button disabled={loading}>
        {loading ? "Adding..." : "Add Expense"}
      </button>

      {error && <p style={{ color: "red" }}>{error}</p>}
    </form>
  );
}

export default ExpenseForm;