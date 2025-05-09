import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'
import Navbar from './components/layout/Navbar'
import Home from './pages/Home'
import NewCollateral from './pages/NewCollateral'
import SearchResults from './pages/SearchResults'
import UpdateCollateral from './pages/UpdateCollateral'

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <main>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/new-collateral" element={<NewCollateral />} />
            <Route path="/search" element={<SearchResults />} />
            <Route path="/update-collateral/:id" element={<UpdateCollateral />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App
