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
    if (recordType === 'platformer' && record.recordTime) {
      const formattedDuration = formatISO8601ToDuration(record.recordTime);
      setDurationInput(formattedDuration);
    }
  }, [record, recordType]);

  const handleOpenChange = (open: boolean) => {
    setIsOpen(open);
    if (open) {
      setFormData(record);
      if (recordType === 'platformer' && record.recordTime) {
        const formattedDuration = formatISO8601ToDuration(record.recordTime);
        setDurationInput(formattedDuration);
      }
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (name === 'recordTime') {
      setDurationInput(value);
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
  };

  const formatTimeToISO8601 = (time: string): string => {
    const [hours, minutes, seconds, milliseconds] = time.split(/[:.]/).map(Number);
    return `PT${hours}H${minutes}M${seconds}.${milliseconds.toString().padStart(3, '0')}S`;
  };

  const formatISO8601ToDuration = (input: string): string => {
    console.log('Input:', input);

    if (/^\d{2}:\d{2}:\d{2}\.\d{3}$/.test(input)) {
      return input;
    }

    const match = input.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)(?:\.(\d+))?S)?/);
    if (match) {
      const hours = match[1] ? match[1].padStart(2, '0') : '00';
      const minutes = match[2] ? match[2].padStart(2, '0') : '00';
      const seconds = match[3] ? match[3].split('.')[0].padStart(2, '0') : '00';
      const milliseconds = match[4] ? match[4].padEnd(3, '0') : '000';
      
      const result = `${hours}:${minutes}:${seconds}.${milliseconds}`;
      console.log('Formatted result:', result);
      return result;
    }

    console.log('No match found, returning default');
    return '00:00:00.000';
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
    <Dialog open={isOpen} onOpenChange={handleOpenChange}>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon">
          <Pencil className="h-4 w-4" />
          <span className="sr-only">Edit record</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[600px]">
        <DialogHeader>
          <DialogTitle>Edit Record</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="space-y-2 md:col-span-1">
            <Label htmlFor="level">Level</Label>
            <Input id="level" value={getLevelName(formData.level)} readOnly />
          </div>
          <div className="space-y-2 md:col-span-1">
            <Label htmlFor="player">Player</Label>
            <Input id="player" value={formData.player} readOnly />
          </div>
          {recordType === 'classic' ? (
            <div className="space-y-2 md:col-span-2">
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
            <div className="space-y-2 md:col-span-2">
              <Label htmlFor="recordTime">Record Time (HH:mm:ss.SSS)</Label>
              <Input 
                id="recordTime" 
                name="recordTime" 
                type="text" 
                pattern="[0-9]{2}:[0-9]{2}:[0-9]{2}\.[0-9]{3}"
                placeholder="01:10:32.456"
                value={durationInput} 
                onChange={handleInputChange} 
                required 
              />
            </div>
          )}
          <div className="space-y-2 md:col-span-2">
            <Label htmlFor="link">Video Link</Label>
            <Input 
              id="link" 
              name="link" 
              type="url" 
              value={formData.link} 
              onChange={handleInputChange} 
            />
          </div>
          <div className="flex justify-end md:col-span-2">
            <Button type="submit">Update Record</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

export default EditRecord;
