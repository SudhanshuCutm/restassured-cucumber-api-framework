Feature: JSONPlaceholder API - Complete CRUD Operations Demo
  As a QA Engineer
  I want to demonstrate end-to-end API testing
  To showcase the framework capabilities to the client

  @demo @smoke
  Scenario: Get a specific post by ID
    Given I have the demo API endpoint "/posts/1"
    When I send a GET request
    Then I should receive a response with status 200
    And The response should match schema "schema_get_post.json"
    And The response should contain field "userId" with value "1"
    And The response should contain field "id" with value "1"

  @demo @smoke
  Scenario: Create a new post
    Given I have the demo API endpoint "/posts"
    When I send a POST request with payload "create_post_payload.json"
    Then I should receive a response with status 201
    And The response should match schema "schema_create_post.json"
    And The response should contain field "title" with value "API Automation Demo"

  @demo @regression
  Scenario: Update an existing post
    Given I have the demo API endpoint "/posts/1"
    When I send a PUT request with payload "update_post_payload.json"
    Then I should receive a response with status 200
    And The response should match schema "schema_update_post.json"
    And The response should contain field "id" with value "1"
