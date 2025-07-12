import { Routes, Route, Navigate } from "react-router-dom";
import AuthForm from "./auth/AuthForm";   // Login ve Register toggle component
import Dashboard from "./pages/Dashboard";
import { useAuth } from "./context/AuthContext";

function App() {
  const { token } = useAuth();
  console.log("Token in App.js:", token);


  return (
    <div className="App">
      <Routes>
        <Route path="/auth" element={<AuthForm />} />
        <Route
          path="/dashboard"
          element={token ? <Dashboard /> : <Navigate to="/auth" />}
        />
        
        <Route path="/" element={<Navigate to="/auth" />} />
        <Route path="*" element={<Navigate to="/auth" />} />
      </Routes>
    </div>
  );
}

export default App;
