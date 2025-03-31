
const API_BASE_URL = 'http://localhost:8080';

export interface ApiError extends Error {
  status?: number;
}

async function fetchApi<T>(
  endpoint: string,
  options: RequestInit = {}
): Promise<T> {
  const url = `${API_BASE_URL}${endpoint}`;
  
  try {
    const response = await fetch(url, options);
    
    if (!response.ok) {
      const error = new Error(`HTTP error! Status: ${response.status}`) as ApiError;
      error.status = response.status;
      throw error;
    }
    
    return await response.json() as T;
  } catch (error) {
    console.error('API request failed:', error);
    throw error;
  }
}

export const api = {
  getAll: async <T>(): Promise<T> => {
    return await fetchApi<T>('/api/collateral', {
      method: 'GET',
    });
  },
  
  getById: async <T>(id: string): Promise<T> => {
    return await fetchApi<T>(`/api/collateral/${id}`, {
      method: 'GET',
    });
  },
  
  search: async <T>(query: string): Promise<T> => {
    return await fetchApi<T>(`/api/collateral/search?query=${encodeURIComponent(query)}`, {
      method: 'GET',
    });
  },
  
  post: async <T>(endpoint: string, data: any): Promise<T> => {
    return await fetchApi<T>(endpoint, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
  },
  
  put: async <T>(endpoint: string, data: any): Promise<T> => {
    return await fetchApi<T>(endpoint, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
  },
  
  delete: async <T>(endpoint: string): Promise<T> => {
    return await fetchApi<T>(endpoint, {
      method: 'DELETE',
    });
  },
  
  getDatapoints: async <T>(collateralId: string): Promise<T> => {
    return await fetchApi<T>(`/api/collateral/${collateralId}/datapoints`, {
      method: 'GET',
    });
  },
  
  updateDatapoint: async <T>(id: string, data: any): Promise<T> => {
    return await fetchApi<T>(`/api/datapoints/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(data),
    });
  }
};
