# RAILCONNECT — Authentication & Authorization Module (`railconnect-auth`)

Robust Role-Based Access Control (RBAC), user authentication, password hashing, and stateless JSON Web Token (JWT) management for **RailConnect**.

## 📌 Features
- **Stateless JWT Security**: Issues cryptographically signed HMAC-SHA256 JWT tokens.
- **BCrypt Password Hashing**: Adaptive work factor preventing brute-force attacks.
- **Three-Tier Role Model**:
  - `ROLE_PASSENGER`: Access booking, seat selection, digital passes, and berth exchange.
  - `ROLE_INSPECTOR`: Authorized to verify digital passes and onboard tickets.
  - `ROLE_ADMIN`: Complete access to railway configuration, trains, fares, and audits.
- **CORS & CSRF Shield**: Pre-configured CORS support for SPA frontends with stateless session policy.

## 🛠️ Endpoints
* `POST /api/auth/register` — Register a new passenger account.
* `POST /api/auth/login` — Authenticate credentials and receive Bearer JWT.
* `GET /api/auth/me` — Retrieve active authenticated user profile.
* `POST /api/auth/logout` — Stateless session invalidation.

## 🔒 Security Configuration
```java
.requestMatchers("/api/admin/**").hasRole("ADMIN")
.requestMatchers("/api/tickets/verify/**").hasAnyRole("ADMIN", "INSPECTOR")
.requestMatchers("/api/bookings/**").authenticated()
.requestMatchers("/api/berth-exchange/**").authenticated()
```
