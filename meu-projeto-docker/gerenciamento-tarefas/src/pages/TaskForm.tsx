import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { createTask, getTaskById, updateTask } from "../services/TaskService";
import type { Tasks } from "../componentes/types/Tasks";
import { useAuth } from "../context/AuthContext";
import type { User } from "../componentes/types/User";
import { type StatusOption } from "../../src/componentes/StatusSelect";
import StatusSelect from "../../src/componentes/StatusSelect";
import { getAllUsers } from "../services/TaskService";
import { updateResponsible } from "../services/UserService";
import api from "../services/Api";


export default function TarefaForm() {
  const { id } = useParams(); // ID da rota, se existir estamos editando
  const navigate = useNavigate();
  const { user } = useAuth();

  const [form, setForm] = useState<Partial<Tasks>>({
    title: "",
    description: "",
    dueDate: "",
    status: "TODO",
    responsible: undefined,

  });

  const [users, setUsers] = useState<User[]>([]);

  useEffect(() => {
    getAllUsers()
      .then(setUsers)
      .catch(() => console.error("Erro ao buscar usuários"));
  }, []);


  const [carregando, setCarregando] = useState(false);
  const [erro, setErro] = useState("");


  useEffect(() => {
    if (id) {
      setCarregando(true);
      getTaskById(Number(id))
        .then((data) => setForm(data))
        .catch(() => setErro("Erro ao carregar tarefa"))
        .finally(() => setCarregando(false));
    }
  }, [id]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    const { name, value } = e.target;

    setForm((prev) => ({
      ...prev,
      [name]: name === "responsible" ? Number(value) : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setErro("");

    if (!form.title || !form.description || !form.dueDate) {
      setErro("Preencha todos os campos obrigatórios.");
      return;
    }

    try {
      if (user?.role === "ADMIN" && !form.responsible) {
        setErro("Selecione um responsável para a tarefa.");
        return;
      }
      const payload = {
        title: form.title,
        description: form.description,
        dueDate: form.dueDate,
        status: form.status ?? undefined,
      };

      if (id) {
        const originalTask = await getTaskById(Number(id));

        const originalResponsibleId = Number(originalTask.responsible);
        const newResponsibleId = Number(form.responsible);

        if (newResponsibleId !== originalResponsibleId && user?.role === "ADMIN") {
          await updateResponsible(Number(id), Number(newResponsibleId));
        }

        await updateTask(Number(id), payload);
      } else {
        console.log("Payload enviado:", { ...payload, responsibleId: form.responsible });
        await createTask({ ...payload, responsibleId: form.responsible });
      }

      navigate("/dashboard");

    } catch (error: any) {
      console.error(error);
      setErro("Erro ao salvar tarefa");
    }
  };


  const handleDelete = async () => {
    if (!id) return;

    const confirmDelete = window.confirm("Tem certeza que deseja deletar esta tarefa?");
    if (!confirmDelete) return;

    try {
      await api.delete(`/tasks/${id}`);
      alert("Tarefa deletada com sucesso!");
      navigate("/dashboard");
    } catch (err: any) {
      console.error(err);
      alert("Erro ao deletar tarefa: " + (err.response?.data?.message || "Erro desconhecido"));
    }
  };




  const handleResponsibleChange = async (e: React.ChangeEvent<HTMLSelectElement>) => {
    const selectedId = Number(e.target.value);
    setForm((prev) => ({
      ...prev,
      responsible: selectedId,
    }));

    if (id) {  // 'id' vindo do useParams()
      try {
        await updateResponsible(Number(id), selectedId);
      } catch (error) {
        console.error("Erro ao atualizar responsável:", error);
      }
    }
  };




  return (
    <div className="container mt-4">
      <h2>{id ? "Editar Tarefa" : "Nova Tarefa"}</h2>

      {erro && <div className="alert alert-danger">{erro}</div>}

      {carregando ? (
        <p>Carregando...</p>
      ) : (
        <form onSubmit={handleSubmit}>
          <div className="mb-3">
            <label className="form-label">Título*</label>
            <input
              type="text"
              className="form-control"
              name="title"
              value={form.title}
              onChange={handleChange}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Descrição*</label>
            <textarea
              className="form-control"
              name="description"
              value={form.description}
              onChange={handleChange}
            />
          </div>



          <StatusSelect
            value={form.status as StatusOption}
            onChange={(value: StatusOption) => setForm({ ...form, status: value })}
          />





          <div className="mb-3">
            <label className="form-label">Data de Entrega*</label>
            <input
              type="date"
              className="form-control"
              name="dueDate"
              value={form.dueDate?.substring(0, 10)}
              onChange={handleChange}
            />
          </div >


          <div className="mb-3">
            <label className="form-label">Responsável</label>
            <select
              className="form-select"
              name="responsible"
              value={form.responsible ?? ""}
              onChange={handleResponsibleChange}
              required={user?.role === "ADMIN"}

            >
              <option value="">Selecione um responsável</option>
              {users.map((user) => (
                <option key={user.id} value={user.id}>
                  {user.name}
                </option>
              ))}
            </select>
          </div>


          {user?.role === "ADMIN" && id && (
            <button
              type="button"
              className="btn btn-danger me-2"
              onClick={handleDelete}
            >
              Deletar
            </button>
          )}

          <button type="submit" className="btn btn-primary me-2">
            Salvar
          </button>
          <button type="button" className="btn btn-secondary" onClick={() => navigate("/dashboard")}>
            Cancelar
          </button>
        </form>
      )}
    </div>
  );
}
