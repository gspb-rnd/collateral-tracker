import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { api } from '../utils/api';
import { Collateral } from '../types/collateral';
import { 
  Card, 
  CardContent, 
  CardDescription, 
  CardHeader, 
  CardTitle 
} from '../components/ui/card';
import { Button } from '../components/ui/button';
import { useNavigate } from 'react-router-dom';
import { ArrowLeft } from 'lucide-react';

const SearchResults: React.FC = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get('query') || '';
  const [results, setResults] = useState<Collateral[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchResults = async () => {
      if (!query) {
        setResults([]);
        setIsLoading(false);
        return;
      }

      setIsLoading(true);
      setError(null);

      try {
        const data = await api.get<Collateral[]>(`/api/collateral/search?query=${encodeURIComponent(query)}`);
        setResults(data);
      } catch (err) {
        console.error('Error fetching search results:', err);
        setError('An error occurred while fetching results. Please try again.');
      } finally {
        setIsLoading(false);
      }
    };

    fetchResults();
  }, [query]);

  const handleBackClick = () => {
    navigate(-1);
  };

  const handleCollateralClick = (id: string) => {
    console.log('Navigate to collateral detail:', id);
  };

  return (
    <div className="container mx-auto p-6">
      <div className="flex items-center mb-6">
        <Button 
          variant="ghost" 
          className="mr-4" 
          onClick={handleBackClick}
        >
          <ArrowLeft className="h-4 w-4 mr-2" />
          Back
        </Button>
        <h1 className="text-2xl font-bold">
          Search Results: "{query}"
        </h1>
      </div>

      {isLoading && (
        <div className="text-center py-8">
          <p className="text-gray-600">Loading results...</p>
        </div>
      )}

      {error && (
        <Card className="mb-6 border-red-200 bg-red-50">
          <CardContent className="pt-6">
            <p className="text-red-600">{error}</p>
          </CardContent>
        </Card>
      )}

      {!isLoading && !error && results.length === 0 && (
        <Card className="mb-6">
          <CardContent className="pt-6">
            <p className="text-gray-600">No results found for "{query}". Please try another search term.</p>
          </CardContent>
        </Card>
      )}

      {!isLoading && !error && results.length > 0 && (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {results.map((collateral) => (
            <Card 
              key={collateral.id} 
              className="cursor-pointer hover:shadow-md transition-shadow"
              onClick={() => collateral.id && handleCollateralClick(collateral.id)}
            >
              <CardHeader>
                <CardTitle>{collateral.name}</CardTitle>
                <CardDescription>{collateral.type}</CardDescription>
              </CardHeader>
              <CardContent>
                <p className="text-gray-600">{collateral.description}</p>
              </CardContent>
            </Card>
          ))}
        </div>
      )}
    </div>
  );
};

export default SearchResults;
