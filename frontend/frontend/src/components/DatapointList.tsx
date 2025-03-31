import React, { useEffect, useState } from 'react';
import { Datapoint } from '../types/datapoint';
import { api } from '../utils/api';

interface DatapointListProps {
  collateralId: string;
}

const DatapointList: React.FC<DatapointListProps> = ({ collateralId }) => {
  const [datapoints, setDatapoints] = useState<Datapoint[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDatapoints = async () => {
      setIsLoading(true);
      try {
        const data = await api.getDatapoints<Datapoint[]>(collateralId);
        setDatapoints(data);
        setError(null);
      } catch (err) {
        console.error('Error fetching datapoints:', err);
        setError('Failed to load datapoints');
      } finally {
        setIsLoading(false);
      }
    };

    if (collateralId) {
      fetchDatapoints();
    }
  }, [collateralId]);

  const handleDatapointUpdate = async (datapoint: Datapoint, value: string) => {
    try {
      const updatedDatapoint = { ...datapoint };
      updatedDatapoint.selectedValues = [value];
      updatedDatapoint.status = 'completed';
      
      const result = await api.updateDatapoint<Datapoint>(datapoint.id!, updatedDatapoint);
      
      setDatapoints(datapoints.map(dp => dp.id === result.id ? result : dp));
    } catch (err) {
      console.error('Error updating datapoint:', err);
      setError('Failed to update datapoint');
    }
  };

  if (isLoading) return <div>Loading datapoints...</div>;
  if (error) return <div className="text-red-500">{error}</div>;

  return (
    <div className="mt-4">
      <h2 className="text-xl font-bold mb-2">Datapoints</h2>
      {datapoints.length === 0 ? (
        <div>No datapoints available</div>
      ) : (
        <div className="space-y-4">
          {datapoints.map((datapoint) => (
            <div key={datapoint.id} className="p-4 border rounded">
              <div className="font-medium">{datapoint.displayName}</div>
              <div className="text-sm text-gray-500">{datapoint.description}</div>
              <div className="mt-2">
                {datapoint.displayType === 'free text' ? (
                  <input
                    type="text"
                    value={datapoint.selectedValues[0] || ''} 
                    onChange={(e) => handleDatapointUpdate(datapoint, e.target.value)}
                    placeholder={`Enter ${datapoint.displayName}`}
                    className="w-full p-2 border rounded"
                  />
                ) : (
                  <select
                    className="w-full p-2 border rounded"
                    value={datapoint.selectedValues[0] || ''}
                    onChange={(e) => handleDatapointUpdate(datapoint, e.target.value)}
                  >
                    <option value="">Select a value</option>
                    {datapoint.selectableValues.map((value) => (
                      <option key={value} value={value}>{value}</option>
                    ))}
                  </select>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default DatapointList;
