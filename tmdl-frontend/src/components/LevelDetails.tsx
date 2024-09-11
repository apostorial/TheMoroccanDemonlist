import { useState, useEffect } from 'react';
import axios from '../normal-axios';
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
  name: string;
  publisher: string;
  difficulty: string;
  link: string;
  thumbnail: string;
  points: number;
}

interface Record {
  player: string;
  recordPercentage?: number;
  recordTime?: string;
  link: string;
}

function LevelDetails() {
  const { id, levelType } = useParams<{ id: string; levelType: 'classic' | 'platformer' }>();
  const [level, setLevel] = useState<LevelDetails | null>(null);
  const [records, setRecords] = useState<Record[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchLevelDetails = async () => {
      try {
        setLoading(true);
        const levelResponse = await axios.get<LevelDetails>(`/api/public/${levelType}-levels/${id}`);
        setLevel(levelResponse.data);
        
        const recordsResponse = await axios.get<Record[]>(`/api/public/${levelType}-records/level/${id}`);
        const fetchedRecords = Array.isArray(recordsResponse.data) ? recordsResponse.data : [];
        setRecords(fetchedRecords);
      } catch (err) {
        setError(`Failed to fetch ${levelType} level details and records`);
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchLevelDetails();
  }, [id, levelType]);

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

  const formatTime = (time: string | undefined) => {
    if (!time) return 'N/A';
    
    const match = time.match(/PT(\d+H)?(\d+M)?(\d+S)?/);
    if (!match) return time;

    const hours = parseInt(match[1]) || 0;
    const minutes = parseInt(match[2]) || 0;
    const seconds = parseInt(match[3]) || 0;

    let result = '';
    if (hours > 0) result += `${hours}h `;
    if (minutes > 0 || hours > 0) result += `${minutes}m `;
    result += `${seconds.toString().padStart(2, '0')}s`;

    return result.trim();
  };

  return (
    <Card className="rounded-lg p-4 mb-4">
      <CardHeader>
        <CardTitle className="text-2xl font-bold">#{level.ranking} - {level.name}</CardTitle>
        <CardTitle className="mb-4"><span className="font-normal">Published by:</span> {level.publisher}</CardTitle>
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
            <p><span className="font-bold">Level ID:</span> {level.id}</p>
            {levelType === 'classic' && (
              <p><span className="font-bold">Duration:</span> {formatDuration(level.duration)}</p>
            )}
            <p><span className="font-bold">Difficulty:</span> {formatDifficulty(level.difficulty)}</p>
          </div>
          <div>
            <p><span className="font-bold">Points:</span> {level.points}</p>
            {levelType === 'classic' && (
              <>
                <p><span className="font-bold">Minimum Points:</span> {level.minPoints}</p>
                <p><span className="font-bold">Minimum Completion:</span> {level.minimumCompletion}%</p>
              </>
            )}
          </div>
        </div>
        <Separator className="mt-6 mb-2" />
        <h2 className="text-2xl font-bold mb-4">Records</h2>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>Player</TableHead>
              <TableHead>{levelType === 'classic' ? 'Percentage' : 'Time'}</TableHead>
              <TableHead>Video</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody className="hover:text-primary">
            {records.map((record, index) => (
              <TableRow key={index} className={
                levelType === 'classic' && record.recordPercentage === 100 ? 'font-bold' : ''
              }>
                <TableCell>
                  <a href={`/profile/${record.player}`} target="_blank">{record.player}</a>
                </TableCell>
                <TableCell>
                  {levelType === 'classic' 
                    ? (record.recordPercentage != null ? `${record.recordPercentage}%` : 'N/A')
                    : formatTime(record.recordTime)
                  }
                </TableCell>
                <TableCell>
                  <Button asChild>
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
