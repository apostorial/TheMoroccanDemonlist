import { useState, useEffect } from 'react';
import axios from '../normal-axios';
import LevelCard from './LevelCard';
import LevelCardSkeleton from './LevelCardSkeleton';

interface Level {
  ranking: number;
  name: string;
  publisher: string;
  id: string;
  thumbnail: string;
  difficulty: string;
  points: number;
}

interface ListProps {
  level_type: string;
  list_type: string;
}

function List({ level_type, list_type }: ListProps) {
  const [levels, setLevels] = useState<Level[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchLevels();
  }, [level_type, list_type]);

  const fetchLevels = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get<Level[]>(`/api/public/${level_type}-levels/list/${list_type}`);
      setLevels(Array.isArray(response.data) ? response.data : []);
      setError(null);
    } catch (error) {
      console.error('Error fetching levels:', error);
      setError('Failed to load levels. Please try again later.');
      setLevels([]);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="space-y-2">
      {isLoading && <LevelCardSkeleton />}
      {error && <p className="text-red-500">{error}</p>}
      {!isLoading && !error && Array.isArray(levels) && levels.length > 0 ? (
        levels.map((level) => (
          <LevelCard
            key={level.id}
            ranking={level.ranking}
            name={level.name}
            publisher={level.publisher}
            id={level.id}
            thumbnail={level.thumbnail}
            difficulty={level.difficulty}
            points={level.points}
          />
        ))
      ) : (
        <LevelCardSkeleton />
      )}
    </div>
  );
}

export default List;