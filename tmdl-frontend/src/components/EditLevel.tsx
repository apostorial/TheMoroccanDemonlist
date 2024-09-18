import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import axios from '../jwt-axios';
import { toast } from "sonner";
import { Pencil } from 'lucide-react';

interface Player {
  id: string;
  username: string;
}

interface ClassicLevel {
    id: string;
    name: string;
    publisher: string;
    difficulty: string;
    duration: string;
    minimumCompletion: number;
    link: string;
    thumbnail: string;
    firstVictor: string | null;
    ranking: number;
}

interface PlatformerLevel {
    id: string;
    name: string;
    publisher: string;
    difficulty: string;
    duration: string;
    link: string;
    thumbnail: string;
    recordHolder: string | null;
    ranking: number;
}

type Level = ClassicLevel | PlatformerLevel;

interface EditLevelProps {
  levelType: 'classic' | 'platformer';
  level: Level;
  onLevelEdited: () => void;
}

const EditLevel: React.FC<EditLevelProps> = ({ levelType, level, onLevelEdited }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [formData, setFormData] = useState<Level>(level);
  const [players, setPlayers] = useState<Player[]>([]);

  useEffect(() => {
    setFormData(level);
  }, [level]);

  useEffect(() => {
    if (isOpen) {
      fetchPlayers();
    }
  }, [isOpen]);

  const fetchPlayers = async () => {
    try {
      const response = await axios.get('/api/public/players');
      setPlayers(response.data);
    } catch (error) {
      console.error('Error fetching players:', error);
      toast.error('Failed to fetch players');
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'number' ? Number(value) : value
    }));
  };

  const handleSelectChange = (name: string, value: string) => {
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      let dataToSend: Partial<ClassicLevel | PlatformerLevel> = { ...formData };

      if (levelType === 'classic') {
        const classicLevel = formData as ClassicLevel;
        if (classicLevel.firstVictor === 'none') {
          (dataToSend as Partial<ClassicLevel>).firstVictor = null;
        } else {
          (dataToSend as Partial<ClassicLevel>).firstVictor = classicLevel.firstVictor;
        }
      } else {
        const platformerLevel = formData as PlatformerLevel;
        if (platformerLevel.recordHolder) {
          const player = players.find(p => p.username === platformerLevel.recordHolder);
          if (player) {
            (dataToSend as Partial<PlatformerLevel>).recordHolder = player.id;
          }
        }
      }

      await axios.put(`/api/staff/${levelType}-levels/update/${level.id}`, dataToSend);
      toast.success('Level updated successfully');
      setIsOpen(false);
      onLevelEdited();
    } catch (error) {
      console.error('Error updating level:', error);
      toast.error('Failed to update level');
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon">
          <Pencil className="h-4 w-4" />
          <span className="sr-only">Edit level</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle>Edit Level</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="space-y-2">
            <Label htmlFor="id">ID</Label>
            <Input 
              id="id" 
              name="id" 
              value={formData.id} 
              onChange={handleInputChange} 
              required 
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="name">Name</Label>
            <Input id="name" name="name" value={formData.name} onChange={handleInputChange} required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="publisher">Publisher</Label>
            <Input id="publisher" name="publisher" value={formData.publisher} onChange={handleInputChange} required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="difficulty">Difficulty</Label>
            <Select name="difficulty" onValueChange={(value) => handleSelectChange('difficulty', value)} value={formData.difficulty || ''}>
              <SelectTrigger>
                <SelectValue placeholder="Select difficulty" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="EASY_DEMON">Easy</SelectItem>
                <SelectItem value="MEDIUM_DEMON">Medium</SelectItem>
                <SelectItem value="HARD_DEMON">Hard</SelectItem>
                <SelectItem value="INSANE_DEMON">Insane</SelectItem>
                <SelectItem value="EXTREME_DEMON">Extreme</SelectItem>
              </SelectContent>
            </Select>
          </div>
          {levelType === 'classic' && (
            <>
              <div className="space-y-2">
                <Label htmlFor="duration">Duration</Label>
                <Select name="duration" onValueChange={(value) => handleSelectChange('duration', value)} value={formData.duration || ''}>
                  <SelectTrigger>
                    <SelectValue placeholder="Select duration" />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="TINY">Tiny</SelectItem>
                    <SelectItem value="SHORT">Short</SelectItem>
                    <SelectItem value="MEDIUM">Medium</SelectItem>
                    <SelectItem value="LONG">Long</SelectItem>
                    <SelectItem value="XL">XL</SelectItem>
                  </SelectContent>
                </Select>
              </div>
              <div className="space-y-2">
                <Label htmlFor="minimumCompletion">List Percentage (%)</Label>
                <Input
                  id="minimumCompletion"
                  name="minimumCompletion"
                  type="number"
                  min="0" 
                  max="100"
                  value={(formData as ClassicLevel).minimumCompletion}
                  onChange={handleInputChange}
                  required
                />
              </div>
            </>
          )}
          <div className="space-y-2">
            <Label htmlFor="link">Link</Label>
            <Input id="link" name="link" type="url" value={formData.link} onChange={handleInputChange} required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="thumbnail">Thumbnail URL</Label>
            <Input id="thumbnail" name="thumbnail" type="url" value={formData.thumbnail} onChange={handleInputChange} required />
          </div>
          {levelType === 'classic' && (
            <div className="space-y-2">
              <Label htmlFor="firstVictor">First Victor</Label>
              <Select 
                name="firstVictor" 
                onValueChange={(value) => handleSelectChange('firstVictor', value)}
                value={(formData as ClassicLevel).firstVictor || undefined}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select first victor" />
                </SelectTrigger>
                <SelectContent>
                  {players.map((player) => (
                    <SelectItem key={player.id} value={player.id}>
                      {player.username}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          )}
          {levelType === 'platformer' && (
            <div className="space-y-2">
              <Label htmlFor="recordHolder">Record Holder</Label>
              <Select 
                name="recordHolder" 
                onValueChange={(value) => handleSelectChange('recordHolder', value)}
                value={(formData as PlatformerLevel).recordHolder || undefined}
              >
                <SelectTrigger>
                  <SelectValue placeholder="Select record holder" />
                </SelectTrigger>
                <SelectContent>
                  {players.map((player) => (
                    <SelectItem key={player.id} value={player.id}>
                      {player.username}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          )}
          <div className="col-span-1 sm:col-span-2 flex justify-end">
            <Button type="submit">Update</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default EditLevel;
