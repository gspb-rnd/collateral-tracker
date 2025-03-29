import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { api } from '../utils/api';
import { Collateral } from '../types/collateral';
import { 
  Card,
  CardHeader,
  CardTitle,
  CardContent,
} from "../components/ui/card";

const SearchResults: React.FC = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('query') || '';
  const [results, setResults] = useState<Collateral[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchResults = async () => {
      if (!query) {
        setResults([]);
        setLoading(false);
        return;
      }

      try {
        setLoading(true);
        setError(null);
        
        const data = await api.get<Collateral[]>(`/api/collateral/search?query=${encodeURIComponent(query)}`);
        setResults(data);
      } catch (err) {
        console.error('Error fetching search results:', err);
        setError('Failed to fetch search results. MongoDB connection error. Please try again later or contact support.');
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, [query]);

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-4">Search Results for "{query}"</h1>
      
      {loading && <p className="text-gray-600">Loading results...</p>}
      
      {error && (
        <div className="p-4 mb-4 rounded bg-red-50 text-red-700">
          {error}
        </div>
      )}
      
      {!loading && !error && results.length === 0 && (
        <p className="text-gray-600">No results found for "{query}"</p>
      )}
      
      {results.length > 0 && (
        <div className="grid grid-cols-1 gap-4 mt-4">
          {results.map((item) => (
            <Card key={item.id}>
              <CardHeader>
                <CardTitle>{item.name}</CardTitle>
              </CardHeader>
              <CardContent>
                <p className="text-gray-600 mb-2">{item.description}</p>
                <div className="flex items-center space-x-2">
                  <span className="text-sm bg-gray-100 px-2 py-1 rounded">{item.type}</span>
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default SearchResults;
