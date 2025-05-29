import { useState } from "react";
import type { Tasks } from "../componentes/types/Tasks";
import type { StatusOption } from "../../src/componentes/StatusSelect";
import StatusSelect from "../../src/componentes/StatusSelect";


type TaskProps = {
  task: Tasks;
  onUpdate?: (id: number, update: Partial<Tasks>) => void;
};

export default function TaskCard({ task, onUpdate }: TaskProps) {
  const [description] = useState(task.description || "");
  const [editando, setEditando] = useState(false);
  const [status, setStatus] = useState(task.status);

  const handleSalvar = () => {
    onUpdate?.(task.id, {
      description,
      status,
    });
    setEditando(false);
  };


  const statusMap = {
  DONE: 'Concluída',
  IN_PROGRESS: 'Em andamento',
  TODO: 'A fazer',
};

  return (
    <div className="card mb-3 shadow-sm">
      <div className="card-body">
        <h5 className="card-title">{task.title}</h5>
        <p className="card-text"><strong>Descrição:</strong> {task.description}</p>
        <p className="card-text">
    <strong>Status:</strong> {statusMap[task.status] || 'Desconhecido'}
  </p>
        <p className="card-text"><strong>Entrega:</strong> {new Date(task.dueDate).toLocaleDateString()}</p>

        {editando ? (
          <>
            <div className="mb-2">

               <StatusSelect
      value={status as StatusOption}
      onChange={(value: StatusOption) => setStatus(value)}
    />

            </div>
            <button className="btn btn-success btn-sm me-2" onClick={handleSalvar}>
              Salvar e Finalizar
            </button>
            <button className="btn btn-secondary btn-sm" onClick={() => setEditando(false)}>
              Cancelar
            </button>
          </>
        ) : (
          <>
            {task.status !== "DONE" && (
              <button className="btn btn-primary btn-sm" onClick={() => setEditando(true)}>
                Editar
              </button>
            )}
          </>
        )}
      </div>
    </div>
  );
}
