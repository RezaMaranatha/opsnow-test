import React from 'react';
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from 'react-router-dom';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import { AuthProvider, useAuth } from './contexts/AuthContext';
import Layout from './components/layout/Layout';
import ProtectedRoute from './components/auth/ProtectedRoute';
import Login from './components/auth/Login';
import EmployeeManagement from './components/employees/EmployeeManagement';
import DepartmentManagement from './components/departments/DepartmentManagement';
import LocationManagement from './components/locations/LocationManagement';
import TierManagement from './components/tiers/TierManagement';
import CumulativeSalary from './components/analytics/CumulativeSalary';
import DepartmentAnalysis from './components/analytics/DepartmentAnalysis';
import EmployeeRanking from './components/analytics/EmployeeRanking';
import ApiLoggingDashboard from './components/logging/ApiLoggingDashboard';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2',
    },
    secondary: {
      main: '#dc004e',
    },
  },
});

const AppRoutes = () => {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route
        path="/login"
        element={
          isAuthenticated ? <Navigate to="/employees" replace /> : <Login />
        }
      />
      <Route
        path="/"
        element={
          <ProtectedRoute>
            <Layout />
          </ProtectedRoute>
        }
      >
        <Route index element={<Navigate to="/employees" replace />} />
        <Route path="employees" element={<EmployeeManagement />} />
        <Route path="departments" element={<DepartmentManagement />} />
        <Route path="locations" element={<LocationManagement />} />
        <Route path="tiers" element={<TierManagement />} />
        <Route path="cumulative-salary" element={<CumulativeSalary />} />
        <Route path="department-analysis" element={<DepartmentAnalysis />} />
        <Route path="employee-ranking" element={<EmployeeRanking />} />
        <Route path="logs" element={<ApiLoggingDashboard />} />
      </Route>
    </Routes>
  );
};

function App() {
  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <AuthProvider>
        <Router>
          <AppRoutes />
        </Router>
      </AuthProvider>
    </ThemeProvider>
  );
}

export default App;
