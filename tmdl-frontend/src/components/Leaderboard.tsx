import React, { useState, useEffect } from 'react';
import axios from '../normal-axios';
import { toast } from "sonner";
import { Button } from "@/components/ui/button";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { User, Medal } from "lucide-react";
import { useNavigate } from 'react-router-dom';
import { ScrollArea } from "@/components/ui/scroll-area";

interface Player {
  id: string;
  username: string;
  classicPoints: number;
  platformerPoints: number;
  avatarUrl?: string;
}

const Leaderboard: React.FC = () => {
  const navigate = useNavigate();
  const [classicPlayers, setClassicPlayers] = useState<Player[]>([]);
  const [platformerPlayers, setPlatformerPlayers] = useState<Player[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [activeTab, setActiveTab] = useState<'classic' | 'platformer'>('classic');

  useEffect(() => {
    fetchLeaderboard();
  }, []);

  const handlePlayerClick = (username: string) => {
    navigate(`/profile/${username}`);
  };

  const fetchLeaderboard = async () => {
    setIsLoading(true);
    try {
      const [classicResponse, platformerResponse] = await Promise.all([
        axios.get('/api/public/players/classic-points'),
        axios.get('/api/public/players/platformer-points')
      ]);
      const classicPlayersWithAvatars = await Promise.all(classicResponse.data.map(fetchPlayerAvatar));
      const platformerPlayersWithAvatars = await Promise.all(platformerResponse.data.map(fetchPlayerAvatar));
      setClassicPlayers(classicPlayersWithAvatars);
      setPlatformerPlayers(platformerPlayersWithAvatars);
    } catch (error) {
      toast.error('Failed to fetch leaderboard');
    } finally {
      setIsLoading(false);
    }
  };

  const fetchPlayerAvatar = async (player: Player): Promise<Player> => {
    try {
      const response = await axios.get(`/api/public/players/${player.id}/avatar`, {
        responseType: 'arraybuffer'
      });
      const base64 = btoa(
        new Uint8Array(response.data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          '',
        )
      );
      return { ...player, avatarUrl: `data:image/jpeg;base64,${base64}` };
    } catch (error) {
      return player;
    }
  };

  const formatPoints = (points: number): string => {
    return points.toFixed(2);
  };

  const getMedalIcon = (index: number) => {
    switch(index) {
      case 0:
        return <Medal className="w-6 h-6 text-yellow-400" />; // Gold
      case 1:
        return <Medal className="w-6 h-6 text-gray-400" />; // Silver
      case 2:
        return <Medal className="w-6 h-6 text-amber-600" />; // Bronze
      default:
        return null;
    }
  };

  const renderPlayerList = (players: Player[]) => {
    return players.map((player, index) => (
      <li 
        key={player.id} 
        className="flex items-center justify-between bg-secondary/20 rounded-lg p-4 shadow-sm cursor-pointer hover:bg-secondary/30 transition-colors"
        onClick={() => handlePlayerClick(player.username)}
      >
        <div className="flex items-center space-x-4">
          <span className="font-bold text-lg w-8 flex items-center justify-center">
            {getMedalIcon(index) || (index + 1)}
          </span>
          <Avatar className="w-10 h-10">
            {player.avatarUrl ? (
              <AvatarImage src={player.avatarUrl} alt={player.username} className="object-cover w-full h-full"/>
            ) : (
              <AvatarFallback><User className="w-6 h-6" /></AvatarFallback>
            )}
          </Avatar>
          <span className="font-semibold">{player.username}</span>
        </div>
        <span className="font-bold">
          {formatPoints(activeTab === 'classic' ? player.classicPoints : player.platformerPoints)} points
        </span>
      </li>
    ));
  };

  if (isLoading) {
    return <div className="p-6 rounded-lg shadow-md">Loading...</div>;
  }

  return (
    <div className="p-6 rounded-lg shadow-md">
      <div className="flex justify-between items-center mb-4">
        <h2 className="text-2xl font-bold">Leaderboard</h2>
        <div className="space-x-2">
          <Button 
            onClick={() => setActiveTab('classic')} 
            variant={activeTab === 'classic' ? 'default' : 'outline'}
          >
            Classic
          </Button>
          <Button 
            onClick={() => setActiveTab('platformer')} 
            variant={activeTab === 'platformer' ? 'default' : 'outline'}
          >
            Platformer
          </Button>
        </div>
      </div>
      <ScrollArea className="h-[calc(100vh-200px)]">
        <ul className="space-y-4 p-4 rounded-md">
          {activeTab === 'classic' 
            ? renderPlayerList(classicPlayers)
            : renderPlayerList(platformerPlayers)
          }
        </ul>
      </ScrollArea>
    </div>
  );
};

export default Leaderboard;
