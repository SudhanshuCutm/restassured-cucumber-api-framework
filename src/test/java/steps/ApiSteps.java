package steps;

import io.restassured.RestAssured;
import io.cucumber.java.en.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class ApiSteps {
    private String endpoint;
    private Response response;
    private Map<String, Object> payload = new HashMap<>();

    @Given("I have the API endpoint {string}")
    public void iHaveTheApiEndpoint(String endpoint) {
        this.endpoint = endpoint;
        RestAssured.baseURI = "https://api.yourserver.com"; // Change this to match your API's base URL
    }

    @When("I send a PUT request with the dynamic payload")
    public void iSendAPutRequestWithTheDynamicPayload() {
        // Dynamic Payload
        payload.put("taskId", 60283);
        payload.put("taskDetail", "wefonwefwef");
        payload.put("taskDueDate", "2025-03-19T07:08:08");
        payload.put("assignToParentTransfereeId", 443234);
        payload.put("subTaskList", new Object[]{}); // Empty Array for subtasks
        payload.put("documentList", null); // Null for now
        payload.put("taskActiveInd", true);
        payload.put("coContactId", null);
        payload.put("createdBy", null);
        payload.put("createdByUserName", null);
        payload.put("updatedBy", null);
        payload.put("updatedByUserName", "abdultaite@aires.com");
        payload.put("userName", "abdultaite@aires.com");

        // PUT Request
        response = given()
            .header("Content-Type", "application/json") // Add any other required headers here
            .body(payload) // Add dynamic payload
        .when()
            .put(endpoint); // PUT method here
    }

    @Then("I should receive a response with status {int}")
    public void iShouldReceiveAResponseWithStatus(int statusCode) {
        // Verify Status Code
        response.then()
            .statusCode(statusCode);
    }

    @And("The response should match schema {string}")
    public void theResponseShouldMatchSchema(String schemaFile) {
        // Verify with JSON Schema
        response.then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath(schemaFile));
    }
}