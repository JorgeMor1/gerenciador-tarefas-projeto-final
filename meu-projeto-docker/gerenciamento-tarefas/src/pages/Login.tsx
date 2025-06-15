import { useForm } from "react-hook-form";
import { useAuth } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { useEffect } from "react";

export default function Login() {
  const { register, handleSubmit } = useForm();
  const { login, user } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (user) {
      navigate("/dashboard");
    }
  }, [user]);


  const onSubmit = async (data: any) => {
    console.log("Enviando login com:", data);
    try {
      await login(data.email, data.password);
      navigate("/dashboard");
    } catch (err) {
      console.error("Erro no login:", err);
      alert("Login inválido");
    }
  };



  return (
    <div className="container mt-5">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <h2 className="text-center mb-4">Gerenciador de Tarefas</h2>
          <form onSubmit={handleSubmit(onSubmit)}>
            <div className="mb-3">
              <label htmlFor="email" className="form-label">Email</label>
              <input type="email" {...register("email")} placeholder="Digite seu E-mail" className="form-control" />
              </div>
              <div className="mb-3">
                <label htmlFor="password" className="form-label">Senha</label>
                <input type="password" {...register("password")} placeholder="Digite sua Senha" className="form-control" />
              </div>
              <button type="submit" className="btn btn-primary w-100">Entrar</button>
          </form>
        </div>
      </div>
    </div>
  );
}


