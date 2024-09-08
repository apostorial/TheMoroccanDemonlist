import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axios from '../axios-config';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";

interface LevelCount {
  main: number;
  extended: number;
  legacy: number;
}

interface LevelInfo {
  id: string;
  name: string;
  // Add other properties as needed
}

interface ProfileData {
  classicHardestLevel: LevelInfo;
  classicCompletedLevels: LevelInfo[];
  classicFirstVictor: LevelInfo[];
  classicLevelCount: LevelCount;
  platformerHardestLevel?: LevelInfo;
  platformerCompletedLevels?: LevelInfo[];
  platformerFirstVictor?: LevelInfo[];
  platformerLevelCount?: LevelCount;
}

export function Profile() {
  const { playerId: urlPlayerId } = useParams<{ playerId?: string }>();
  const navigate = useNavigate();
  const [inputPlayerId, setInputPlayerId] = useState('');
  const [profileData, setProfileData] = useState<ProfileData | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (urlPlayerId) {
      fetchProfileData(urlPlayerId);
    }
  }, [urlPlayerId]);

  const fetchProfileData = async (id: string) => {
    setLoading(true);
    setError('');
    try {
      const [
        classicHardestLevel,
        classicRecords,
        classicFirstVictor,
        classicLevelCount,
        platformerHardestLevel,
        platformerRecords,
        platformerFirstVictor,
        platformerLevelCount
      ] = await Promise.all([
        axios.get(`/api/public/classic-levels/hardestLevel/${id}`),
        axios.get(`/api/public/classic-levels/player/${id}`),
        axios.get(`/api/public/classic-levels/firstVictor/${id}`),
        axios.get(`/api/public/classic-levels/count/${id}`),
        axios.get(`/api/public/platformer-levels/hardestLevel/${id}`).catch(() => ({ data: null })),
        axios.get(`/api/public/platformer-levels/player/${id}`).catch(() => ({ data: [] })),
        axios.get(`/api/public/platformer-levels/recordHolder/${id}`).catch(() => ({ data: [] })),
        axios.get(`/api/public/platformer-levels/count/${id}`).catch(() => ({ data: null }))
      ]);

      setProfileData({
        classicHardestLevel: classicHardestLevel.data,
        classicCompletedLevels: classicRecords.data,
        classicFirstVictor: classicFirstVictor.data,
        classicLevelCount: classicLevelCount.data,
        platformerHardestLevel: platformerHardestLevel.data,
        platformerCompletedLevels: platformerRecords.data,
        platformerFirstVictor: platformerFirstVictor.data,
        platformerLevelCount: platformerLevelCount.data
      });
    } catch (err) {
      setError('Failed to fetch profile data');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (inputPlayerId) {
      navigate(`/profile/${inputPlayerId}`);
    }
  };

  return (
    <div className="p-4 space-y-4">
      <form onSubmit={handleSearch} className="flex gap-2">
        <Input
          type="text"
          value={inputPlayerId}
          onChange={(e) => setInputPlayerId(e.target.value)}
          placeholder="Enter player ID"
          className="flex-grow"
        />
        <Button type="submit">Search</Button>
      </form>

      {loading && <p>Loading...</p>}
      {error && <p className="text-red-500">{error}</p>}

      {profileData && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <Card>
            <CardHeader>
              <CardTitle>Classic Levels</CardTitle>
            </CardHeader>
            <CardContent>
              <p><strong>Hardest Level:</strong> {profileData.classicHardestLevel.name}</p>
              <p><strong>Levels Count:</strong></p>
              <ul>
                <li>Main: {profileData.classicLevelCount.main}</li>
                <li>Extended: {profileData.classicLevelCount.extended}</li>
                <li>Legacy: {profileData.classicLevelCount.legacy}</li>
              </ul>
              <p><strong>Completed Levels:</strong></p>
              <div className="flex flex-wrap gap-2">
                {profileData.classicCompletedLevels.map((level) => (
                  <Button key={level.id} variant="outline" size="sm">{level.name}</Button>
                ))}
              </div>
              <p><strong>First Victor In:</strong></p>
              <div className="flex flex-wrap gap-2">
                {profileData.classicFirstVictor.map((level) => (
                  <Button key={level.id} variant="outline" size="sm">{level.name}</Button>
                ))}
              </div>
            </CardContent>
          </Card>

          {profileData.platformerHardestLevel && (
            <Card>
              <CardHeader>
                <CardTitle>Platformer Levels</CardTitle>
              </CardHeader>
              <CardContent>
                <p><strong>Hardest Level:</strong> {profileData.platformerHardestLevel.name}</p>
                {profileData.platformerLevelCount && (
                  <>
                    <p><strong>Levels Count:</strong></p>
                    <ul>
                      <li>Main: {profileData.platformerLevelCount.main}</li>
                      <li>Extended: {profileData.platformerLevelCount.extended}</li>
                      <li>Legacy: {profileData.platformerLevelCount.legacy}</li>
                    </ul>
                  </>
                )}
                {profileData.platformerCompletedLevels && profileData.platformerCompletedLevels.length > 0 && (
                  <>
                    <p><strong>Completed Levels:</strong></p>
                    <div className="flex flex-wrap gap-2">
                      {profileData.platformerCompletedLevels.map((level) => (
                        <Button key={level.id} variant="outline" size="sm">{level.name}</Button>
                      ))}
                    </div>
                  </>
                )}
                {profileData.platformerFirstVictor && profileData.platformerFirstVictor.length > 0 && (
                  <>
                    <p><strong>Record Holder In:</strong></p>
                    <div className="flex flex-wrap gap-2">
                      {profileData.platformerFirstVictor.map((level) => (
                        <Button key={level.id} variant="outline" size="sm">{level.name}</Button>
                      ))}
                    </div>
                  </>
                )}
              </CardContent>
            </Card>
          )}
        </div>
      )}
    </div>
  );
}

export default Profile;