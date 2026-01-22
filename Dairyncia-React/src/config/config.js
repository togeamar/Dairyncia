// src/config/config.js 

const BACKENDS = {
  DOTNET: import.meta.env.VITE_DOTNET_API_URL || 'http://localhost:5225',
  SPRINGBOOT: import.meta.env.VITE_SPRINGBOOT_API_URL || 'http://localhost:8080'
};

// Read from environment variable, fallback to DOTNET
const ACTIVE_BACKEND = import.meta.env.VITE_BACKEND || 'DOTNET';

export const API_CONFIG = {
  baseURL: BACKENDS[ACTIVE_BACKEND],
  timeout: parseInt(import.meta.env.VITE_API_TIMEOUT) || 10000,
  headers: {
    'Content-Type': 'application/json',
  }
};

// Log current configuration in development
if (import.meta.env.DEV) {
  console.log('🚀 Active Backend:', ACTIVE_BACKEND);
  console.log('🔗 API Base URL:', API_CONFIG.baseURL);
}

// Endpoint mappings with backend-specific overrides if needed
export const ENDPOINTS = {
  AUTH: {
    REGISTER: '/api/auth/register',
    LOGIN: '/api/auth/login',
  },
  
  ADMIN: {
    ASSIGN_ROLE: '/api/admin/assign-role',
    PENDING_USERS: '/api/admin/pending-users',
    FARMERS: '/api/admin/farmers',
    FARMER_DETAILS: (id) => `/api/admin/farmers/${id}`,
    FARMER_ADDRESS: (id) => `/api/admin/farmers/${id}/address`,
    FARMER_BANK: (id) => `/api/admin/farmers/${id}/bank`,
    DELETE_FARMER: (id) => `/api/admin/farmers/${id}`,
    UPDATE_FARMER: (id) => `/api/admin/farmers/${id}`,
    MANAGERS: '/api/admin/managers',
    MANAGER_DETAILS: (id) => `/api/admin/managers/${id}`,
    UPDATE_MANAGER: (id) => `/api/admin/managers/${id}`,
    DELETE_MANAGER: (id) => `/api/admin/managers/${id}`,
  },
  
  MANAGER: {
    FARMER_MILK_COLLECTION: '/api/manager/get-farmer-milk-collection',
  },
  
  MILK_COLLECTION: {
    CREATE: '/api/manager/milk-collection/create',
    ALL: '/api/manager/milk-collection/all',
    TODAYS: '/api/manager/milk-collection/todays',
    BY_ID: (id) => `/api/manager/milk-collection/${id}`,
    UPDATE: (id) => `/api/manager/milk-collection/update/${id}`,
    DELETE: (id) => `/api/manager/milk-collection/delete/${id}`,
  },
  
  FARMER: {
    PROFILE: '/api/farmer/profile',
    MILK_COLLECTIONS: '/api/farmer/milk-collections',
  },
  
  CONTACT: '/api/contact',
  
  MILK_RATE: {
    GENERATE: '/api/milk-rate-matrix/generate',
  }
};

// Backend-specific configurations for handling differences
export const BACKEND_CONFIG = {
  DOTNET: {
    dateFormat: 'iso',
    errorField: 'title',
    // Add any .NET specific transformations
    transformResponse: (data) => data,
  },
  SPRINGBOOT: {
    dateFormat: 'iso',
    errorField: 'message',
    // Add any Spring Boot specific transformations
    transformResponse: (data) => data,
  }
};

export const getCurrentBackendConfig = () => BACKEND_CONFIG[ACTIVE_BACKEND];
export const getCurrentBackend = () => ACTIVE_BACKEND;