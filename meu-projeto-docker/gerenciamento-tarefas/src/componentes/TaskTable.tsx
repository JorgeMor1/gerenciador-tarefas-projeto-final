import {  useNavigate } from "react-router-dom";
import type { Tasks } from "./types/Tasks";
//import { useState } from "react";
//import { get } from "react-hook-form";
import type { User } from "./types/User";

type Props = {
  tasks: Tasks[];
  users: User[];
};


export default function TaskTable({ tasks, users }: Props) {
  const navigate = useNavigate();
  
  const statusLabels: Record<string, string> = {
    TODO: "A Fazer",
    IN_PROGRESS: "Em Progresso",
    DONE: "Concluída",
  };
  
  const formatDate = (dateStr: string) =>
    new Date(dateStr).toLocaleDateString("pt-BR", { timeZone: "UTC" });
  
  const getUserNameById = (id?: number) => {
    if (!users) return "Sem responsável"; 
    
    const user = users.find((u) => u.id === id);
    return user ? user.name : "Sem responsável";
  };
  

  //const handleEdit = (id: number) => navigate(`/tarefas/${id}/editar`);

  if (tasks.length === 0) {
    return <div className="alert alert-info">Nenhuma tarefa encontrada.</div>;
  }

  return (
    <div className="table-responsive">
    <table className="table table-striped table-hover align-middle" aria-label="Lista de tarefas">
      <thead className="table-dark">
        <tr>
          <th>Título</th>
          <th>Status</th>
          <th>Responsável</th>
          <th>Data de Entrega</th>
          <th>Ações</th>
        </tr>
      </thead>
      <tbody>
        {tasks.map((task) => (
          <tr key={task.id}>
            <td>{task.title}</td>
            <td>{task.status ? statusLabels[task.status] : "Sem status"}</td>
            <td>{ getUserNameById(task.responsible)}</td>
            <td>{formatDate(task.dueDate)}</td>
            <td style={{ minWidth: "100px" }}>
                <button className="btn btn-outline-primary btn-sm" onClick={() => navigate(`/tarefas/${task.id}/editar`)}>
        Editar
            </button>
            </td>
          </tr>

        ))}
      </tbody>
    </table>
    </div>
  );
}
