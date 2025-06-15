import api from "./Api";
import type { User } from "../componentes/types/User";

export const getAllUsers = async (): Promise<User[]> => {
  const res = await api.get("/admin/users");
  return res.data;
};

export const updateResponsible = async (taskId: number, newResponsibleId: number) => {
  try {
    const response = await api.put(`/tasks/${taskId}/responsible`, {
      responsibleId: newResponsibleId,
    });
    return response.data;
  } catch (error: any) {
    let errorMessage = "Erro ao atualizar responsável.";
    if (error.response?.data?.message) {
      errorMessage = error.response.data.message;
    }
    throw new Error(errorMessage);
  }
};
