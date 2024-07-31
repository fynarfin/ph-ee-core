Feature: Validate Channel Api Request Headers And Body

  Scenario: Error Validity check for Post Transfer API for request body with no payee
    Given I can create a TransactionChannelRequestDTO with no payee
    When I call the post transfer API with expected status of 400
    Then I should be able to assert the api validation for schema validation error response

  Scenario: Unsupported header validation for Post Transfer Api Test
    Given I can create a TransactionChannelRequestDTO
    When I call the post transfer API having unsupported header with expected status of 400
    Then I should get non empty response
    Then I should be able to assert the api validation for header validation error response

  Scenario: Required header validation for Post Transfer Api Test
    Given I can create a TransactionChannelRequestDTO
    When I call the post transfer API without required header Platform-TenantId with expected status of 400
    Then I should get non empty response
    Then I should be able to assert the api validation for header validation error response

  Scenario: Error Validity check for Transaction Request API for request body with no payee
    Given I can create a TransactionChannelRequestDTO with no payee
    When I call the transaction request API with expected status of 400
    Then I should be able to assert the api validation for schema validation error response

  Scenario: Unsupported header validation for Transaction Request Api Test
    Given I can create a TransactionChannelRequestDTO
    When I call the transaction request API having unsupported header with expected status of 400
    Then I should get non empty response
    Then I should be able to assert the api validation for header validation error response

  Scenario: Required header validation for Transaction Request Api Test
    Given I can create a TransactionChannelRequestDTO
    When I call the transaction request API without required header Platform-TenantId with expected status of 400
    Then I should get non empty response
    Then I should be able to assert the api validation for header validation error response

  Scenario: Error Validity check for GSMA Transaction API for request body with no payee list
    Given I can create a GsmaTransfer DTO with no payee list
    When I call the GSMA transaction API with expected status of 400
    Then I should be able to assert the api validation for schema validation error response

  Scenario: Unsupported header validation for GSMA Transaction Api Test
    Given I can create a GsmaTransfer DTO
    When I call the gsma transaction API having unsupported header with expected status of 400
    Then I should get non empty response
    Then I should be able to assert the api validation for header validation error response

  Scenario: Required header validation for Transaction Request Api Test
    Given I can create a GsmaTransfer DTO
    When I call the gsma transaction API without required header accountHoldingInstitutionId with expected status of 400
    Then I should get non empty response
    Then I should be able to assert the api validation for header validation error response