# Test Execution Commands

## 1. Run ALL Feature Files (Both)
```powershell
mvn clean test
```

## 2. Run Individual Feature File

### Run only user_api_demo.feature
```powershell
mvn test -Dcucumber.filter.name="JSONPlaceholder API"
```
OR
```powershell
mvn test -Dcucumber.features="src/test/resources/features/user_api_demo.feature"
```

### Run only api_testing.feature
```powershell
mvn test -Dcucumber.filter.name="Test REST-API"
```
OR
```powershell
mvn test -Dcucumber.features="src/test/resources/features/api_testing.feature"
```

## 3. Run Individual Scenario by Tag

### Run only @smoke scenarios
```powershell
mvn test -Dcucumber.filter.tags="@smoke"
```

### Run only @regression scenarios
```powershell
mvn test -Dcucumber.filter.tags="@regression"
```

### Run all @demo scenarios
```powershell
mvn test -Dcucumber.filter.tags="@demo"
```

## 4. Run Specific Scenario by Name

### Run "Get a specific post by ID"
```powershell
mvn test -Dcucumber.filter.name="Get a specific post by ID"
```

### Run "Create a new post"
```powershell
mvn test -Dcucumber.filter.name="Create a new post"
```

### Run "Update an existing post"
```powershell
mvn test -Dcucumber.filter.name="Update an existing post"
```

### Run "Validate API Response for PUT Request"
```powershell
mvn test -Dcucumber.filter.name="Validate API Response for PUT Request"
```

## 5. Run Multiple Tags (AND/OR logic)

### Run scenarios tagged with @demo AND @smoke
```powershell
mvn test -Dcucumber.filter.tags="@demo and @smoke"
```

### Run scenarios tagged with @smoke OR @regression
```powershell
mvn test -Dcucumber.filter.tags="@smoke or @regression"
```

## 6. Quick Run (Skip Clean)
```powershell
mvn test
```

---

## 📊 Allure Report Commands

### Generate and open Allure report (recommended)
```powershell
mvn clean test allure:serve
```

### Generate Allure report only (without opening browser)
```powershell
mvn clean test allure:report
```

### Open existing Allure report
```powershell
mvn allure:serve
```

### View Allure report manually
After running tests, the report HTML is in: `target/site/allure-maven-plugin/index.html`

### Clean Allure results
```powershell
Remove-Item -Recurse -Force target/allure-results, target/site/allure-maven-plugin
```

---

**Note:** All commands should be run from the project root directory:
`c:\API_Automation\restassured-cucumber-api-framework`
