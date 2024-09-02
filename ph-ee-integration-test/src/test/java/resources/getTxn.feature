
Feature: Get Txn Req API test
  @common @gov
  Scenario: GTX-001 Get Txn Req API Test With Auth
    Given I have tenant as "paymentbb2"
    And I call collection api with expected status 200
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with expected status of 200
    Then I should get non empty response
    And I should have clientCorrelationId in response

  @common @gov
  Scenario: GTX-002 Get Txn Req API with Params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with date today minus 2 day and "current date" with expected status of 200
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-004,005 Get Txn Req with future date in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with end date before start date expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-006 Get Txn Req with future date in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
#    Start date as empty query parameter
    When I call the get txn API with empty query params expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-007,014 Get Txn Req with invalid txn Id in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with invalid transactionId expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-008,009 Get Txn Req with invalid page retrieval and size in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with size -5 and page -3 expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-016,017 Get Txn Req with invalid amount and currency in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with currency "USD" and amount -1 expecting expected status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-018,019 Get Txn Req with invalid date format in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with invalid startFrom and startTo format expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-020.023 Get Txn Req with invalid sorting order format in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with invalid sorting order expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response

  Scenario: GTX-022 Get Txn Req with invalid sortedBy format in params
    Given I have tenant as "paymentbb2"
    When I call the operations-app auth endpoint with username: "mifos" and password: "password"
    Then I should get a valid token
    When I call the get txn API with invalid sortedBy expecting status of 400
    Then I should get non empty response
    And I should have startedAt and completedAt in response
