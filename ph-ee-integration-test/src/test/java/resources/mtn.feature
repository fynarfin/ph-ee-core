
Feature: Mtn Flow Test

  Scenario: MTN Flow validation test
    Given I can create a TransactionChannelRequestDTO for MTN Transaction Request with PayerId ""
    Then I set the header as invalid for mtn
    When I call the transaction request API for MTN with expected status of 400

  Scenario: Mtn Flow Test for Successful Payment Response
    When I can inject MockServer
    Then I can start mock server
    And I can register the stub with "/transactionRequest" endpoint for "PUT" request with status of 200
    Given I can create a TransactionChannelRequestDTO for MTN Transaction Request with PayerId "875621381"
    When I call the transaction request API for MTN with expected status of 202 and "/transactionRequest"
    Then I should be able to extract response body from callback for mtn
    And I should have "status" and "SUCCESSFUL" in mtn callback response

  Scenario: Mtn Flow Test for Failed Payment Response
    When I can inject MockServer
    Then I can start mock server
    And I can register the stub with "/transactionRequest" endpoint for "PUT" request with status of 200
    Given I can create a TransactionChannelRequestDTO for MTN Transaction Request with PayerId "46733123451"
    When I call the transaction request API for MTN with expected status of 202 and "/transactionRequest"
    Then I should be able to extract response body from callback for mtn
    And I should have "status" and "FAILED" in mtn callback response