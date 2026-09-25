# RAILCONNECT — Journey Weather Alert Module (`railconnect-weather`)

Real-time journey weather monitoring, rainfall warnings, and passenger travel advisories for **RailConnect**.

## 📌 Features
* **Journey Segment Weather Forecast**: Queries departure and arrival station meteorological conditions simultaneously.
* **Intelligent Severity Classification**: Automatic assignment into `NORMAL`, `CAUTION`, `WARNING`, and `SEVERE` status based on rain probability, gust velocity, and visibility parameters.
* **Air-Tight Security**: External API keys are kept exclusively on the server in `.env` variables and never leaked to frontend JavaScript.
* **Resilient Mock Fallback**: When external network weather providers are offline, seamlessly renders realistic Indian railway weather feeds with an explicit `"Demo Weather Data"` badge.

## 🛠️ REST API
```http
GET /api/weather/journey?from=TPJ&to=MAS&date=2026-10-10
```

### Sample Response:
```json
{
  "departureStation": "TPJ",
  "destinationStation": "MAS",
  "journeyDate": "2026-10-10",
  "departureWeather": {
    "stationCode": "TPJ",
    "stationName": "Tiruchchirappalli Junction (Trichy)",
    "temperatureC": 32.0,
    "condition": "Partly Cloudy",
    "rainProbability": 20,
    "severity": "NORMAL"
  },
  "destinationWeather": {
    "stationCode": "MAS",
    "stationName": "Chennai Central",
    "temperatureC": 29.0,
    "condition": "Heavy Rain",
    "rainProbability": 80,
    "severity": "WARNING"
  },
  "routeAdvisory": "WEATHER WARNING: Significant precipitation or heavy showers expected at Chennai Central. Please carry rain protection and plan local transit in advance.",
  "banner": "Demo Weather Data"
}
```
