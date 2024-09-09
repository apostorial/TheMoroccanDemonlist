import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";

function Guidelines() {
  return (
    <div className="p-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Allowed Hacks</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="list-disc pl-5 space-y-1">
              <li>FPS bypass up to 360FPS, same goes for TPS bypass</li>
              <li>Show hitboxes on death, only for non memory levels</li>
              <li>Speedhack only x1.1+</li>
              <li>Zero delay</li>
              <li>Smart StartPos</li>
              <li>No wave pulse</li>
              <li>No death effect</li>
              <li>No respawn pulse</li>
              <li>Trail always on/off</li>
            </ul>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Record Submitting</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="list-disc pl-5 space-y-1">
              <li>Extremes demon completions require audible clicks</li>
              <li>Insane demons in the top 75 hardest insanes (visit the idl) require audible clicks</li>
              <li>2 player levels will require a recording with a hand cam</li>
              <li>Cheat indicator must always be on, rule doesn't apply to users without MegaHackPro</li>
              <li>The recording must be of clear quality</li>
              <li>The recording must be submitted on our Discord server</li>
            </ul>
          </CardContent>
        </Card>
      </div>

      <div className="mt-6 grid grid-cols-1 md:grid-cols-2 gap-6">
        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Banning Players</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="list-disc pl-5 space-y-1">
              <li>A flag role in the Discord will be given to hackers, a flagged player will go through more severe hack detection and will be obligated to provide more proof</li>
              <li>If a player has been caught hacking a second time they will be banned for a duration from 2 weeks to 2 months</li>
              <li>If a player has been caught hacking a third time they will be banned permanently</li>
            </ul>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Bonus</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="list-disc pl-5 space-y-1">
              <li>1 attempt practice completions are allowed</li>
              <li>100% accuracy runs won't be accepted</li>
              <li>Exiting at the endscreen won't count as a completion and the record will not be accepted</li>
              <li>If a level has been beaten on an external LDM that's not supported by the list the record won't be accepted</li>
              <li>Using a StartPos from 0% is allowed</li>
            </ul>
          </CardContent>
        </Card>
      </div>

      <div className="mt-6">
        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Unauthorized mods</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="list-disc pl-5 space-y-1">
              <li>CBF Mod (Click Between Frames)</li>
            </ul>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default Guidelines;
