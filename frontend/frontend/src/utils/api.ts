
export interface ApiError extends Error {
  status?: number;
}

export const API_BASE_URL = 'http://localhost:8080';

export async function fetchApi<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;
  
  const response = await fetch(url, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });
  
  if (!response.ok) {
    const error = new Error('API request failed') as ApiError;
    error.status = response.status;
    throw error;
  }
  
  return response.json();
}

export const api = {
  get: <T>(endpoint: string): Promise<T> => fetchApi<T>(endpoint),
  
  post: <T>(endpoint: string, data: any): Promise<T> => 
    fetchApi<T>(endpoint, {
      method: 'POST',
      body: JSON.stringify(data),
    }),
  
  put: <T>(endpoint: string, data: any): Promise<T> => 
    fetchApi<T>(endpoint, {
      method: 'PUT',
      body: JSON.stringify(data),
    }),
  
  delete: <T>(endpoint: string): Promise<T> => 
    fetchApi<T>(endpoint, {
      method: 'DELETE',
    }),
};
