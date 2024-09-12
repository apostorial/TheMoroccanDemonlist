import React, { useState, useEffect } from 'react';
import jwtAxios from '../jwt-axios';
import { useNavigate } from 'react-router-dom';
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from "@/components/ui/dropdown-menu";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Search, User, Menu, LogIn } from "lucide-react";
import { ThemeSwitcher } from './ThemeSwitcher';
import Login from './Login';
import { useAuthContext } from '../contexts/AuthContext';

interface NavbarProps {
  onMenuClick: () => void;
}

interface Profile {
  id: string;
  username: string;
}

const Navbar: React.FC<NavbarProps> = ({ onMenuClick }) => {
  const navigate = useNavigate();
  const { user, logout } = useAuthContext();
  const [profile, setProfile] = useState<Profile | null>(null);
  const [avatar, setAvatar] = useState<string | null>(null);
  const [isLoginOpen, setIsLoginOpen] = useState(false);

  useEffect(() => {
    if (user) {
      jwtAxios.get('/api/authenticated/players/profile')
        .then(response => {
          setProfile(response.data);
          fetchAvatar(response.data.id);
        })
        .catch(error => console.error('Error fetching profile:', error));
    } else {
      setProfile(null);
      setAvatar(null);
    }
  }, [user]);
  const fetchAvatar = async (playerId: string) => {
    try {
      const response = await jwtAxios.get(`/api/public/players/${playerId}/avatar`, {
        responseType: 'arraybuffer'
      });
      const base64 = btoa(
        new Uint8Array(response.data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          '',
        )
      );
      setAvatar(`data:image/jpeg;base64,${base64}`);
    } catch (error) {
      console.error('Error fetching avatar:', error);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/');
  };

  const handleLogin = () => {
    setIsLoginOpen(true);
  };

  const handleProfileClick = () => {
    if (profile && profile.username) {
      navigate(`/profile/${profile.username}`);
    }
  };

  const handleSubmissionsClick = () => {
    navigate('/submissions');
  };

  const handleSettingsClick = () => {
    navigate('/settings');
  };

  return (
    <>
      <nav className="shadow-sm border-b">
        <div className="px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center flex-1">
              <Button variant="ghost" className="md:hidden mr-2 text-gray-200" onClick={onMenuClick}>
                <Menu className="h-6 w-6" />
              </Button>
              <div className="relative max-w-xs w-full mr-4">
                <div className="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <Search className="h-5 w-5" />
                </div>
                <Input
                  type="search"
                  placeholder="Search"
                  className="block w-full pl-10 pr-3 py-2 border rounded-md leading-5 focus:outline-none focus:ring-1 sm:text-sm"
                />
              </div>
            </div>
            
            <div className="flex items-center">
              <div className="mr-2">
                <ThemeSwitcher />
              </div>
              {user ? (
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" className="relative rounded-full p-1">
                      <span className="sr-only">Open user menu</span>
                      <Avatar>
                        {avatar ? (
                          <AvatarImage 
                            src={avatar} 
                            alt={profile?.username}
                            className="object-cover w-full h-full"
                          />
                        ) : (
                          <AvatarFallback><User /></AvatarFallback>
                        )}
                      </Avatar>
                    </Button>
                  </DropdownMenuTrigger>
                  <DropdownMenuContent align="end">
                    <DropdownMenuItem onClick={handleProfileClick}>Profile</DropdownMenuItem>
                    <DropdownMenuItem onClick={handleSubmissionsClick}>Submissions</DropdownMenuItem>
                    <DropdownMenuItem onClick={handleSettingsClick}>Settings</DropdownMenuItem>
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
      <Login isOpen={isLoginOpen} onClose={() => setIsLoginOpen(false)} />
    </>
  );
};

export default Navbar;
