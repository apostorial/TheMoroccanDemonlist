import React, { useState, useEffect } from 'react';
import axios from '../jwt-axios';
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";
import { useNavigate } from 'react-router-dom';
import StatViewerSkeleton from './StatViewerSkeleton';

interface Region {
  id: string;
  name: string;
  classicPoints: number;
  platformerPoints: number;
}

interface Player {
  id: string;
  username: string;
  classicPoints: number;
  platformerPoints: number;
}

const StatViewer: React.FC = () => {
  const navigate = useNavigate();
  const [regions, setRegions] = useState<Region[]>([]);
  const [selectedRegion, setSelectedRegion] = useState<Region | null>(null);
  const [players, setPlayers] = useState<Player[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchRegions();
  }, []);

  const handlePlayerClick = (username: string) => {
    navigate(`/profile/${username}`);
  };

  useEffect(() => {
    if (regions.length > 0 && !selectedRegion) {
      handleRegionClick(regions[0]);
    }
  }, [regions]);

  const fetchRegions = async () => {
    setIsLoading(true);
    try {
      const response = await axios.get('/api/public/regions');
      setRegions(response.data || []);
    } catch (error) {
      console.error('Error fetching regions:', error);
      toast.error('Failed to fetch regions');
    } finally {
      setIsLoading(false);
    }
  };

  const fetchPlayers = async (regionId: string) => {
    setIsLoading(true);
    try {
      const response = await axios.get(`/api/public/players/region/${regionId}`);
      setPlayers(response.data || []);
    } catch (error) {
      console.error('Error fetching players:', error);
      toast.error('Failed to fetch players');
    } finally {
      setIsLoading(false);
    }
  };

  const handleRegionClick = (region: Region) => {
    setSelectedRegion(region);
    fetchPlayers(region.id);
  };

  const formatPoints = (points: number): string => {
    return points.toFixed(2);
  };

  const pointsLayoutClass = "flex flex-col space-y-2 [&>*]:flex-1 max-[1300px]:flex-col max-[1300px]:space-y-2 min-[1301px]:flex-row min-[1301px]:space-x-2 min-[1301px]:space-y-0";

  if (isLoading && regions.length === 0) {
    return <StatViewerSkeleton />;
  }

  return (
    <>
    <div className="flex flex-col lg:flex-row space-y-4 lg:space-y-0 lg:space-x-4 p-4 sm:p-6">
      <Card className="w-full lg:w-1/2">
        <CardHeader>
          <CardTitle>Regions</CardTitle>
        </CardHeader>
        <CardContent>
          <ScrollArea className="h-[calc(100vh-250px)]">
            <ul className="space-y-2 pr-4 pl-1 sm:pl-4">
              {regions.map((region) => (
                <li key={region.id}
                    className="flex flex-col p-2 bg-secondary/20 rounded-lg cursor-pointer hover:bg-secondary/30 transition-colors"
                    onClick={() => handleRegionClick(region)}
                >
                  <span className="mb-2">{region.name}</span>
                  <div className={pointsLayoutClass}>
                    <Badge variant="secondary">Classic: {formatPoints(region.classicPoints)}</Badge>
                    <Badge variant="secondary">Platformer: {formatPoints(region.platformerPoints)}</Badge>
                  </div>
                </li>
              ))}
            </ul>
          </ScrollArea>
        </CardContent>
      </Card>

      <Card className="w-full lg:w-1/2">
        <CardHeader>
          <CardTitle>{selectedRegion ? `Players in ${selectedRegion.name}` : 'Select a region'}</CardTitle>
        </CardHeader>
        <CardContent>
          {selectedRegion ? (
            <ScrollArea className="h-[calc(100vh-250px)]">
              <ul className="space-y-2 pr-4 pl-1 sm:pl-4">
                {players.map((player) => (
                  <li 
                    key={player.id} 
                    className="flex flex-col p-2 bg-secondary/20 rounded-lg cursor-pointer hover:bg-secondary/30 transition-colors"
                    onClick={() => handlePlayerClick(player.username)}
                  >
                    <span className="mb-2">{player.username}</span>
                    <div className={pointsLayoutClass}>
                      <Badge variant="secondary">Classic: {formatPoints(player.classicPoints)}</Badge>
                      <Badge variant="secondary">Platformer: {formatPoints(player.platformerPoints)}</Badge>
                    </div>
                  </li>
                ))}
              </ul>
            </ScrollArea>
          ) : (
            <p className="text-center text-muted-foreground">Select a region to view its players</p>
          )}
        </CardContent>
      </Card>
    </div>
    </>
  );
};

export default StatViewer;
