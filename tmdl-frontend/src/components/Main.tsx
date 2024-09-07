import { useState, useEffect } from 'react';
import { Route, Routes, useNavigate, useLocation } from 'react-router-dom';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import List from './List';
import Login from './Login';
import LevelDetails from './LevelDetails';
import Guidelines from './Guidelines';
import { Toaster } from "@/components/ui/sonner"
import Info from './Info';

function Main() {
  const [isSidebarOpen, setIsSidebarOpen] = useState(false);
  const navigate = useNavigate();
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const token = params.get('token');
    if (token) {
      localStorage.setItem('jwtToken', token);
      navigate('/', { replace: true });
    }
  }, [location, navigate]);

  if (location.pathname === '/login') {
    return <Login />;
  }

  return (
    <div className="flex h-screen overflow-hidden bg-gray-00 text-gray-100">
      <Sidebar isOpen={isSidebarOpen} setIsOpen={setIsSidebarOpen} />
      <div className="flex flex-col flex-1">
        <Navbar onMenuClick={() => setIsSidebarOpen(!isSidebarOpen)} />
        <main className="flex-1 overflow-y-auto bg-gray-800">
            <div className="flex h-screen bg-gray-900 text-white p-4">
              <div className="flex-1 pr-4">
                <Routes>
                  <Route path="/" element={<List level_type="classic" list_type="main" />} />
                  <Route path="/classic/extended" element={<List level_type="classic" list_type="extended" />} />
                  <Route path="/classic/legacy" element={<List level_type="classic" list_type="legacy" />} />

                  <Route path="/platformer/main" element={<List level_type="platformer" list_type="main" />} />
                  <Route path="/platformer/extended" element={<List level_type="platformer" list_type="extended" />} />
                  <Route path="/platformer/legacy" element={<List level_type="platformer" list_type="legacy" />} />

                  <Route path="/level/:levelId" element={<LevelDetails />} />
                  <Route path="/guidelines" element={<Guidelines />} />
                </Routes>
              </div>
              {location.pathname !== '/guidelines' && (
              <div className="w-1/4">
                <Info />
              </div>
            )}
            </div>
        </main>
      </div>
      <Toaster
        className="bg-gray-800 text-gray-100"
        toastOptions={{
          classNames: {
            toast: "bg-gray-800 text-gray-100 border-gray-800",
            title: "text-gray-100",
            description: "text-gray-300",
            actionButton: "bg-gray-800 text-gray-100",
            cancelButton: "bg-gray-800 text-gray-100",
          },
        }}
      />
    </div>
  );
}

export default Main;
