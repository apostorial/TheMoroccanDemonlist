import React from 'react';
import { useAuthContext } from '../contexts/AuthContext';
import { Button } from "@/components/ui/button";
import { ChevronDown, ChevronUp } from "lucide-react";
import { useNavigate } from 'react-router-dom';

interface SidebarProps {
  isOpen: boolean;
  setIsOpen: (isOpen: boolean) => void;
}

const Sidebar: React.FC<SidebarProps> = ({ isOpen }) => {
  const { user } = useAuthContext();
  const [isClassicDemonlistOpen, setIsClassicDemonlistOpen] = React.useState(false);
  const [isPlatformerDemonlistOpen, setIsPlatformerDemonlistOpen] = React.useState(false);
  const [isStaffMenuOpen, setIsStaffMenuOpen] = React.useState(false);
  const [isClassicStaffOpen, setIsClassicStaffOpen] = React.useState(false);
  const [isPlatformerStaffOpen, setIsPlatformerStaffOpen] = React.useState(false);
  const navigate = useNavigate();

  return (
    <div className={`border-r transition-all duration-300 ease-in-out ${isOpen ? 'w-64' : 'w-0'} md:w-64 flex flex-col h-full`}>
      <div className="flex-grow overflow-y-auto p-4">
        <h2 className="text-2xl font-bold mb-4">TMDL</h2>
        <nav>
          <ul className="space-y-2">
            <li>
              <Button
                variant="ghost"
                className="w-full justify-between text-left"
                onClick={() => setIsClassicDemonlistOpen(!isClassicDemonlistOpen)}
              >
                <span>Classic demonlist</span>
                {isClassicDemonlistOpen ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
              </Button>
              <div className={`overflow-hidden transition-all duration-300 ease-in-out ${isClassicDemonlistOpen ? 'max-h-48' : 'max-h-0'}`}>
                <ul className="ml-4 mt-2 space-y-2">
                  <li>
                    <Button 
                      variant="ghost" 
                      className="w-full justify-start text-left text-sm"
                      onClick={() => {navigate("/");}}
                    >
                      Main list
                    </Button>
                  </li>
                  <li>
                    <Button variant="ghost"
                    className="w-full justify-start text-left text-sm"
                    onClick={() => {navigate("/classic/extended");}}
                    >
                      Extended list
                    </Button>
                  </li>
                  <li>
                    <Button variant="ghost"
                    className="w-full justify-start text-left text-sm"
                    onClick={() => {navigate("/classic/legacy");}}
                    >
                      Legacy list
                    </Button>
                  </li>
                  <li>
                    <Button variant="ghost" className="w-full justify-start text-left text-sm">
                      Stat Viewer
                    </Button>
                  </li>
                </ul>
              </div>
            </li>
            <li>
              <Button
                variant="ghost"
                className="w-full justify-between text-left"
                onClick={() => setIsPlatformerDemonlistOpen(!isPlatformerDemonlistOpen)}
              >
                <span>Platformer demonlist</span>
                {isPlatformerDemonlistOpen ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
              </Button>
              <div className={`overflow-hidden transition-all duration-300 ease-in-out ${isPlatformerDemonlistOpen ? 'max-h-48' : 'max-h-0'}`}>
                <ul className="ml-4 mt-2 space-y-2">
                  <li>
                    <Button variant="ghost"
                      className="w-full justify-start text-left text-sm"
                      onClick={() => {navigate("/platformer/main");}}
                      >
                        Main list
                    </Button>
                  </li>
                  <li>
                    <Button variant="ghost"
                      className="w-full justify-start text-left text-sm"
                      onClick={() => {navigate("/platformer/extended");}}
                      >
                        Extended list
                    </Button>
                  </li>
                  <li>
                    <Button variant="ghost"
                      className="w-full justify-start text-left text-sm"
                      onClick={() => {navigate("/platformer/legacy");}}
                      >
                        Legacy list
                    </Button>
                  </li>
                  <li>
                    <Button variant="ghost" className="w-full justify-start text-left text-sm">
                      Stat Viewer
                    </Button>
                  </li>
                </ul>
              </div>
            </li>
            <li>
              <Button variant="ghost"
                className="w-full justify-start text-left text-sm"
                onClick={() => {navigate("/guidelines");}}
                >
                  Guidelines
              </Button>
            </li>
            
            {user?.role === 'STAFF' && (
  <li>
    <Button
      variant="ghost"
      className="w-full justify-between text-left"
      onClick={() => setIsStaffMenuOpen(!isStaffMenuOpen)}
    >
      <span>Staff Menu</span>
      {isStaffMenuOpen ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
    </Button>
    <div className={`overflow-hidden transition-all duration-300 ease-in-out ${isStaffMenuOpen ? 'max-h-96' : 'max-h-0'}`}>
      <ul className="ml-4 mt-2 space-y-2">
        <li>
          <Button
            variant="ghost"
            className="w-full justify-between text-left text-sm"
            onClick={() => setIsClassicStaffOpen(!isClassicStaffOpen)}
          >
            <span>Classic Demonlist</span>
            {isClassicStaffOpen ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
          </Button>
          <div className={`overflow-hidden transition-all duration-300 ease-in-out ${isClassicStaffOpen ? 'max-h-32' : 'max-h-0'}`}>
            <ul className="ml-4 mt-2 space-y-2">
            <li>
                <Button
                  variant="ghost"
                  className="w-full justify-start text-left text-xs"
                  onClick={() => navigate("/staff/classic-levels")}
                >
                  Levels
                </Button>
              </li>
              <li><Button variant="ghost" className="w-full justify-start text-left text-xs">Records</Button></li>
              <li><Button variant="ghost" className="w-full justify-start text-left text-xs">Regions</Button></li>
            </ul>
          </div>
        </li>
        <li>
          <Button
            variant="ghost"
            className="w-full justify-between text-left text-sm"
            onClick={() => setIsPlatformerStaffOpen(!isPlatformerStaffOpen)}
          >
            <span>Platformer Demonlist</span>
            {isPlatformerStaffOpen ? <ChevronUp className="h-4 w-4" /> : <ChevronDown className="h-4 w-4" />}
          </Button>
          <div className={`overflow-hidden transition-all duration-300 ease-in-out ${isPlatformerStaffOpen ? 'max-h-32' : 'max-h-0'}`}>
            <ul className="ml-4 mt-2 space-y-2">
            <li>
                <Button
                  variant="ghost"
                  className="w-full justify-start text-left text-xs"
                  onClick={() => navigate("/staff/platformer-levels")}
                >
                  Levels
                </Button>
              </li>
              <li><Button variant="ghost" className="w-full justify-start text-left text-xs">Records</Button></li>
              <li><Button variant="ghost" className="w-full justify-start text-left text-xs">Regions</Button></li>
            </ul>
          </div>
        </li>
      </ul>
    </div>
  </li>
)}
          </ul>
        </nav>
      </div>
    </div>
  );
};

export default Sidebar;
