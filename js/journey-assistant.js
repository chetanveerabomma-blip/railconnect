/**
 * RAILCONNECT — Dynamic Journey Assistant & Flexi-Fare Estimator
 *
 * Provides real-time journey calculations, weather alerts,
 * tatkal surge formulas, and interactive station notifications.
 */

const JourneyAssistant = {
  calculateFareBreakdown(distanceKm, coachType, trainType = 'EXPRESS', isTatkal = false) {
    const baseRates = {
      'VANDE_BHARAT': { 'CC': 1.45, 'EC': 2.65 },
      'RAJDHANI': { '3A': 1.45, '2A': 2.10, '1A': 3.10 },
      'EXPRESS': { 'SL': 0.45, '3A': 1.25, '2A': 1.80, '1A': 2.80, 'CC': 0.95 }
    };

    const typeRates = baseRates[trainType] || baseRates['EXPRESS'];
    const rate = typeRates[coachType] || 1.20;

    let baseFare = Math.max(120.0, distanceKm * rate);
    const reservationFee = coachType === '1A' || coachType === 'EC' ? 60.0 : 40.0;
    const superfastFee = trainType === 'VANDE_BHARAT' ? 75.0 : 45.0;
    let tatkalFee = 0.0;

    if (isTatkal) {
      tatkalFee = Math.max(100.0, baseFare * 0.30);
    }

    const subTotal = baseFare + reservationFee + superfastFee + tatkalFee;
    const gst = coachType === 'SL' ? 0.0 : subTotal * 0.05;
    const finalFare = Math.round((subTotal + gst) * 100.0) / 100.0;

    return {
      baseFare: Math.round(baseFare * 100) / 100,
      reservationFee,
      superfastFee,
      tatkalFee: Math.round(tatkalFee * 100) / 100,
      gst: Math.round(gst * 100) / 100,
      totalFare: finalFare,
      ratePerKm: rate
    };
  },

  formatTimeRemaining(targetTimeString) {
    if (!targetTimeString) return '--';
    const now = new Date();
    const [hours, minutes] = targetTimeString.split(':').map(Number);
    const target = new Date();
    target.setHours(hours, minutes, 0, 0);

    let diffMs = target - now;
    if (diffMs < 0) diffMs += 24 * 60 * 60 * 1000;

    const diffHours = Math.floor(diffMs / (1000 * 60 * 60));
    const diffMins = Math.floor((diffMs % (1000 * 60 * 60)) / (1000 * 60));
    return `${diffHours}h ${diffMins}m remaining`;
  }
};

window.JourneyAssistant = JourneyAssistant;
