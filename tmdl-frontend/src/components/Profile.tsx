import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from '../normal-axios';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { FaDiscord, FaYoutube, FaTwitter, FaTwitch } from 'react-icons/fa';
import { GoDotFill } from "react-icons/go";
import { User } from "lucide-react";
import { toast } from 'sonner';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";
import { Skeleton } from "@/components/ui/skeleton";

interface PlayerData {
  id: string;
  username: string;
  region: string;
  classicPoints: number;
  platformerPoints: number;
  discord?: string;
  youtube?: string;
  twitter?: string;
  twitch?: string;
  isStaff: boolean;
  isActive: boolean;
}

interface LevelData {
  id: string;
  name: string;
}

interface LevelCount {
  main: number;
  extended: number;
  legacy: number;
}

const Profile = () => {
  const { username } = useParams();
  const [playerData, setPlayerData] = useState<PlayerData | null>(null);
  const [avatar, setAvatar] = useState<string | null>(null);
  const [regionName, setRegionName] = useState<string>('');
  const [classicData, setClassicData] = useState<{
    hardest: LevelData | null;
    firstVictor: LevelData[];
    completed: LevelData[];
    count: LevelCount;
  }>({ hardest: null, firstVictor: [], completed: [], count: { main: 0, extended: 0, legacy: 0 } });
  const [platformerData, setPlatformerData] = useState<{
    hardest: LevelData | null;
    firstVictor: LevelData[];
    completed: LevelData[];
    count: LevelCount;
  }>({ hardest: null, firstVictor: [], completed: [], count: { main: 0, extended: 0, legacy: 0 } });
  const [activeTab, setActiveTab] = useState('classic');
  const [isLoading, setIsLoading] = useState(true);

  const renderSocialMediaIcons = () => {
    if (!playerData) return null;
  
    const socialMediaHandles = [
      { icon: FaDiscord, handle: playerData.discord, name: 'Discord' },
      { icon: FaYoutube, handle: playerData.youtube, name: 'YouTube' },
      { icon: FaTwitter, handle: playerData.twitter, name: 'Twitter' },
      { icon: FaTwitch, handle: playerData.twitch, name: 'Twitch' },
    ].filter(({ handle }) => handle);
  
    if (socialMediaHandles.length === 0) return null;
  
    const handleSocialClick = (social: string, handle: string) => {
      if (social === 'Discord') {
        navigator.clipboard.writeText(handle);
        toast.success(`Discord handle copied to clipboard!`, {
          description: handle,
        });
      } else {
        window.open(handle, '_blank');
      }
    };
  
    return (
      <div className="flex gap-2 items-center">
        <GoDotFill className="text-sm hover:text-primary" />
        <TooltipProvider>
          {socialMediaHandles.map(({ icon: Icon, handle, name }) => (
            <Tooltip key={name}>
              <TooltipTrigger asChild>
                <button
                  onClick={() => handleSocialClick(name, handle!)}
                  className="focus:outline-none"
                  aria-label={name === 'Discord' ? `Copy ${name} handle` : `Open ${name} profile`}
                >
                  <Icon className="text-xl hover:text-primary transition-colors" />
                </button>
              </TooltipTrigger>
              <TooltipContent>
                <p>{handle}</p>
              </TooltipContent>
            </Tooltip>
          ))}
        </TooltipProvider>
      </div>
    );
  };

  useEffect(() => {
    const fetchData = async () => {
      setIsLoading(true);
      try {
        const playerResponse = await axios.get<PlayerData>(`/api/public/players/username/${username}`);
        setPlayerData(playerResponse.data);
        fetchAvatar(playerResponse.data.id);
        fetchRegionName(playerResponse.data.region);
        const playerId = playerResponse.data.id;

        const [classicCount, platformerCount, classicHardest, platformerHardest] = await Promise.all([
          axios.get<LevelCount>(`/api/public/classic-levels/count/${playerId}`),
          axios.get<LevelCount>(`/api/public/platformer-levels/count/${playerId}`),
          axios.get<LevelData>(`/api/public/classic-levels/hardestLevel/${playerId}`),
          axios.get<LevelData>(`/api/public/platformer-levels/hardestLevel/${playerId}`),
        ]);

        setClassicData(prev => ({ ...prev, count: classicCount.data, hardest: classicHardest.data }));
        setPlatformerData(prev => ({ ...prev, count: platformerCount.data, hardest: platformerHardest.data }));

        const [classicCompleted, classicFirstVictor, platformerCompleted, platformerFirstVictor] = await Promise.all([
          axios.get<LevelData[]>(`/api/public/classic-levels/player/${playerId}`),
          axios.get<LevelData[]>(`/api/public/classic-levels/firstVictor/${playerId}`),
          axios.get<LevelData[]>(`/api/public/platformer-levels/player/${playerId}`),
          axios.get<LevelData[]>(`/api/public/platformer-levels/recordHolder/${playerId}`),
        ]);

        setClassicData(prev => ({ ...prev, completed: classicCompleted.data, firstVictor: classicFirstVictor.data }));
        setPlatformerData(prev => ({ ...prev, completed: platformerCompleted.data, firstVictor: platformerFirstVictor.data }));
      } catch (error) {
        console.error('Error fetching data:', error);
      } finally {
        setIsLoading(false);
      }
    };

    fetchData();
  }, [username]);

  const fetchAvatar = async (playerId: string) => {
    try {
      const response = await axios.get(`/api/public/players/${playerId}/avatar`, {
        responseType: 'arraybuffer'
      });
      const base64 = btoa(
        new Uint8Array(response.data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          '',
        )
      );
      setAvatar(`data:image/jpeg;base64,${base64}`);
    } catch (error) {
      console.error('Error fetching avatar:', error);
    }
  };

  const fetchRegionName = async (regionId: string) => {
    try {
      const response = await axios.get(`/api/public/regions/${regionId}`);
      setRegionName(response.data.name);
    } catch (error) {
      console.error('Error fetching region name:', error);
      setRegionName('Unknown Region');
    }
  };

  const renderLevelList = (levels: LevelData[], activeTab: string) => {
    const levelType = activeTab === 'classic' ? 'classic' : 'platformer';
    if (isLoading) {
      return (
        <>
          <Skeleton className="h-10 w-32 mr-1 mb-1" />
          <Skeleton className="h-10 w-24 mr-1 mb-1" />
          <Skeleton className="h-10 w-28 mr-1 mb-1" />
          <Skeleton className="h-10 w-36 mr-1 mb-1" />
        </>
      );
    }
    if (levels.length === 0) {
      return <Button disabled>No levels available</Button>;
    }
    return (
      <>
        {levels.map((level) => (
          <a href={`/${levelType}/level/${level.id}`} key={level.id} target="_blank">
            <Button>{level.name}</Button>
          </a>
        ))}
      </>
    );
  };

  const formatPoints = (points: number): string => {
    return points.toFixed(2);
  };

  const renderLevel = (level: LevelData | null, activeTab: string) => {
    if (isLoading) {
      return <Skeleton className="h-10 w-32" />;
    }
    if (!level) {
      return <Button disabled>No level available</Button>;
    }

    const levelType = activeTab === 'classic' ? 'classic' : 'platformer';

    return (
      <a href={`/${levelType}/level/${level.id}`} target="_blank">
        <Button>
          {level.name}
        </Button>
      </a>
    );
  };

  const renderLevelCount = (count: LevelCount) => {
    if (isLoading) {
      return (
        <>
          <Skeleton className="h-10 w-24 mr-1" />
          <Skeleton className="h-10 w-24 mr-1" />
          <Skeleton className="h-10 w-24" />
        </>
      );
    }
    return (
      <>
        <Button className='cursor-default'>Main: {count.main}</Button>
        <Button className='cursor-default'>Extended: {count.extended}</Button>
        <Button className='cursor-default'>Legacy: {count.legacy}</Button>
      </>
    );
  };

  if (!playerData) {
    return <div>Loading...</div>;
  }

  return (
    <>
      <Card className="rounded-lg p-4 mb-4">
        <CardHeader>
          <CardTitle className="flex flex-col sm:flex-row items-center gap-4">
            <Avatar className="w-32 h-32">
              {avatar ? (
                <AvatarImage 
                  src={avatar} 
                  alt={playerData.username}
                  className="object-cover w-full h-full"
                />
              ) : (
                <AvatarFallback><User className="w-16 h-16" /></AvatarFallback>
              )}
            </Avatar>
            <div className="flex flex-col items-center sm:items-start gap-2 mt-4 sm:mt-0">
              <CardTitle className="text-3xl font-bold flex items-center gap-2 text-center sm:text-left">
                {playerData.username} {renderSocialMediaIcons()}
              </CardTitle>
              <div className="flex flex-col gap-0.5 items-center sm:items-start">
                <p className="font-normal"><span className="font-bold">Region:</span> {regionName}</p>
                <p className="font-normal"><span className="font-bold">Classic points:</span> {formatPoints(playerData.classicPoints)}</p>
                <p className="font-normal"><span className="font-bold">Platformer points:</span> {formatPoints(playerData.platformerPoints)}</p>
              </div>
              {playerData.isStaff && <Badge variant="destructive" className="max-w-fit mt-2 sm:mt-0">List mod</Badge>}
            </div>
          </CardTitle>
        </CardHeader>
        <CardContent>
          <Tabs defaultValue="classic" onValueChange={setActiveTab}>
            <TabsList className="grid w-full grid-cols-2">
              <TabsTrigger value="classic">Classic</TabsTrigger>
              <TabsTrigger value="platformer">Platformer</TabsTrigger>
            </TabsList>
            <div className="text-2xl font-bold mt-4 mb-2">
              {activeTab === 'classic' ? 'Classic Details:' : 'Platformer Details:'}
            </div>
            <TabsContent value="classic" className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Hardest level:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevel(classicData.hardest, activeTab)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Level count:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelCount(classicData.count)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">First victor levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(classicData.firstVictor, activeTab)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Completed levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(classicData.completed, activeTab)}
                </CardContent>
              </Card>
            </TabsContent>
            <TabsContent value="platformer" className="grid grid-cols-1 sm:grid-cols-2 gap-4">
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Hardest level:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevel(platformerData.hardest, activeTab)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Level count:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelCount(platformerData.count)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Record holder levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(platformerData.firstVictor, activeTab)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Completed levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(platformerData.completed, activeTab)}
                </CardContent>
              </Card>
            </TabsContent>
          </Tabs>
        </CardContent>
      </Card>
    </>
  );
};

export default Profile;
