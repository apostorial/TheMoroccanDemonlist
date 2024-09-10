import React, { useState, useEffect } from 'react';
import axios from '../jwt-axios';
import { toast } from "sonner";
import { Trash2, Pencil } from 'lucide-react';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";

interface Region {
  id: string;
  name: string;
  classicPoints: number;
  platformerPoints: number;
}

const AddRegion: React.FC<{ onRegionAdded: () => void }> = ({ onRegionAdded }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [name, setName] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post('/api/staff/regions/create', { name });
      toast.success('Region added successfully');
      setIsOpen(false);
      onRegionAdded();
      setName('');
    } catch (error) {
      console.error('Error adding region:', error);
      toast.error('Failed to add region');
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button>Add Region</Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Add New Region</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Region Name</Label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="flex justify-end">
            <Button type="submit">Add Region</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

const EditRegion: React.FC<{ region: Region; onRegionEdited: () => void }> = ({ region, onRegionEdited }) => {
  const [isOpen, setIsOpen] = useState(false);
  const [name, setName] = useState(region.name);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.put(`/api/staff/regions/update/${region.id}`, { name });
      toast.success('Region updated successfully');
      setIsOpen(false);
      onRegionEdited();
    } catch (error) {
      console.error('Error updating region:', error);
      toast.error('Failed to update region');
    }
  };

  return (
    <Dialog open={isOpen} onOpenChange={setIsOpen}>
      <DialogTrigger asChild>
        <Button variant="ghost" size="icon">
          <Pencil className="h-4 w-4" />
          <span className="sr-only">Edit region</span>
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Edit Region</DialogTitle>
        </DialogHeader>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="space-y-2">
            <Label htmlFor="name">Region Name</Label>
            <Input
              id="name"
              value={name}
              onChange={(e) => setName(e.target.value)}
              required
            />
          </div>
          <div className="flex justify-end">
            <Button type="submit">Update Region</Button>
          </div>
        </form>
      </DialogContent>
    </Dialog>
  );
};

const RegionList: React.FC = () => {
  const [regions, setRegions] = useState<Region[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchRegions();
  }, []);

  const fetchRegions = async () => {
    setIsLoading(true);
    try {
      const response = await axios.get('/api/public/regions');
      setRegions(response.data || []);
    } catch (error) {
      console.error('Error fetching regions:', error);
      toast.error('Failed to fetch regions');
      setRegions([]);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDelete = async (regionId: string) => {
    try {
      await axios.delete(`/api/staff/regions/delete/${regionId}`);
      toast.success('Region deleted successfully');
      fetchRegions();
    } catch (error) {
      console.error('Error deleting region:', error);
      toast.error('Failed to delete region');
    }
  };

  if (isLoading) {
    return <div className="p-6 rounded-lg shadow-md">Loading...</div>;
  }

  return (
    <div className="p-6 rounded-lg shadow-md">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">Regions</h2>
        <AddRegion onRegionAdded={fetchRegions} />
      </div>
      <div className="mb-4">
        <p className="text-sm">Total regions: {regions.length}</p>
      </div>
      {regions.length > 0 ? (
        <ul className="space-y-4 p-4 rounded-md max-h-[calc(100vh-300px)] overflow-y-auto">
          {regions.map((region) => (
            <li key={region.id} className="flex items-center justify-between bg-secondary/20 rounded-lg p-4 shadow-sm">
                <div className="flex flex-col mb-2 sm:mb-0">
                <span className="font-semibold text-lg">{region.name}</span>
                <span className="text-sm text-muted-foreground">Classic Points: {region.classicPoints}</span>
                <span className="text-sm text-muted-foreground">Platformer Points: {region.platformerPoints}</span>
                </div>
              <div className="flex items-center space-x-2">
                <EditRegion region={region} onRegionEdited={fetchRegions} />
                <AlertDialog>
                  <AlertDialogTrigger asChild>
                    <Button variant="ghost" size="icon" className="text-destructive hover:text-destructive/90">
                      <Trash2 size={20} />
                      <span className="sr-only">Delete region</span>
                    </Button>
                  </AlertDialogTrigger>
                  <AlertDialogContent>
                    <AlertDialogHeader>
                      <AlertDialogTitle>Are you sure?</AlertDialogTitle>
                      <AlertDialogDescription>
                        This action cannot be undone. This will permanently delete the region "{region.name}".
                      </AlertDialogDescription>
                    </AlertDialogHeader>
                    <AlertDialogFooter>
                      <AlertDialogCancel>Cancel</AlertDialogCancel>
                      <AlertDialogAction onClick={() => handleDelete(region.id)} className="bg-destructive text-destructive-foreground hover:bg-destructive/90">Delete</AlertDialogAction>
                    </AlertDialogFooter>
                  </AlertDialogContent>
                </AlertDialog>
              </div>
            </li>
          ))}
        </ul>
      ) : (
        <p className="text-center mt-4">No regions found.</p>
      )}
    </div>
  );
};

const StaffRegionList: React.FC = () => {
  return (
    <div className="container mx-auto">
      <RegionList />
    </div>
  );
};

export default StaffRegionList;