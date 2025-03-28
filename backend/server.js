const express = require('express');
const cors = require('cors');
const app = express();
const port = 8080;

app.use(cors());
app.use(express.json());

const collateralItems = [
  {
    id: '1',
    name: 'Treasury Bond',
    description: 'US Treasury Bond with 10-year maturity',
    type: 'Government Bond',
    createdAt: '2025-01-15T10:30:00Z',
    updatedAt: '2025-01-15T10:30:00Z'
  },
  {
    id: '2',
    name: 'Corporate Bond',
    description: 'AAA-rated corporate bond from Tech Corp',
    type: 'Corporate Bond',
    createdAt: '2025-02-10T14:45:00Z',
    updatedAt: '2025-02-10T14:45:00Z'
  },
  {
    id: '3',
    name: 'Equity Shares',
    description: 'Common stock shares of Finance Inc',
    type: 'Equity',
    createdAt: '2025-02-20T09:15:00Z',
    updatedAt: '2025-02-20T09:15:00Z'
  },
  {
    id: '4',
    name: 'Real Estate Property',
    description: 'Commercial real estate in downtown area',
    type: 'Real Estate',
    createdAt: '2025-03-05T11:20:00Z',
    updatedAt: '2025-03-05T11:20:00Z'
  },
  {
    id: '5',
    name: 'Gold Bullion',
    description: '24K gold bullion bars',
    type: 'Commodity',
    createdAt: '2025-03-10T16:30:00Z',
    updatedAt: '2025-03-10T16:30:00Z'
  }
];

app.get('/api/collateral', (req, res) => {
  res.json(collateralItems);
});

app.get('/api/collateral/search', (req, res) => {
  const query = req.query.query?.toLowerCase() || '';
  if (!query) {
    return res.json([]);
  }
  
  const results = collateralItems.filter(item => 
    item.name.toLowerCase().includes(query) || 
    item.description.toLowerCase().includes(query)
  );
  
  res.json(results);
});

app.get('/api/collateral/:id', (req, res) => {
  const item = collateralItems.find(c => c.id === req.params.id);
  if (!item) {
    return res.status(404).json({ message: 'Collateral not found' });
  }
  res.json(item);
});

app.post('/api/collateral', (req, res) => {
  const newItem = {
    id: (collateralItems.length + 1).toString(),
    ...req.body,
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };
  
  collateralItems.push(newItem);
  res.status(201).json(newItem);
});

app.listen(port, () => {
  console.log(`Mock server running at http://localhost:${port}`);
});
