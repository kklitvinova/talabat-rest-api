import React, { useState } from 'react';
import axios from 'axios';

const API_URL = 'http://localhost:8080';
const ORANGE = '#FF6600';
const YELLOW = '#CDEC00';
const DARK = '#1A1A1A';

function CustomSelect({ value, onChange, options, placeholder }) {
  const [open, setOpen] = useState(false);
  const selected = options.find(o => o.value === value);
  return (
    <div style={{ position: 'relative' }}>
      <div onClick={() => setOpen(!open)} style={{ padding: '9px 36px 9px 14px', border: '1px solid #e5e5e5', borderRadius: '8px', fontSize: '13px', color: value ? '#333' : '#aaa', cursor: 'pointer', background: 'white', userSelect: 'none', whiteSpace: 'nowrap', minWidth: '140px' }}>
        {selected ? selected.label : placeholder}
        <span style={{ position: 'absolute', right: '12px', top: '50%', transform: `translateY(-50%) rotate(${open ? 180 : 0}deg)`, fontSize: '10px', color: '#aaa', transition: '0.2s' }}>▼</span>
      </div>
      {open && (
        <div style={{ position: 'absolute', top: '100%', left: 0, right: 0, background: 'white', border: '1px solid #e5e5e5', borderRadius: '8px', boxShadow: '0 8px 24px rgba(0,0,0,0.12)', zIndex: 100, overflow: 'hidden', marginTop: '4px' }}>
          {options.map(o => (
            <div key={o.value} onClick={() => { onChange(o.value); setOpen(false); }}
              style={{ padding: '10px 14px', fontSize: '13px', cursor: 'pointer', color: o.value === value ? ORANGE : '#333', background: o.value === value ? '#fff7f0' : 'white', fontWeight: o.value === value ? '600' : '400' }}
              onMouseEnter={e => { if (o.value !== value) e.target.style.background = '#f9f9f9'; }}
              onMouseLeave={e => { if (o.value !== value) e.target.style.background = 'white'; }}>
              {o.label}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

function App() {
  const [token, setToken] = useState(null);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [orders, setOrders] = useState([]);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [searchId, setSearchId] = useState('');
  const [filterCity, setFilterCity] = useState('');
  const [filterStatus, setFilterStatus] = useState('');
  const [filterPayment, setFilterPayment] = useState('');
  const [newOrder, setNewOrder] = useState({ city: '', paymentMethod: '', delivered: false });
  const [showForm, setShowForm] = useState(false);

  const login = async () => {
    try {
      setError('');
      const res = await axios.post(`${API_URL}/api/auth/login`, { email, password });
      setToken(res.data.token);
      loadOrders(res.data.token);
    } catch {
      setError('Invalid email or password');
    }
  };

  const loadOrders = async (t = token) => {
    try {
      setLoading(true);
      const res = await axios.get(`${API_URL}/api/orders`, { headers: { Authorization: `Bearer ${t}` } });
      setOrders(res.data);
    } catch {
      setError('Failed to load orders');
    } finally {
      setLoading(false);
    }
  };

  const searchById = async () => {
    if (!searchId) return loadOrders();
    try {
      setError('');
      const res = await axios.get(`${API_URL}/api/orders/${searchId}`, { headers: { Authorization: `Bearer ${token}` } });
      setOrders([res.data]);
    } catch {
      setError('Order not found');
      setOrders([]);
    }
  };

  const filterOrders = async () => {
    try {
      setLoading(true);
      setError('');
      let url = `${API_URL}/api/orders`;
      if (filterCity) url = `${API_URL}/api/orders/city/${filterCity}`;
      else if (filterStatus !== '') url = `${API_URL}/api/orders/status/${filterStatus}`;
      else if (filterPayment) url = `${API_URL}/api/orders/payment/${filterPayment}`;
      const res = await axios.get(url, { headers: { Authorization: `Bearer ${token}` } });
      setOrders(res.data);
    } catch {
      setError('Filter failed');
    } finally {
      setLoading(false);
    }
  };

  const createOrder = async () => {
    try {
      await axios.post(`${API_URL}/api/orders`, newOrder, { headers: { Authorization: `Bearer ${token}` } });
      setShowForm(false);
      setNewOrder({ city: '', paymentMethod: '', delivered: false });
      loadOrders();
    } catch {
      setError('Failed to create order');
    }
  };

  const deleteOrder = async (id) => {
    try {
      await axios.delete(`${API_URL}/api/orders/${id}`, { headers: { Authorization: `Bearer ${token}` } });
      loadOrders();
    } catch {
      setError('Cannot delete this order');
    }
  };

  const logout = () => { setToken(null); setOrders([]); setEmail(''); setPassword(''); };

  if (!token) {
    return (
      <div style={{ minHeight: '100vh', display: 'flex', fontFamily: "'Segoe UI', sans-serif" }}>
        <div style={{ flex: 1, background: ORANGE, display: 'flex', flexDirection: 'column', justifyContent: 'center', padding: '60px', color: 'white' }}>
          <div style={{ fontSize: '48px', fontWeight: '900', marginBottom: '20px' }}>talabat</div>
          <div style={{ fontSize: '32px', fontWeight: '700', lineHeight: 1.3, marginBottom: '16px' }}>
            We deliver<br />to the region<br />
            <span style={{ color: YELLOW }}>that delivers.</span>
          </div>
          <p style={{ opacity: 0.75, fontSize: '15px', maxWidth: '340px', lineHeight: 1.6 }}>
            Manager portal — track and manage all delivery orders across Egypt in real time.
          </p>
          <div style={{ marginTop: '40px', display: 'flex', gap: '12px' }}>
            {['200+ Orders', '5 Cities', 'Real-time'].map(tag => (
              <span key={tag} style={{ background: 'rgba(255,255,255,0.15)', padding: '6px 14px', borderRadius: '20px', fontSize: '13px' }}>{tag}</span>
            ))}
          </div>
        </div>
        <div style={{ width: '440px', display: 'flex', alignItems: 'center', justifyContent: 'center', padding: '40px', background: '#fafafa' }}>
          <div style={{ width: '100%' }}>
            <h2 style={{ fontSize: '26px', fontWeight: '800', color: DARK, marginBottom: '6px' }}>Welcome back</h2>
            <p style={{ color: '#888', fontSize: '14px', marginBottom: '32px' }}>Sign in to your manager account</p>
            {error && <div style={{ background: '#fff0f0', border: '1px solid #ffcccc', borderRadius: '8px', padding: '12px 16px', color: '#cc0000', fontSize: '13px', marginBottom: '20px' }}>{error}</div>}
            <label style={labelStyle}>Email address</label>
            <input style={inputFull} type="email" placeholder="your@email.com" value={email}
              onChange={e => setEmail(e.target.value)} onKeyPress={e => e.key === 'Enter' && login()} />
            <label style={labelStyle}>Password</label>
            <input style={inputFull} type="password" placeholder="••••••••" value={password}
              onChange={e => setPassword(e.target.value)} onKeyPress={e => e.key === 'Enter' && login()} />
            <button style={{ width: '100%', padding: '14px', background: ORANGE, color: 'white', border: 'none', borderRadius: '10px', fontSize: '15px', fontWeight: '700', cursor: 'pointer', marginTop: '4px' }}
              onClick={login}>Sign in →</button>
          </div>
        </div>
      </div>
    );
  }

  const delivered = orders.filter(o => o.delivered).length;
  const pending = orders.filter(o => !o.delivered).length;

  return (
    <div style={{ fontFamily: "'Segoe UI', sans-serif", background: '#f5f5f5', minHeight: '100vh' }}>
      <div style={{ background: ORANGE, height: '56px', display: 'flex', alignItems: 'center', justifyContent: 'space-between', padding: '0 32px', position: 'sticky', top: 0, zIndex: 50 }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
          <span style={{ color: 'white', fontSize: '22px', fontWeight: '900' }}>talabat</span>
          <span style={{ background: YELLOW, color: DARK, fontSize: '10px', fontWeight: '800', padding: '3px 10px', borderRadius: '20px', letterSpacing: '0.5px' }}>MANAGER</span>
        </div>
        <div style={{ display: 'flex', alignItems: 'center', gap: '20px' }}>
          <span style={{ color: 'rgba(255,255,255,0.8)', fontSize: '13px' }}>{orders.length} orders loaded</span>
          <button onClick={logout} style={{ background: 'rgba(255,255,255,0.15)', color: 'white', border: '1px solid rgba(255,255,255,0.25)', padding: '7px 18px', borderRadius: '20px', cursor: 'pointer', fontSize: '13px' }}>
            Sign out
          </button>
        </div>
      </div>

      <div style={{ background: 'white', borderBottom: '1px solid #eee', padding: '14px 32px', display: 'flex', gap: '10px', alignItems: 'center', flexWrap: 'wrap' }}>
        <div style={{ position: 'relative', flex: '0 0 200px' }}>
          <span style={{ position: 'absolute', left: '12px', top: '50%', transform: 'translateY(-50%)', color: '#aaa', fontSize: '14px' }}>🔍</span>
          <input style={{ width: '100%', padding: '9px 12px 9px 34px', border: '1px solid #e5e5e5', borderRadius: '8px', fontSize: '13px', outline: 'none', boxSizing: 'border-box' }}
            placeholder="Search by ID..." value={searchId} onChange={e => setSearchId(e.target.value)} onKeyPress={e => e.key === 'Enter' && searchById()} />
        </div>
        <input style={inputSmall} placeholder="City..." value={filterCity} onChange={e => setFilterCity(e.target.value)} />
        <CustomSelect value={filterStatus} onChange={setFilterStatus} placeholder="All statuses" options={[
          { value: '', label: 'All statuses' },
          { value: 'true', label: '✓ Delivered' },
          { value: 'false', label: '⏳ Pending' },
        ]} />
        <CustomSelect value={filterPayment} onChange={setFilterPayment} placeholder="All payments" options={[
          { value: '', label: 'All payments' },
          { value: 'Cash', label: '💵 Cash' },
          { value: 'Wallet', label: '👛 Wallet' },
          { value: 'Credit Card', label: '💳 Credit Card' },
        ]} />
        <button style={btnOrange} onClick={filterOrders}>Filter</button>
        <button style={btnOutline} onClick={() => { setSearchId(''); setFilterCity(''); setFilterStatus(''); setFilterPayment(''); loadOrders(); }}>Reset</button>
        <div style={{ marginLeft: 'auto' }}>
          <button style={{ ...btnOrange, background: DARK }} onClick={() => setShowForm(!showForm)}>+ New Order</button>
        </div>
      </div>

      {error && (
        <div style={{ background: '#fff0f0', borderBottom: '1px solid #ffcccc', padding: '10px 32px', color: '#cc0000', fontSize: '13px', display: 'flex', justifyContent: 'space-between' }}>
          {error} <span style={{ cursor: 'pointer' }} onClick={() => setError('')}>✕</span>
        </div>
      )}

      <div style={{ padding: '24px 32px' }}>
        <div style={{ display: 'flex', gap: '16px', marginBottom: '24px' }}>
          {[
            { label: 'Total Orders', value: orders.length, sub: 'All orders loaded', color: ORANGE },
            { label: 'Delivered', value: delivered, sub: `${orders.length ? Math.round(delivered / orders.length * 100) : 0}% success rate`, color: '#16a34a' },
            { label: 'Pending', value: pending, sub: 'Awaiting delivery', color: '#d97706' },
          ].map(s => (
            <div key={s.label} style={{ flex: 1, background: 'white', borderRadius: '14px', padding: '22px 26px', boxShadow: '0 1px 3px rgba(0,0,0,0.06)', border: '1px solid #f0f0f0' }}>
              <div style={{ fontSize: '36px', fontWeight: '800', color: s.color, lineHeight: 1 }}>{s.value}</div>
              <div style={{ fontSize: '14px', fontWeight: '600', color: DARK, marginTop: '6px' }}>{s.label}</div>
              <div style={{ fontSize: '12px', color: '#aaa', marginTop: '3px' }}>{s.sub}</div>
            </div>
          ))}
        </div>

        {showForm && (
          <div style={{ background: 'white', borderRadius: '14px', padding: '24px', marginBottom: '20px', boxShadow: '0 1px 3px rgba(0,0,0,0.06)', border: '1px solid #f0f0f0', maxWidth: '440px' }}>
            <h3 style={{ margin: '0 0 20px', fontSize: '16px', fontWeight: '700' }}>Create New Order</h3>
            <label style={labelStyle}>City</label>
            <input style={inputFull} placeholder="e.g. Cairo" value={newOrder.city} onChange={e => setNewOrder({ ...newOrder, city: e.target.value })} />
            <label style={labelStyle}>Payment Method</label>
            <div style={{ marginBottom: '14px' }}>
              <CustomSelect value={newOrder.paymentMethod} onChange={v => setNewOrder({ ...newOrder, paymentMethod: v })} placeholder="Select payment method" options={[
                { value: 'Cash', label: '💵 Cash' },
                { value: 'Wallet', label: '👛 Wallet' },
                { value: 'Credit Card', label: '💳 Credit Card' },
              ]} />
            </div>
            <label style={{ display: 'flex', alignItems: 'center', gap: '8px', fontSize: '14px', color: '#555', marginBottom: '20px', cursor: 'pointer' }}>
              <input type="checkbox" checked={newOrder.delivered} onChange={e => setNewOrder({ ...newOrder, delivered: e.target.checked })} />
              Mark as delivered
            </label>
            <div style={{ display: 'flex', gap: '10px' }}>
              <button style={btnOrange} onClick={createOrder}>Create order</button>
              <button style={btnOutline} onClick={() => setShowForm(false)}>Cancel</button>
            </div>
          </div>
        )}

        {loading ? (
          <div style={{ textAlign: 'center', padding: '80px', color: '#bbb' }}>
            <div style={{ fontSize: '32px', marginBottom: '12px' }}>⏳</div>
            Loading orders...
          </div>
        ) : (
          <div style={{ background: 'white', borderRadius: '14px', overflow: 'hidden', boxShadow: '0 1px 3px rgba(0,0,0,0.06)', border: '1px solid #f0f0f0' }}>
            <table style={{ width: '100%', borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ borderBottom: '1px solid #f0f0f0' }}>
                  {['ID', 'User', 'City', 'Payment', 'Status', 'Duration', 'Distance', ''].map(h => (
                    <th key={h} style={{ padding: '13px 18px', textAlign: 'left', fontSize: '11px', fontWeight: '700', color: '#aaa', textTransform: 'uppercase', letterSpacing: '0.8px' }}>{h}</th>
                  ))}
                </tr>
              </thead>
              <tbody>
                {orders.map((order) => (
                  <tr key={order.orderId} style={{ borderBottom: '1px solid #f7f7f7' }}
                    onMouseEnter={e => e.currentTarget.style.background = '#fafafa'}
                    onMouseLeave={e => e.currentTarget.style.background = 'white'}>
                    <td style={td}><span style={{ fontWeight: '700', color: DARK }}>#{order.orderId}</span></td>
                    <td style={td}><span style={{ color: '#888', fontSize: '12px' }}>{order.userId || '—'}</span></td>
                    <td style={td}><span style={{ fontWeight: '500' }}>{order.city}</span></td>
                    <td style={td}>{order.paymentMethod}</td>
                    <td style={td}>
                      {order.delivered
                        ? <span style={{ background: '#dcfce7', color: '#16a34a', padding: '4px 12px', borderRadius: '20px', fontSize: '12px', fontWeight: '600' }}>Delivered</span>
                        : <span style={{ background: '#fef9c3', color: '#ca8a04', padding: '4px 12px', borderRadius: '20px', fontSize: '12px', fontWeight: '600' }}>Pending</span>
                      }
                    </td>
                    <td style={td}>{order.deliveryDurationMinutes ? `${order.deliveryDurationMinutes} min` : '—'}</td>
                    <td style={td}>{order.deliveryDistanceKm ? `${order.deliveryDistanceKm?.toFixed(1)} km` : '—'}</td>
                    <td style={td}>
                      <button onClick={() => deleteOrder(order.orderId)}
                        style={{ background: 'none', color: '#ddd', border: '1px solid #eee', padding: '5px 12px', borderRadius: '6px', cursor: 'pointer', fontSize: '12px' }}
                        onMouseEnter={e => { e.target.style.background = '#fee2e2'; e.target.style.color = '#dc2626'; e.target.style.borderColor = '#fca5a5'; }}
                        onMouseLeave={e => { e.target.style.background = 'none'; e.target.style.color = '#ddd'; e.target.style.borderColor = '#eee'; }}>
                        Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            {orders.length === 0 && (
              <div style={{ textAlign: 'center', padding: '80px', color: '#bbb' }}>
                <div style={{ fontSize: '40px', marginBottom: '12px' }}>📭</div>
                No orders found
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
}

const labelStyle = { display: 'block', fontSize: '13px', fontWeight: '600', color: '#555', marginBottom: '6px' };
const inputFull = { width: '100%', padding: '11px 14px', border: '1px solid #e5e5e5', borderRadius: '8px', fontSize: '14px', marginBottom: '14px', boxSizing: 'border-box', outline: 'none' };
const inputSmall = { padding: '9px 14px', border: '1px solid #e5e5e5', borderRadius: '8px', fontSize: '13px', outline: 'none', background: 'white' };
const btnOrange = { padding: '9px 20px', background: '#FF6600', color: 'white', border: 'none', borderRadius: '8px', cursor: 'pointer', fontSize: '13px', fontWeight: '600' };
const btnOutline = { padding: '9px 20px', background: 'white', color: '#555', border: '1px solid #e5e5e5', borderRadius: '8px', cursor: 'pointer', fontSize: '13px' };
const td = { padding: '14px 18px', fontSize: '13px', color: '#444' };

export default App;