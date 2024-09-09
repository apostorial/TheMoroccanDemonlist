import axios from 'axios';

const normalAxios = axios.create({
  baseURL: 'http://localhost:8080',
});

export default normalAxios;