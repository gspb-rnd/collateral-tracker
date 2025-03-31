export interface AuditLogEntry {
  oldValue: string | null;
  newValue: string | null;
  timeChanged: string | null;
}

export interface Datapoint {
  id?: string;
  type: string;
  valueType: string | null;
  displayName: string | null;
  dependencyExpression: string | null;
  description: string | null;
  displayType: string | null;
  selectableValues: string[];
  selectedValues: string[];
  status: string | null;
  auditLog: AuditLogEntry[];
  collateralId?: string;
}

export type DatapointDisplayType = 'free text' | 'dropdown';

export type DatapointStatus = 
  | 'in progress' 
  | 'completed' 
  | 'not relevant' 
  | 'not started';
