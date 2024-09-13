import axios from 'axios';

const normalAxios = axios.create({
  baseURL: 'http://144.24.195.186:8080',
});

export default normalAxios;