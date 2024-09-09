import axios from 'axios';

const jwtAxios = axios.create({
  baseURL: 'http://localhost:8080',
});

jwtAxios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default jwtAxios;