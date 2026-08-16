export interface Transaction {
  id: number;
  amount?: number;
  transaction_amount?: {
    value: number;
    currency: string;
  };
  value?: number;
  description?: string;
  status: 'CREATED' | 'SUCCESS' | 'FAILED';
}