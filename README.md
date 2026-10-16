# Reness POS

[![Build Windows Installer](https://github.com/Bahri232/floreantpos-code-r1966-trunk/actions/workflows/build-windows-installer.yml/badge.svg)](https://github.com/Bahri232/floreantpos-code-r1966-trunk/actions/workflows/build-windows-installer.yml)
[![Java 17](https://img.shields.io/badge/Java-17%20LTS-blue.svg)](https://adoptium.net/)
[![Platform](https://img.shields.io/badge/Platform-Windows%20x64-lightgrey.svg)](https://github.com/Bahri232/floreantpos-code-r1966-trunk/releases)
[![Release](https://img.shields.io/badge/Release-v1.5.0-brightgreen.svg)](https://github.com/Bahri232/floreantpos-code-r1966-trunk/releases)

**Reness POS** is a high-performance, standalone desktop Point-of-Sale application engineered for restaurants, bars, and retail environments. Upgraded to modern **Java 17 LTS**, it features automated database backups, high-DPI touchscreen scaling, single-instance window locking, low-latency G1 garbage collection, and hardened connection pooling.

---

## ✨ Key Features & Architectural Enhancements

### 🔒 Single-Instance Application Lock
- Prevents concurrent instances from launching when users click desktop shortcuts or taskbar icons multiple times.
- Detects existing running instances, displays an informative prompt (*"The application is already running"*), and automatically restores and brings the existing POS window to the front.
- Safe lock release mechanism handles normal exits and unexpected restarts cleanly without leaving dangling locks.

### ⚡ JVM & Memory Optimization (G1GC)
- Pre-configured low-latency G1 Garbage Collection profile (`-XX:+UseG1GC -XX:+ExplicitGCInvokesConcurrent -Xms256m -Xmx1024m`).
- Eliminates UI micro-stutters and freeze intervals during busy dinner rush hours and shift closes.
- Fully compatible with Java 17+ strong encapsulation rules via explicit JVM modular opening flags.

### 🖥️ Touchscreen & High-DPI Scaling
- Seamless High-DPI display scaling powered by FlatLaf (`flatlaf.uiScale=auto`).
- Touch-friendly 16px scrollbars and 32px table headers designed for POS touchscreen monitors and tablets.

### 💾 Automated Rolling Database Backups
- Non-blocking background backup service runs on startup once per calendar day.
- Generates compressed archives inside `database/backup/posdb-backup-YYYY-MM-DD.zip`.
- Implements an automated rolling 7-day retention policy to prune obsolete backups and conserve disk space.
- Clean Apache Derby database shutdown hook ensures zero database corruption upon application exit.

### 🌐 Resilient C3P0 Database Connection Pooling
- Built-in connection testing on checkout (`testConnectionOnCheckout=true`) automatically detects and discards stale/dropped database connections (e.g. overnight sleep or Wi-Fi blips).
- Proactive 60-second idle connection validation keeps multi-terminal MySQL and Derby network pools healthy.
- Three-attempt acquisition retry loop (`acquireRetryAttempts=3`) guarantees resilience against brief network jitter.

### 🛡️ Security Hardening
- Administrative password hashing upgraded to salted SHA-256 with transparent verification and automatic migration for existing configurations.
- Dynamic JDBC driver resolution supporting both modern MySQL 8 (`com.mysql.cj.jdbc.Driver`) and legacy connectors.

---

## 📦 System Requirements

- **Operating System**: Windows 10 / Windows 11 (64-bit)
- **Memory**: 4 GB RAM minimum (8 GB recommended)
- **Disk Space**: 500 MB free disk space
- **Runtime**: Bundled with self-contained JRE 17 inside the standalone installer (no separate Java installation needed for end users).

---

## 🚀 Installation & First-Time Setup

1. Download the latest installer (`RenessPOS_Setup_v1.5.0.exe`) from the [Releases](https://github.com/Bahri232/floreantpos-code-r1966-trunk/releases) page.
2. *(Optional - To verify publisher authenticity)*: Run `Trust_Publisher_Certificate.bat` as Administrator once to add the publisher certificate into the Windows Trusted Store.
3. Run `RenessPOS_Setup_v1.5.0.exe` and follow the setup wizard.
4. Launch **Reness POS** directly from the Desktop shortcut or Start Menu.

---

## 🛠️ Building from Source

### Prerequisites
- **JDK**: Eclipse Temurin or OpenJDK 17 LTS
- **Build Tool**: Apache Maven 3.8+
- **Inno Setup 6** (for building the Windows setup installer)

### Build Commands
```bash
# Clone repository
git clone https://github.com/Bahri232/floreantpos-code-r1966-trunk.git
cd floreantpos-code-r1966-trunk

# Compile and package application (Java 17 target)
mvn clean package -DskipTests
```

The compiled binaries will be output to:
- Standalone JAR: `target/floreantpos.jar`
- Packaged Distribution: `target/floreantpos-bin/floreantpos/`
- Windows Launcher: `target/floreantpos-bin/floreantpos/RenessPOS.exe`

---

## 🤖 CI/CD & Automated Release Pipeline

The project uses GitHub Actions with a fully modernized pipeline running on Node 24:
- **Builds**: Automatically builds, packages, and verifies cross-compilation on every push.
- **Installer Generation**: Bundles a stripped, lightweight Java 17 JRE with Inno Setup.
- **Authenticode Signing**: Automatically applies digital signatures using the verified publisher certificate.
- **Release Automation**: Pushing a version tag (e.g., `git push origin v1.5.0`) triggers an automated GitHub Release with attached installer executables, certificate, and release notes.

---

## 📄 License & Organization

- **Publisher**: Abdul Malik Barrie (BAHRI)
- **Contact**: [abdulmalikbarrie@gmail.com](mailto:abdulmalikbarrie@gmail.com)
- **Copyright**: Copyright &copy; 2026 Abdul Malik Barrie (BAHRI). All rights reserved.
