import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import NavBar from './components/NavBar';
import CampaignList from './components/CampaignList';
import CampaignDetails from './components/CampaignDetails';
import CreateCampaign from './components/CreateCampaign';
import './App.css';

function App() {
  return (
    <Router>
      <NavBar />
      <div style={{ padding: '0 0 2rem 0', minHeight: '90vh' }}>
        <Routes>
          <Route path="/" element={<CampaignList />} />
          <Route path="/campaign/:id" element={<CampaignDetails />} />
          <Route path="/create" element={<CreateCampaign />} />
        </Routes>
      </div>
    </Router>
  );
}
export default App;
