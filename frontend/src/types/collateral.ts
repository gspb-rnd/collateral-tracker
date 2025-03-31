import { Datapoint } from './datapoint';

export interface Collateral {
  id?: string;
  name: string;
  description: string;
  type: string;
  createdAt?: string;
  updatedAt?: string;
  datapoints?: Record<string, Datapoint>;
}
