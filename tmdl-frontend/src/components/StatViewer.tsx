import React, { useState, useEffect } from 'react';
import axios from '../jwt-axios';
import { toast } from "sonner";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";

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
  const [regions, setRegions] = useState<Region[]>([]);
  const [selectedRegion, setSelectedRegion] = useState<Region | null>(null);
  const [players, setPlayers] = useState<Player[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    fetchRegions();
  }, []);

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

  if (isLoading && regions.length === 0) {
    return <div className="p-6 rounded-lg shadow-md">Loading...</div>;
  }

  return (
    <div className="flex space-x-4">
      <Card className="w-1/2">
        <CardHeader>
          <CardTitle>Regions</CardTitle>
        </CardHeader>
        <CardContent>
          <ScrollArea className="h-[calc(100vh-200px)]">
            <ul className="space-y-2">
              {regions.map((region) => (
                <li key={region.id}>
                  <Button
                    variant={selectedRegion?.id === region.id ? "secondary" : "ghost"}
                    className="w-full justify-between"
                    onClick={() => handleRegionClick(region)}
                  >
                    <span>{region.name}</span>
                    <div className="flex space-x-2">
                      <Badge variant="secondary">Classic: {region.classicPoints}</Badge>
                      <Badge variant="secondary">Platformer: {region.platformerPoints}</Badge>
                    </div>
                  </Button>
                </li>
              ))}
            </ul>
          </ScrollArea>
        </CardContent>
      </Card>

      <Card className="w-1/2">
        <CardHeader>
          <CardTitle>{selectedRegion ? `Players in ${selectedRegion.name}` : 'Select a region'}</CardTitle>
        </CardHeader>
        <CardContent>
          {selectedRegion ? (
            <ScrollArea className="h-[calc(100vh-200px)]">
              <ul className="space-y-2">
                {players.map((player) => (
                  <li key={player.id} className="flex justify-between items-center p-2 bg-secondary/20 rounded-lg">
                    <span>{player.username}</span>
                    <div className="flex space-x-2">
                      <Badge variant="secondary">Classic: {player.classicPoints}</Badge>
                      <Badge variant="secondary">Platformer: {player.platformerPoints}</Badge>
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
  );
};

export default StatViewer;
