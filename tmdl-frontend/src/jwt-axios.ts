import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const jwtAxios = axios.create({
  baseURL: 'https://www.themoroccandemonlist.live',
  // baseURL: 'http://localhost:8080',
});

jwtAxios.interceptors.request.use((config) => {
  const token = localStorage.getItem('jwtToken');
  if (token) {
    const decodedToken: any = jwtDecode(token);
    if (decodedToken.exp * 1000 < Date.now()) {
      localStorage.removeItem('jwtToken');
      window.location.href = '/';
      return Promise.reject('Token expired');
    }
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default jwtAxios;