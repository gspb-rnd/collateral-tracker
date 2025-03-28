import React, { useState, useEffect, useRef } from 'react';
import { Input } from "../../components/ui/input";
import { Button } from "../../components/ui/button";
import { Search, Home } from "lucide-react";
import { useNavigate } from 'react-router-dom';
import { api } from '../../utils/api';
import { Collateral } from '../../types/collateral';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
} from "../../components/ui/command";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "../../components/ui/popover";

const Navbar: React.FC = () => {
  const navigate = useNavigate();
  const [searchTerm, setSearchTerm] = useState('');
  const [searchResults, setSearchResults] = useState<Collateral[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [isPopoverOpen, setIsPopoverOpen] = useState(false);
  const searchInputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    const fetchSearchResults = async () => {
      if (!searchTerm.trim()) {
        setSearchResults([]);
        return;
      }

      setIsLoading(true);
      try {
        const results = await api.get<Collateral[]>(`/api/collateral/search?query=${encodeURIComponent(searchTerm)}`);
        setSearchResults(results);
      } catch (error) {
        console.error('Error fetching search results:', error);
        setSearchResults([]);
      } finally {
        setIsLoading(false);
      }
    };

    const debounceTimeout = setTimeout(fetchSearchResults, 300);
    return () => clearTimeout(debounceTimeout);
  }, [searchTerm]);

  const handleNewCollateral = () => {
    navigate('/new-collateral');
  };

  const handleHome = () => {
    navigate('/');
  };

  const handleSearch = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter') {
      e.preventDefault();
      navigate(`/search?query=${encodeURIComponent(searchTerm)}`);
      setIsPopoverOpen(false);
    }
  };

  const handleSearchItemClick = (collateralId: string) => {
    navigate(`/search?query=${encodeURIComponent(searchTerm)}`);
    setIsPopoverOpen(false);
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
        <Popover open={isPopoverOpen && searchResults.length > 0} onOpenChange={setIsPopoverOpen}>
          <PopoverTrigger asChild>
            <div className="relative w-full">
              <Search className="absolute left-2 top-2.5 h-4 w-4 text-muted-foreground" />
              <Input
                type="search"
                placeholder="Search collateral..."
                className="w-full pl-8"
                value={searchTerm}
                onChange={(e) => {
                  setSearchTerm(e.target.value);
                  setIsPopoverOpen(!!e.target.value.trim());
                }}
                onKeyDown={handleSearch}
                ref={searchInputRef}
              />
            </div>
          </PopoverTrigger>
          <PopoverContent className="p-0 w-[300px]" align="end">
            <Command>
              <CommandInput placeholder="Search collateral..." value={searchTerm} onValueChange={setSearchTerm} />
              {isLoading ? (
                <div className="py-6 text-center text-sm">Loading results...</div>
              ) : (
                <>
                  <CommandEmpty>No results found.</CommandEmpty>
                  <CommandGroup>
                    {searchResults.map((item) => (
                      <CommandItem 
                        key={item.id} 
                        onSelect={() => handleSearchItemClick(item.id!)}
                        className="cursor-pointer"
                      >
                        {item.name}
                      </CommandItem>
                    ))}
                  </CommandGroup>
                </>
              )}
            </Command>
          </PopoverContent>
        </Popover>
      </div>
    </nav>
  );
};

export default Navbar;
