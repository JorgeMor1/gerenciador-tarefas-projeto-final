import api from "./Api";
import type { Tasks } from "../componentes/types/Tasks";
import type { User } from "../componentes/types/User";

export const getMyTasks = async () => {
  const res = await api.get("/tasks/user");
  return res.data;
};

export const getAllTasks = async () => {
  const res = await api.get("/tasks/admin");
  return res.data;
};

type TaskFilters = {
  status?: string;
  startDate?: string;
  endDate?: string;
  userId?: string;
};

export const filterTasks = async (filters: TaskFilters) => {
  const params = new URLSearchParams(filters).toString();
  const res = await api.get(`/tasks/filter?${params}`);
  return res.data;
};

export const updateTask = async (id: number, update: Partial<Tasks>) => {
    const token = localStorage.getItem("accessToken");
  if (!token) {
    throw new Error("Usuário não autenticado. Token ausente.");
  }

  const res = await api.put(`/tasks/user/${id}`, update, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
  return res.data;
};

export const getTaskById = async (id: number): Promise<Tasks> => {
  const res = await api.get(`/tasks/admin/${id}`);
  return res.data;
};

type NewTaskPayload = {
  title: string;
  description: string;
  dueDate: string;
  status?: "TODO" | "IN_PROGRESS" | "DONE";
  responsible?: number; // ID do responsável
};


export async function createTask(task: NewTaskPayload): Promise<Tasks> {
  const res = await api.post("/tasks/create", task);
  return res.data;
}


export const updateResponsible = async (taskId: number, responsibleId: number | null) => {
  const token = localStorage.getItem("accessToken");
  if (!token) throw new Error("Token JWT ausente.");

  const res = await api.put(`/tasks/${taskId}/responsible`, 
    { responsibleId }, 
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return res.data;
};


export const getAllUsers = async (): Promise<User[]> => {
  const token = localStorage.getItem("accessToken");
  console.log("Token atual:", token);

  if (!token) {
    throw new Error("Usuário não autenticado. Token ausente.");
  }

  const response = await fetch(`${process.env.REACT_APP_API_URL}/admin/users`, {
    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  if (!response.ok) {
    throw new Error("Erro ao buscar usuários");
  }

  return response.json();
};