import React, { useState } from 'react';
import { api } from '../utils/api';
import { Collateral } from '../types/collateral';
import DatapointList from '../components/DatapointList';

const NewCollateral: React.FC = () => {
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [type, setType] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitSuccess, setSubmitSuccess] = useState<Collateral | null>(null);
  const [submitError, setSubmitError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setIsSubmitting(true);
    setSubmitError(null);
    
    try {
      const data = { name, description, type };
      const result = await api.post<Collateral>('/api/collateral', data);
      console.log('Collateral created:', result);
      setSubmitSuccess(result);
      setName('');
      setDescription('');
      setType('');
    } catch (error) {
      console.error('Error creating collateral:', error);
      setSubmitError('Failed to create collateral. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="container mx-auto p-6">
      <h1 className="text-2xl font-bold mb-4">New Collateral</h1>
      
      <form onSubmit={handleSubmit} className="mb-6">
        <div className="mb-4">
          <label className="block text-gray-700 mb-2">Name</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full p-2 border rounded"
            required
          />
        </div>
        
        <div className="mb-4">
          <label className="block text-gray-700 mb-2">Description</label>
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            className="w-full p-2 border rounded min-h-[100px]"
            required
          />
        </div>
        
        <div className="mb-4">
          <label className="block text-gray-700 mb-2">Type</label>
          <input
            type="text"
            value={type}
            onChange={(e) => setType(e.target.value)}
            className="w-full p-2 border rounded"
            required
          />
        </div>
        
        <button
          type="submit"
          disabled={isSubmitting}
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 disabled:bg-blue-300"
        >
          {isSubmitting ? 'Creating...' : 'Create Collateral'}
        </button>
      </form>
      
      {submitSuccess && (
        <div className="mt-4 p-4 rounded bg-green-50 text-green-700">
          <p className="font-bold">Collateral created successfully!</p>
          {submitSuccess.id && <DatapointList collateralId={submitSuccess.id} />}
        </div>
      )}
      
      {submitError && (
        <div className="mt-4 p-4 rounded bg-red-50 text-red-700">
          {submitError}
        </div>
      )}
    </div>
  );
};

export default NewCollateral;
