import React, { useState, useEffect } from 'react';
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Label } from "@/components/ui/label";
import { toast } from 'sonner';
import jwtAxios from '../jwt-axios';
import { User } from "lucide-react";

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

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await jwtAxios.put('/api/authenticated/players/profile/update', settings);
      
      if (newAvatarFile) {
        const formData = new FormData();
        formData.append('file', newAvatarFile); // Changed 'avatar' to 'file'
        await jwtAxios.post('/api/authenticated/players/upload/avatar', formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });
        fetchAvatar(settings.id);
      }
      
      toast.success('Settings updated successfully');
    } catch (error) {
      console.error('Error updating settings:', error);
      toast.error('Failed to update settings');
    }
  };

  return (
    <Card className="w-full max-w-2xl mx-auto">
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
            <Input type="file" onChange={handleImageChange} accept="image/*" />
          </div>
          
          <div className="space-y-2">
            <Label htmlFor="username">Username</Label>
            <Input id="username" name="username" value={settings.username} onChange={handleChange} placeholder="Your Geometry Dash username" />
          </div>
          
          {['discord', 'youtube', 'twitter', 'twitch'].map((social) => (
            <div key={social} className="space-y-2">
              <Label htmlFor={social}>{social.charAt(0).toUpperCase() + social.slice(1)}</Label>
              <Input
                id={social}
                name={social}
                value={settings[social as keyof UserSettings] || ''}
                onChange={handleChange}
                placeholder={`${social.charAt(0).toUpperCase() + social.slice(1)} handle`}
              />
            </div>
          ))}
          
          <Button type="submit">Save Changes</Button>
        </form>
      </CardContent>
    </Card>
  );
};

export default Settings;
