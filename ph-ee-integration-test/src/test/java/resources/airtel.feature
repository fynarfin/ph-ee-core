@amsIntegration
Feature: Airtel Test

  Scenario: AM-001
    Given I can inject MockServer
    And I can start mock server
    Then I should be able to get instance of mock server
    And I can register the stub with "/airtelCallback" endpoint for "POST" request with status of 200
    Then I will update the  mock server and register stub as done
    Given I have tenant as "payerfsp"
    And I have clientCorrelationId as "123456"
    And I have MSISDN as "1580354289"
    And I have accountId as "L000000001"
    And I have the request body with payer ams identifiers using keys MSISDN and accountId, currency "KES", and amount "100"
    When I call the channel collection API with client correlation id, country "kenya", callback "/airtelCallback", payment schema "airtel" and expected status of 200
    Then I should get transaction id in response
    Then I should be able to verify that "/airtelCallback" endpoint received 1 request with status code "TS"
    When I call the get txn API in ops app with transactionId as parameter
    Then  I should get transaction state as completed and externalId not null

  Scenario: AM-002
    Given I can inject MockServer
    And I can start mock server
    And I can register the stub with "/airtelCallback" endpoint for "POST" request with status of 200
    Then I will update the  mock server and register stub as done
    Given I have tenant as "payerfsp"
    And I have clientCorrelationId as "1278320"
    And I have MSISDN as "1103687051"
    And I have accountId as "L000000001"
    And I have amount as "100"
    And I have currency as "KES"
    And I have the request body with payer ams identifiers using keys MSISDN and accountId, currency "KES", and amount "100"
    When I call the channel collection API with client correlation id, country "kenya", callback "/airtelCallback", payment schema "airtel" and expected status of 200
    Then I should get transaction id in response
    Then I should be able to verify that "/airtelCallback" endpoint received 1 request with status code "TF"
    When I call the get txn API in ops app with transactionId as parameter
    Then  I should get transaction state as completed and externalId not null

  Scenario: AM-003 USSD Push Payment API Is Unsuccessful
    Given  I have MSISDN as "6729461912"
    And I have transaction id as "1278320"
    And I can mock USSD push payment request body
    When  I call USSD push payment API with MSISDN with expected status of 200
    Then I should get non empty response
    And I should get airtel USSD push payment response body in response
    And I should get response with data field null
