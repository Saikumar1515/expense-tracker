import React, { useEffect, useState, useCallback } from "react";
import { fetchExpenses } from "../services/api";

function ExpenseList({ refresh }) {
  const [expenses, setExpenses] = useState([]);
  const [category, setCategory] = useState("");
  const [sort, setSort] = useState("date_desc");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  //
  const loadData = useCallback(async () => {
    setLoading(true);
    setError("");

    try {
      const data = await fetchExpenses(category, sort);
      setExpenses(data);
    } catch (err) {
      setError("Failed to load expenses");
    } finally {
      setLoading(false);
    }
  }, [category, sort]);

  //
  useEffect(() => {
    loadData();
  }, [loadData, refresh]);

  // Total calculation
  const total = expenses.reduce(
    (sum, e) => sum + Number(e.amount || 0),
    0
  );

  // Summary by category
  const summaryByCategory = expenses.reduce((acc, e) => {
    const cat = e.category || "Others";
    acc[cat] = (acc[cat] || 0) + Number(e.amount || 0);
    return acc;
  }, {});

  return (
    <div>
      {/* Filter */}
      <input
        placeholder="Filter category"
        value={category}
        onChange={(e) => setCategory(e.target.value)}
      />

      {/* Sort */}
      <select value={sort} onChange={(e) => setSort(e.target.value)}>
        <option value="date_desc">Newest First</option>
        <option value="date_asc">Oldest First</option>
      </select>

      {/* States */}
      {loading && <p>Loading expenses...</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}

      {/* Total */}
      <h3>Total: ₹{total}</h3>

      {/* Summary View */}
      {expenses.length > 0 && (
        <>
          <h4>Summary by Category:</h4>
          <ul>
            {Object.entries(summaryByCategory).map(([cat, amount]) => (
              <li key={cat}>
                {cat}: ₹{amount}
              </li>
            ))}
          </ul>
        </>
      )}

      {/* Empty State */}
      {!loading && expenses.length === 0 && (
        <p>No expenses found</p>
      )}

      {/* List */}
      <ul>
        {expenses.map((e) => (
          <li key={e.id}>
            ₹{e.amount} | {e.category} | {e.date}
          </li>
        ))}
      </ul>
    </div>
  );
}

export default ExpenseList;