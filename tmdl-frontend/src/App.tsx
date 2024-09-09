import { BrowserRouter as Router } from 'react-router-dom';
import { ThemeProvider } from './components/ThemeProvider';
import Main from './components/Main';
import './App.css';

function App() {
  return (
    <Router>
      <ThemeProvider defaultTheme="system" storageKey="vite-ui-theme">
        <Main />
      </ThemeProvider>
    </Router>
  );
}

export default App;