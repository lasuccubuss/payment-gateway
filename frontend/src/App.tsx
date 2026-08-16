import { useEffect, useState, FormEvent } from 'react';
import api from './services/api';
import type { Transaction } from './types/Transaction';

function App() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [loading, setLoading] = useState(true);

  // Estados do Formulário
  const [amount, setAmount] = useState<string>('');
  const [description, setDescription] = useState<string>('');
  const [payeeId, setPayeeId] = useState<string>('');
  const [submitting, setSubmitting] = useState(false);

  // Função para buscar o histórico de transações
  const fetchTransactions = () => {
    api.get('/api/transactions/user/1/history')
      .then(response => {
        setTransactions(Array.isArray(response.data) ? response.data : []);
        setLoading(false);
      })
      .catch(error => {
        console.error("Erro ao buscar transações:", error);
        setLoading(false);
      });
  };

  useEffect(() => {
    fetchTransactions();
  }, []);

  // Função para enviar o pagamento (POST)
  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!amount || !payeeId) return alert('Preencha os campos obrigatórios!');

    setSubmitting(true);

    const idempotenceKey = crypto.randomUUID();

    const paymentRequest = {
      order_id: Math.floor(Math.random() * 90000) + 10000,
      user_id: 1,
      payee_id: Number(payeeId),
      payment_method: 'PIX',
      transaction_amount: {
        value: Math.round(Number(amount) * 100),
        currency: 'BRL'
      },
      customer: {
        name: 'Susana Garcia',
        address: 'Rua das Flores, 123'
      }
    };

    try {
      await api.post('/api/transactions', paymentRequest, {
        headers: {
          'Idempotence-Key': idempotenceKey,
          'Content-Type': 'application/json'
        }
      });

      alert('Pagamento processado com sucesso!');

      setAmount('');
      setDescription('');
      setPayeeId('');

      fetchTransactions();
    } catch (error: any) {
      console.error("Erro ao processar pagamento:", error);
      const msg = error.response?.data?.detail || error.response?.data?.title || 'Erro ao enviar pagamento.';
      alert(`Falha: ${msg}`);
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) return <p style={{ textAlign: 'center', marginTop: '50px' }}>Carregando dados...</p>;

  return (
    <div style={{ padding: '40px', fontFamily: 'sans-serif', backgroundColor: '#f9f9f9', minHeight: '100vh' }}>
      <div style={{ maxWidth: '900px', margin: '0 auto', backgroundColor: 'white', padding: '20px', borderRadius: '12px', boxShadow: '0 4px 6px rgba(0,0,0,0.1)' }}>

        <h1 style={{ textAlign: 'center', color: '#2c3e50' }}>💳 Gateway de Pagamentos</h1>

        {/* FORMULÁRIO DE NOVO PAGAMENTO */}
        <div style={{ backgroundColor: '#f8f9fa', padding: '20px', borderRadius: '8px', marginBottom: '30px', border: '1px solid #eee' }}>
          <h3 style={{ marginTop: 0, color: '#34495e' }}>Enviar Novo Pagamento</h3>
          <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: '15px' }}>

            <div style={{ display: 'flex', gap: '15px' }}>
              <div style={{ flex: 1 }}>
                <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>ID do Recebedor:</label>
                <input
                  type="number"
                  value={payeeId}
                  onChange={(e) => setPayeeId(e.target.value)}
                  placeholder="Ex: 2"
                  required
                  style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
                />
              </div>
              <div style={{ flex: 1 }}>
                <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Valor (R$):</label>
                <input
                  type="number"
                  step="0.01"
                  min="0.01"
                  value={amount}
                  onChange={(e) => setAmount(e.target.value)}
                  placeholder="0.00"
                  required
                  style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
                />
              </div>
            </div>

            <div>
              <label style={{ display: 'block', marginBottom: '5px', fontWeight: 'bold' }}>Descrição:</label>
              <input
                type="text"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
                placeholder="Ex: Aluguel, Alimentação..."
                style={{ width: '100%', padding: '8px', borderRadius: '4px', border: '1px solid #ccc' }}
              />
            </div>

            <button
              type="submit"
              disabled={submitting}
              style={{
                backgroundColor: submitting ? '#95a5a6' : '#2ecc71',
                color: 'white',
                padding: '10px',
                border: 'none',
                borderRadius: '4px',
                fontWeight: 'bold',
                cursor: submitting ? 'not-allowed' : 'pointer',
                transition: '0.2s'
              }}
            >
              {submitting ? 'Processando...' : 'Confirmar Pagamento'}
            </button>
          </form>
        </div>

        {/* TABELA DE HISTÓRICO */}
        <h3 style={{ borderBottom: '2px solid #eee', paddingBottom: '10px', color: '#7f8c8d' }}>Histórico (PostgreSQL)</h3>

        {transactions.length === 0 ? (
          <p style={{ textAlign: 'center', color: '#95a5a6', padding: '20px' }}>Nenhuma transação encontrada.</p>
        ) : (
          <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
            <thead>
              <tr style={{ textAlign: 'left', backgroundColor: '#f8f9fa' }}>
                <th style={{ padding: '12px', borderBottom: '2px solid #dee2e6' }}>Valor</th>
                <th style={{ padding: '12px', borderBottom: '2px solid #dee2e6' }}>Descrição</th>
                <th style={{ padding: '12px', borderBottom: '2px solid #dee2e6' }}>Status</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((t: any) => {
                const rawAmount = t.amount ?? t.transaction_amount?.value ?? t.value ?? 0;
                return (
                  <tr key={t.id} style={{ borderBottom: '1px solid #eee' }}>
                    <td style={{ padding: '12px' }}>
                      R$ {(rawAmount / 100).toFixed(2)}
                    </td>
                    <td style={{ padding: '12px' }}>{t.description || "Sem descrição"}</td>
                    <td style={{ padding: '12px' }}>
                      <span style={{
                        backgroundColor: t.status === 'CREATED' ? '#fff3cd' : '#d4edda',
                        color: t.status === 'CREATED' ? '#856404' : '#155724',
                        padding: '4px 10px',
                        borderRadius: '20px',
                        fontSize: '0.85em',
                        fontWeight: 'bold'
                      }}>
                        {t.status}
                      </span>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
}

export default App;