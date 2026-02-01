# REST Assured + Cucumber API Automation Framework

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-blue.svg)](https://maven.apache.org/)
[![REST Assured](https://img.shields.io/badge/REST%20Assured-5.3.0-green.svg)](https://rest-assured.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber-7.12.1-brightgreen.svg)](https://cucumber.io/)
[![TestNG](https://img.shields.io/badge/TestNG-7.8.0-red.svg)](https://testng.org/)

A scalable and maintainable API automation framework using **REST Assured** and **Cucumber BDD** with dynamic JSON payload handling, Bearer token authentication, JSON schema validation, and comprehensive Allure reporting.

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Configuration](#configuration)
- [Running Tests](#running-tests)
- [Writing Tests](#writing-tests)
- [Test Reports](#test-reports)
- [CI/CD Integration](#cicd-integration)
- [Troubleshooting](#troubleshooting)
- [Best Practices](#best-practices)
- [Contributing](#contributing)
- [Contact](#contact)

---

## ✨ Features

- **BDD Approach** - Write tests in plain English using Cucumber Gherkin syntax
- **REST Assured** - Powerful Java library for API testing
- **Dynamic Payloads** - Load test data from external JSON files
- **Bearer Token Authentication** - Secure API authentication support
- **JSON Schema Validation** - Automated response structure validation
- **Allure Reports** - Interactive and comprehensive test reporting
- **TestNG Integration** - Robust test execution framework
- **Maven Build** - Easy dependency and build management
- **Reusable Components** - Modular step definitions
- **Tag-based Execution** - Run specific test suites using tags

---

## 🛠️ Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 17 | Programming Language |
| **Maven** | 3.6+ | Build & Dependency Management |
| **REST Assured** | 5.3.0 | API Testing Library |
| **Cucumber** | 7.12.1 | BDD Framework |
| **TestNG** | 7.8.0 | Test Execution Framework |
| **Allure** | 2.24.0 | Test Reporting |
| **JSON Schema Validator** | 5.3.0 | Response Validation |

---

## 📁 Project Structure

```
restassured-cucumber-api-framework/
│
├── src/
│   └── test/
│       ├── java/
│       │   ├── runners/
│       │   │   └── TestRunner.java          # Test execution entry point
│       │   └── steps/
│       │       └── ApiSteps.java            # Step definitions (Gherkin → Java mapping)
│       │
│       └── resources/
│           ├── features/
│           │   └── api_testing.feature      # Test scenarios in Gherkin
│           ├── testdata/
│           │   └── task_payload.json        # Request payloads
│           └── schemas/
│               └── schema_update_task.json  # Response validation schemas
│
├── target/
│   ├── cucumber-html-report.html            # HTML Test Report
│   ├── cucumber-reports/                    # JSON & XML Reports
│   └── surefire-reports/                    # TestNG Reports
│
├── pom.xml                                   # Maven dependencies & plugins
├── testng.xml                                # TestNG suite configuration
├── README.md                                 # This file
└── FRAMEWORK_DOCUMENTATION.md                # Detailed framework documentation

```

---

## 📦 Prerequisites

Before you begin, ensure you have the following installed:

### Required
- **Java JDK 17** or higher
  - [Download Java](https://www.oracle.com/java/technologies/downloads/)
  - Verify: `java -version`
  
- **Apache Maven 3.6+**
  - [Download Maven](https://maven.apache.org/download.cgi)
  - Verify: `mvn -version`

### Optional
- **IDE** - IntelliJ IDEA, Eclipse, or VS Code with Java extensions
- **Git** - For version control
- **Postman** - For manual API testing (optional)

---

## 🚀 Installation

### 1. Clone the Repository
```bash
git clone https://github.com/SudhanshuCutm/restassured-cucumber-api-framework.git
cd restassured-cucumber-api-framework
```

### 2. Install Dependencies
```bash
mvn clean install
```

### 3. Verify Installation
```bash
mvn test
```

---

## ⚙️ Configuration

### 1. Update API Base URL
Edit `src/test/java/steps/ApiSteps.java`:
```java
private static final String BASE_URL = "https://your-api-url.com";
```

### 2. Add Authentication Token
Edit `src/test/java/steps/ApiSteps.java`:
```java
private static final String BEARER_TOKEN = "your_actual_bearer_token_here";
```

### 3. Update Test Data (Optional)
Edit JSON payloads in `src/test/resources/testdata/`:
```json
{
  "taskId": 60283,
  "taskDetail": "Sample task",
  "taskDueDate": "2025-03-19T07:08:08",
  ...
}
```

### 4. Update JSON Schema (Optional)
Edit schemas in `src/test/resources/schemas/` to match your API response structure.

---

## 🎯 Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run with Specific TestNG Suite
```bash
mvn test -DsuiteXmlFile=testng.xml
```

### Run Specific Feature
```bash
mvn test -Dcucumber.options="src/test/resources/features/api_testing.feature"
```

### Run with Tags
```bash
mvn test -Dcucumber.filter.tags="@smoke"
```

### Generate Reports Only
```bash
mvn surefire-report:report
```

---

## ✍️ Writing Tests

### 1. Create Feature File
Create a new file in `src/test/resources/features/`:

```gherkin
Feature: User Management API

  Scenario: Create a new user
    Given I have the API endpoint "/users"
    When I send a POST request with the dynamic payload
    Then I should receive a response with status 201
    And The response should match schema "schema_user.json"
```

### 2. Create Test Data
Create payload in `src/test/resources/testdata/user_payload.json`:

```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "role": "admin"
}
```

### 3. Create JSON Schema
Create schema in `src/test/resources/schemas/schema_user.json`:

```json
{
  "$schema": "http://json-schema.org/draft-07/schema",
  "type": "object",
  "properties": {
    "id": { "type": "integer" },
    "name": { "type": "string" },
    "email": { "type": "string", "format": "email" }
  },
  "required": ["id", "name", "email"]
}
```

### 4. Add Step Definition (if needed)
Add method in `src/test/java/steps/ApiSteps.java`:

```java
@When("I send a POST request with the dynamic payload")
public void iSendAPostRequest() throws IOException {
    String filePath = "src/test/resources/testdata/user_payload.json";
    payload = new String(Files.readAllBytes(Paths.get(filePath)));
    
    response = given()
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + BEARER_TOKEN)
        .body(payload)
    .when()
        .post(endpoint);
}
```

---

## 📊 Test Reports

After test execution, reports are generated in multiple formats:

### HTML Report (Cucumber)
- **Location:** `target/cucumber-html-report.html`
- **Best for:** Quick visual overview
- **Open:** Double-click the file or open in browser

### TestNG HTML Report
- **Location:** `target/surefire-reports/index.html`
- **Best for:** Detailed test results with TestNG features
- **Open:** Double-click the file

### JSON Report
- **Location:** `target/cucumber-reports/cucumber.json`
- **Best for:** CI/CD integration, custom reporting tools

### XML Report
- **Location:** `target/cucumber-reports/Cucumber.xml`
- **Best for:** Jenkins/CI integration

### Sample Report View
```
Feature: Test REST-API with Dynamic Payload for PUT Request
  Scenario: Validate API Response for PUT Request
    ✅ Given I have the API endpoint "/update-task"
    ✅ When I send a PUT request with the dynamic payload
    ✅ Then I should receive a response with status 200
    ✅ And The response should match schema "schema_update_task.json"

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
Time elapsed: 3.5s
```

---

## 🔄 CI/CD Integration

### Jenkins Pipeline Example

```groovy
pipeline {
    agent any
    
    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/yourusername/restassured-cucumber-api-framework.git'
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }
        
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        
        stage('Reports') {
            steps {
                cucumber buildStatus: 'UNSTABLE',
                    reportTitle: 'API Test Report',
                    fileIncludePattern: '**/cucumber.json',
                    jsonReportDirectory: 'target/cucumber-reports'
            }
        }
    }
}
```

### GitHub Actions Example

```yaml
name: API Automation Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v2
    
    - name: Set up JDK 17
      uses: actions/setup-java@v2
      with:
        java-version: '17'
        distribution: 'adopt'
    
    - name: Run tests
      run: mvn clean test
    
    - name: Publish Test Report
      uses: EnricoMi/publish-unit-test-result-action@v2
      if: always()
      with:
        files: target/cucumber-reports/Cucumber.xml
```

---

## 🐛 Troubleshooting

### Common Issues & Solutions

#### Issue 1: 403 Forbidden Error
```
Expected status code <200> but was <403>
```
**Solution:**
- Update the `BEARER_TOKEN` in `ApiSteps.java` with a valid token
- Verify token hasn't expired
- Check if token has required permissions

#### Issue 2: Schema Validation Fails
```
Schema validation error: $.taskId: integer found, string expected
```
**Solution:**
- Update JSON schema to match actual API response
- Verify response structure with Postman
- Check data types in schema file

#### Issue 3: File Not Found
```
java.nio.file.NoSuchFileException: src/test/resources/testdata/task_payload.json
```
**Solution:**
- Verify file exists at specified path
- Check file name spelling
- Run `mvn clean` and rebuild

#### Issue 4: Maven Compilation Errors
```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin
```
**Solution:**
- Verify Java 17 is installed: `java -version`
- Update `JAVA_HOME` environment variable
- Run `mvn clean install -U`

#### Issue 5: Tests Not Running
**Solution:**
- Check TestNG suite file: `testng.xml`
- Verify TestRunner class extends `AbstractTestNGCucumberTests`
- Ensure feature files are in correct location

---

## 📚 Best Practices

1. ✅ **One Scenario per Feature** - Keep scenarios focused and independent
2. ✅ **Meaningful Names** - Use descriptive scenario and step names
3. ✅ **External Test Data** - Store payloads in JSON files, not hardcoded
4. ✅ **Schema Validation** - Always validate response structure
5. ✅ **Reusable Steps** - Write generic step definitions
6. ✅ **Use Tags** - Organize tests with `@smoke`, `@regression`, etc.
7. ✅ **Environment Variables** - Use config files for different environments
8. ✅ **Version Control** - Commit feature files, schemas, and test data
9. ✅ **Clean Reports** - Review and analyze test reports after each run
10. ✅ **Documentation** - Keep README and documentation up to date

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork the repository**
2. **Create a feature branch**
   ```bash
   git checkout -b feature/new-api-test
   ```
3. **Make your changes**
4. **Run tests**
   ```bash
   mvn clean test
   ```
5. **Commit your changes**
   ```bash
   git commit -m "Add: New API test for user creation"
   ```
6. **Push to branch**
   ```bash
   git push origin feature/new-api-test
   ```
7. **Open a Pull Request**

### Coding Standards
- Follow Java naming conventions
- Write clear Gherkin scenarios
- Add comments for complex logic
- Update documentation for new features

---

##  Contact

**Project Maintainer:** Sudhanshu Kumar Pradhan  
**Email:** skp.cutm@gmail.com  
**LinkedIn:** [Sudhanshu Pradhan](https://www.linkedin.com/in/sudhanshu-pradhan/)  
**GitHub:** [@SudhanshuCutm](https://github.com/SudhanshuCutm)

---

## 🙏 Acknowledgments

- [REST Assured](https://rest-assured.io/) - For the amazing API testing library
- [Cucumber](https://cucumber.io/) - For the BDD framework
- [TestNG](https://testng.org/) - For the test execution framework
- All contributors who help improve this framework

---

## 📖 Additional Resources

- [Complete Framework Documentation](FRAMEWORK_DOCUMENTATION.md)
- [REST Assured Documentation](https://rest-assured.io/)
- [Cucumber Documentation](https://cucumber.io/docs/cucumber/)
- [JSON Schema Validator](https://json-schema.org/)
- [Maven Guide](https://maven.apache.org/guides/)

---

**⭐ If you find this framework useful, please give it a star!**

---

*Last Updated: January 2026*
