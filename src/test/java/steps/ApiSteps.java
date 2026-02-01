package steps;

import io.restassured.RestAssured;
import io.cucumber.java.en.*;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import io.restassured.response.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ApiSteps {
    private String endpoint;
    private Response response;
    private String payload;
    
    private static final String BASE_URL = "https://api.server.com";
    private static final String BEARER_TOKEN = "BEARER_TOKEN_HERE";
    
    private static final String DEMO_API_BASE_URL = "https://jsonplaceholder.typicode.com";
    private static final String DEMO_BEARER_TOKEN = "demo_token_12345_for_client_presentation";

    @Given("I have the API endpoint {string}")
    public void iHaveTheApiEndpoint(String endpoint) {
        this.endpoint = endpoint;
        RestAssured.baseURI = BASE_URL;
    }
    
    @Given("I have the demo API endpoint {string}")
    public void iHaveTheDemoApiEndpoint(String endpoint) {
        this.endpoint = endpoint;
        RestAssured.baseURI = DEMO_API_BASE_URL;
    }

    @When("I send a PUT request with the dynamic payload")
    public void iSendAPutRequestWithTheDynamicPayload() throws IOException {
        String filePath = "src/test/resources/testdata/task_payload.json";
        payload = new String(Files.readAllBytes(Paths.get(filePath)));

        response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + BEARER_TOKEN)
            .body(payload)
        .when()
            .put(endpoint);
    }

    @Then("I should receive a response with status {int}")
    public void iShouldReceiveAResponseWithStatus(int statusCode) {
        response.then()
            .statusCode(statusCode);
    }

    @And("The response should match schema {string}")
    public void theResponseShouldMatchSchema(String schemaFile) {
        response.then()
            .assertThat()
            .body(matchesJsonSchemaInClasspath("schemas/" + schemaFile));
    }
    
    @When("I send a GET request")
    public void iSendAGetRequest() {
        response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + DEMO_BEARER_TOKEN)
        .when()
            .get(endpoint);
    }
    
    @When("I send a POST request with payload {string}")
    public void iSendAPostRequestWithPayload(String payloadFile) throws IOException {
        String filePath = "src/test/resources/testdata/" + payloadFile;
        payload = new String(Files.readAllBytes(Paths.get(filePath)));
        
        response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + DEMO_BEARER_TOKEN)
            .body(payload)
        .when()
            .post(endpoint);
    }
    
    @When("I send a PUT request with payload {string}")
    public void iSendAPutRequestWithPayload(String payloadFile) throws IOException {
        String filePath = "src/test/resources/testdata/" + payloadFile;
        payload = new String(Files.readAllBytes(Paths.get(filePath)));
        
        response = given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + DEMO_BEARER_TOKEN)
            .body(payload)
        .when()
            .put(endpoint);
    }
    
    @And("The response should contain field {string} with value {string}")
    public void theResponseShouldContainFieldWithValue(String fieldName, String expectedValue) {
        try {
            int intValue = Integer.parseInt(expectedValue);
            response.then()
                .body(fieldName, anyOf(
                    equalTo(expectedValue),
                    equalTo(intValue)
                ));
        } catch (NumberFormatException e) {
            response.then()
                .body(fieldName, equalTo(expectedValue));
        }
    }
    
    @And("The response should contain field {string}")
    public void theResponseShouldContainField(String fieldName) {
        response.then()
            .body(fieldName, notNullValue());
    }
    
    /*
     * Real authentication method for production APIs.
     * Uncomment and configure when integrating with actual authentication endpoint.
     * 
     * @Given("I authenticate with username {string} and password {string}")
     * public void iAuthenticateWithUsernameAndPassword(String username, String password) throws IOException {
     *     String loginPayload = String.format(
     *         "{\"username\": \"%s\", \"password\": \"%s\"}",
     *         username,
     *         password
     *     );
     *     
     *     Response loginResponse = given()
     *         .header("Content-Type", "application/json")
     *         .body(loginPayload)
     *     .when()
     *         .post(BASE_URL + "/auth/login");
     *     
     *     loginResponse.then().statusCode(200);
     *     String token = loginResponse.jsonPath().getString("token");
     *     
     *     RestAssured.requestSpecification = given()
     *         .header("Authorization", "Bearer " + token);
     * }
     */
}