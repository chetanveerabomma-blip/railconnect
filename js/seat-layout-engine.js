/**
 * RAILCONNECT — Interactive Coach Seat Map & Lock Countdown Engine
 *
 * Dynamically visualizes coach layouts (CC, EC, 3A, 2A, 1A, SL) with
 * real-time click-to-lock, 10-minute countdown badges, and proximity grouping.
 */

const SeatLayoutEngine = {
  activeLocks: {},

  renderCoach(containerId, coachType, bookedSeatIds = [], onSeatClick) {
    const container = document.getElementById(containerId);
    if (!container) return;

    let layout = '';
    if (coachType === 'CC' || coachType === 'EC') {
      layout = this.generateChairCarHTML(coachType, bookedSeatIds);
    } else {
      layout = this.generateSleeperHTML(coachType, bookedSeatIds);
    }

    container.innerHTML = layout;

    // Attach click handlers
    container.querySelectorAll('.interactive-seat:not(.seat-booked)').forEach(el => {
      el.addEventListener('click', (e) => {
        const seatNum = parseInt(el.dataset.seatNumber, 10);
        const berthType = el.dataset.berthType;
        const isSelected = el.classList.toggle('seat-selected');
        if (onSeatClick) {
          onSeatClick({ seatNumber: seatNum, berthType, selected: isSelected, element: el });
        }
      });
    });
  },

  generateChairCarHTML(coachType, bookedSeatIds) {
    const totalSeats = coachType === 'EC' ? 40 : 75;
    const cols = coachType === 'EC' ? 4 : 5; // EC 2x2, CC 3x2
    let html = `<div style="text-align:center; margin-bottom:16px;">
      <span class="badge ${coachType === 'EC' ? 'badge-vande-bharat' : 'badge-primary'}">${coachType} Executive Layout</span>
      <div style="font-size:0.8rem; color:var(--text-muted); margin-top:4px;">Direction of Movement ➔ (180° Rotatable Chairs)</div>
    </div><div style="display:grid; grid-template-columns: repeat(${cols}, 1fr); gap:10px; max-width:480px; margin:0 auto; padding:16px; background:var(--bg-subtle); border-radius:var(--radius-lg); border:1px solid var(--border);">`;

    const types = coachType === 'EC' ? ['WINDOW', 'AISLE', 'AISLE', 'WINDOW'] : ['WINDOW', 'MIDDLE', 'AISLE', 'AISLE', 'WINDOW'];

    for (let i = 1; i <= totalSeats; i++) {
      const bType = types[(i - 1) % cols];
      const isBooked = bookedSeatIds.includes(i);
      const isAisleGap = coachType === 'CC' && (i % cols === 3);

      html += `
        <div class="interactive-seat ${isBooked ? 'seat-booked' : 'seat-available'}" 
             data-seat-number="${i}" 
             data-berth-type="${bType}"
             style="padding:10px 4px; text-align:center; border-radius:6px; border:1px solid ${isBooked ? '#CBD5E1' : '#3B82F6'}; background:${isBooked ? '#E2E8F0' : '#EFF6FF'}; cursor:${isBooked ? 'not-allowed' : 'pointer'}; transition:all 0.2s; position:relative;">
          <div style="font-weight:800; font-size:0.9rem; color:${isBooked ? '#94A3B8' : '#1E3A8A'};">${i}</div>
          <div style="font-size:0.65rem; color:${isBooked ? '#94A3B8' : '#64748B'}; font-weight:700;">${bType.substring(0, 3)}</div>
        </div>
      `;
    }
    html += '</div>';
    return html;
  },

  generateSleeperHTML(coachType, bookedSeatIds) {
    const totalSeats = coachType === '3A' ? 64 : 72;
    const types = ['LOWER', 'MIDDLE', 'UPPER', 'LOWER', 'MIDDLE', 'UPPER', 'SIDE LOWER', 'SIDE UPPER'];
    let html = `<div style="text-align:center; margin-bottom:16px;">
      <span class="badge badge-info">${coachType} Traditional Indian Rail Sleeper Layout</span>
      <div style="font-size:0.8rem; color:var(--text-muted); margin-top:4px;">Arranged in 8-berth contiguous passenger bays</div>
    </div><div style="display:grid; grid-template-columns: repeat(4, 1fr) 20px repeat(2, 1fr); gap:8px; max-width:620px; margin:0 auto; padding:16px; background:var(--bg-subtle); border-radius:var(--radius-lg); border:1px solid var(--border);">`;

    for (let i = 1; i <= totalSeats; i++) {
      const bType = types[(i - 1) % 8];
      const isBooked = bookedSeatIds.includes(i);

      if ((i - 1) % 8 === 6) {
        html += `<div style="grid-column: span 1; pointer-events:none;"></div>`; // Gangway aisle gap
      }

      html += `
        <div class="interactive-seat ${isBooked ? 'seat-booked' : 'seat-available'}" 
             data-seat-number="${i}" 
             data-berth-type="${bType}"
             style="padding:10px 4px; text-align:center; border-radius:6px; border:1px solid ${isBooked ? '#CBD5E1' : '#0D9488'}; background:${isBooked ? '#E2E8F0' : '#F0FDFA'}; cursor:${isBooked ? 'not-allowed' : 'pointer'}; transition:all 0.2s;">
          <div style="font-weight:800; font-size:0.9rem; color:${isBooked ? '#94A3B8' : '#0F766E'};">${i}</div>
          <div style="font-size:0.65rem; color:${isBooked ? '#94A3B8' : '#0D9488'}; font-weight:700;">${bType.substring(0, 3)}</div>
        </div>
      `;
    }
    html += '</div>';
    return html;
  },

  startLockTimer(displayElementId, durationSeconds = 600, onExpire) {
    let timeLeft = durationSeconds;
    const el = document.getElementById(displayElementId);
    if (!el) return null;

    const timer = setInterval(() => {
      timeLeft--;
      if (timeLeft <= 0) {
        clearInterval(timer);
        el.textContent = '00:00 (EXPIRED)';
        el.classList.add('badge-danger');
        if (onExpire) onExpire();
      } else {
        const mins = Math.floor(timeLeft / 60);
        const secs = timeLeft % 60;
        el.textContent = `🔒 Locked: ${mins.toString().padStart(2, '0')}:${secs.toString().padStart(2, '0')}`;
      }
    }, 1000);

    return timer;
  }
};

window.SeatLayoutEngine = SeatLayoutEngine;
