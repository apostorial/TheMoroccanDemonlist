import { useState, useEffect, useCallback } from 'react';
import { jwtDecode } from 'jwt-decode';
import axios from 'axios';

interface User {
  id: string;
  email: string;
  authorities: string[];
  role: 'STAFF' | 'USER';
}

export const useAuth = () => {
  const [user, setUser] = useState<User | null>(null);

  const refreshUser = useCallback(() => {
    const token = localStorage.getItem('jwtToken');
    if (token) {
      try {
        const decodedToken = jwtDecode<User>(token);
        const role = decodedToken.authorities.includes('ROLE_STAFF') ? 'STAFF' : 'USER';
        setUser({ ...decodedToken, role });
      } catch (error) {
        console.error('Invalid token', error);
        localStorage.removeItem('jwtToken');
        setUser(null);
      }
    } else {
      setUser(null);
    }
  }, []);

  useEffect(() => {
    refreshUser();
  }, [refreshUser]);

  const login = async (email: string, password: string) => {
    try {
      const response = await axios.post('/api/auth/login', { email, password });
      const { token } = response.data;
      localStorage.setItem('jwtToken', token);
      refreshUser();
    } catch (error) {
      console.error('Login failed', error);
      throw error;
    }
  };

  const logout = () => {
    localStorage.removeItem('jwtToken');
    setUser(null);
  };

  return { user, login, logout, refreshUser };
};
