//Classe do 'cabeçalho'

import { useEffect, useState } from "react";

type Props = {
    onFilter: (filters: {
        nome: string;
        status: string;
        responsavel?: string;
        data?: string;
    }) => void;
    isAdmin: boolean;
};

export default function TaskFilters({ onFilter, isAdmin }: Props) {
    const [nome, setNome] = useState("");
    const [status, setStatus] = useState("");
    const [responsavel, setResponsavel] = useState("");
    const [data, setData] = useState("");
    const [autoFiltrar, setAutoFiltrar] = useState(true);

    useEffect(() => {
        if (autoFiltrar) {
            handleFilter();
        }
    }, [nome, responsavel, data, status, autoFiltrar]);

    const handleFilter = () => {
        onFilter({ nome, responsavel, data, status });
    };

    return (
        <div className="container mt-4">
            <div className="row g-3">
                <div className="col-md-4">
                    <input className="form-control" placeholder="Nome da tarefa" value={nome} onChange={(e) => setNome(e.target.value)} />
                </div>
                {isAdmin && (
                    <>
                        <div className="col-md-4">
                            <input className="form-control" placeholder="Responsável" value={responsavel} onChange={(e) => setResponsavel(e.target.value)} />
                        </div>
                        <div className="col-md-4">
                            <input type="date" className="form-control" value={data} onChange={(e) => setData(e.target.value)} />
                        </div>
                    </>
                )}
                <div className="col-md-4"></div>
                <select className="form-select" value={status} onChange={(e) => setStatus(e.target.value)}>
                    <option value="">Todos</option>
                    <option value="TODO">Pendente</option>
                    <option value="IN_PROGRESS">Em Progresso</option>
                    <option value="DONE">Entregue</option>
                </select>
            </div>
            {!autoFiltrar && (
                <div className="col-md-2">
                    <button className="btn btn-primary w-100" onClick={handleFilter}>
                        Filtrar
                    </button>
                </div>
            )}

            <div className="col-md-2 d-flex align-items-center">
                <div className="form-check form-switch">
                    <input
                        className="form-check-input"
                        type="checkbox"
                        id="autoFiltrar"
                        checked={autoFiltrar}
                        onChange={() => setAutoFiltrar(!autoFiltrar)}
                    />
                    <label className="form-check-label" htmlFor="autoFiltrar">
                        Auto Filtrar
                    </label>
                </div>
            </div>
        </div>
    );
}
