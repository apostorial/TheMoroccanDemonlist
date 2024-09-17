import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { ScrollArea } from "@/components/ui/scroll-area";

function Guidelines() {
  return (
    <div className="p-6">
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
      <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Allowed Hacks</CardTitle>
          </CardHeader>
          <CardContent>
          <ScrollArea className="h-[300px] pr-4">
            <ul className="list-disc pl-5 space-y-1">
              <li>Physics Bypass up to 360 only for pre 2.2 levels</li>
              <li>TPS Bypass</li>
              <li>CBF (Click Between Frames)</li>
              <li>Accurate Percentage</li>
              <li>Classic Particles</li>
              <li>Coins Show Uncollected</li>
              <li>Collect Coins in Practice</li>
              <li>Dash Orb Colour Split</li>
              <li>Hide Attempts</li>
              <li>Hide Checkpoints</li>
              <li>Hide Pause Button</li>
              <li>Inversed Trail</li>
              <li>Mini Cube Icon</li>
              <li>No Background Flash</li>
              <li>No Death Effect</li>
              <li>No Effect Circle</li>
              <li>No Force Player Glow</li>
              <li>No Glow</li>
              <li>No New Best Popup</li>
              <li>No Orb Ring</li>
              <li>No Particles</li>
              <li>No Portal Lightning</li>
              <li>No Pulse</li>
              <li>No Respawn Flash</li>
              <li>No Robot Fire</li>
              <li>No Spider Dash</li>
              <li>No Wave Pulse</li>
              <li>No Wave Trail Behind</li>
              <li>Practice Pulse</li>
              <li>Show Total Attempts</li>
              <li>Solid Wave Trail</li>
              <li>Spider Bug Fix</li>
              <li>Stable Pulse</li>
              <li>Stop Triggers on Death</li>
              <li>Trail Always Off</li>
              <li>Trail Bug Fix</li>
              <li>Wave Trail Bug Fix</li>
              <li>Wave Trail on Death</li>
              <li>Auto Deafen</li>
              <li>Auto Kill</li>
              <li>Auto Low Detail Mode</li>
              <li>Auto Music Sync</li>
              <li>Auto No Auto-Retry</li>
              <li>Auto Practice Mode</li>
              <li>Ball Rotation Bug</li>
              <li>Confirm Exit</li>
              <li>Corrective Music Sync</li>
              <li>Everything Hurts</li>
              <li>Ignore ESC</li>
              <li>P1/P2 Input Switch</li>
              <li>Pause During Complete</li>
              <li>Practice Bug Fix</li>
              <li>Practice Music Hack</li>
              <li>Show Hitboxes on Death (Only if it doesn't benefit the player)</li>
              <li>StartPos Lag Bug Fix</li>
              <li>StartPos Switcher</li>
              <li>Void Click Bug Fix</li>
              <li>Allow Low Volume</li>
              <li>Auto Safe Mode</li>
              <li>Fast Alt-Tab</li>
              <li>Force Visibility</li>
              <li>Free Window Resize</li>
              <li>Lock Cursor</li>
              <li>Mute on Unfocus</li>
              <li>No Menu Music</li>
              <li>No Transition</li>
              <li>Pause on Unfocus</li>
              <li>Pitch Shifter</li>
              <li>Safe Mode</li>
              <li>Transition Customiser</li>
              <li>Transparent BG</li>
              <li>Transparent Lists</li>
              <li>Zero Delay</li>
              <li>Vertical Sync</li>
              <li>Smooth Fix (if it doesn't slow down the game)</li>
              <li>Force Smooth Fix, Force SF (if it doesn't slow down the game)</li>
              <li>Flicker Fix</li>
              <li>Low Detail (Help LDM)</li>
              <li>AntiCheat Bypass</li>
              <li>Hide Level Complete Screen</li>
              <li>Invisible Dual Fix</li>
              <li>Only Show %</li>
            </ul>
          </ScrollArea>
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-2xl">Record Submitting</CardTitle>
          </CardHeader>
          <CardContent>
            <ul className="list-disc pl-5 space-y-1">
              <li>Audible clicks are required (no click sound mods, excluding mobile players).</li>
              <li>Extreme demon completions require raw footage (atleast 2 minutes of gameplay before completion attempt)</li>
              <li>2 player levels will require a recording with a hand cam.</li>
              <li>Extreme demons must have cheat indicator on (IOS players excluded).</li>
              <li>The recording must show the level being completed from 0% until 100% and show the full level complete screen.</li>
              <li>The recording must be of clear quality.</li>
              <li>The recording must be submitted on our <a href="https://discord.com/invite/CfzT9EQpr2" target="_blank" className="text-blue-500 hover:text-blue-600">Discord server</a> or our <span className="text-blue-500 hover:text-blue-600 cursor-pointer" onClick={() => window.location.href = '/submissions'}>website</span>.</li>
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
              <li>If a player has been caught hacking a second time they will be <span className="font-bold">banned</span> for a duration from 2 weeks to 2 months</li>
              <li>If a player has been caught hacking a third time they will be <span className="font-bold">banned permanently</span></li>
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
              <li>100% accuracy runs <span className="font-bold">won't</span> be accepted</li>
              <li>Exiting at the endscreen <span className="font-bold">won't</span> count as a completion and the record will not be accepted.</li>
              <li>If a level has been beaten on an external LDM that's not supported by the list the record <span className="font-bold">won't</span> be accepted.</li>
              <li>Using a start position from 0% is allowed</li>
            </ul>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}

export default Guidelines;
