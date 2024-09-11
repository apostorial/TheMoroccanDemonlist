import React from 'react';
import { useNavigate } from 'react-router-dom';
import { toast } from 'sonner';
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { AspectRatio } from "@/components/ui/aspect-ratio";

interface LevelCardProps {
  id: string;
  name: string;
  ranking: number;
  difficulty: string;
  publisher: string;
  thumbnail: string;
  points: number;
  levelType: 'classic' | 'platformer';
}

const LevelCard: React.FC<LevelCardProps> = ({ 
  ranking, name, publisher, id, thumbnail, difficulty, points, levelType 
}) => {
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`/${levelType}/level/${id}`);
  };

  const handleCopyLevelId = (e: React.MouseEvent) => {
    e.stopPropagation();
    navigator.clipboard.writeText(id);
    toast.success('Level ID copied to clipboard!', {
      description: `Level ID: ${id}`,
    });
  };

  const formatDifficulty = (difficulty: string) => {
    return difficulty.toLowerCase().split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
  };

  return (
    <Card 
      className="w-full cursor-pointer hover:shadow-lg transition-shadow"
      onClick={handleClick}
    >
      <CardContent className="p-4 flex">
        <div className="w-1/4 mr-4">
          <AspectRatio ratio={16 / 9}>
              <img src={thumbnail} alt={`Thumbnail for ${name}`} className="object-cover w-full h-full rounded-lg" />
          </AspectRatio>
        </div>
        <div className="w-2/3 flex flex-col justify-between">
          <div>
            <h2 className="text-lg font-bold">#{ranking} - {name}</h2>
            <p><span className="font-semibold">Published by:</span> {publisher}</p>
            <p><span className="font-semibold">Difficulty:</span> {formatDifficulty(difficulty)}</p>
            <p><span className="font-semibold">Points:</span> {points} points</p>
            <p><span className="font-semibold">Type:</span> {levelType.charAt(0).toUpperCase() + levelType.slice(1)}</p>
            <Button onClick={handleCopyLevelId} className="mt-2" >
              Copy Level ID
            </Button>
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

export default LevelCard;
