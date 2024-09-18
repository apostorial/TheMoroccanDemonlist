import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import jwtAxios from '../jwt-axios';
import axios from 'axios';
import { toast } from "sonner";

interface Level {
  id: string;
  name: string;
}

interface Player {
  id: string;
  username: string;
}

interface AddRecordProps {
  recordType: 'classic' | 'platformer';
  onRecordAdded: () => void;
}

const AddRecord: React.FC<AddRecordProps> = ({ recordType, onRecordAdded }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [levels, setLevels] = useState<Level[]>([]);
  const [players, setPlayers] = useState<Player[]>([]);
  const [formData, setFormData] = useState({
    level: '',
    player: '',
    recordPercentage: '',
    recordTime: '',
    link: '',
  });

  useEffect(() => {
    if (isOpen) {
      fetchLevels();
      fetchPlayers();
    }
  }, [isOpen, recordType]);

  const fetchLevels = async () => {
    try {
      const response = await jwtAxios.get(`/api/public/${recordType}-levels/list`);
      setLevels(response.data);
    } catch (error) {
      console.error('Error fetching levels:', error);
      toast.error('Failed to fetch levels');
    }
  };

  const fetchPlayers = async () => {
    try {
      const response = await jwtAxios.get('/api/public/players');
      setPlayers(response.data);
    } catch (error) {
      console.error('Error fetching players:', error);
      toast.error('Failed to fetch players');
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSelectChange = (name: string, value: string) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const formatTimeToISO8601 = (time: string): string => {
    const [hours, minutes, seconds] = time.split(':').map(Number);
    return `PT${hours}H${minutes}M${seconds}S`;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const dataToSend = {
        ...formData,
        recordTime: recordType === 'platformer' ? formatTimeToISO8601(formData.recordTime) : undefined,
        recordPercentage: recordType === 'classic' ? Number(formData.recordPercentage) : undefined,
        link: formData.link.trim() === '' ? null : formData.link,
      };
      await jwtAxios.post(`/api/staff/${recordType}-records/create`, dataToSend);
      toast.success('Record added successfully');
      setIsOpen(false);
      onRecordAdded();
    } catch (error) {
      console.error('Error adding record:', error);
      if (axios.isAxiosError(error) && error.code === 'ERR_BAD_REQUEST') {
        toast.error('Record already exists', {
          description: 'A record with this level and player already exists.',
        });
      } else {
        toast.error('Failed to add record');
      }
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button onClick={() => setIsOpen(true)}>Add Record</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle>Add New Record</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-2 md:col-span-1">
            <Label htmlFor="level">Level</Label>
            <Select name="level" onValueChange={(value) => handleSelectChange('level', value)} required>
              <SelectTrigger>
                <SelectValue placeholder="Select level" />
              </SelectTrigger>
              <SelectContent>
                {levels.map((level) => (
                  <SelectItem key={level.id} value={level.id}>{level.name}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          <div className="space-y-2 md:col-span-1">
            <Label htmlFor="player">Player</Label>
            <Select name="player" onValueChange={(value) => handleSelectChange('player', value)} required>
              <SelectTrigger>
                <SelectValue placeholder="Select player" />
              </SelectTrigger>
              <SelectContent>
                {players.map((player) => (
                  <SelectItem key={player.id} value={player.id}>{player.username}</SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          {recordType === 'classic' ? (
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="recordPercentage">Percentage (%)</Label>
              <Input 
                id="recordPercentage" 
                name="recordPercentage" 
                type="number" 
                min="0" 
                max="100" 
                value={formData.recordPercentage} 
                onChange={handleInputChange}
                placeholder="69"
                required 
              />
            </div>
          ) : (
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="recordTime">Record Time (HH:MM:SS)</Label>
              <Input 
                id="recordTime" 
                name="recordTime" 
                type="text" 
                pattern="[0-9]{2}:[0-9]{2}:[0-9]{2}"
                placeholder="01:10:32"
                value={formData.recordTime} 
                onChange={handleInputChange} 
                required 
              />
            </div>
          )}
          <div className="space-y-2 md:col-span-2">
            <Label htmlFor="link">Record Link</Label>
            <Input 
              id="link" 
              name="link" 
              type="url" 
              value={formData.link} 
              onChange={handleInputChange} 
              placeholder="https://youtu.be/dQw4w9WgXcQ"
            />
          </div>
          <div className="flex justify-end md:col-span-2">
            <Button type="submit">Add Record</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddRecord;
