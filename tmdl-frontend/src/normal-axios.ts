import axios from 'axios';

const normalAxios = axios.create({
  baseURL: 'https://www.themoroccandemonlist.live',
  // baseURL: 'http://localhost:8080',
});

export default normalAxios;