import { Navigate, Outlet } from 'react-router-dom';

const ProtectedRoute = ({ allowedRoles }) => {
  const token = localStorage.getItem('token');
  const userRole = localStorage.getItem('type'); // Login.jsx saves status as 'type'
  console.log("DEBUG CHECK:");
  console.log("1. Storage Role:", `"${userRole}"`); // Quotes help see hidden spaces!
  console.log("2. Allowed Roles:", allowedRoles);
  console.log("3. Is Match?:", allowedRoles.includes(userRole));

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && !allowedRoles.includes(userRole)) {
    // User is authenticated but doesn't have the right role
    // Redirect to home or appropriate dashboard based on their actual role
    if (userRole === 'admin') {
        return <Navigate to="/admin-dashboard" replace />;
    } else if (userRole === 'manager') {
        return <Navigate to="/manager-dashboard" replace />;
    }
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
};

export default ProtectedRoute;
