import React, { useState } from 'react';
import api from '../services/Api';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useNavigate } from 'react-router-dom';

interface RegisterProps {
  onSuccess: () => void;
}

const Register: React.FC<RegisterProps> = ({ onSuccess }) => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    role: 'USER'
  });

  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const endpoint = formData.role === 'ADMIN' ? '/admin/register' : '/admin/user/register';
      await api.post(endpoint, formData);
      onSuccess();
      navigate('/dashboard');
    } catch (err: any) {
      if (err.response) {
        console.error('Erro na resposta:', err.response.data);
      } else if (err.request) {
        console.error('Erro na requisição:', err.request);
      } else {
        console.error('Erro desconhecido:', err.message);
      }
      setError('Erro ao cadastrar usuário. Verifique os dados ou tente novamente.');
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        name="name"
        className="form-control mb-3"
        type="text"
        placeholder="Nome"
        value={formData.name}
        onChange={handleChange}
        required
      />

      <input
        name="email"
        className="form-control mb-3"
        type="email"
        placeholder="Email"
        value={formData.email}
        onChange={handleChange}
        required
      />

      <input
        name="password"
        className="form-control mb-3"
        type="password"
        placeholder="Senha"
        value={formData.password}
        onChange={handleChange}
        required
      />

      <select
        name="role"
        className="form-select mb-3"
        value={formData.role}
        onChange={handleChange}
      >
        <option value="USER">Usuário</option>
        <option value="ADMIN">Administrador</option>
      </select>

      <button className="btn btn-primary w-100" type="submit" >Cadastrar Usuário</button>

      <button className="btn btn-secondary" type="button" onClick={() => navigate("/dashboard")}>
        Cancelar
      </button>

      {error && <div className="alert alert-danger mt-3">{error}</div>}
    </form>
  );
};

export default Register;