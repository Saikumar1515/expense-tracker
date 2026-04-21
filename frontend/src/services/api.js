const BASE_URL = "http://localhost:8080/expenses";

export const fetchExpenses = async (category, sort) => {
  let url = `${BASE_URL}?sort=${sort}`;
  if (category) url += `&category=${category}`;

  const res = await fetch(url);

  if (!res.ok) throw new Error("Failed to fetch expenses");

  return res.json();
};

export const createExpense = async (expense) => {
  const key = Date.now().toString();

  const res = await fetch(BASE_URL, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Idempotency-Key": key,
    },
    body: JSON.stringify(expense),
  });

  if (!res.ok) throw new Error("Failed to create expense");

  return res.json();
};