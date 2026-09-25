/**
 * RAILCONNECT — Central Frontend API Client
 * Seamlessly interfaces with the Java Spring Boot Backend on http://localhost:8080
 */

const API_BASE = (typeof window !== 'undefined' && window.location.protocol.startsWith('http') && window.location.port === '8080') ? '/api' : 'http://localhost:8080/api';

const Api = {
  getToken() {
    return localStorage.getItem('railconnect_token');
  },

  setToken(token) {
    if (token) {
      localStorage.setItem('railconnect_token', token);
    } else {
      localStorage.removeItem('railconnect_token');
    }
  },

  getUser() {
    const raw = localStorage.getItem('railconnect_user');
    return raw ? JSON.parse(raw) : null;
  },

  setUser(user) {
    if (user) {
      localStorage.setItem('railconnect_user', JSON.stringify(user));
    } else {
      localStorage.removeItem('railconnect_user');
    }
  },

  async request(endpoint, options = {}) {
    const isGitHubPages = typeof window !== 'undefined' && (window.location.hostname.endsWith('github.io') || window.location.protocol === 'file:');

    if (!isGitHubPages) {
      try {
        const headers = {
          'Content-Type': 'application/json',
          ...options.headers,
        };

        const token = this.getToken();
        if (token) {
          headers['Authorization'] = `Bearer ${token}`;
        }

        const controller = new AbortController();
        const timeoutId = setTimeout(() => controller.abort(), 3500);

        const response = await fetch(`${API_BASE}${endpoint}`, {
          ...options,
          headers,
          signal: controller.signal
        });
        clearTimeout(timeoutId);

        const text = await response.text();
        let data = {};
        try {
          data = text ? JSON.parse(text) : {};
        } catch (parseErr) {
          data = { message: text };
        }

        if (response.status === 401) {
          localStorage.removeItem('railconnect_token');
          localStorage.removeItem('railconnect_user');
        }

        if (response.ok) {
          return data;
        }
        console.warn(`Server responded with ${response.status}, evaluating simulation fallback.`);
      } catch (err) {
        console.warn(`[RailConnect Backend Offline on ${endpoint}]: Falling back to in-browser simulation engine`);
      }
    }

    if (window.RailConnectMockEngine) {
      return this.dispatchMock(endpoint, options);
    }
    throw new Error('Service currently unavailable');
  },

  dispatchMock(endpoint, options = {}) {
    const mock = window.RailConnectMockEngine;
    const body = options.body ? JSON.parse(options.body) : {};
    const method = options.method || 'GET';

    if (endpoint === '/auth/login') return mock.login(body.username, body.password);
    if (endpoint === '/auth/register') return mock.register(body);
    if (endpoint === '/auth/me') return mock.login(this.getUser()?.username || 'admin');
    if (endpoint === '/trains/stations') return mock.getStations();
    if (endpoint.startsWith('/trains/search')) {
      const url = new URL('http://dummy' + endpoint);
      return mock.searchTrains(url.searchParams.get('from'), url.searchParams.get('to'), url.searchParams.get('date'));
    }
    if (endpoint === '/seats/lock') return mock.lockSeat(body.seatId, body.durationMinutes);
    if (endpoint === '/seats/unlock') return mock.unlockSeat(body.seatId);
    if (endpoint.startsWith('/seats/available/')) return [1, 2, 5, 8, 12, 15, 18, 22, 25, 30];
    if (endpoint === '/bookings/create') return mock.createBooking(body);
    if (endpoint === '/bookings/my') return mock.getDB().bookings;
    if (endpoint.startsWith('/pnr/')) {
      const pnr = endpoint.replace('/pnr/', '');
      return mock.getPnrStatus(pnr);
    }
    if (endpoint.startsWith('/weather/journey')) {
      const url = new URL('http://dummy' + endpoint);
      return mock.getJourneyWeather(url.searchParams.get('from'), url.searchParams.get('to'), url.searchParams.get('date'));
    }
    if (endpoint.startsWith('/tickets/qr')) {
      return { qrImage: 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADIAQAAAACFI5MzAAAA4klEQVR4Xu2USw7EIAxD3RXH4KYt3JRjsCpjp51qvut6gYVQmtcFipNg/BM+E5cmmUSaxIZ0UMtoQC4KsxNRWDvhqM9PH1KQa8/14IZktDW5Enmu242MqCXSt9u3k5gSuq3nv0/J7eRQj8QlE9LBHiwsZ9jOwIjI6jy6ciVpVqzIkaPRa2q03Ym0TUVtG38RjJwNWWJE9rBa1TUioR6LGdoxRuTcLjprPNyJZF41+pHxbkYKopypAYrtiDqRVqsr7QgrCrldzQgvvToWzCkXwkpqRLY4eO3E28lPTTKJNIk3eQCoOFRqAPJjvgAAAABJRU5ErkJggg==' };
    }
    if (endpoint === '/admin/dashboard') return mock.getDashboardStats();
    if (endpoint === '/admin/trains') {
      if (method === 'POST') return mock.addTrain(body);
      return mock.getAllTrains();
    }
    if (endpoint.startsWith('/admin/trains/') && endpoint.endsWith('/routes')) {
      const parts = endpoint.split('/');
      const trainId = parts[3];
      if (method === 'POST') return mock.addRouteStop(trainId, body);
      return mock.getTrainRoutes(trainId);
    }
    if (endpoint.startsWith('/admin/trains/routes/')) {
      const routeId = endpoint.split('/').pop();
      return mock.removeRouteStop(routeId);
    }
    if (endpoint.startsWith('/admin/trains/')) {
      const trainId = endpoint.split('/').pop();
      if (method === 'DELETE') return mock.removeTrain(trainId);
    }
    if (endpoint === '/admin/fares') {
      if (method === 'POST') return mock.configureFare(body);
      return mock.getAllFares();
    }
    if (endpoint === '/admin/audits') return mock.getAuditLogs();
    if (endpoint === '/tickets/verify') return mock.verifyTicket(body.pnrOrTicketId, body.comments);
    if (endpoint.startsWith('/berth-exchange/eligible')) {
      return [
        { bookingId: 101, passengerId: 201, passengerName: 'Rahul Sharma', coach: 'B1', seatNumber: 1, berthType: 'LOWER', status: 'CONFIRMED' }
      ];
    }
    if (endpoint === '/berth-exchange/request') {
      return { id: Date.now(), status: 'REQUESTED', message: 'Swap request submitted' };
    }
    if (endpoint === '/berth-exchange/my') {
      return mock.getDB().exchanges;
    }

    return { status: 'OK', simulated: true };
  },

  // --------------------------------------------------------------------------
  // Authentication Module Endpoints
  // --------------------------------------------------------------------------
  async login(username, password) {
    const res = await this.request('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    });
    this.setToken(res.token);
    this.setUser({
      id: res.userId,
      username: res.username,
      email: res.email,
      role: res.role,
      fullName: res.fullName,
    });
    return res;
  },

  async register(data) {
    const res = await this.request('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });
    this.setToken(res.token);
    this.setUser({
      id: res.userId,
      username: res.username,
      email: res.email,
      role: res.role,
      fullName: res.fullName,
    });
    return res;
  },

  async getCurrentUser() {
    return await this.request('/auth/me');
  },

  logout() {
    this.setToken(null);
    this.setUser(null);
    window.location.href = 'login.html';
  },

  // --------------------------------------------------------------------------
  // Train Search & Route Endpoints
  // --------------------------------------------------------------------------
  async getStations() {
    return await this.request('/trains/stations');
  },

  async searchTrains(from, to, date) {
    return await this.request(`/trains/search?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}&date=${date}`);
  },

  // --------------------------------------------------------------------------
  // Seat Engine Endpoints (Locking & Auto-Allocation)
  // --------------------------------------------------------------------------
  async getCoachLayout(coachId) {
    return await this.request(`/seats/layout/${coachId}`);
  },

  async lockSeat(seatId, durationMinutes = 10) {
    return await this.request('/seats/lock', {
      method: 'POST',
      body: JSON.stringify({ seatId, durationMinutes }),
    });
  },

  async unlockSeat(seatId) {
    return await this.request('/seats/unlock', {
      method: 'POST',
      body: JSON.stringify({ seatId }),
    });
  },

  async autoAllocateSeats(trainId, coachType, passengers, journeyDate) {
    return await this.request('/seats/auto-allocate', {
      method: 'POST',
      body: JSON.stringify({ trainId, coachType, passengers, journeyDate }),
    });
  },

  // --------------------------------------------------------------------------
  // Booking & PNR Endpoints
  // --------------------------------------------------------------------------
  async createBooking(bookingData) {
    return await this.request('/bookings/create', {
      method: 'POST',
      body: JSON.stringify(bookingData),
    });
  },

  async getMyBookings() {
    return await this.request('/bookings/my');
  },

  async cancelBooking(pnr, reason) {
    return await this.request(`/bookings/cancel/${pnr}`, {
      method: 'POST',
      body: JSON.stringify({ reason }),
    });
  },

  async getPnrStatus(pnr) {
    return await this.request(`/pnr/${pnr}`);
  },

  // --------------------------------------------------------------------------
  // Journey Weather Alert Endpoints
  // --------------------------------------------------------------------------
  async getJourneyWeather(from, to, date) {
    return await this.request(`/weather/journey?from=${encodeURIComponent(from)}&to=${encodeURIComponent(to)}&date=${date}`);
  },

  // --------------------------------------------------------------------------
  // Berth Exchange Endpoints
  // --------------------------------------------------------------------------
  async getEligiblePassengers(trainId, journeyDate, coachType, excludeBookingId) {
    return await this.request(`/berth-exchange/eligible?trainId=${trainId}&journeyDate=${journeyDate}&coachType=${coachType}&excludeBookingId=${excludeBookingId || ''}`);
  },

  async requestBerthExchange(data) {
    return await this.request('/berth-exchange/request', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  async respondBerthExchange(requestId, accept, reason) {
    return await this.request(`/berth-exchange/respond/${requestId}`, {
      method: 'POST',
      body: JSON.stringify({ accept, reason }),
    });
  },

  async getMyExchanges() {
    return await this.request('/berth-exchange/my');
  },

  // --------------------------------------------------------------------------
  // Admin & Ticket Inspection Endpoints
  // --------------------------------------------------------------------------
  async getDashboardStats() {
    return await this.request('/admin/dashboard');
  },

  async getAllTrains() {
    return await this.request('/admin/trains');
  },

  async addTrain(trainData) {
    return await this.request('/admin/trains', {
      method: 'POST',
      body: JSON.stringify(trainData),
    });
  },

  async updateTrain(id, trainData) {
    return await this.request(`/admin/trains/${id}`, {
      method: 'PUT',
      body: JSON.stringify(trainData),
    });
  },

  async removeTrain(id) {
    return await this.request(`/admin/trains/${id}`, {
      method: 'DELETE',
    });
  },

  async getTrainRoutes(trainId) {
    return await this.request(`/admin/trains/${trainId}/routes`);
  },

  async addRouteStop(trainId, routeData) {
    return await this.request(`/admin/trains/${trainId}/routes`, {
      method: 'POST',
      body: JSON.stringify(routeData),
    });
  },

  async removeRouteStop(routeId) {
    return await this.request(`/admin/trains/routes/${routeId}`, {
      method: 'DELETE',
    });
  },

  async getAllFares() {
    return await this.request('/admin/fares');
  },

  async configureFare(fareData) {
    return await this.request('/admin/fares', {
      method: 'POST',
      body: JSON.stringify(fareData),
    });
  },

  async getAuditLogs() {
    return await this.request('/admin/audits');
  },

  async verifyTicket(pnrOrTicketId, comments = '') {
    return await this.request('/tickets/verify', {
      method: 'POST',
      body: JSON.stringify({ pnrOrTicketId, comments }),
    });
  },

  async getQRCode(text, size = 200) {
    return await this.request(`/tickets/qr?text=${encodeURIComponent(text)}&size=${size}`);
  }
};

window.Api = Api;
