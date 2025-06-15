
import axios from "axios";

const api = axios.create({
  baseURL: "http://localhost:8080",
  //withCredentials: true,
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  console.log("TOKEN NO INTERCEPTOR:", token);
  

  if (token) {
    config.headers = config.headers || {};
    console.log("Antes de setar Authorization:", config.headers.Authorization);
    config.headers.Authorization = `Bearer ${token}`;
    console.log("Depois de setar Authorization:", config.headers.Authorization);
  }else {
    console.log("Depois de setar Authorization:", config.headers.Authorization);
  }
  return config;
},(error) => {
  return Promise.reject(error);
});

api.interceptors.response.use(
  res => res,
  async err => {
    if (err.response.status === 401 && !err.config._retry) {
      err.config._retry = true;
      return axios(err.config);
    }
    return Promise.reject(err);
  }
);

export default api;
