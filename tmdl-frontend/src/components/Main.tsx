import { useState, useEffect } from 'react';
import { Route, Routes, useNavigate, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import List from './List';
import LevelDetails from './LevelDetails';
import Guidelines from './Guidelines';
import { Toaster } from "@/components/ui/sonner"
import Info from './Info';
import Profile from './Profile';
import Settings from './Settings';
import ProtectedRoute from './ProtectedRoute';
import StaffList from './StaffList';
import StaffRecordList from './StaffRecordList';

function Main() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  const showInfo = !['/guidelines', '/staff/levels', '/staff/records'].includes(location.pathname);

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    if (token) {
      localStorage.setItem('jwtToken', token);
      navigate('/', { replace: true });
    }
  }, [location, navigate]);

  return (
    <div className="flex h-screen overflow-hidden">
      <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
      <div className="flex flex-col flex-1">
        <Navbar onMenuClick={() => setIsSidebarOpen(!isSidebarOpen)} />
        <main className="flex-1 overflow-y-auto">
          <div className={`grid gap-4 p-4 ${showInfo ? 'grid-cols-1 lg:grid-cols-4' : 'grid-cols-1'}`}>
            <div className={showInfo ? 'lg:col-span-3' : 'col-span-full'}>
              <Routes>
                <Route path="/" element={<List level_type="classic" list_type="main" />} />
                <Route path="/classic/extended" element={<List level_type="classic" list_type="extended" />} />
                <Route path="/classic/legacy" element={<List level_type="classic" list_type="legacy" />} />

                <Route path="/platformer/main" element={<List level_type="platformer" list_type="main" />} />
                <Route path="/platformer/extended" element={<List level_type="platformer" list_type="extended" />} />
                <Route path="/platformer/legacy" element={<List level_type="platformer" list_type="legacy" />} />

                <Route path="/level/:id" element={<LevelDetails />} />
                <Route path="/guidelines" element={<Guidelines />} />
                <Route path="/profile/:username" element={<Profile />} />
                <Route 
                  path="/settings" 
                  element={
                    <ProtectedRoute>
                      <Settings />
                    </ProtectedRoute>
                  } 
                />
                <Route 
                  path="/staff/levels" 
                  element={
                    <ProtectedRoute staffOnly={true}>
                      <StaffList />
                    </ProtectedRoute>
                  } 
                />
                <Route 
                  path="/staff/records" 
                  element={
                    <ProtectedRoute staffOnly={true}>
                      <StaffRecordList />
                    </ProtectedRoute>
                  } 
                />
              </Routes>
            </div>
            {showInfo && (
              <div className="lg:col-span-1">
                <Info />
              </div>
            )}
          </div>
        </main>
      </div>
      <Toaster />
    </div>
  );
}

export default Main;
