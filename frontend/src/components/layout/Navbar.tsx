import React, { useState, useEffect, useRef } from 'react';
import { Input } from "../../components/ui/input";
import { Button } from "../../components/ui/button";
import { Search, Home } from "lucide-react";
import { useNavigate } from 'react-router-dom';
import { Collateral } from '../../types/collateral';
import { api } from '../../utils/api';

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [searchResults, setSearchResults] = useState<Collateral[]>([]);
  const [isDropdownOpen, setIsDropdownOpen] = useState<boolean>(false);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);
  const searchTimeoutRef = useRef<number | null>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const handleNewCollateral = () => {
    navigate('/new-collateral');
  };

  const handleHome = () => {
    navigate('/');
  };

  useEffect(() => {
    if (searchQuery.trim().length < 2) {
      setSearchResults([]);
      setIsDropdownOpen(false);
      setError(null);
      return;
    }

    if (searchTimeoutRef.current) {
      window.clearTimeout(searchTimeoutRef.current);
    }

    searchTimeoutRef.current = window.setTimeout(async () => {
      setIsLoading(true);
      setError(null);
      try {
        const results = await api.get<Collateral[]>(`/api/collateral/search?query=${encodeURIComponent(searchQuery)}`);
        setSearchResults(results.slice(0, 5)); // Limit to 5 results in dropdown
        setIsDropdownOpen(true);
      } catch (err) {
        console.error('Error searching collateral:', err);
        setError('MongoDB connection error. Please try again later.');
        setSearchResults([]);
      } finally {
        setIsLoading(false);
      }
    }, 300);

    return () => {
      if (searchTimeoutRef.current) {
        window.clearTimeout(searchTimeoutRef.current);
      }
    };
  }, [searchQuery]);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchQuery(e.target.value);
  };

  const handleSearchKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && searchQuery.trim()) {
      e.preventDefault();
      navigate(`/search?query=${encodeURIComponent(searchQuery.trim())}`);
      setIsDropdownOpen(false);
      inputRef.current?.blur();
    }
  };

  const handleSelectResult = (result: Collateral) => {
    navigate(`/search?query=${encodeURIComponent(result.name)}`);
    setSearchQuery(result.name);
    setIsDropdownOpen(false);
  };

  return (
    <nav className="flex items-center justify-between p-4 bg-gray-100 border-b shadow-sm">
      <div className="flex items-center space-x-4">
        <Button 
          onClick={handleHome}
          variant="ghost"
          className="flex items-center gap-2"
        >
          <Home className="h-4 w-4" />
          Home
        </Button>
        <Button 
          onClick={handleNewCollateral}
          variant="default"
        >
          New Collateral
        </Button>
      </div>
      
      <div className="flex-1"></div>
      
      <div className="flex items-center w-1/3 relative">
        <div className="relative w-full">
          <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
          <Input
            ref={inputRef}
            type="search"
            placeholder="Search collateral..."
            className="w-full pl-8"
            value={searchQuery}
            onChange={handleSearchChange}
            onKeyDown={handleSearchKeyDown}
            onFocus={() => searchResults.length > 0 && setIsDropdownOpen(true)}
          />
          
          {isDropdownOpen && (
            <div className="absolute z-10 mt-1 w-full rounded-md bg-white shadow-lg">
              <div className="py-1">
                {isLoading ? (
                  <div className="px-4 py-2 text-sm text-gray-500">Loading...</div>
                ) : error ? (
                  <div className="px-4 py-2 text-sm text-red-500">{error}</div>
                ) : searchResults.length === 0 ? (
                  <div className="px-4 py-2 text-sm text-gray-500">No results found</div>
                ) : (
                  searchResults.map((result) => (
                    <div 
                      key={result.id}
                      className="px-4 py-2 text-sm text-gray-700 hover:bg-gray-100 cursor-pointer"
                      onClick={() => handleSelectResult(result)}
                    >
                      {result.name}
                    </div>
                  ))
                )}
              </div>
            </div>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
