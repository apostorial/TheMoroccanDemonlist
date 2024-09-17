import React, { useState, useEffect } from 'react';
import { DndContext, closestCenter, KeyboardSensor, PointerSensor, useSensor, useSensors } from '@dnd-kit/core';
import { arrayMove, SortableContext, sortableKeyboardCoordinates, verticalListSortingStrategy } from '@dnd-kit/sortable';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import axios from '../jwt-axios';
import { toast } from "sonner";
import { restrictToVerticalAxis } from '@dnd-kit/modifiers';
import AddLevel from './AddLevel';
import EditLevel from './EditLevel';
import { GripVertical } from 'lucide-react';
import { Trash2 } from 'lucide-react';
import { AlertDialog, AlertDialogAction, AlertDialogCancel, AlertDialogContent, AlertDialogDescription, AlertDialogFooter, AlertDialogHeader, AlertDialogTitle, AlertDialogTrigger } from "@/components/ui/alert-dialog";
import { Button } from "@/components/ui/button";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Loader2 } from 'lucide-react';

interface Level {
  id: string;
  name: string;
  publisher: string;
  difficulty: string;
  duration: string;
  minimumCompletion: number;
  link: string;
  thumbnail: string;
  firstVictor: string | null;
  recordHolder: string | null;
  ranking: number;
}

const SortableLevel: React.FC<{ level: Level; levelType: 'classic' | 'platformer'; onLevelEdited: () => void; onLevelDeleted: () => void }> = ({ level, levelType, onLevelEdited, onLevelDeleted }) => {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
  } = useSortable({ id: level.id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`/api/staff/${levelType}-levels/delete/${level.id}`);
      onLevelDeleted();
    } catch (error) {
      console.error('Error deleting level:', error);
      toast.error('Failed to delete level');
    }
  };

  return (
    <li 
      ref={setNodeRef} 
      style={style} 
      className="flex items-center border rounded-md mb-2"
    >
      <div 
        {...attributes} 
        {...listeners}
        className="p-2 cursor-move"
      >
        <GripVertical size={20} />
      </div>
      <div className="p-2 flex-grow">
        #{level.ranking} - {level.name}
      </div>
      <div className="p-2">
        <EditLevel levelType={levelType} level={level} onLevelEdited={onLevelEdited} />
      </div>
      <div className="p-2">
        <AlertDialog>
          <AlertDialogTrigger asChild>
            <Button variant="ghost" size="icon" className="text-red-500 hover:text-red-700">
              <Trash2 size={20} />
            </Button>
          </AlertDialogTrigger>
          <AlertDialogContent>
            <AlertDialogHeader>
              <AlertDialogTitle>Are you sure?</AlertDialogTitle>
              <AlertDialogDescription>
                This action cannot be undone. This will permanently delete the level.
              </AlertDialogDescription>
            </AlertDialogHeader>
            <AlertDialogFooter>
              <AlertDialogCancel>Cancel</AlertDialogCancel>
              <AlertDialogAction onClick={handleDelete} className="bg-red-500 hover:bg-red-600">Delete</AlertDialogAction>
            </AlertDialogFooter>
          </AlertDialogContent>
        </AlertDialog>
      </div>
    </li>
  );
};

const StaffLevelList: React.FC<{ levelType: 'classic' | 'platformer' }> = ({ levelType }) => {
  const [levels, setLevels] = useState<Level[]>([]);
  const [isReordering, setIsReordering] = useState(false);
  const sensors = useSensors(
    useSensor(PointerSensor),
    useSensor(KeyboardSensor, {
      coordinateGetter: sortableKeyboardCoordinates,
    })
  );

  useEffect(() => {
    fetchLevels();
  }, [levelType]);

  const fetchLevels = async () => {
    try {
      const response = await axios.get(`/api/public/${levelType}-levels/list`);
      setLevels(response.data.sort((a: Level, b: Level) => a.ranking - b.ranking));
    } catch (error) {
      console.error('Error fetching levels:', error);
    }
  };

  const handleDragEnd = async (event: any) => {
    const { active, over } = event;

    if (active.id !== over.id) {
      setIsReordering(true);
      setLevels((items) => {
        const oldIndex = items.findIndex((item) => item.id === active.id);
        const newIndex = items.findIndex((item) => item.id === over.id);

        const newItems = arrayMove(items, oldIndex, newIndex);
        const updatedItems = newItems.map((item, index) => ({
          ...item,
          ranking: index + 1
        }));

        updateLevelOrder(updatedItems);
        return updatedItems;
      });
    }
  };

  const updateLevelOrder = async (updatedLevels: Level[]) => {
    try {
      const levelOrderData = updatedLevels.map(level => ({
        id: level.id,
        ranking: level.ranking
      }));
      await axios.put(`/api/staff/${levelType}-levels/reorder`, levelOrderData);
      toast.success('Levels reordered successfully');
    } catch (error) {
      console.error('Error updating level rankings:', error);
      toast.error('Failed to update level rankings');
      fetchLevels();
    } finally {
      setIsReordering(false);
    }
  };

  const handleLevelAdded = () => {
    fetchLevels();
  };

  const handleLevelEdited = () => {
    fetchLevels();
  };

  const handleLevelDeleted = (deletedLevelId: string) => {
    setLevels(prevLevels => prevLevels.filter(level => level.id !== deletedLevelId));
    toast.success('Level deleted successfully');
  };

  return (
    <div className="p-6 rounded-lg shadow-md">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold">{levelType.charAt(0).toUpperCase() + levelType.slice(1)} Levels</h2>
        <AddLevel levelType={levelType} onLevelAdded={handleLevelAdded} />
      </div>
      <div className="mb-4 flex items-center justify-between mt-1">
        <p className="text-sm mt-1">Total levels: {levels.length}</p>
        {isReordering && (
          <div className="flex items-center text-sm text-muted-foreground">
            <Loader2 className="mr-2 h-4 w-4 animate-spin" />
            Reordering... Please wait until reordering is complete.
          </div>
        )}
      </div>
      <DndContext 
        sensors={sensors}
        collisionDetection={closestCenter}
        onDragEnd={handleDragEnd}
        modifiers={[restrictToVerticalAxis]}
      >
        <SortableContext 
          items={levels.map(level => level.id)}
          strategy={verticalListSortingStrategy}
        >
          <ScrollArea>
            <ul className="space-y-2 p-4 rounded-md">
              {levels.map((level) => (
                <SortableLevel 
                  key={level.id} 
                  level={level} 
                  levelType={levelType} 
                  onLevelEdited={handleLevelEdited} 
                  onLevelDeleted={() => handleLevelDeleted(level.id)}
                />
              ))}
            </ul>
          </ScrollArea>
        </SortableContext>
      </DndContext>
      {levels.length === 0 && (
        <p className="text-center mt-4">No levels found. Add some levels to get started.</p>
      )}
    </div>
  );
};

const StaffList: React.FC = () => {
  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <StaffLevelList levelType="classic" />
      <StaffLevelList levelType="platformer" />
    </div>
  );
};

export default StaffList;
