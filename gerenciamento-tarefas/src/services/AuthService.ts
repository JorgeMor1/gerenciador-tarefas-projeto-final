import api from "./Api";

type LoginResponse = {
  id: number;
  name: string;
  email: string;
  role: string;
  token: string;
};

export async function login(email: string, password: string) {
  const res = await api.post<LoginResponse>("/auth/login", { email, password });
  const { token, ...user } = res.data;

  localStorage.setItem("accessToken", token);
  localStorage.setItem("user", JSON.stringify(user));

  return user;
}

