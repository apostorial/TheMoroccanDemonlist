import { useState, useEffect } from 'react';
import axios from '../normal-axios';
import { Card, CardHeader, CardTitle, CardContent, CardFooter } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

interface StaffMember {
  username: string;
}

function Info() {
  const [staffMembers, setStaffMembers] = useState<StaffMember[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchStaffMembers = async () => {
      try {
        setLoading(true);
        const response = await axios.get<StaffMember[]>('/api/public/players/staff');
        setStaffMembers(response.data);
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
    <div className="space-y-4">
      <Card>
        <CardHeader>
          <CardTitle className="text-2xl font-bold">List editors</CardTitle>
        </CardHeader>
        <CardContent>
          <p className="mb-2">Contact any of these people if you need assistance regarding the list.</p>
          {staffMembers && staffMembers.length > 0 ? (
            <div className="flex flex-wrap gap-1">
              {staffMembers.map((item) => (
                <a href={`/profile/${item.username}`} target="_blank" key={item.username}>
                  <Button>
                    {item.username}
                  </Button>
                </a>
              ))}
            </div>
          ) : (
            <Button className="w-full" disabled>
              No staff members listed.
            </Button>
          )}
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle className="text-2xl font-bold">Guidelines</CardTitle>
        </CardHeader>
        <CardContent>
          <p>Before any submission please consider checking the guidelines for the list. Any submission that does not follow the guidelines will be rejected.</p>
        </CardContent>
        <CardFooter>
          <a href="/guidelines" target="_blank" className="w-full block">
            <Button className="w-full">Read the guidelines</Button>
          </a>
        </CardFooter>
      </Card>

      <Card>
        <CardContent className="p-0">
          <iframe 
            src='https://discord.com/widget?id=1144377634831220847&theme=dark'
            width='100%'
            height='500px'
            sandbox="allow-popups allow-popups-to-escape-sandbox allow-same-origin allow-scripts"
            title="Discord Widget"
          ></iframe>
        </CardContent>
      </Card>
    </div>
  );
}

export default Info;
