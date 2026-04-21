import React, { useState } from "react";
import ExpenseForm from "./components/ExpenseForm";
import ExpenseList from "./components/ExpenseList";

function App() {
  const [refresh, setRefresh] = useState(false);
  const [error, setError] = useState(null);

  return (
    <div style={{ maxWidth: "600px", margin: "auto", fontFamily: "Arial" }}>
      <h1 style={{ textAlign: "center" }}>Expense Tracker</h1>

      {/* Show global error */}
      {error && <p style={{ color: "red" }}>{error}</p>}

      <ExpenseForm
        onSuccess={() => setRefresh(!refresh)}
        setError={setError}
      />

      <hr />

      <ExpenseList
        refresh={refresh}
        setError={setError}
      />
    </div>
  );
}

export default App;