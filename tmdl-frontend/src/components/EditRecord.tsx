import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import jwtAxios from '../jwt-axios';
import { toast } from "sonner";
import { Pencil } from 'lucide-react';

interface EditRecordProps {
  recordType: 'classic' | 'platformer';
  record: {
    id: string;
    level: string;
    player: string;
    recordPercentage?: number;
    recordTime?: string;
    link: string;
  };
  onRecordEdited: () => void;
  getLevelName: (levelId: string) => string;
}

const EditRecord: React.FC<EditRecordProps> = ({ recordType, record, onRecordEdited, getLevelName }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [formData, setFormData] = useState(record);
  const [durationInput, setDurationInput] = useState('');

  useEffect(() => {
    setFormData(record);
    if (recordType === 'platformer' && record.recordTime) {
      setDurationInput(formatISO8601ToDuration(record.recordTime));
    }
  }, [record, recordType]);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (name === 'recordTime') {
      setDurationInput(value);
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
  };

  const formatTimeToISO8601 = (time: string): string => {
    const [hours, minutes, seconds] = time.split(':').map(Number);
    return `PT${hours}H${minutes}M${seconds}S`;
  };

  const formatISO8601ToDuration = (iso8601: string): string => {
    const match = iso8601.match(/PT(\d+H)?(\d+M)?(\d+S)?/);
    if (!match) return '00:00:00';
    
    const hours = match[1] ? match[1].replace('H', '') : '0';
    const minutes = match[2] ? match[2].replace('M', '') : '0';
    const seconds = match[3] ? match[3].replace('S', '') : '0';
    
    return `${hours.padStart(2, '0')}:${minutes.padStart(2, '0')}:${seconds.padStart(2, '0')}`;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const dataToSend = {
        ...formData,
        recordTime: recordType === 'platformer' ? formatTimeToISO8601(durationInput) : undefined,
        recordPercentage: recordType === 'classic' ? Number(formData.recordPercentage) : undefined,
      };
      await jwtAxios.put(`/api/staff/${recordType}-records/update/${record.id}`, dataToSend);
      toast.success('Record updated successfully');
      setIsOpen(false);
      onRecordEdited();
    } catch (error) {
      console.error('Error updating record:', error);
      toast.error('Failed to update record');
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon">
          <Pencil className="h-4 w-4" />
          <span className="sr-only">Edit record</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Edit Record</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="level">Level</Label>
            <Input id="level" value={getLevelName(formData.level)} readOnly />
          </div>
          <div className="space-y-2">
            <Label htmlFor="player">Player</Label>
            <Input id="player" value={formData.player} readOnly />
          </div>
          {recordType === 'classic' ? (
            <div className="space-y-2">
              <Label htmlFor="recordPercentage">Percentage</Label>
              <Input 
                id="recordPercentage" 
                name="recordPercentage" 
                type="number" 
                min="0" 
                max="100" 
                value={formData.recordPercentage} 
                onChange={handleInputChange} 
                required 
              />
            </div>
          ) : (
            <div className="space-y-2">
              <Label htmlFor="recordTime">Record Time (HH:MM:SS)</Label>
              <Input 
                id="recordTime" 
                name="recordTime" 
                type="text" 
                pattern="[0-9]{2}:[0-9]{2}:[0-9]{2}"
                placeholder="00:00:00"
                value={durationInput} 
                onChange={handleInputChange} 
                required 
              />
            </div>
          )}
          <div className="space-y-2">
            <Label htmlFor="link">Video Link</Label>
            <Input 
              id="link" 
              name="link" 
              type="url" 
              value={formData.link} 
              onChange={handleInputChange} 
              required 
            />
          </div>
          <div className="flex justify-end">
            <Button type="submit">Update Record</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default EditRecord;
