import axios from 'axios';

export const api = axios.create({
    baseURL: 'http://localhost:8081'
    });

export interface PaymentLoad {
    order_id: number;
    user_id: number;
    payee_id: number;
    payment_method: 'PIX' | 'CREDIT_CARD' | 'DEBIT_CARD';
    transaction_amount: {
        value: number;
        currency: string;
        };
    customer: {
        name: string;
        address: string;
        };
    }

export const processPayment = async (data: PaymentLoad, idempotenceKey: string) => {
    return await api.post('/api/transactions', data, {
        headers: {
            'Idempotence-Key': idempotenceKey,
            'Content-Type': 'application/json',
            },
        });
    };

export default api;