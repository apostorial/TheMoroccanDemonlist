import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Label } from "@/components/ui/label";
import axios from '../jwt-axios';
import { toast } from "sonner";

interface AddLevelProps {
  levelType: 'classic' | 'platformer';
  onLevelAdded: () => void;
}

const initialFormData = {
  id: '',
  name: '',
  publisher: '',
  difficulty: '',
  duration: '',
  minimumCompletion: '',
  link: '',
  thumbnail: '',
};

const AddLevel: React.FC<AddLevelProps> = ({ levelType, onLevelAdded }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [formData, setFormData] = useState(initialFormData);

  const resetForm = () => {
    setFormData(initialFormData);
  };

  useEffect(() => {
    if (!isOpen) {
      resetForm();
    }
  }, [isOpen]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSelectChange = (name: string, value: string) => {
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post(`/api/staff/${levelType}-levels/create`, formData);
      toast.success('Level added successfully');
      setIsOpen(false);
      onLevelAdded();
      resetForm();
    } catch (error) {
      console.error('Error adding level:', error);
      toast.error('Failed to add level');
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button>Add Level</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle>Add New Level</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <div className="space-y-2">
            <Label htmlFor="id">ID</Label>
            <Input id="id" name="id" value={formData.id} onChange={handleInputChange} placeholder="86407629" required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="name">Name</Label>
            <Input id="name" name="name" value={formData.name} onChange={handleInputChange} placeholder="Tidal Wave" required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="publisher">Publisher</Label>
            <Input id="publisher" name="publisher" value={formData.publisher} onChange={handleInputChange} placeholder="OniLinkGD" required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="difficulty">Difficulty</Label>
            <Select name="difficulty" onValueChange={(value) => handleSelectChange('difficulty', value)} required>
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
                <Select name="duration" onValueChange={(value) => handleSelectChange('duration', value)} required>
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
                <Input id="minimumCompletion" name="minimumCompletion" type="number" min="0" max="100" value={formData.minimumCompletion} onChange={handleInputChange} placeholder="55" required />
              </div>
            </>
          )}
          <div className="space-y-2">
            <Label htmlFor="link">Link</Label>
            <Input id="link" name="link" type="url" value={formData.link} onChange={handleInputChange} placeholder="https://youtu.be/dQw4w9WgXcQ" required />
          </div>
          <div className="space-y-2">
            <Label htmlFor="thumbnail">Thumbnail URL</Label>
            <Input id="thumbnail" name="thumbnail" type="url" value={formData.thumbnail} onChange={handleInputChange} placeholder="https://i.ytimg.com/vi/dQw4w9WgXcQ/maxresdefault.jpg" required />
          </div>
          <div className="col-span-1 sm:col-span-2 flex justify-end">
            <Button type="submit">Add</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default AddLevel;
