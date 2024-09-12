import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axios from '../normal-axios';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { FaDiscord, FaYoutube, FaTwitter, FaTwitch } from 'react-icons/fa';
import { GoDotFill } from "react-icons/go";
import { User } from "lucide-react";
import { toast } from 'sonner';
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip";

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
      try {
        const playerResponse = await axios.get<PlayerData>(`/api/public/players/username/${username}`);
        setPlayerData(playerResponse.data);
        fetchAvatar(playerResponse.data.id);
        fetchRegionName(playerResponse.data.region);
        const playerId = playerResponse.data.id;

        const fetchClassicLevelData = async () => {
          const hardestResponse = await axios.get<LevelData>(`/api/public/classic-levels/hardestLevel/${playerId}`);
          const hardest = hardestResponse.data;

          const completedResponse = await axios.get<LevelData[]>(`/api/public/classic-levels/player/${playerId}`);
          const completed = completedResponse.data;
          
          const firstVictorResponse = await axios.get<LevelData[]>(`/api/public/classic-levels/firstVictor/${playerId}`);
          const firstVictor = firstVictorResponse.data;

          const countResponse = await axios.get<LevelCount>(`/api/public/classic-levels/count/${playerId}`);
          const count = countResponse.data;

          return { hardest , completed, firstVictor, count};
        };

        const fetchPlatformerLevelData = async () => {
          const hardestResponse = await axios.get<LevelData>(`/api/public/platformer-levels/hardestLevel/${playerId}`);
          const hardest = hardestResponse.data;

          const completedResponse = await axios.get<LevelData[]>(`/api/public/platformer-levels/player/${playerId}`);
          const completed = completedResponse.data;
          
          const firstVictorResponse = await axios.get<LevelData[]>(`/api/public/platformer-levels/recordHolder/${playerId}`);
          const firstVictor = firstVictorResponse.data;

          const countResponse = await axios.get<LevelCount>(`/api/public/platformer-levels/count/${playerId}`);
          const count = countResponse.data;

          return { hardest , completed, firstVictor, count};
        };

        const classicLevelData = await fetchClassicLevelData();
        setClassicData(classicLevelData);

        const platformerLevelData = await fetchPlatformerLevelData();
        setPlatformerData(platformerLevelData);
      } catch (error) {
        console.error('Error fetching data:', error);
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

  const renderLevelList = (levels: LevelData[]) => {
    if (levels.length === 0) {
      return <Button disabled>No levels available</Button>;
    }
    return (
      <>
        {levels.map((level) => (
          <a href={`/level/${level.id}`} key={level.id} target="_blank">
            <Button>{level.name}</Button>
          </a>
        ))}
      </>
    );
  };

  const renderLevel = (level: LevelData | null) => {
    if (!level) {
      return <Button disabled>No level available</Button>;
    }
    return (
      <a href={`/level/${level.id}`} target="_blank">
        <Button>{level.name}</Button>
      </a>
    );
  };

  if (!playerData) {
    return <div>Loading...</div>;
  }

  return (
    <>
      {!playerData.isActive && (
        <Alert variant="destructive" className="mb-4">
          <AlertTitle>Action Required</AlertTitle>
          <AlertDescription>
            Your account is inactive. Please change your username in your account settings to activate your account.
          </AlertDescription>
        </Alert>
      )}
      <Card className="rounded-lg p-4 mb-4">
        <CardHeader>
          <CardTitle className="flex items-center gap-4">
            <div className="flex items-center gap-4">
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
              <div className="flex flex-col gap-2">
                <CardTitle className="text-3xl font-bold flex items-center gap-2">
                  {playerData.username} {renderSocialMediaIcons()}
                </CardTitle>
                <div className="flex flex-col gap-0.5">
                  <p className="font-normal"><span className="font-bold">Region:</span> {regionName}</p>
                  <p className="font-normal"><span className="font-bold">Classic points:</span> {playerData.classicPoints}</p>
                  <p className="font-normal"><span className="font-bold">Platformer points:</span> {playerData.platformerPoints}</p>
                </div>
                {playerData.isStaff && <Badge variant="destructive" className="max-w-fit">List mod</Badge>}
              </div>
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
                  {renderLevel(classicData.hardest)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">First victor levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(classicData.firstVictor)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Completed levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(classicData.completed)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Level count:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  <Button className='cursor-default'>Main: {classicData.count.main}</Button>
                  <Button className='cursor-default'>Extended: {classicData.count.extended}</Button>
                  <Button className='cursor-default'>Legacy: {classicData.count.legacy}</Button>
                </CardContent>
              </Card>
            </TabsContent>
            <TabsContent value="platformer" className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Hardest level:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevel(platformerData.hardest)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Record holder levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(platformerData.firstVictor)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Completed levels:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  {renderLevelList(platformerData.completed)}
                </CardContent>
              </Card>
              <Card>
                <CardHeader>
                  <CardTitle className="text-xl font-bold">Level count:</CardTitle>
                </CardHeader>
                <CardContent className="flex flex-wrap gap-1">
                  <Button className='cursor-default'>Main: {platformerData.count.main}</Button>
                  <Button className='cursor-default'>Extended: {platformerData.count.extended}</Button>
                  <Button className='cursor-default'>Legacy: {platformerData.count.legacy}</Button>
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
