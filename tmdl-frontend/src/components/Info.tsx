import { useState, useEffect } from 'react';
import axios from '../axios-config';
import { Button } from './ui/button';
import { Card, CardContent } from "@/components/ui/card"
import { useNavigate } from 'react-router-dom';

interface StaffMember {
  id: string;
  username: string;
}

function Info() {
  const navigate = useNavigate();
  const [staffMembers, setStaffMembers] = useState<string[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStaffMembers = async () => {
      try {
        setLoading(true);
        const response = await axios.get<StaffMember[]>('/api/public/players/staff');
        const staffUsernames = response.data.map(member => member.username);
        setStaffMembers(staffUsernames);
      } catch (err) {
        setError('Failed to fetch staff members');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchStaffMembers();
  }, []);

  if (loading) return <div>Loading staff information...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <>
      <div className="bg-gray-800 rounded-lg p-4 mb-4">
        <h2 className="text-xl font-bold mb-2 text-gray-100">List editors</h2>
        <p className="text-gray-300 mb-2">Contact any of these people if you need assistance regarding the list.</p>
        {staffMembers && (
          <ul className="mb-2 text-gray-200">
            {staffMembers.map((item, index) => (
              <li key={index}>{item}</li>
            ))}
          </ul>
        )}
      </div>
      <div className="bg-gray-800 rounded-lg p-4 mb-4">
        <h2 className="text-xl font-bold mb-2 text-gray-100">Guidelines</h2>
        <p className="text-gray-300 mb-2">Before any submission please consider checking the guidelines for the list. Any submission that does not follow the guidelines will be rejected.</p>
        <Button className="w-full" onClick={() => {navigate("/guidelines");}}>Read the guidelines</Button>
      </div>
      <Card className="bg-card text-card-foreground border-none shadow-none">
        <CardContent className="p-0">
          <iframe 
            src='https://discord.com/widget?id=1144377634831220847&theme=dark'
            width='100%'
            height='500px'
            allowTransparency={true} 
            sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"
            title="Discord Widget"
          ></iframe>
        </CardContent>
      </Card>
    </>
  );
}

export default Info;
