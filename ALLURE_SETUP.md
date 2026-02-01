# Allure Report Setup & Usage Guide

## Overview
This framework is integrated with Allure for comprehensive test reporting with interactive dashboards, detailed test execution views, and rich visualizations.

## Integration Status
- Allure dependencies configured in pom.xml
- TestRunner configured with Allure plugin
- Test execution generates results in `target/allure-results`

## Test Results Summary
**Latest Test Execution:**
- **Passed:** 3 scenarios (Get post, Create post, Update post)
- **Failed:** 1 scenario (api_testing.feature - requires API server configuration)

## Installation

### Option 1: Using Scoop (Recommended for Windows)
```powershell
# Install Scoop
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
irm get.scoop.sh | iex

# Install Allure
scoop install allure
```

### Option 2: Using Chocolatey
```powershell
# Run as Administrator
choco install allure
```

### Option 3: Manual Installation
1. Download from: https://github.com/allure-framework/allure2/releases
2. Extract to `C:\allure`
3. Add `C:\allure\bin` to system PATH
4. Restart PowerShell

## Usage
```powershell
# Run tests and generate report
cd c:\API_Automation\restassured-cucumber-api-framework
mvn clean test

# View report in browser
allure serve target/allure-results
```

### Generate Static HTML Report
```powershell
# Generate report
allure generate target/allure-results -o target/allure-report --clean

# Open report
start target/allure-report/index.html
```

## Report Features

### Dashboard
- Test execution statistics (passed/failed/broken)
- Total execution time and success rate
- Historical trends across multiple runs

### Views
- **Behaviors:** BDD-style view (Features → Scenarios → Steps)
- **Suites:** Test organization by feature files
- **Graphs:** Visual test result representations
- **Timeline:** Execution timeline view
- **Categories:** Failure categorization

### Test Details
- Step-by-step execution status
- Request/Response details for API tests
- Attachments (logs, JSON payloads)
- Individual step execution timing
- Tags and categories

## Commands

### Run All Tests
```powershell
mvn clean test
```

### Run specific scenarios and view report
```powershell
# Run smoke tests only
mvn test -Dcucumber.filter.tags="@smoke"
allure serve target/allure-results
```

### Run and View in One Command
```powershell
mvn clean test; allure serve target/allure-results
```

## Report Locations
- **Allure Results:** `target/allure-results/` (raw data)
- **Allure HTML Report:** `target/allure-report/index.html` (after generate)
- **Cucumber HTML:** `target/cucumber-html-report.html`
- **TestNG Report:** `target/surefire-reports/index.html`

## Troubleshooting

### Command Not Found
Ensure Allure CLI is installed and in system PATH. Restart terminal after installation.

### No Results in Report
Run `mvn clean test` first to generate test results. Verify `target/allure-results/` contains JSON files.

### Old Data Displayed
Run `mvn clean test` to clear previous results or use `allure generate --clean` flag.

## Verified Commands

### Recommended Approach
```powershell
# Navigate to project
cd c:\API_Automation\restassured-cucumber-api-framework

# Run tests
mvn clean test

# View report (with PATH refresh if needed)
$env:Path = [System.Environment]::GetEnvironmentVariable("Path","User") + ";" + [System.Environment]::GetEnvironmentVariable("Path","Machine"); allure serve target/allure-results
```

### Single Command (PowerShell)
```powershell
mvn clean test; $env:Path = [System.Environment]::GetEnvironmentVariable("Path","User") + ";" + [System.Environment]::GetEnvironmentVariable("Path","Machine"); allure serve "C:\API_Automation\restassured-cucumber-api-framework\target\allure-results"
```
