import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuthContext } from '../contexts/AuthContext';

interface ProtectedRouteProps {
  children: React.ReactNode;
  staffOnly?: boolean;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, staffOnly = false }) => {
  const { user } = useAuthContext();
  const location = useLocation();

  if (!user) {
    return <Navigate to="/" state={{ openLogin: true, from: location.pathname }} replace />;
  }

  if (staffOnly && user.role !== 'STAFF') {
    return <Navigate to="/" replace />;
  }

  return <>{children}</>;
};

export default ProtectedRoute;
