import axios from 'axios';

const API_BASE_URL =
  process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

// Create axios instance with default config
const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add request interceptor to include auth token
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('authToken');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// Add response interceptor to handle auth errors
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Authentication API
export const authAPI = {
  login: (credentials) => apiClient.post('/auth/login', credentials),
  logout: () => apiClient.post('/auth/logout'),
};

// Employee API
export const employeeAPI = {
  getAll: (params) => apiClient.get('/employee', { params }),
  getById: (id) => apiClient.get(`/employee/${id}`),
  create: (data) => apiClient.post('/employee', data),
  update: (data) => apiClient.put('/employee', data),
  delete: (id) => apiClient.delete(`/employee/${id}`),
};

// Department API
export const departmentAPI = {
  getAll: (params) => apiClient.get('/department', { params }),
  getById: (id) => apiClient.get(`/department/${id}`),
  create: (data) => apiClient.post('/department', data),
  update: (data) => apiClient.put('/department', data),
  delete: (id) => apiClient.delete(`/department/${id}`),
};

// Location API
export const locationAPI = {
  getAll: (params) => apiClient.get('/location', { params }),
  getById: (id) => apiClient.get(`/location/${id}`),
  create: (data) => apiClient.post('/location', data),
  update: (data) => apiClient.put('/location', data),
  delete: (id) => apiClient.delete(`/location/${id}`),
};

// Tier API
export const tierAPI = {
  getAll: (params) => apiClient.get('/tier', { params }),
  getById: (id) => apiClient.get(`/tier/${id}`),
  create: (data) => apiClient.post('/tier', data),
  update: (data) => apiClient.put('/tier', data),
  delete: (id) => apiClient.delete(`/tier/${id}`),
};

// API Logging API
export const loggingAPI = {
  getHistory: (params) => apiClient.get('/logging/history', { params }),
  getHistoryByUser: (userIdentifier, params) =>
    apiClient.get(`/logging/history/user/${userIdentifier}`, { params }),
  getHistoryByEndpoint: (params) =>
    apiClient.get('/logging/history/endpoint', { params }),
  getHistoryByMethod: (method, params) =>
    apiClient.get(`/logging/history/method/${method}`, { params }),
  getHistoryByStatus: (status, params) =>
    apiClient.get(`/logging/history/status/${status}`, { params }),
  getHistoryByDateRange: (params) =>
    apiClient.get('/logging/history/date-range', { params }),
};

export default apiClient;
