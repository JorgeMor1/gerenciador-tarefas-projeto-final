//Select criado para poder fazer a seleção de Status

export type StatusOption = "TODO" | "IN_PROGRESS" | "DONE";


const statusLabels: Record<StatusOption, string> = {
  TODO: "A Fazer",
  IN_PROGRESS: "Em Progresso",
  DONE: "Concluído",
};

interface StatusSelectProps {
  value: StatusOption;
  onChange: (value: StatusOption) => void;
  label?: string;
}

export default function StatusSelect({
  value,
  onChange,
  label = "Status",
}: StatusSelectProps) {
  return (
    <div className="mb-3">
      <label className="form-label">{label}</label>
      <select
        className="form-select"
        value={value}
        onChange={(e) => onChange(e.target.value as StatusOption)}
      >
        {Object.entries(statusLabels).map(([key, label]) => (
          <option key={key} value={key}>
            {label}
          </option>
        ))}
      </select>
    </div>
  );
}
