package runners;

import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "steps",
    plugin = {
        "pretty",
        "html:target/cucumber-html-report.html", // Generate HTML Report
        "json:target/cucumber-reports/cucumber.json", // Generate JSON Report
        "junit:target/cucumber-reports/Cucumber.xml" // Generate JUnit XML Report
    },
    monochrome = true // Better Console Output
)
public class TestRunner extends AbstractTestNGCucumberTests {}