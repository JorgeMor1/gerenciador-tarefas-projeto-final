import { Navigate } from 'react-router-dom';

interface Props {
  children: React.ReactNode;
  requiredRole?: 'user' | 'admin';
}

const ProtectedRoute = ({ children, requiredRole }: Props) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (!token) return <Navigate to="/" />;
  if (requiredRole && role !== requiredRole) return <Navigate to="/" />;

  return <>{children}</>;
};

export default ProtectedRoute;