import { useState, useEffect } from 'react';
import axios from 'axios';
import LevelCard from './LevelCard';
import InfoCard from './InfoCard';

interface Level {
  ranking: number;
  name: string;
  publisher: string;
  levelId: string;
  thumbnail: string;
}

function Content() {
  const [levels, setLevels] = useState<Level[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    fetchLevels();
  }, []);

  const fetchLevels = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get<Level[]>('http://localhost:8080/api/public/classic-levels');
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
    <div className="flex h-screen bg-gray-900 text-white p-4">
      <div className="flex-1 pr-4">
        {isLoading && <p>Loading levels...</p>}
        {error && <p className="text-red-500">{error}</p>}
        {!isLoading && !error && levels.map((level) => (
          <LevelCard
            key={level.levelId}
            ranking={level.ranking}
            name={level.name}
            publisher={level.publisher}
            levelId={level.levelId}
            thumbnail={level.thumbnail}  // Add this line
          />
        ))}
      </div>
      <div className="w-1/4">
        <InfoCard 
          title="List editors"
          content="Contact any of these people if you need assistance regarding the list."
          items={["Apostorial"]}
        />
        <InfoCard 
          title="Guidelines"
          content="Before any submission please consider checking the guidelines for the list. Any submission that does not follow the guidelines will be rejected."
          buttonText="Read the guidelines"
        />
      </div>
    </div>
  );
}

export default Content;