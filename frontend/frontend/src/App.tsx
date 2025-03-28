import { BrowserRouter as Router, Routes, Route } from 'react-router-dom'
import './App.css'
import Navbar from './components/layout/Navbar'
import Home from './pages/Home'
import NewCollateral from './pages/NewCollateral'

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <Navbar />
        <main>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/new-collateral" element={<NewCollateral />} />
          </Routes>
        </main>
      </div>
    </Router>
  )
}

export default App
