import { useState, useEffect } from 'react';
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from "@/components/ui/alert";
import { Badge } from "@/components/ui/badge";
import { IoIosLink } from "react-icons/io";
import { BsFiletypeRaw } from "react-icons/bs";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import axios from '../jwt-axios';
import { toast } from "sonner";
import { ScrollArea } from "@/components/ui/scroll-area";

interface Submission {
  id: string;
  level: string;
  player: string;
  recordPercentage?: number;
  recordTime?: string;
  link: string;
  rawFootage?: string;
  status: 'PENDING' | 'APPROVED' | 'REJECTED';
}

function SubmissionList() {
  const [activeType, setActiveType] = useState<'classic' | 'platformer'>('classic');
  const [isActive, setIsActive] = useState(true);
  const [submissions, setSubmissions] = useState<Submission[]>([]);
  const [levelNames, setLevelNames] = useState<{ [key: string]: string }>({});
  const [levels, setLevels] = useState<{ id: string; name: string }[]>([]);
  const [isDialogOpen, setIsDialogOpen] = useState(false);
  const [newSubmission, setNewSubmission] = useState({
    link: '',
    rawFootage: '',
    comment: '',
    level: '',
    recordPercentage: '',
    recordTime: '',
  });

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await axios.get('/api/authenticated/players/profile');
        setIsActive(response.data.isActive);
      } catch (error) {
        console.error('Error fetching profile:', error);
      }
    };

    fetchProfile();
  }, []);

  useEffect(() => {
    const fetchSubmissions = async () => {
      try {
        const response = await axios.get(`/api/authenticated/${activeType}-submissions/list`);
        setSubmissions(response.data);
        fetchLevelNames(response.data);
      } catch (error) {
        console.error('Error fetching submissions:', error);
      }
    };

    fetchSubmissions();
  }, [activeType]);

  useEffect(() => {
    const fetchLevels = async () => {
      try {
        const response = await axios.get(`/api/public/${activeType}-levels/list`);
        setLevels(response.data);
      } catch (error) {
        console.error('Error fetching levels:', error);
        setLevels([]);
      }
    };

    fetchLevels();
  }, [activeType]);

  const fetchLevelNames = async (submissions: Submission[]) => {
    try {
      const levelIds = [...new Set(submissions.map(submission => submission.level))];
      const levelPromises = levelIds.map(id => 
        axios.get(`/api/public/${activeType}-levels/${id}`)
      );
      const responses = await Promise.all(levelPromises);
      const nameMap = responses.reduce((acc: { [key: string]: string }, response) => {
        const level = response.data;
        acc[level.id] = level.name;
        return acc;
      }, {});
      setLevelNames(nameMap);
    } catch (error) {
      console.error('Error fetching level names:', error);
    }
  };

  const getLevelName = (levelId: string): string => {
    return levelNames[levelId] || 'Loading...';
  };

  const formatDuration = (duration: string): string => {
    const match = duration.match(/PT(?:(\d+)H)?(?:(\d+)M)?(?:(\d+)(?:\.\d+)?S)?/);
    if (!match) return duration;

    const [, hours, minutes, seconds] = match;
    let result = '';

    if (hours) result += `${hours}h `;
    if (minutes) result += `${minutes}m `;
    if (seconds) result += `${Math.round(parseFloat(seconds))}s`;

    return result.trim() || '0s';
  };

  const formatTimeToISO = (time: string): string => {
    const [hours, minutes, seconds] = time.split(':').map(Number);
    return `PT${hours}H${minutes}M${seconds}S`;
  };

  const getStatusBadge = (status: Submission['status']) => {
    switch (status) {
      case 'PENDING':
        return <Badge variant="secondary" className="bg-yellow-500 hover:bg-yellow-600">Pending</Badge>;
      case 'APPROVED':
        return <Badge variant="secondary" className="bg-green-500 hover:bg-green-600">Approved</Badge>;
      case 'REJECTED':
        return <Badge variant="secondary" className="bg-red-500 hover:bg-red-600">Rejected</Badge>;
      default:
        return null;
    }
  };

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    const { name, value } = e.target;
    setNewSubmission(prev => ({ ...prev, [name]: value }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const submissionData = { ...newSubmission };
      if (activeType === 'platformer') {
        submissionData.recordTime = formatTimeToISO(newSubmission.recordTime);
      }
      await axios.post(`/api/authenticated/${activeType}-submissions/create`, submissionData);
      setIsDialogOpen(false);
      setNewSubmission({ link: '', rawFootage: '', comment: '', level: '', recordPercentage: '', recordTime: '' });
      const response = await axios.get(`/api/authenticated/${activeType}-submissions/list`);
      setSubmissions(response.data);
      toast.success("Submission created successfully!");
    } catch (error) {
      console.error('Error creating submission:', error);
      toast.error("Failed to create submission. Please try again.");
    }
  };

  return (
    <div className="container mx-auto p-4">
      {!isActive && (
        <Alert variant="destructive" className="mb-4">
          <AlertTitle>Action Required</AlertTitle>
          <AlertDescription>
            Your account is inactive. Please change your username in your account settings to activate your account.
          </AlertDescription>
        </Alert>
      )}

      <div className="flex justify-between items-center mb-4">
        <h1 className="text-2xl font-bold">Submissions</h1>
        <div className="flex space-x-2">
          <Button 
            variant={activeType === 'classic' ? 'default' : 'outline'}
            onClick={() => setActiveType('classic')}
          >
            Classic
          </Button>
          <Button 
            variant={activeType === 'platformer' ? 'default' : 'outline'}
            onClick={() => setActiveType('platformer')}
          >
            Platformer
          </Button>
        </div>
      </div>

      <div className="mb-4">
        <Dialog open={isDialogOpen} onOpenChange={setIsDialogOpen}>
          <DialogTrigger asChild>
            <Button disabled={!isActive}>Create Submission</Button>
          </DialogTrigger>
          <DialogContent>
            <DialogHeader>
              <DialogTitle>Create {activeType.charAt(0).toUpperCase() + activeType.slice(1)} Submission</DialogTitle>
            </DialogHeader>
            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <Label htmlFor="link">Link</Label>
                <Input id="link" name="link" value={newSubmission.link} onChange={handleInputChange} required />
              </div>
              <div>
                <Label htmlFor="rawFootage">Raw Footage Link (Optional - Only mandatory for Extreme Demons)</Label>
                <Input id="rawFootage" name="rawFootage" value={newSubmission.rawFootage} onChange={handleInputChange} />
              </div>
              <div>
                <Label htmlFor="comment">Comment</Label>
                <Input id="comment" name="comment" value={newSubmission.comment} onChange={handleInputChange} />
              </div>
              <div>
                <Label htmlFor="level">Level</Label>
                <Select 
                  name="level" 
                  value={newSubmission.level} 
                  onValueChange={(value) => handleInputChange({ target: { name: 'level', value } } as any)}
                >
                  <SelectTrigger>
                    <SelectValue placeholder="Select a level" />
                  </SelectTrigger>
                  <SelectContent>
                    {Array.isArray(levels) && levels.length > 0 ? (
                      levels.map((level) => (
                        <SelectItem key={level.id} value={level.id}>{level.name}</SelectItem>
                      ))
                    ) : (
                      <SelectItem value="no-levels" disabled>No levels available</SelectItem>
                    )}
                  </SelectContent>
                </Select>
              </div>
              {activeType === 'classic' ? (
                <div>
                  <Label htmlFor="recordPercentage">Record Percentage (%)</Label>
                  <Input
                    id="recordPercentage"
                    name="recordPercentage"
                    type="number"
                    min="0"
                    max="100"
                    value={newSubmission.recordPercentage}
                    onChange={handleInputChange}
                    required
                  />
                </div>
              ) : (
                <div>
                  <Label htmlFor="recordTime">Record Time (HH:MM:SS)</Label>
                  <Input 
                    id="recordTime" 
                    name="recordTime" 
                    type="text" 
                    pattern="[0-9]{2}:[0-9]{2}:[0-9]{2}"
                    placeholder="01:10:32"
                    value={newSubmission.recordTime} 
                    onChange={handleInputChange} 
                    required 
                  />
                </div>
              )}
              <Button type="submit">Submit</Button>
            </form>
          </DialogContent>
        </Dialog>
      </div>

      <ScrollArea>
        <ul className="space-y-4 p-4 rounded-md">
          {submissions.map((submission) => (
            <li key={submission.id} className="flex flex-col sm:flex-row sm:items-center justify-between bg-secondary/20 rounded-lg p-4 shadow-sm">
              <div className="flex flex-col mb-2 sm:mb-0">
                <span className="font-semibold text-lg">{submission.player}</span>
                <span className="text-sm text-muted-foreground">{getLevelName(submission.level)}</span>
              </div>
              <div className="flex items-center space-x-4">
                <Badge variant="secondary" className="text-sm">
                  {activeType === 'classic' 
                    ? `${submission.recordPercentage}%`
                    : formatDuration(submission.recordTime || '')
                  }
                </Badge>
                {getStatusBadge(submission.status)}
                <div className="flex space-x-2">
                  <a 
                    href={submission.link} 
                    target="_blank" 
                    rel="noopener noreferrer" 
                    className="text-blue-500 hover:text-blue-700 transition-colors"
                  >
                    <IoIosLink size={20} />
                    <span className="sr-only">Video link</span>
                  </a>
                  {submission.rawFootage && (
                    <a 
                      href={submission.rawFootage} 
                      target="_blank" 
                      rel="noopener noreferrer" 
                      className="text-green-500 hover:text-green-700 transition-colors"
                    >
                      <BsFiletypeRaw size={20} />
                      <span className="sr-only">Raw footage</span>
                    </a>
                  )}
                </div>
              </div>
            </li>
          ))}
        </ul>
      </ScrollArea>
    </div>
  );
}

export default SubmissionList;
