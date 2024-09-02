Feature: Get Transfers API test

        @common @gov
        @cucumberCli
        Scenario: GT-001 Get Transfers API With Auth
                Given I have tenant as "paymentBB2"
                When I call the operations-app auth endpoint with username: "mifos" and password: "password"
                Then I should get a valid token
                When I call the transfer API with expected status of 200
                Then I should get non empty response
                And I should have clientCorrelationId in response

        Scenario: GT-012 Get Transfers API With Page retrieval and size
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with size -4 and page -2 expecting expected status of 400
                Then I should get non empty response
                And I should have page and size in response

        Scenario: GT-006 Get Transfers API with end date is before the start date
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with end date is before the start date expecting expected status of 400
                Then I should get non empty response

        Scenario: GT-002 Get Transfers API with invalid date format
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with invalid date format expecting expected status of 400
                Then I should get non empty response

        Scenario: GT-004 Get Transfers API with empty date query parameter value
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with an empty start date expecting expected status of 400
                Then I should get non empty response

        Scenario: GT-007,008 Get Transfers API With invalid amount and currency
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with currency "RUPEES" and amount -1 expecting expected status of 400
                Then I should get non empty response
                And I should have currency and amount in response

        Scenario: GT-010 Get Transfer API with invalid sorting order
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with invalid sorting order with expected status of 400
                Then I should get non empty response

        Scenario: GT-005 Get Transfers API with invalid status format
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with status set to PROCESSING expecting expected status of 400
                Then I should get non empty response

        Scenario: GT-009,017 Get Transfers API with invalid clientCorrelationId length
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with clientCorrelationId of exceeding max length with status of 400
                Then I should get non empty response

        Scenario: GT-001 Get Transfers API with invalid txnID
                Given I have tenant as "paymentBB2"
                Then I should get a valid token
                When I call the transfer API with invalid transactionId with status of 400
                Then I should get non empty response
