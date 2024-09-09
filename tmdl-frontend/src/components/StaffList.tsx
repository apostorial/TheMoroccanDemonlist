import React, { useState, useEffect } from 'react';
import { DndContext, closestCenter, KeyboardSensor, PointerSensor, useSensor, useSensors } from '@dnd-kit/core';
import { arrayMove, SortableContext, sortableKeyboardCoordinates, verticalListSortingStrategy } from '@dnd-kit/sortable';
import { useSortable } from '@dnd-kit/sortable';
import { CSS } from '@dnd-kit/utilities';
import axios from '../jwt-axios';
import { toast } from "sonner";
import { restrictToVerticalAxis } from '@dnd-kit/modifiers';

interface Level {
  id: string;
  name: string;
  ranking: number;
}

const SortableLevel: React.FC<{ id: string; ranking: number; name: string }> = ({ id, ranking, name }) => {
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
  } = useSortable({ id });

  const style = {
    transform: CSS.Transform.toString(transform),
    transition,
  };

  return (
    <li ref={setNodeRef} style={style} {...attributes} {...listeners} className="border rounded-md mb-2">
      <div className="p-2">
        <span>#{ranking} - {name}</span>
      </div>
    </li>
  );
};

const StaffLevelList: React.FC<{ levelType: 'classic' | 'platformer' }> = ({ levelType }) => {
  const [levels, setLevels] = useState<Level[]>([]);
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
      toast.error('Failed to fetch levels');
    }
  };

  const handleDragEnd = (event: any) => {
    const { active, over } = event;

    if (active.id !== over.id) {
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
    }
  };

  return (
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
        <ul className="space-y-2 p-4">
          {levels.map((level) => (
            <SortableLevel key={level.id} id={level.id} ranking={level.ranking} name={level.name} />
          ))}
        </ul>
      </SortableContext>
    </DndContext>
  );
};

export default StaffLevelList;
