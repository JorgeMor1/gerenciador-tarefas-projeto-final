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
  const token = localStorage.getItem("accessToken");
  if (!token) {
    throw new Error("Usuário não autenticado. Token ausente.");
  }
  const cleanedFilters = Object.entries(filters)
    .filter(([_, value]) => value !== undefined && value !== "")
    .reduce((acc, [key, value]) => {
      acc[key] = value;
      return acc;
    }, {} as Record<string, string>);

  const params = new URLSearchParams(cleanedFilters).toString();
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
  responsibleId?: number; // ID do responsável
};


export async function createTask(task: NewTaskPayload): Promise<Tasks> {
  const res = await api.post("/tasks/create", task);
  return res.data;
}


/*export const updateResponsible = async (taskId: number, responsibleId: number | null) => {
  const token = localStorage.getItem("accessToken");
  if (!token) throw new Error("Token JWT ausente.");

  const res = await api.put(`/tasks/${taskId}/responsible`, 
    { responsibleId }, 
    { headers: { Authorization: `Bearer ${token}` } }
  );
  return res.data;
};*/

export const updateResponsible = async (taskId: number, responsibleId: number | null) => {
  const token = localStorage.getItem("accessToken");

  console.log("Token no updateResponsible:", token);

  if (!token) throw new Error("Token JWT ausente.");

  // Como o interceptor já injeta o token, não é necessário passar o header manualmente
  const res = await api.put(`/tasks/${taskId}/responsible`, { responsibleId });
  return res.data;
};



/*export const getAllUsers = async (): Promise<User[]> => {
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
};*/

export const getAllUsers = async (): Promise<User[]> => {
  const token = localStorage.getItem("accessToken");
  if (!token) {
    throw new Error("Usuário não autenticado. Token ausente.");
  }

  const res = await api.get("/admin/users", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return res.data;
};

export const updateTaskAsAdmin = async (
  taskId: number,
  updates: {
    title?: string;
    description?: string;
    dueDate?: string;
    status?: "TODO" | "IN_PROGRESS" | "DONE";
  }
) => {
  try {
    const response = await api.patch(`/tasks/admin/${taskId}`, updates);
    return response.data;
  } catch (error: any) {
    let errorMessage = "Erro ao atualizar a tarefa.";
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message;
    }
    throw new Error(errorMessage);
  }
};
