Feature: Test REST-API with Dynamic Payload for PUT Request

  Scenario Outline: Validate API Response for PUT Request with Dynamic Payload
    Given I have the API endpoint "<endpoint>"
    When I send a PUT request with the dynamic payload
    Then I should receive a response with status <status_code>
    And The response should match schema "<schema_file>"

  Examples:
    | endpoint       | status_code | schema_file              |
    | /update-task   | 200         | schema_update_task.json  |