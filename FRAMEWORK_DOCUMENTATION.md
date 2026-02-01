# REST Assured + Cucumber API Automation Framework
## Framework Documentation

---

## 📋 Table of Contents
1. [Framework Overview](#framework-overview)
2. [Architecture & Flow](#architecture--flow)
3. [Component Breakdown](#component-breakdown)
4. [How Everything Works Together](#how-everything-works-together)
5. [Running the Tests](#running-the-tests)
6. [Adding New Tests](#adding-new-tests)
7. [Configuration Guide](#configuration-guide)

---

## 🎯 Framework Overview

This is a **BDD (Behavior Driven Development)** API automation framework that combines:
- **REST Assured** - Java library for testing REST APIs
- **Cucumber** - BDD framework for writing tests in plain English
- **TestNG** - Test execution framework
- **JSON Schema Validation** - Response validation
- **Maven** - Build and dependency management

**Key Features:**
- ✅ Human-readable test scenarios
- ✅ Dynamic payload from JSON files
- ✅ Bearer token authentication
- ✅ Schema validation for API responses
- ✅ HTML/JSON/XML report generation
- ✅ Reusable step definitions

---

## 🏗️ Architecture & Flow

```
┌─────────────────────────────────────────────────────────────┐
│                    Test Execution Flow                      │
└─────────────────────────────────────────────────────────────┘

1. Maven Test Command (mvn test)
          ↓
2. TestNG Suite Execution (testng.xml)
          ↓
3. TestRunner.java picks up Feature Files
          ↓
4. Cucumber reads api_testing.feature
          ↓
5. Maps Gherkin steps to ApiSteps.java methods
          ↓
6. Loads JSON payload from task_payload.json
          ↓
7. Sends PUT request to API with Bearer Token
          ↓
8. Validates Response (Status Code + Schema)
          ↓
9. Generates Reports (HTML/JSON/XML)
```

---

## 📦 Component Breakdown

### 1️⃣ **Feature File** - The Test Script (Gherkin Language)
**Location:** `src/test/resources/features/api_testing.feature`

```gherkin
Feature: Test REST-API with Dynamic Payload for PUT Request

  Scenario Outline: Validate API Response for PUT Request with Dynamic Payload
    Given I have the API endpoint "<endpoint>"
    When I send a PUT request with the dynamic payload
    Then I should receive a response with status <status_code>
    And The response should match schema "<schema_file>"

  Examples:
    | endpoint       | status_code | schema_file              |
    | /update-task   | 200         | schema_update_task.json  |
```

**Explanation:**
- **Feature:** High-level description of what we're testing
- **Scenario Outline:** Template for test cases with multiple data sets
- **Given/When/Then/And:** BDD keywords that map to Java methods
- **Examples:** Data-driven testing - each row creates one test execution
- **`<endpoint>`**, **`<status_code>`**, **`<schema_file>`** are parameters passed to step definitions

**How it works:**
- Cucumber reads this file line by line
- Matches each step with Java methods using annotations (@Given, @When, @Then, @And)
- Replaces `<endpoint>` with `/update-task`, `<status_code>` with `200`, etc.

---

### 2️⃣ **Test Runner** - The Orchestrator
**Location:** `src/test/java/runners/TestRunner.java`

```java
@CucumberOptions(
    features = "src/test/resources/features",          // Where feature files are
    glue = "steps",                                    // Package containing step definitions
    plugin = {
        "pretty",                                      // Readable console output
        "html:target/cucumber-html-report.html",       // HTML Report
        "json:target/cucumber-reports/cucumber.json",  // JSON Report
        "junit:target/cucumber-reports/Cucumber.xml"   // JUnit XML Report
    },
    monochrome = true                                  // Clean console output
)
public class TestRunner extends AbstractTestNGCucumberTests {}
```

**Explanation:**
- **@CucumberOptions:** Configuration for Cucumber execution
- **features:** Directory path where `.feature` files are located
- **glue:** Package name where step definitions (Java methods) are
- **plugin:** Reporting formats to generate after test execution
- **monochrome:** Makes console output easier to read (removes colors/special chars)
- **extends AbstractTestNGCucumberTests:** Integrates Cucumber with TestNG

**Purpose:**
- Entry point for test execution
- Tells Cucumber where to find features and step definitions
- Configures report generation

---

### 3️⃣ **Step Definitions** - The Java Implementation
**Location:** `src/test/java/steps/ApiSteps.java`

#### **Class Variables**
```java
private String endpoint;           // Stores API endpoint like "/update-task"
private Response response;         // Stores API response for validation
private String payload;            // Stores JSON payload read from file

private static final String BASE_URL = "https://api.yourserver.com";
private static final String BEARER_TOKEN = "YOUR_BEARER_TOKEN_HERE";
```

#### **Step 1: Setup API Endpoint**
```java
@Given("I have the API endpoint {string}")
public void iHaveTheApiEndpoint(String endpoint) {
    this.endpoint = endpoint;              // Saves endpoint value
    RestAssured.baseURI = BASE_URL;        // Sets base URL for all requests
}
```

**What happens:**
- Cucumber matches `Given I have the API endpoint "/update-task"` from feature file
- Passes `"/update-task"` as the `endpoint` parameter
- Stores it in the instance variable for later use
- Sets REST Assured's base URI to your configured API URL

---

#### **Step 2: Send PUT Request**
```java
@When("I send a PUT request with the dynamic payload")
public void iSendAPutRequestWithTheDynamicPayload() throws IOException {
    // Read payload from external JSON file
    String filePath = "src/test/resources/testdata/task_payload.json";
    payload = new String(Files.readAllBytes(Paths.get(filePath)));

    // PUT Request with Bearer Token Authentication
    response = given()
        .header("Content-Type", "application/json")           // Set content type
        .header("Authorization", "Bearer " + BEARER_TOKEN)     // Add authentication
        .body(payload)                                         // Attach JSON payload
    .when()
        .put(endpoint);                                        // Send PUT request
}
```

**Detailed Flow:**
1. **Read JSON File:**
   - Locates `task_payload.json` file
   - Reads all bytes and converts to String
   - Stores in `payload` variable

2. **Build HTTP Request (REST Assured Syntax):**
   - `given()` - Start building the request
   - `.header("Content-Type", "application/json")` - Tell server we're sending JSON
   - `.header("Authorization", "Bearer " + BEARER_TOKEN)` - Add Bearer token for auth
   - `.body(payload)` - Attach the JSON payload as request body

3. **Execute Request:**
   - `.when()` - Transition keyword
   - `.put(endpoint)` - Sends HTTP PUT request to `BASE_URL + endpoint`
     - Example: `https://api.yourserver.com/update-task`

4. **Store Response:**
   - API response is automatically stored in `response` variable
   - Includes status code, headers, body, etc.

---

#### **Step 3: Validate Status Code**
```java
@Then("I should receive a response with status {int}")
public void iShouldReceiveAResponseWithStatus(int statusCode) {
    response.then()
        .statusCode(statusCode);           // Assert status code equals expected
}
```

**What happens:**
- Cucumber passes `200` (from feature file) as `statusCode` parameter
- REST Assured validates that actual response status code equals 200
- If not (e.g., 403, 404, 500), test fails with assertion error

---

#### **Step 4: Validate Response Schema**
```java
@And("The response should match schema {string}")
public void theResponseShouldMatchSchema(String schemaFile) {
    response.then()
        .assertThat()
        .body(matchesJsonSchemaInClasspath(schemaFile));
}
```

**What happens:**
- Cucumber passes `"schema_update_task.json"` as `schemaFile` parameter
- REST Assured reads the schema file from `src/test/resources/schemas/`
- Validates that response body structure matches the defined JSON schema
- Checks data types, required fields, formats, etc.

---

### 4️⃣ **Test Data - JSON Payload**
**Location:** `src/test/resources/testdata/task_payload.json`

```json
{
  "taskId": 60283,
  "taskDetail": "wefonwefwef",
  "taskDueDate": "2025-03-19T07:08:08",
  "assignToParentTransfereeId": 443234,
  "subTaskList": [],
  "documentList": null,
  "taskActiveInd": true,
  "coContactId": null,
  "createdBy": null,
  "createdByUserName": null,
  "updatedBy": null,
  "updatedByUserName": "abdultaite@aires.com",
  "userName": "abdultaite@aires.com"
}
```

**Purpose:**
- Stores request payload externally (not hardcoded)
- Easy to modify test data without changing code
- Can create multiple payload files for different scenarios
- Supports data-driven testing

---

### 5️⃣ **JSON Schema - Response Validation**
**Location:** `src/test/resources/schemas/schema_update_task.json`

```json
{  
  "$schema": "http://json-schema.org/draft-07/schema",  
  "type": "object",  
  "properties": {  
    "taskId": { "type": "integer" },  
    "taskDetail": { "type": "string" },  
    "taskDueDate": { "type": "string", "format": "date-time" },  
    "assignToParentTransfereeId": { "type": "integer" },  
    "subTaskList": { "type": "array" },  
    "documentList": { "type": ["array", "null"] },  
    "taskActiveInd": { "type": "boolean" },
    "userName": { "type": "string" }  
  },  
  "required": ["taskId", "taskDetail", "taskDueDate", "userName"]  
}
```

**Explanation:**
- **type:** Expected data type for each field
- **format:** Additional validation (e.g., date-time format)
- **type: ["array", "null"]:** Field can be array OR null
- **required:** Fields that MUST be present in response

**Purpose:**
- Validates response structure automatically
- Catches API contract violations
- Ensures data types are correct
- Verifies required fields are present

---

### 6️⃣ **Maven Configuration (pom.xml)**

**Key Dependencies:**
```xml
<!-- Cucumber BDD Framework -->
<dependency>
    <groupId>io.cucumber</groupId>
    <artifactId>cucumber-java</artifactId>
    <version>7.12.1</version>
</dependency>

<!-- REST Assured for API Testing -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.0</version>
</dependency>

<!-- JSON Schema Validator -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>json-schema-validator</artifactId>
    <version>5.3.0</version>
</dependency>

<!-- TestNG Test Runner -->
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <version>7.8.0</version>
</dependency>
```

---

## 🔄 How Everything Works Together

### Complete Test Execution Flow

```
Step 1: Developer runs → mvn test
          ↓
Step 2: Maven executes → testng.xml suite
          ↓
Step 3: TestNG triggers → TestRunner.java
          ↓
Step 4: Cucumber reads → api_testing.feature
          ↓
Step 5: For each scenario:
        ├─ "Given I have the API endpoint /update-task"
        │   └─ Calls: iHaveTheApiEndpoint("/update-task")
        │   └─ Sets: baseURI = "https://api.yourserver.com"
        │
        ├─ "When I send a PUT request with the dynamic payload"
        │   └─ Calls: iSendAPutRequestWithTheDynamicPayload()
        │   └─ Reads: task_payload.json
        │   └─ Sends: PUT https://api.yourserver.com/update-task
        │   └─ Headers: Content-Type, Authorization Bearer Token
        │   └─ Body: JSON payload
        │
        ├─ "Then I should receive a response with status 200"
        │   └─ Calls: iShouldReceiveAResponseWithStatus(200)
        │   └─ Validates: response.statusCode == 200
        │
        └─ "And The response should match schema schema_update_task.json"
            └─ Calls: theResponseShouldMatchSchema("schema_update_task.json")
            └─ Validates: response body against JSON schema
          ↓
Step 6: Test Pass/Fail
          ↓
Step 7: Reports Generated:
        ├─ target/cucumber-html-report.html
        ├─ target/cucumber-reports/cucumber.json
        └─ target/surefire-reports/
```

---

## 🚀 Running the Tests

### Prerequisites
- Java 17 installed
- Maven installed
- API credentials (Bearer Token)
- API base URL

### Configuration Steps
1. **Update API Details in ApiSteps.java:**
   ```java
   private static final String BASE_URL = "https://your-actual-api.com";
   private static final String BEARER_TOKEN = "your_actual_bearer_token";
   ```

2. **Update Test Data (if needed):**
   - Edit `src/test/resources/testdata/task_payload.json`

3. **Update Schema (if needed):**
   - Edit `src/test/resources/schemas/schema_update_task.json`

### Execution Commands

**Run all tests:**
```bash
mvn test
```

**Run with specific TestNG suite:**
```bash
mvn test -DsuiteXmlFile=testng.xml
```

**Run and generate reports:**
```bash
mvn clean test
```

**View Reports:**
- HTML Report: `target/cucumber-html-report.html`
- TestNG Report: `target/surefire-reports/index.html`
- JSON Report: `target/cucumber-reports/cucumber.json`

---

## ➕ Adding New Tests

### Example: Add POST Request Test

**1. Add Scenario to Feature File:**
```gherkin
Scenario: Create a new task via POST request
  Given I have the API endpoint "/create-task"
  When I send a POST request with the dynamic payload
  Then I should receive a response with status 201
  And The response should match schema "schema_create_task.json"
```

**2. Create Payload File:**
Create `src/test/resources/testdata/create_task_payload.json`

**3. Create Schema File:**
Create `src/test/resources/schemas/schema_create_task.json`

**4. Add Step Definition:**
```java
@When("I send a POST request with the dynamic payload")
public void iSendAPostRequestWithTheDynamicPayload() throws IOException {
    String filePath = "src/test/resources/testdata/create_task_payload.json";
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

## 🔧 Configuration Guide

### Environment-Specific Configuration

For multiple environments (DEV, QA, PROD), create separate property files:

**config.properties:**
```properties
base.url=https://api.yourserver.com
bearer.token=your_token_here
```

**Load in ApiSteps.java:**
```java
Properties prop = new Properties();
prop.load(new FileInputStream("config.properties"));
String BASE_URL = prop.getProperty("base.url");
String BEARER_TOKEN = prop.getProperty("bearer.token");
```

---

## 📊 Report Samples

### Console Output
```
Scenario Outline: Validate API Response for PUT Request with Dynamic Payload
  Given I have the API endpoint "/update-task"         ✅ PASSED
  When I send a PUT request with the dynamic payload   ✅ PASSED
  Then I should receive a response with status 200     ✅ PASSED
  And The response should match schema                 ✅ PASSED

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

---

## 🎓 Key Concepts Summary

| Component | Purpose | Technology |
|-----------|---------|------------|
| Feature Files | Test scenarios in plain English | Cucumber/Gherkin |
| Step Definitions | Java implementation of steps | Cucumber + Java |
| Test Runner | Execution configuration | TestNG + Cucumber |
| REST Assured | API request/response handling | REST Assured |
| JSON Schema | Response structure validation | JSON Schema Validator |
| Maven | Build & dependency management | Maven |
| TestNG | Test framework | TestNG |

---

## 🐛 Common Issues & Solutions

### Issue 1: 403 Forbidden Error
**Cause:** Missing or invalid Bearer token  
**Solution:** Update `BEARER_TOKEN` in ApiSteps.java

### Issue 2: Schema Validation Fails
**Cause:** Response structure doesn't match schema  
**Solution:** Update schema file to match actual API response

### Issue 3: File Not Found (task_payload.json)
**Cause:** Incorrect file path  
**Solution:** Verify file exists at `src/test/resources/testdata/`

---

## 📝 Best Practices

1. ✅ Keep feature files simple and readable
2. ✅ Use meaningful scenario names
3. ✅ Store test data externally (JSON files)
4. ✅ Use schema validation for all responses
5. ✅ One scenario per test case
6. ✅ Use Scenario Outline for data-driven tests
7. ✅ Keep step definitions reusable
8. ✅ Add proper error handling
9. ✅ Generate reports after every run
10. ✅ Use version control for schemas and test data

---

## 🤝 Team Collaboration Tips

- **QA:** Write feature files in Gherkin
- **Developers:** Implement step definitions
- **BA/PO:** Review feature files for business logic
- **All:** Update test data and schemas as API evolves

---

**Questions? Contact the Automation Team! 🚀**
