import React, { useState } from 'react';
import { Collateral } from '../types/collateral';
import { Datapoint } from '../types/datapoint';
import { api } from '../utils/api';

interface DatapointListProps {
  collateral: Collateral;
  onUpdate?: (collateral: Collateral) => void;
}

const DatapointList: React.FC<DatapointListProps> = ({ collateral, onUpdate }) => {
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const handleDatapointUpdate = async (datapointKey: string, value: string) => {
    if (!collateral.id) return;

    setIsLoading(true);
    try {
      const updatedCollateral = await api.updateDatapoint<Collateral>(
        collateral.id,
        datapointKey,
        value
      );
      setError(null);
      if (onUpdate) {
        onUpdate(updatedCollateral);
      }
    } catch (err) {
      console.error('Error updating datapoint:', err);
      setError('Failed to update datapoint');
    } finally {
      setIsLoading(false);
    }
  };

  const datapoints = collateral.datapoints || {};
  const datapointKeys = Object.keys(datapoints).filter(key => key.startsWith('dp_'));

  if (isLoading) return <div>Loading datapoints...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="mt-4">
      <h2 className="text-xl font-bold mb-2">Datapoints</h2>
      {datapointKeys.length === 0 ? (
        <div>No datapoints available</div>
      ) : (
        <div className="space-y-4">
          {datapointKeys.map((key) => {
            const datapoint = datapoints[key] as Datapoint;
            return (
              <div key={key} className="p-4 border rounded">
                <div className="font-medium">{datapoint.displayName}</div>
                <div className="text-sm text-gray-500">{datapoint.description}</div>
                <div className="mt-2">
                  {datapoint.displayType === 'free text' ? (
                    <input
                      type="text"
                      value={datapoint.selectedValues?.[0] || ''} 
                      onChange={(e) => handleDatapointUpdate(key, e.target.value)}
                      placeholder={`Enter ${datapoint.displayName}`}
                      className="w-full p-2 border rounded"
                    />
                  ) : (
                    <select
                      className="w-full p-2 border rounded"
                      value={datapoint.selectedValues?.[0] || ''}
                      onChange={(e) => handleDatapointUpdate(key, e.target.value)}
                    >
                      <option value="">Select a value</option>
                      {(datapoint.selectableValues || []).map((value) => (
                        <option key={value} value={value}>{value}</option>
                      ))}
                    </select>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default DatapointList;
