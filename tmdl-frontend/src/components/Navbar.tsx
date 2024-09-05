import React, { useState, useEffect } from 'react';
import axios from '../axios-config';
import { useNavigate } from 'react-router-dom';
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Search, User, Menu, LogIn } from "lucide-react";

interface NavbarProps {
  onMenuClick: () => void;
}

interface Profile {
  profilePicture: string;
}

const Navbar: React.FC<NavbarProps> = ({ onMenuClick }) => {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem('jwtToken');
  const [profile, setProfile] = useState<Profile | null>(null);

  useEffect(() => {
    if (isAuthenticated) {
      axios.get('/api/authenticated/players/profile')
        .then(response => setProfile(response.data))
        .catch(error => console.error('Error fetching profile:', error));
    }
  }, [isAuthenticated]);

  const handleLogout = () => {
    localStorage.removeItem('jwtToken');
    navigate('/login');
  };

  const handleLogin = () => {
    navigate('/login');
  };

  return (
    <nav className="bg-gray-900 shadow-sm border-b border-gray-700">
      <div className="px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center flex-1">
            <Button variant="ghost" className="md:hidden mr-2 text-gray-200" onClick={onMenuClick}>
              <Menu className="h-6 w-6" />
            </Button>
            <div className="relative max-w-xs w-full mr-4">
              <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                <Search className="h-5 w-5 text-gray-400" />
              </div>
              <Input
                type="search"
                placeholder="Search"
                className="block w-full pl-10 pr-3 py-2 border border-gray-600 rounded-md leading-5 bg-gray-700 placeholder-gray-400 focus:outline-none focus:placeholder-gray-300 focus:ring-1 focus:ring-blue-500 focus:border-blue-500 sm:text-sm text-gray-100"
              />
            </div>
          </div>
          
          <div className="flex items-center">
            {isAuthenticated ? (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" className="relative rounded-full bg-gray-700 p-1 text-gray-300 hover:text-gray-100 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500">
                    <span className="sr-only">Open user menu</span>
                    <Avatar>
                      <AvatarImage src={profile?.profilePicture} />
                      <AvatarFallback><User /></AvatarFallback>
                    </Avatar>
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end" className="bg-gray-700 text-gray-100">
                  <DropdownMenuItem>Profile</DropdownMenuItem>
                  <DropdownMenuItem>Settings</DropdownMenuItem>
                  <DropdownMenuItem onClick={handleLogout}>Sign out</DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            ) : (
              <Button variant="ghost" className="w-full justify-start text-left text-sm" onClick={handleLogin}>
                <LogIn className="h-5 w-5 mr-2" /> Login
              </Button>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
