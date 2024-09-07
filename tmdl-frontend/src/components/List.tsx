import { useState, useEffect } from 'react';
import axios from '../axios-config';
import LevelCard from './LevelCard';

interface Level {
  ranking: number;
  name: string;
  publisher: string;
  levelId: string;
  thumbnail: string;
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
      setLevels(response.data);
      setError(null);
    } catch (error) {
      console.error('Error fetching levels:', error);
      setError('Failed to load levels. Please try again later.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      {isLoading && <p>Loading levels...</p>}
      {error && <p className="text-red-500">{error}</p>}
      {!isLoading && !error && levels.map((level) => (
        <LevelCard
          key={level.levelId}
          ranking={level.ranking}
          name={level.name}
          publisher={level.publisher}
          levelId={level.levelId}
          thumbnail={level.thumbnail}
        />
      ))}
    </>
  );
}

export default List;