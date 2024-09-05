import React from 'react';
import { Button } from './ui/button';

interface LevelCardProps {
  ranking: number;
  name: string;
  publisher: string;
  levelId: string;
  thumbnail: string;
}

const LevelCard: React.FC<LevelCardProps> = ({ ranking, name, publisher, levelId, thumbnail }) => {
  return (
    <div className="bg-gray-700 rounded-lg p-4 mb-4 flex items-start">
      <img 
        src={thumbnail || '/path/to/fallback-image.jpg'} 
        alt={`Thumbnail for ${name}`} 
        className="w-[259px] h-[146px] object-cover rounded-lg mr-4"
        onError={(e) => {
          e.currentTarget.src = '/path/to/fallback-image.jpg';
        }}
      />
      <div>
        <h2 className="text-xl font-bold text-gray-100">#{ranking} - {name}</h2>
        <p className="text-gray-300">Published by: {publisher}</p>
        <Button className="mt-2" value={levelId}>Copy Level ID</Button>
      </div>
    </div>
  );
};

export default LevelCard;
