# Trust Wallet Appium Automation Framework

A robust Java-based mobile automation framework leveraging Appium for comprehensive testing of Trust Wallet's Android application, with primary focus on the "Create Wallet" user journey.

## Architecture Overview

```
.
├── pom.xml                # Maven dependency management
├── testng.xml             # Test suite configuration
├── run-tests.sh           # Test execution orchestration
├── apps/                  # Application binaries
├── src/
│   ├── main/java/com/trustwallet/
│   │   ├── config/        # Environment configurations
│   │   ├── pages/         # Page Object Models
│   │   └── utils/         # Testing utilities
│   └── test/java/com/trustwallet/
│       └── tests/         # Test implementations
├── manual-test-cases/     # Test documentation
│   ├── create-wallet/
│   │   ├── happy-path.md
│   │   ├── edge-cases.md
│   │   └── non-functional.md
│   └── test-strategy.md   # Testing methodology
```

## Technical Requirements

1. Java 11+
2. Maven 3.6+
3. Android SDK with platform-tools
4. Appium 2.0+
5. Android device or emulator

## Environment Setup

### 1. Appium Configuration

```bash
npm install -g appium
appium driver install uiautomator2
```

### 2. Server Initialization

```bash
appium -p 4725 -a 127.0.0.1 -pa /wd/hub
```

### 3. Environment Configuration

The framework supports multiple testing environments through dedicated property files:

- `src/main/java/com/trustwallet/config/environments/dev.properties`
- `src/main/java/com/trustwallet/config/environments/staging.properties`
- `src/main/java/com/trustwallet/config/environments/prod.properties`

## Test Execution

```bash
# Execute the full test suite
mvn clean test

# Target specific environment
mvn clean test -Denv=dev
```

## Reporting

The framework integrates Allure for comprehensive test reporting:

```bash
# Generate detailed test reports
mvn allure:report

# Launch report dashboard
mvn allure:serve
```

## Framework Features

1. **Page Object Model Architecture**: Ensures maintainable, scalable test code with clear separation of concerns
2. **Environment-Specific Configurations**: Supports testing across development, staging, and production environments
3. **Comprehensive Test Documentation**: Includes detailed test cases covering happy paths, edge cases, and non-functional requirements
4. **Robust Test Strategy**: Implements a methodical approach to mobile application testing
5. **Advanced Reporting**: Provides detailed test execution reports with Allure integration

## Troubleshooting

If encountering driver-related errors:

```
Error: Could not find a driver for automationName 'UiAutomator2' and platformName 'android'
```

Install the required Appium driver:

```bash
appium driver install uiautomator2
```

Note: The `target` directory contains build artifacts generated during Maven execution and can be safely removed when needed.
