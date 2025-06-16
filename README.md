<!-- ProFAN README -->
# ProFAN — Fan Testing and Calibration System

![Java](https://img.shields.io/badge/Java-8%2B-blue)
![JavaFX](https://img.shields.io/badge/JavaFX-8%2B-green)
![Version](https://img.shields.io/badge/Version-0.0.0.4-orange)

A comprehensive JavaFX application for configuring, executing, and analyzing fan performance tests.

---

## 📋 Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Getting Started](#getting-started)
   - [Prerequisites](#prerequisites)
   - [Installation](#installation)
   - [Configuration](#configuration)
5. [Usage](#usage)
6. [Controller Implementation](#controller-implementation)
7. [Development](#development)
8. [Contributing](#contributing)
9. [License](#license)
10. [Support](#support)

---

## 🔍 Overview
ProFAN is a robust fan testing and calibration system designed to validate fan performance across multiple parameters. With a user-friendly JavaFX interface, ProFAN supports model-based configurations, sequential and step-by-step test execution, and comprehensive data analysis.

---

## 🛠️ Features
- **Test Setup Management**: create, save, load, and reuse configuration presets
- **Execution Modes**: sequential, step-by-step, pause, resume, skip
- **Real-time Measurements**: voltage, current, power, RPM, wind speed
- **Automatic Control**: voltage setting & verification, reporting
- **Data Management**: MySQL integration, history browsing, CSV/PDF export

---

## 🏗️ Architecture
- **FanProjectSetupController**: handles model & test point configuration
- **FanProjectExecuteController**: manages test flow, measurement, and logging
- **DeviceDataManagerController**: abstracts serial/device communication
- **Services**: DAO layers for ProjectRun and Result entities (Hibernate + Spring)

---

## 🚀 Getting Started

### Prerequisites
- Java JRE 8 or newer (OpenJDK 11+ with JavaFX modules supported)
- MySQL 5.7+ or compatible
- Windows 10 / Linux / macOS
- 4 GB RAM minimum

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/SuryaPrasathB/ProFAN.git
   ```
2. Build with Maven or Gradle:
   ```bash
   mvn clean install
   ```
3. Configure `app_config_fan_v1_6.json` (see [Configuration](#configuration)).
4. Run the application:
   ```bash
   java -jar target/ProFAN-0.0.0.4.jar
   ```

### Configuration
Edit `app_config_fan_v1_6.json` to set:
```json
{
  "database": {
    "url": "jdbc:mysql://localhost:3306/fan_test_db",
    "username": "root",
    "password": "your_password"
  },
  "serial": {
    "port": "COM3",
    "baudRate": 9600
  },
  "testDefaults": {
    "voltageTolerance": 2.5,
    "retryLimit": 3
  }
}
```

---

## 💡 Usage

### Step-by-Step
1. **Test Setup**
   - Launch application
   - Select fan model
   - Add/modify test points (voltage, RPM, wind speed)
   - Define thresholds and limits
   - Save configuration

2. **Test Execution**
   - Mode: **Start** (full run) or **Step Run** (single test point)
   - **Resume** and **Stop** controls available
   - Live data displayed with progress indicators

3. **Data Management**
   - Results stored in MySQL under `ProjectRun` and `Result` tables
   - History panel: browse by model, serial number, or date
   - Export to CSV/PDF for reporting

---

## ⚙️ Controller Implementation
The core execution logic resides in `FanProjectExecuteController.java`:

- **Initialization**: sets up FXML bindings, table views, cell factories, and event listeners.
- **Execution Flow**:
  - `btnTestPointStartOnClick()`: validates input, creates or reuses `ProjectRun`, and schedules `runTestPointsSequentially()`.
  - `runTestPointsSequentially()`: iterates through `FanTestSetup` list, supports jumps and resume.
  - `runSingleTestPoint()`: sets voltage, verifies within tolerance, waits for stabilization, collects measurements, updates UI, and saves `Result` via `DeviceDataManagerController` services.
- **Concurrency**: uses JavaFX `Platform.runLater`, `Task`, and `ScheduledExecutorService` for auto-updates.
- **Logging**: appends to `ListView<LogEntry>` with color-coded levels (INFO, DEBUG, ERROR).

All public methods include descriptive Javadoc comments as per code style guidelines.

---

## 🛠️ Development
- **Language**: Java 8+ with JavaFX 8+
- **Build**: Maven (preferred) or Gradle
- **ORM**: Hibernate via Spring Data JPA
- **UI**: JavaFX FXML
- **Dependency Injection**: Spring Framework

**Code Style**:
- All functions must have a block Javadoc comment (`/** … */`) explaining purpose, params, and returns.
- Follow standard Java naming conventions and modular packaging.

---

## 🤝 Contributing
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/my-feature`
3. Commit changes with clear messages
4. Open a Pull Request against `main`
5. Ensure all tests pass and code adheres to style

---

## 📄 License
Apache License 2.0. See [LICENSE](LICENSE) for details.

---

## 📣 Support
For issues or feature requests, contact LS Control Systems at `support@lscontrol.com`.
