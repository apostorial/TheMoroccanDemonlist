import React, { useState, useEffect } from 'react';
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Label } from "@/components/ui/label";
import { toast } from 'sonner';
import jwtAxios from '../jwt-axios';
import { User, Upload } from "lucide-react";
import axios from 'axios';

interface UserSettings {
  id: string;
  username: string;
  discord?: string;
  youtube?: string;
  twitter?: string;
  twitch?: string;
}

const Settings: React.FC = () => {
  const [settings, setSettings] = useState<UserSettings>({
    id: '',
    username: '',
    discord: '',
    youtube: '',
    twitter: '',
    twitch: '',
  });
  const [avatarUrl, setAvatarUrl] = useState<string | null>(null);
  const [newAvatarFile, setNewAvatarFile] = useState<File | null>(null);

  useEffect(() => {
    jwtAxios.get('/api/authenticated/players/profile')
      .then(response => {
        setSettings(response.data);
        fetchAvatar(response.data.id);
      })
      .catch(error => console.error('Error fetching settings:', error));
  }, []);

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
      setAvatarUrl(`data:image/jpeg;base64,${base64}`);
    } catch (error) {
      console.error('Error fetching avatar:', error);
    }
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSettings({ ...settings, [e.target.name]: e.target.value });
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files[0]) {
      setNewAvatarFile(e.target.files[0]);
    }
  };

  const handleSocialChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    let newValue = value;
    
    if (name === 'youtube') {
      newValue = value.startsWith('https://www.youtube.com/@') ? value : `https://www.youtube.com/@${value}`;
    } else if (name === 'twitter') {
      newValue = value.startsWith('https://twitter.com/') ? value : `https://twitter.com/${value}`;
    } else if (name === 'twitch') {
      newValue = value.startsWith('https://www.twitch.tv/') ? value : `https://www.twitch.tv/${value}`;
    }

    setSettings({ ...settings, [name]: newValue });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await jwtAxios.put('/api/authenticated/players/profile/update', settings);
      
      if (newAvatarFile) {
        const formData = new FormData();
        formData.append('file', newAvatarFile);
        await jwtAxios.post('/api/authenticated/players/upload/avatar', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        fetchAvatar(settings.id);
      }
      
      toast.success('Settings updated successfully');
    } catch (error) {
      console.error('Error updating settings:', error);
      if (axios.isAxiosError(error) && error.code === 'ERR_NETWORK') {
        toast.error('File upload failed', {
          description: 'The file size may exceed the server limit (1MB).',
        });
      } else {
        toast.error('Failed to update settings');
      }
    }
  };

  return (
    <Card className="w-full mx-auto">
      <CardHeader>
        <CardTitle>User Settings</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex items-center space-x-4">
            <Avatar className="w-20 h-20">
              {avatarUrl ? (
                <AvatarImage 
                  src={avatarUrl} 
                  alt={settings.username} 
                  className="object-cover w-full h-full"
                />
              ) : (
                <AvatarFallback><User className="w-10 h-10" /></AvatarFallback>
              )}
            </Avatar>
            <div className="relative">
              <Input
                type="file"
                onChange={handleImageChange}
                accept="image/*"
                className="hidden"
                id="avatar-upload"
              />
              <Label
                htmlFor="avatar-upload"
                className="flex items-center justify-center px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-md shadow-sm text-sm font-medium text-gray-700 dark:text-gray-200 bg-white dark:bg-gray-800 hover:bg-gray-50 dark:hover:bg-gray-700 cursor-pointer"
              >
                <Upload className="w-5 h-5 mr-2" />
                Choose File
              </Label>
              {newAvatarFile && (
                <p className="mt-2 text-sm text-gray-500 dark:text-gray-400">
                  {newAvatarFile.name}
                </p>
              )}
              <p className="mt-2 text-sm text-gray-500 dark:text-gray-400">
                Note: File uploads should not exceed 1MB
              </p>
            </div>
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="discord">Discord</Label>
            <Input id="discord" name="discord" value={settings.discord} onChange={handleChange} placeholder="@username" />
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="youtube">YouTube</Label>
            <div className="flex">
              <span className="inline-flex items-center px-3 text-sm border border-r-0 rounded-l-md">
                https://www.youtube.com/@
              </span>
              <Input
                id="youtube"
                name="youtube"
                value={settings.youtube?.replace('https://www.youtube.com/@', '')}
                onChange={handleSocialChange}
                className="rounded-l-none"
                placeholder="username"
              />
            </div>
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="twitter">Twitter</Label>
            <div className="flex">
              <span className="inline-flex items-center px-3 text-sm border border-r-0 rounded-l-md">
                https://twitter.com/
              </span>
              <Input
                id="twitter"
                name="twitter"
                value={settings.twitter?.replace('https://twitter.com/', '')}
                onChange={handleSocialChange}
                className="rounded-l-none"
                placeholder="username"
              />
            </div>
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="twitch">Twitch</Label>
            <div className="flex">
              <span className="inline-flex items-center px-3 text-sm border border-r-0 rounded-l-md">
                https://www.twitch.tv/
              </span>
              <Input
                id="twitch"
                name="twitch"
                value={settings.twitch?.replace('https://www.twitch.tv/', '')}
                onChange={handleSocialChange}
                className="rounded-l-none"
                placeholder="username"
              />
            </div>
          </div>
          
          <Button type="submit">Save Changes</Button>
        </form>
      </CardContent>
    </Card>
  );
};

export default Settings;
