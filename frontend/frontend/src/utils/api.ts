import { Collateral } from '../types/collateral';

export interface ApiError extends Error {
  status?: number;
}

const API_BASE_URL = 'http://localhost:8080';

async function fetchApi<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;
  
  try {
    const response = await fetch(url, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    if (!response.ok) {
      const error = new Error(`API Error: ${response.statusText}`) as ApiError;
      error.status = response.status;
      throw error;
    }

    return await response.json();
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
}

export const api = {
  get: <T>(endpoint: string): Promise<T> => {
    return fetchApi<T>(endpoint);
  },
  
  post: <T>(endpoint: string, data: any): Promise<T> => {
    return fetchApi<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },
  
  put: <T>(endpoint: string, data: any): Promise<T> => {
    return fetchApi<T>(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },
  
  delete: <T>(endpoint: string): Promise<T> => {
    return fetchApi<T>(endpoint, {
      method: 'DELETE',
    });
  },
};
