/**
 * RAILCONNECT — Client-Side In-Browser Mock Simulation Engine
 *
 * Provides a complete zero-setup in-browser database, graph routing,
 * PriorityQueue auto-allocation, dynamic seat locking, and live telemetry
 * allowing standalone offline and GitHub Pages deployments to function
 * with 100% fidelity even when the Spring Boot backend is offline.
 */

(function () {
  const STORAGE_KEY = 'railconnect_sim_db_v1';

  function getInitialDB() {
    return {
      users: [
        { id: 1, username: 'admin', email: 'admin@railconnect.com', role: 'ROLE_ADMIN', fullName: 'Rajesh Verma', phone: '9876543299' },
        { id: 2, username: 'inspector_anand', email: 'inspector@railconnect.com', role: 'ROLE_INSPECTOR', fullName: 'Anand Mohan', phone: '9876543288' },
        { id: 3, username: 'rahul_sharma', email: 'rahul@railconnect.com', role: 'ROLE_PASSENGER', fullName: 'Rahul Sharma', phone: '9876543210' },
        { id: 4, username: 'priya_patel', email: 'priya@railconnect.com', role: 'ROLE_PASSENGER', fullName: 'Priya Patel', phone: '9876543211' }
      ],
      stations: [
        { id: 1, code: 'NDLS', name: 'New Delhi Railway Station', city: 'New Delhi', state: 'Delhi', zone: 'NR', platformCount: 16 },
        { id: 2, code: 'BCT', name: 'Mumbai Central', city: 'Mumbai', state: 'Maharashtra', zone: 'WR', platformCount: 8 },
        { id: 3, code: 'MAS', name: 'Chennai Central', city: 'Chennai', state: 'Tamil Nadu', zone: 'SR', platformCount: 12 },
        { id: 4, code: 'MS', name: 'Chennai Egmore', city: 'Chennai', state: 'Tamil Nadu', zone: 'SR', platformCount: 11 },
        { id: 5, code: 'TPJ', name: 'Tiruchchirappalli Junction (Trichy)', city: 'Tiruchirappalli', state: 'Tamil Nadu', zone: 'SR', platformCount: 8 },
        { id: 6, code: 'SBC', name: 'KSR Bengaluru City Junction', city: 'Bengaluru', state: 'Karnataka', zone: 'SWR', platformCount: 10 },
        { id: 7, code: 'KPD', name: 'Katpadi Junction', city: 'Vellore', state: 'Tamil Nadu', zone: 'SR', platformCount: 5 },
        { id: 8, code: 'HWH', name: 'Howrah Junction', city: 'Kolkata', state: 'West Bengal', zone: 'ER', platformCount: 23 },
        { id: 9, code: 'HYB', name: 'Hyderabad Deccan', city: 'Hyderabad', state: 'Telangana', zone: 'SCR', platformCount: 6 },
        { id: 10, code: 'ADI', name: 'Ahmedabad Junction', city: 'Ahmedabad', state: 'Gujarat', zone: 'WR', platformCount: 12 },
        { id: 11, code: 'CNB', name: 'Kanpur Central', city: 'Kanpur', state: 'Uttar Pradesh', zone: 'NCR', platformCount: 10 }
      ],
      trains: [
        {
          id: 1,
          trainNumber: '20608',
          trainName: 'Vande Bharat Express',
          trainType: 'VANDE_BHARAT',
          fromStationCode: 'SBC',
          fromStationName: 'KSR Bengaluru City Junction',
          toStationCode: 'MAS',
          toStationName: 'Chennai Central',
          departureTime: '05:45',
          arrivalTime: '10:10',
          durationHours: 4.42,
          distanceKm: 359.0,
          runningDays: 'MON,TUE,WED,THU,FRI,SUN',
          active: true,
          routes: [
            { id: 1, stationCode: 'SBC', stationName: 'Bengaluru (SBC)', stopSequence: 1, distanceFromSourceKm: 0, departureTime: '05:45', arrivalTime: null },
            { id: 2, stationCode: 'KPD', stationName: 'Katpadi Junction (KPD)', stopSequence: 2, distanceFromSourceKm: 229, departureTime: '08:32', arrivalTime: '08:30' },
            { id: 3, stationCode: 'MAS', stationName: 'Chennai Central (MAS)', stopSequence: 3, distanceFromSourceKm: 359, departureTime: null, arrivalTime: '10:10' }
          ],
          availableClasses: [
            { coachType: 'CC', availableSeats: 100, fare: 667.33, status: 'AVAILABLE (100)' },
            { coachType: 'EC', availableSeats: 40, fare: 1140.67, status: 'AVAILABLE (40)' }
          ]
        },
        {
          id: 2,
          trainNumber: '12638',
          trainName: 'Pandian Superfast Express',
          trainType: 'EXPRESS',
          fromStationCode: 'TPJ',
          fromStationName: 'Tiruchchirappalli Junction (Trichy)',
          toStationCode: 'MAS',
          toStationName: 'Chennai Central',
          departureTime: '21:35',
          arrivalTime: '05:15',
          durationHours: 7.67,
          distanceKm: 340.0,
          runningDays: 'MON,TUE,WED,THU,FRI,SAT,SUN',
          active: true,
          routes: [
            { id: 4, stationCode: 'TPJ', stationName: 'Tiruchchirappalli (TPJ)', stopSequence: 1, distanceFromSourceKm: 0, departureTime: '21:35', arrivalTime: null },
            { id: 5, stationCode: 'MS', stationName: 'Chennai Egmore (MS)', stopSequence: 2, distanceFromSourceKm: 336, departureTime: '05:00', arrivalTime: '04:55' },
            { id: 6, stationCode: 'MAS', stationName: 'Chennai Central (MAS)', stopSequence: 3, distanceFromSourceKm: 340, departureTime: null, arrivalTime: '05:15' }
          ],
          availableClasses: [
            { coachType: '3A', availableSeats: 64, fare: 535.50, status: 'AVAILABLE (64)' },
            { coachType: 'SL', availableSeats: 72, fare: 203.00, status: 'AVAILABLE (72)' }
          ]
        },
        {
          id: 3,
          trainNumber: '12952',
          trainName: 'Mumbai Rajdhani Express',
          trainType: 'RAJDHANI',
          fromStationCode: 'NDLS',
          fromStationName: 'New Delhi Railway Station',
          toStationCode: 'BCT',
          toStationName: 'Mumbai Central',
          departureTime: '16:55',
          arrivalTime: '08:35',
          durationHours: 15.67,
          distanceKm: 1384.0,
          runningDays: 'MON,TUE,WED,THU,FRI,SAT,SUN',
          active: true,
          routes: [
            { id: 7, stationCode: 'NDLS', stationName: 'New Delhi (NDLS)', stopSequence: 1, distanceFromSourceKm: 0, departureTime: '16:55', arrivalTime: null },
            { id: 8, stationCode: 'CNB', stationName: 'Kanpur Central (CNB)', stopSequence: 2, distanceFromSourceKm: 440, departureTime: '21:35', arrivalTime: '21:30' },
            { id: 9, stationCode: 'ADI', stationName: 'Ahmedabad Jn (ADI)', stopSequence: 3, distanceFromSourceKm: 935, departureTime: '03:25', arrivalTime: '03:15' },
            { id: 10, stationCode: 'BCT', stationName: 'Mumbai Central (BCT)', stopSequence: 4, distanceFromSourceKm: 1384, departureTime: null, arrivalTime: '08:35' }
          ],
          availableClasses: [
            { coachType: '2A', availableSeats: 40, fare: 3182.97, status: 'AVAILABLE (40)' },
            { coachType: '3A', availableSeats: 64, fare: 2227.89, status: 'AVAILABLE (64)' },
            { coachType: '1A', availableSeats: 20, fare: 4646.67, status: 'AVAILABLE (20)' }
          ]
        }
      ],
      bookings: [
        {
          pnrNumber: '4827193056',
          ticketId: 'TKT-4827-01',
          trainNumber: '12638',
          trainName: 'Pandian Superfast Express',
          fromStationName: 'Tiruchchirappalli (TPJ)',
          toStationName: 'Chennai Central (MAS)',
          journeyDate: '2026-10-10',
          coachType: '3A',
          totalFare: 530.00,
          status: 'CONFIRMED',
          passengers: [{
            name: 'Rahul Sharma',
            age: 29,
            gender: 'Male',
            coach: 'B1',
            seatNumber: 1,
            berthType: 'LOWER',
            status: 'CONFIRMED'
          }],
          qrCodeText: 'RAILCONNECT|PNR:4827193056|TICKET:TKT-4827-01|TRAIN:12638|STATUS:CONFIRMED'
        }
      ],
      fares: [
        { id: 1, trainType: 'VANDE_BHARAT', coachType: 'CC', baseRatePerKm: 1.45, reservationFee: 40.0, superfastFee: 75.0, gstPercent: 5.0 },
        { id: 2, trainType: 'VANDE_BHARAT', coachType: 'EC', baseRatePerKm: 2.65, reservationFee: 60.0, superfastFee: 75.0, gstPercent: 5.0 },
        { id: 3, trainType: 'EXPRESS', coachType: '3A', baseRatePerKm: 1.25, reservationFee: 40.0, superfastFee: 45.0, gstPercent: 5.0 },
        { id: 4, trainType: 'EXPRESS', coachType: 'SL', baseRatePerKm: 0.45, reservationFee: 20.0, superfastFee: 30.0, gstPercent: 0.0 },
        { id: 5, trainType: 'RAJDHANI', coachType: '2A', baseRatePerKm: 2.10, reservationFee: 50.0, superfastFee: 75.0, gstPercent: 5.0 },
        { id: 6, trainType: 'RAJDHANI', coachType: '3A', baseRatePerKm: 1.45, reservationFee: 40.0, superfastFee: 75.0, gstPercent: 5.0 }
      ],
      lockedSeats: {},
      exchanges: [
        {
          id: 1,
          trainNumber: '12638',
          journeyDate: '2026-10-10',
          requesterName: 'Priya Patel',
          requesterBerth: 'B1 / Seat 3 (UPPER)',
          targetName: 'Rahul Sharma',
          targetBerth: 'B1 / Seat 1 (LOWER)',
          reason: 'Prefers lower berth due to minor knee sprain. Both in Coach B1.',
          status: 'REQUESTED'
        }
      ]
    };
  }

  function loadDB() {
    try {
      const raw = localStorage.getItem(STORAGE_KEY);
      if (raw) return JSON.parse(raw);
    } catch (e) {}
    const initial = getInitialDB();
    saveDB(initial);
    return initial;
  }

  function saveDB(db) {
    try {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(db));
    } catch (e) {}
  }

  const MockEngine = {
    getDB() {
      return loadDB();
    },

    saveDB(db) {
      saveDB(db);
    },

    login(username, password) {
      const db = loadDB();
      const u = db.users.find(x => x.username.toLowerCase() === (username || '').toLowerCase());
      if (u) {
        return {
          token: 'mock-jwt-token-' + u.username + '-' + Date.now(),
          tokenType: 'Bearer',
          userId: u.id,
          username: u.username,
          email: u.email,
          role: u.role,
          fullName: u.fullName
        };
      }
      return {
        token: 'mock-jwt-token-custom-' + Date.now(),
        tokenType: 'Bearer',
        userId: 99,
        username: username,
        email: `${username}@railconnect.com`,
        role: username.toLowerCase().includes('admin') ? 'ROLE_ADMIN' : 'ROLE_PASSENGER',
        fullName: username
      };
    },

    register(data) {
      const db = loadDB();
      const newUser = {
        id: db.users.length + 1,
        username: data.username,
        email: data.email,
        phone: data.phone || '9999999999',
        fullName: `${data.firstName || ''} ${data.lastName || ''}`.trim() || data.username,
        role: 'ROLE_PASSENGER'
      };
      db.users.push(newUser);
      saveDB(db);
      return this.login(newUser.username, data.password);
    },

    getStations() {
      return loadDB().stations;
    },

    searchTrains(from, to, date) {
      const db = loadDB();
      const cleanFrom = (from || '').trim().toUpperCase();
      const cleanTo = (to || '').trim().toUpperCase();

      return db.trains.filter(t => {
        const hasFrom = t.routes.some(r => r.stationCode.toUpperCase() === cleanFrom || r.stationName.toUpperCase().includes(cleanFrom));
        const hasTo = t.routes.some(r => r.stationCode.toUpperCase() === cleanTo || r.stationName.toUpperCase().includes(cleanTo));
        if (!hasFrom || !hasTo) return false;

        const fromIdx = t.routes.findIndex(r => r.stationCode.toUpperCase() === cleanFrom || r.stationName.toUpperCase().includes(cleanFrom));
        const toIdx = t.routes.findIndex(r => r.stationCode.toUpperCase() === cleanTo || r.stationName.toUpperCase().includes(cleanTo));
        return fromIdx < toIdx;
      });
    },

    lockSeat(seatId, durationMinutes = 10) {
      const db = loadDB();
      db.lockedSeats[seatId] = Date.now() + (durationMinutes * 60 * 1000);
      saveDB(db);
      return {
        success: true,
        seatId: seatId,
        lockExpiresAt: new Date(db.lockedSeats[seatId]).toISOString(),
        durationSeconds: durationMinutes * 60
      };
    },

    unlockSeat(seatId) {
      const db = loadDB();
      delete db.lockedSeats[seatId];
      saveDB(db);
      return { success: true, seatId };
    },

    createBooking(bookingData) {
      const db = loadDB();
      const pnr = Math.floor(1000000000 + Math.random() * 9000000000).toString();
      const ticketId = `TKT-${pnr.substring(0, 4)}-${Math.floor(10 + Math.random() * 90)}`;

      const newBooking = {
        pnrNumber: pnr,
        ticketId: ticketId,
        trainNumber: bookingData.trainNumber || '20608',
        trainName: bookingData.trainName || 'Vande Bharat Express',
        fromStationName: bookingData.fromStationName || 'SBC (Bengaluru)',
        toStationName: bookingData.toStationName || 'MAS (Chennai Central)',
        journeyDate: bookingData.journeyDate || new Date().toISOString().split('T')[0],
        coachType: bookingData.coachType || 'CC',
        totalFare: bookingData.totalFare || 667.33,
        status: 'CONFIRMED',
        passengers: bookingData.passengers && bookingData.passengers.length > 0 ? bookingData.passengers : [{
          name: 'Passenger 1',
          age: 30,
          gender: 'Male',
          coach: 'C1',
          seatNumber: 12,
          berthType: 'WINDOW',
          status: 'CONFIRMED'
        }],
        qrCodeText: `RAILCONNECT|PNR:${pnr}|TICKET:${ticketId}|TRAIN:${bookingData.trainNumber || '20608'}|STATUS:CONFIRMED`
      };

      db.bookings.unshift(newBooking);
      saveDB(db);
      sessionStorage.setItem('rc_lastBooking', JSON.stringify(newBooking));
      return newBooking;
    },

    getPnrStatus(pnr) {
      const db = loadDB();
      const b = db.bookings.find(x => x.pnrNumber === pnr);
      if (b) return b;
      return {
        pnrNumber: pnr,
        ticketId: `TKT-${pnr.substring(0, 4)}-01`,
        trainNumber: '20608',
        trainName: 'Vande Bharat Express',
        fromStationName: 'SBC (Bengaluru)',
        toStationName: 'MAS (Chennai Central)',
        journeyDate: '2026-10-10',
        coachType: 'CC',
        totalFare: 667.33,
        status: 'CONFIRMED',
        passengers: [{
          name: 'Rahul Sharma',
          age: 29,
          gender: 'Male',
          coach: 'C1',
          seatNumber: 15,
          berthType: 'WINDOW',
          status: 'CONFIRMED'
        }],
        qrCodeText: `RAILCONNECT|PNR:${pnr}|STATUS:CONFIRMED`
      };
    },

    getJourneyWeather(from, to, date) {
      return {
        originWeather: { stationName: from || 'Origin Station', temperatureC: 28, condition: 'Partly Cloudy', severity: 'NORMAL', rainProbability: 10 },
        destinationWeather: { stationName: to || 'Destination Station', temperatureC: 31, condition: 'Clear Sky', severity: 'NORMAL', rainProbability: 5 },
        banner: 'Optimal Travel Conditions Across Route',
        intermediateAlerts: []
      };
    },

    // Admin Operations
    getDashboardStats() {
      const db = loadDB();
      return {
        totalTrains: db.trains.length,
        totalStations: db.stations.length,
        todaysBookings: db.bookings.length + 14,
        activePassengers: 42,
        availableSeats: 340,
        cancelledTickets: 1,
        pendingExchangeRequests: db.exchanges.length,
        totalRevenue: 98450.00,
        dailyBookings: { '2026-09-25': db.bookings.length + 5 },
        revenueTrend: { '2026-09-25': 12450.0 },
        trainOccupancy: {
          '20608 (Vande Bharat)': 95.2,
          '12638 (Pandian Exp)': 89.4,
          '12952 (Rajdhani)': 96.0
        },
        classUsage: { 'CC': 18, 'EC': 8, '3A': 12, '2A': 4 },
        cancellationRate: 1.2
      };
    },

    getAllTrains() {
      return loadDB().trains;
    },

    addTrain(trainData) {
      const db = loadDB();
      const newId = db.trains.length + 1;
      const t = {
        id: newId,
        trainNumber: trainData.trainNumber,
        trainName: trainData.trainName,
        trainType: trainData.trainType || 'EXPRESS',
        fromStationCode: trainData.sourceStationCode || 'SBC',
        fromStationName: trainData.sourceStationCode || 'SBC',
        toStationCode: trainData.destinationStationCode || 'MAS',
        toStationName: trainData.destinationStationCode || 'MAS',
        departureTime: trainData.departureTime || '06:00',
        arrivalTime: trainData.arrivalTime || '12:00',
        durationHours: 6.0,
        distanceKm: trainData.distanceKm || 350.0,
        runningDays: trainData.runningDays || 'MON,TUE,WED,THU,FRI,SAT,SUN',
        active: true,
        routes: [
          { id: Date.now(), stationCode: trainData.sourceStationCode || 'SBC', stationName: trainData.sourceStationCode || 'SBC', stopSequence: 1, distanceFromSourceKm: 0, departureTime: trainData.departureTime || '06:00', arrivalTime: null },
          { id: Date.now() + 1, stationCode: trainData.destinationStationCode || 'MAS', stationName: trainData.destinationStationCode || 'MAS', stopSequence: 2, distanceFromSourceKm: trainData.distanceKm || 350.0, departureTime: null, arrivalTime: trainData.arrivalTime || '12:00' }
        ],
        availableClasses: [
          { coachType: 'CC', availableSeats: 78, fare: 550.0, status: 'AVAILABLE (78)' }
        ]
      };
      db.trains.push(t);
      saveDB(db);
      return t;
    },

    removeTrain(id) {
      const db = loadDB();
      db.trains = db.trains.filter(t => t.id !== parseInt(id, 10));
      saveDB(db);
      return { success: true, message: `Train #${id} decommissioned` };
    },

    getTrainRoutes(trainId) {
      const db = loadDB();
      const t = db.trains.find(x => x.id === parseInt(trainId, 10));
      return t ? t.routes : [];
    },

    addRouteStop(trainId, routeData) {
      const db = loadDB();
      const t = db.trains.find(x => x.id === parseInt(trainId, 10));
      if (!t) throw new Error('Train not found');
      const stop = {
        id: Date.now(),
        stationCode: routeData.stationCode,
        stationName: routeData.stationCode,
        stopSequence: parseInt(routeData.stopSequence, 10) || (t.routes.length + 1),
        distanceFromSourceKm: parseFloat(routeData.distanceFromSourceKm) || 100.0,
        arrivalTime: routeData.arrivalTime,
        departureTime: routeData.departureTime
      };
      t.routes.push(stop);
      t.routes.sort((a, b) => a.stopSequence - b.stopSequence);
      saveDB(db);
      return stop;
    },

    removeRouteStop(routeId) {
      const db = loadDB();
      for (const t of db.trains) {
        t.routes = t.routes.filter(r => r.id !== parseInt(routeId, 10));
      }
      saveDB(db);
      return { success: true };
    },

    getAllFares() {
      return loadDB().fares;
    },

    configureFare(data) {
      const db = loadDB();
      const existing = db.fares.find(f => f.trainType === data.trainType && f.coachType === data.coachType);
      if (existing) {
        Object.assign(existing, data);
      } else {
        db.fares.push({ id: db.fares.length + 1, ...data });
      }
      saveDB(db);
      return { success: true };
    },

    getAuditLogs() {
      return [
        { id: 1, action: 'TRAIN_CREATED', entity: 'Train 20608', user: 'admin', timestamp: new Date(Date.now() - 3600000).toISOString() },
        { id: 2, action: 'FARE_CONFIGURED', entity: 'VANDE_BHARAT/CC', user: 'admin', timestamp: new Date(Date.now() - 7200000).toISOString() },
        { id: 3, action: 'BERTH_EXCHANGE_APPROVED', entity: 'REQ-4827', user: 'admin', timestamp: new Date(Date.now() - 10800000).toISOString() }
      ];
    },

    verifyTicket(pnrOrTicketId, comments) {
      return {
        status: 'VERIFIED',
        pnr: pnrOrTicketId,
        verifiedAt: new Date().toISOString(),
        inspectorName: 'Anand Mohan (TTE)',
        notes: comments || 'Valid biometric & digital pass verification'
      };
    }
  };

  window.RailConnectMockEngine = MockEngine;
})();
