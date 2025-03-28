import React from 'react';
import { Input } from "../../components/ui/input";
import { Button } from "../../components/ui/button";
import { Search, Home } from "lucide-react";
import { useNavigate } from 'react-router-dom';

const Navbar: React.FC = () => {
  const navigate = useNavigate();

  const handleNewCollateral = () => {
    navigate('/new-collateral');
  };

  const handleHome = () => {
    navigate('/');
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
            type="search"
            placeholder="Search collateral..."
            className="w-full pl-8"
          />
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
