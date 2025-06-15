import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import TarefaForm from "../pages/TaskForm";
import Register from "../pages/Register";

export default function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={<Login />} />
      <Route path="/usuarios" element={<Register onSuccess={() => {}} />} />
      <Route
        path="/dashboard"
        element={isAuthenticated ? <Dashboard /> : <Navigate to="/login" />}
      />
      <Route path="*" element={<Navigate to="/dashboard" />} />
      <Route path="/tarefas/nova" element={<TarefaForm />} />
      <Route path="/tarefas/:id/editar" element={<TarefaForm />} />
      {/*<Route path="/usuarios" element={<Register onSuccess={() => { window.location.href = "/dashboard"; }} />}
      />*/}

      
    </Routes>
  );
}
