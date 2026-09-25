# RAILCONNECT — Installation & Deployment Guide

This guide provides instructions for setting up and running RAILCONNECT in both **Zero-Setup Quick Evaluation Mode** and **Production MySQL Mode**.

---

## 1. System Prerequisites

Before running RAILCONNECT, ensure the following software is installed on your operating system:
* **Java Development Kit (JDK)**: Version 17 or higher (Java 17, 21, or 26 supported).
* **Apache Maven**: Version 3.8+ (included in PATH or available via wrapper).
* **Git**: Version 2.20+
* **Web Browser**: Google Chrome, Mozilla Firefox, or Microsoft Edge.
* **Web Server (Optional)**: Python 3 (`python -m http.server`) or Node.js (`npx serve`).

Verify prerequisites:
```bash
java -version
mvn -version
```

---

## 2. Zero-Setup Evaluation (Recommended)

In Zero-Setup mode, RAILCONNECT boots using an embedded in-memory H2 database configured in MySQL compatibility mode (`MODE=MySQL;DATABASE_TO_LOWER=TRUE`). Master stations, train routes, coach layouts, fare rules, and sample user accounts are automatically hydrated into memory upon startup via `DataLoader.java`. No manual database creation or configuration is required!

### Step 1: Clone / Open the Workspace
```bash
cd C:\Users\V CHETAN\.gemini\antigravity\scratch\railconnect-workspace
```

### Step 2: Build All Modular Repositories
Build and install all dependent modules into your local `.m2` repository:
```bash
cd railconnect-auth && mvn clean install -DskipTests && cd ..
cd railconnect-seat-engine && mvn clean install -DskipTests && cd ..
cd railconnect-weather && mvn clean install -DskipTests && cd ..
cd railconnect-booking && mvn clean install -DskipTests && cd ..
cd railconnect-berth-exchange && mvn clean install -DskipTests && cd ..
cd railconnect-admin && mvn clean install -DskipTests && cd ..
```

### Step 3: Launch the Central Spring Boot Backend
```bash
cd railconnect-backend
mvn spring-boot:run
```
The backend initializes on port `8080`. You will see the ASCII banner:
```
=============================================================
  🚆 RAILCONNECT SERVER INITIALIZED SUCCESSFULLY (Port 8080)
  Zero-Setup H2 MySQL-Mode Active. Master Data Hydrated.
=============================================================
```

### Step 4: Serve the Frontend Portal
In a separate terminal window:
```bash
cd railconnect-frontend
python -m http.server 3000
```
Open [http://localhost:3000](http://localhost:3000) in your web browser.

---

## 3. Production MySQL 8.0 Deployment (Optional)

For persistent enterprise deployment with an external MySQL server:

### Step 1: Create Database
Log into your local MySQL CLI:
```sql
CREATE DATABASE railconnect_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### Step 2: Import Schemas & Seed Data
Execute the SQL scripts from `railconnect-database`:
```bash
mysql -u root -p railconnect_db < railconnect-database/database/schema.sql
mysql -u root -p railconnect_db < railconnect-database/database/seed.sql
mysql -u root -p railconnect_db < railconnect-database/database/sample-data.sql
```

### Step 3: Run Backend with MySQL Profile
```bash
cd railconnect-backend
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```
Or set environment variables:
```bash
export DB_USERNAME=root
export DB_PASSWORD=your_mysql_password
```

---

## 4. Pre-Configured Test Credentials

| Role | Username | Password | Purpose |
|---|---|---|---|
| **Passenger** | `rahul_sharma` | `password123` | Train booking, interactive seat locking, pass download |
| **Passenger 2** | `priya_patel` | `password123` | Co-passenger berth exchange target |
| **Inspector (TTE)** | `inspector_anand` | `password123` | Digital QR pass onboard inspection terminal |
| **Admin** | `admin` | `password123` | Operations dashboard, train fleet & fare configuration |
