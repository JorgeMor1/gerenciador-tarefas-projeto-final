import { Navigate, useNavigate } from "react-router-dom";
import type { Tasks } from "./types/Tasks";
import { useState } from "react";

type Props = {
  tasks: Tasks[];
};



export default function TaskTable({ tasks }: Props) {
    const navigate = useNavigate();

  const statusLabels: Record<string, string> = {
    TODO: "A Fazer",
    IN_PROGRESS: "Em Progresso",
    DONE: "Concluída",
  };

  const formatDate = (dateStr: string) =>
    new Date(dateStr).toLocaleDateString("pt-BR", { timeZone: "UTC" });

  const handleEdit = (id: number) => navigate(`/tarefas/${id}/editar`);

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
