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


  /*export const updateResponsible = async (taskId: number, newResponsibleId: number) => {
  const token = localStorage.getItem("accessToken"); 

  if (!token) {
    throw new Error("Token não encontrado");
  }

  try {
    const response = await api.put(`/tasks/${taskId}/responsible`, { responsibleId: newResponsibleId });
    return response.data;
  } catch (error: any) {
    // Tratando erro com mensagem personalizada
    let errorMessage = "Erro ao atualizar responsável.";
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage = error.response.data.message;
    }
    throw new Error(errorMessage);
  }
};*/



 /*const response = await api.put(`${process.env.REACT_APP_API_URL}/tasks/${taskId}/responsible`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`,
      },
      body: JSON.stringify({ responsibleId: newResponsibleId }),
} );



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

};*/
