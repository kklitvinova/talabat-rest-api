Feature: Talabat Delivery Management System
  As a delivery manager
  I want to manage orders through the REST API
  So that I can track and control delivery operations

  Background:
    Given the API is running on "http://localhost:8080"

  # ==========================================
  # USER STORY 1: Manager Authentication
  # ==========================================

  Scenario: Manager logs in with valid credentials
    Given I have login credentials email "manager@talabat.com" and password "manager123"
    When I send a POST request to "/api/auth/login"
    Then the response status code should be 200
    And the response should contain field "token"
    And the response should contain field "role"

  Scenario: Manager logs in with wrong password
    Given I have login credentials email "manager@talabat.com" and password "wrongpass"
    When I send a POST request to "/api/auth/login"
    Then the response status code should be 401

  Scenario: Manager logs in with wrong email
    Given I have login credentials email "wrong@email.com" and password "manager123"
    When I send a POST request to "/api/auth/login"
    Then the response status code should be 401

  Scenario: Manager logs in with empty credentials
    Given I have login credentials email "" and password ""
    When I send a POST request to "/api/auth/login"
    Then the response status code should be 401

  # ==========================================
  # USER STORY 2: Access Control
  # ==========================================

  Scenario: Unauthorized request without token is rejected
    When I send an unauthorized GET request to "/api/orders"
    Then the response status code should be 401

  Scenario: Authorized manager can access orders
    When I send a GET request to "/api/orders"
    Then the response status code should be 200
    And the response should contain a JSON array

  # ==========================================
  # USER STORY 3: View All Orders
  # ==========================================

  Scenario: Get all orders returns list with HATEOAS links
    When I send a GET request to "/api/orders"
    Then the response status code should be 200
    And the response should contain a JSON array
    And the response should contain field "_links"

  # ==========================================
  # USER STORY 4: Find Order by ID
  # ==========================================

  Scenario: Get existing order by ID returns order data
    When I send a GET request to "/api/orders/1"
    Then the response status code should be 200
    And the response should contain field "orderId"
    And the response should contain field "city"
    And the response should contain field "_links"

  Scenario: Get order with non-existing ID returns 404
    When I send a GET request to "/api/orders/99999"
    Then the response status code should be 404

  # ==========================================
  # USER STORY 5: Filter Orders by City
  # ==========================================

  Scenario: Filter orders by existing city Cairo returns matching orders
    When I send a GET request to "/api/orders/city/Cairo"
    Then the response status code should be 200
    And the response should contain a JSON array
    And all orders in response should have "city" equal to "Cairo"

  Scenario: Filter orders by existing city Alexandria
    When I send a GET request to "/api/orders/city/Alexandria"
    Then the response status code should be 200
    And all orders in response should have "city" equal to "Alexandria"

  Scenario: Filter orders by non-existing city returns empty array
    When I send a GET request to "/api/orders/city/Atlantis"
    Then the response status code should be 200
    And the response should be an empty array

  # ==========================================
  # USER STORY 6: Filter by Delivery Status
  # ==========================================

  Scenario: Filter delivered orders returns only delivered
    When I send a GET request to "/api/orders/status/true"
    Then the response status code should be 200
    And all orders in response should have "delivered" equal to "true"

  Scenario: Filter undelivered orders returns only undelivered
    When I send a GET request to "/api/orders/status/false"
    Then the response status code should be 200
    And all orders in response should have "delivered" equal to "false"

  # ==========================================
  # USER STORY 7: Filter by Payment Method
  # ==========================================

  Scenario: Filter orders by payment method Cash
    When I send a GET request to "/api/orders/payment/Cash"
    Then the response status code should be 200
    And all orders in response should have "paymentMethod" equal to "Cash"

  Scenario: Filter orders by payment method Wallet
    When I send a GET request to "/api/orders/payment/Wallet"
    Then the response status code should be 200
    And all orders in response should have "paymentMethod" equal to "Wallet"

  # ==========================================
  # USER STORY 8: Create New Order
  # ==========================================

  Scenario: Create valid order returns 201 with location header
    Given I have an order with city "Cairo", payment "Cash", delivered "false"
    When I send a POST request to "/api/orders"
    Then the response status code should be 201
    And the response should contain field "orderId"
    And the response header "Location" should contain "orders/"

  Scenario: Created order can be retrieved by ID
    Given I have an order with city "Alexandria", payment "Wallet", delivered "false"
    When I send a POST request to "/api/orders"
    Then the response status code should be 201
    And I save the returned orderId
    When I send a GET request to the saved order
    Then the response status code should be 200
    And the response should contain field "orderId"