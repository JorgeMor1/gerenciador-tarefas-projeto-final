import { useNavigate } from "react-router-dom";
import type { Tasks } from "./types/Tasks";

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

  const getUserName = (responsible: any) => {
    if (!responsible) return "Sem responsável";

    if (typeof responsible === "object" && "name" in responsible) {
      return responsible.name;
    }

    const user = users.find((u) => u.id === responsible);
    return user ? user.name : "Sem responsável";
  };


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
              <td>{task.status ? statusLabels[task.status] ?? "Desconhecido" : "Sem status"}</td>
              <td>{getUserName(task.responsible)}</td>
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
