import { BrowserRouter as Router } from 'react-router-dom';
import { ThemeProvider } from './components/ThemeProvider';
import { AuthProvider } from './contexts/AuthContext';
import Main from './components/Main';
import './App.css';

function App() {
  return (
    <Router>
      <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
        <AuthProvider>
          <Main />
        </AuthProvider>
      </ThemeProvider>
    </Router>
  );
}

export default App;