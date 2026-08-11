export interface Transaction {
    id?: number;
    amount: number;
    description: string;
    status: string;
    createdAt?: string;
    }