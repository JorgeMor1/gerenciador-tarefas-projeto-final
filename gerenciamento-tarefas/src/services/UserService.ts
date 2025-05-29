import api from "./Api";
import type { User } from "../componentes/types/User";

export const getAllUsers = async (): Promise<User[]> => {
  const res = await api.get("/admin/users");
  return res.data;
};

  export const updateResponsible = async (taskId: number, newResponsibleId: number) => {
  const token = localStorage.getItem("token"); 

  if (!token) {
    throw new Error("Token não encontrado");
  }

 const response = await fetch(`http://localhost:8080/tasks/${taskId}/responsible`, {
  method: "PUT",
  headers: {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${localStorage.getItem("accessToken")}`,
  },
  body: JSON.stringify({ responsibleId: newResponsibleId }),
});

if (!response.ok) {
  let errorMessage = "Erro ao atualizar responsável.";
  try {
    const errorData = await response.json();
    errorMessage = errorData.message || errorMessage;
  } catch (_) {
    // corpo vazio, nada a fazer
  }
  throw new Error(errorMessage);
}

};
