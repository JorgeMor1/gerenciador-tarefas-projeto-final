import { useEffect, useState } from "react";
import { useAuth } from "../context/AuthContext";
import TaskCard from "../pages/TaskCards";
import TaskFilters from "../componentes/TaskFilter";
import {  filterTasks, updateTask } from "../services/TaskService";
import { getAllUsers } from "../services/UserService";
import { Navigate, useNavigate } from "react-router-dom";
import type { Tasks } from "../componentes/types/Tasks";
import TaskTable from "../componentes/TaskTable";
import type { User } from "../componentes/types/User";

export default function Dashboard() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [tasks, setTasks] = useState<Tasks[]>([]);
  const [users, setUsers] = useState<User[]>([]);

  if (!user) {
    return <Navigate to="/login" />;
  }

  const loadTasks = async (filters: Record<string, any> = {}) => {
    try {
      let data;
  
      if (user.role === "ADMIN") {
        data = await filterTasks(filters); // Admin: filtro normal
      } else {
        // Força o filtro para o usuário logado
        const userFilters = { ...filters, userId: String(user.id) };
        data = await filterTasks(userFilters); // Usuário comum: filtro com userId
      }
  
      setTasks(data);
    } catch (error) {
      console.error("Erro ao carregar tarefas:", error);
    }
    /*let data;
    if (user.role === "ADMIN") {
       data = await filterTasks(filters);
      setTasks(data);
    } else {
       data = await getMyTasks();
       setTasks(data);
      }*/
      //console.log("Dados carregados: ", data)
  };

    const loadUsers = async () => {
      if (user.role === "ADMIN") {
        try {
          const data = await getAllUsers();
          setUsers(data);
        } catch (error) {
          console.error("Erro ao carregar usuários:", error);
        }
      }
    };

  const handleUpdateTask = async (id: number, update: any) => {
    await updateTask(id, update);
    loadTasks();
  };

  useEffect(() => {
    loadTasks();
    loadUsers();
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <div className="container mt-4">
    <div className="d-flex justify-content-between align-items-center mb-4">
      <h2 className="mb-0">Projeto Final do DevClass</h2>
      <button onClick={handleLogout} className="btn btn-outline-danger">Sair</button>
    </div>
     <div className="mb-4">
      <TaskFilters onFilter={loadTasks} isAdmin={user.role === "ADMIN"} />
</div>
      {user.role === "ADMIN" ? (
        <>
        <div className="mb-4">
          <TaskTable tasks={tasks} users={users} />
          </div>
          <div className="d-flex gap-2 flex-wrap">
          <button onClick={() => navigate("/tarefas/nova")} className="btn btn-primary">
            Nova Tarefa
            </button>
          <button onClick={() => navigate("/usuarios")} className="btn btn-secondary">Gerenciar Usuários</button>
          </div>
        </>
      ) : (
        <div className="row">
        {tasks.map(task => (
          <div className="col-md-6 col-lg-4 mb-4" key={task.id}>
            <TaskCard task={task} users={users}  onUpdate={handleUpdateTask} />
          </div>
      ))}
    </div>
    )}
     </div>
  );
}