const BASE_URL = "https://expense-tracker-1-x1hc.onrender.com";

export const fetchExpenses = async (category, sort) => {
  let url = `${BASE_URL}/expenses?sort=${sort}`;

  if (category) {
    url += `&category=${category}`;
  }

  const res = await fetch(url);

  if (!res.ok) throw new Error("Failed to fetch expenses");

  return res.json();
};

export const createExpense = async (expense) => {
  const res = await fetch(`${BASE_URL}/expenses`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      "Idempotency-Key": Date.now().toString(),
    },
    body: JSON.stringify(expense),
  });

  if (!res.ok) throw new Error("Failed to create expense");

  return res.json();
};