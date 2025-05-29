import React, { useState } from 'react';
import axios from 'axios';
import 'bootstrap/dist/css/bootstrap.min.css';

interface RegisterProps {
  onSuccess: () => void;
}

const Register: React.FC<RegisterProps> = ({ onSuccess }) =>{
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    role: 'user'
  });

  const [error, setError] = useState('');

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post('${process.env.REACT_APP_API_URL}/auth/register', formData);
        onSuccess();
    } catch (err: any) {
      setError('Erro ao cadastrar usuário. Verifique os dados ou tente novamente.');
    }
  };

  return (
      <form onSubmit={handleSubmit}>
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
          <option value="user">Usuário</option>
          <option value="admin">Administrador</option>
        </select>

        <button className="btn btn-primary w-100" type="submit">Cadastrar Usuário</button>

        {error && <div className="alert alert-danger mt-3">{error}</div>}
      </form>
  );
};

export default Register;