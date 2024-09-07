import { useState, useEffect } from 'react';
import axios from '../axios-config';
import YouTube from 'react-youtube';
import { useParams } from 'react-router-dom';
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card"
import { Separator } from "@/components/ui/separator"
import { Button } from "@/components/ui/button"
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from "@/components/ui/table"

interface LevelDetails {
  id: string;
  ranking: number;
  duration: string;
  minPoints: number;
  minimumCompletion: number;
  levelId: string;
  name: string;
  publisher: string;
  difficulty: string;
  link: string;
  thumbnail: string;
  points: number;
}

interface Record {
  player: string;
  recordPercentage: number;
  link: string;
}

interface Player {
  id: string;
  username: string;
}

function LevelDetails() {
  const { levelId } = useParams<{ levelId: string }>();
  const [level, setLevel] = useState<LevelDetails | null>(null);
  const [records, setRecords] = useState<Record[]>([]);
  const [players, setPlayers] = useState<{ [key: string]: Player }>({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchLevelDetails = async () => {
      try {
        setLoading(true);
        const levelResponse = await axios.get<LevelDetails>(`/api/public/classic-levels/${levelId}`);
        setLevel(levelResponse.data);
        
        const recordsResponse = await axios.get<Record[]>(`/api/public/classic-records/level/${levelResponse.data.id}`);
        const fetchedRecords = Array.isArray(recordsResponse.data) ? recordsResponse.data : [];
        setRecords(fetchedRecords);

        const playerIds = [...new Set(fetchedRecords.map(record => record.player))];
        const playerPromises = playerIds.map(id => 
          axios.get<Player>(`/api/public/players/${id}`)
        );
        const playerResponses = await Promise.all(playerPromises);
        const playerMap = Object.fromEntries(
          playerResponses.map(response => [response.data.id, response.data])
        );
        setPlayers(playerMap);
      } catch (err) {
        setError('Failed to fetch level details and records');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchLevelDetails();
  }, [levelId]);

  if (loading) return <div className="text-foreground">Loading...</div>;
  if (error) return <div className="text-destructive">Error: {error}</div>;
  if (!level) return <div className="text-foreground">No level data found</div>;

  const getYouTubeVideoId = (url: string) => {
    const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
    const match = url.match(regExp);
    return (match && match[2].length === 11) ? match[2] : null;
  };

  const videoId = getYouTubeVideoId(level.link);

  const formatDifficulty = (difficulty: string) => {
    return difficulty.toLowerCase().split('_').map(word => word.charAt(0).toUpperCase() + word.slice(1)).join(' ');
  };

  const formatDuration = (duration: string) => {
    return duration.charAt(0).toUpperCase() + duration.slice(1).toLowerCase();
  };

  return (
      <Card className="bg-gray-800 rounded-lg p-4 mb-4 border-none">
        <CardHeader>
          <CardTitle className="text-2xl font-bold text-gray-100">#{level.ranking} - {level.name}</CardTitle>
          <CardTitle className="mb-4 text-gray-100"><span className="font-normal">Published by:</span> {level.publisher}</CardTitle>
        </CardHeader>
        <CardContent>
          {videoId && (
            <div className="mb-6">
              <YouTube
                videoId={videoId}
                opts={{ width: '100%', height: '400' }}
              />
            </div>
          )}
          <div className="grid grid-cols-2 gap-4">
            <div>
              {/* <p className="text-gray-300"><span className="font-bold text-gray-100">Publisher:</span> {level.publisher}</p> */}
              <p className="text-gray-300"><span className="font-bold text-gray-100">Level ID:</span> {level.levelId}</p>
              {/* <p className="text-gray-300"><span className="font-bold text-gray-100">Ranking:</span> {level.ranking}</p> */}
              <p className="text-gray-300"><span className="font-bold text-gray-100">Duration:</span> {formatDuration(level.duration)}</p>
              <p className="text-gray-300"><span className="font-bold text-gray-100">Difficulty:</span> {formatDifficulty(level.difficulty)}</p>
            </div>
            <div>
              <p className="text-gray-300"><span className="font-bold text-gray-100">Points:</span> {level.points}</p>
              <p className="text-gray-300"><span className="font-bold text-gray-100">Minimum Points:</span> {level.minPoints}</p>
              <p className="text-gray-300"><span className="font-bold text-gray-100">Minimum Completion:</span> {level.minimumCompletion}%</p>
            </div>
          </div>
          <Separator className="my-6" />
          <h2 className="text-2xl font-bold mb-4 text-gray-100">Records</h2>
          <Table className='text-gray-300'>
            <TableHeader>
              <TableRow>
                <TableHead className="text-gray-100">Player</TableHead>
                <TableHead className="text-gray-100">Percentage</TableHead>
                <TableHead className="text-gray-100">Video</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody className="hover:text-primary">
              {records.map((record, index) => (
                <TableRow key={index} className={record.recordPercentage === 100 ? 'font-bold' : ''}>
                  <TableCell>{players[record.player]?.username || 'Loading...'}</TableCell>
                  <TableCell>{record.recordPercentage}%</TableCell>
                  <TableCell>
                  <Button
                      asChild
                    >
                      <a href={record.link} target="_blank" rel="noopener noreferrer">
                        Watch
                      </a>
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
  );
};

export default LevelDetails;
