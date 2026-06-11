package lt.viko.eif.klitvinova.cucumber;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Step definitions for Cucumber BDD tests.
 * Uses RestAssured to send HTTP requests to the API.
 *
 * @author Klitvinova
 * @version 1.0
 */
public class OrderSteps {

    private String baseUrl;
    private Response response;
    private String token;
    private Map<String, Object> orderBody = new HashMap<>();
    private Map<String, String> loginBody = new HashMap<>();
    private int savedOrderId;

    @Given("the API is running on {string}")
    public void theApiIsRunningOn(String url) {
        this.baseUrl = url;
        RestAssured.baseURI = url;

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", "manager@talabat.com");
        credentials.put("password", "manager123");

        Response loginResponse = RestAssured
                .given()
                .contentType("application/json")
                .body(credentials)
                .post("/api/auth/login");

        this.token = loginResponse.jsonPath().getString("token");
        System.out.println("Token obtained: " + (token != null ? "YES" : "NO"));
    }

    @Given("I have login credentials email {string} and password {string}")
    public void iHaveLoginCredentials(String email, String password) {
        loginBody = new HashMap<>();
        loginBody.put("email", email);
        loginBody.put("password", password);
    }

    @When("I send a GET request to {string}")
    public void iSendAGetRequestTo(String path) {
        response = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .get(baseUrl + path);
    }

    @When("I send an unauthorized GET request to {string}")
    public void iSendAnUnauthorizedGetRequestTo(String path) {
        response = RestAssured
                .given()
                .contentType("application/json")
                .get(baseUrl + path);
    }

    @When("I send a POST request to {string}")
    public void iSendAPostRequestTo(String path) {
        if (path.equals("/api/auth/login")) {
            response = RestAssured
                    .given()
                    .contentType("application/json")
                    .body(loginBody)
                    .post(baseUrl + path);
        } else {
            response = RestAssured
                    .given()
                    .header("Authorization", "Bearer " + token)
                    .contentType("application/json")
                    .body(orderBody)
                    .post(baseUrl + path);
        }
    }

    @Then("the response status code should be {int}")
    public void theResponseStatusCodeShouldBe(int statusCode) {
        Assertions.assertEquals(statusCode, response.getStatusCode());
    }

    @And("the response should contain a JSON array")
    public void theResponseShouldContainAJsonArray() {
        List<?> list = response.jsonPath().getList("$");
        Assertions.assertNotNull(list);
    }

    @And("the response should contain field {string}")
    public void theResponseShouldContainField(String field) {
        Object value = response.jsonPath().get(field);
        Assertions.assertNotNull(value, "Field '" + field + "' not found in response");
    }

    @And("all orders in response should have {string} equal to {string}")
    public void allOrdersInResponseShouldHave(String field, String value) {
        List<Object> rawValues = response.jsonPath().getList(field);
        List<String> values = rawValues.stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.toList());
        Assertions.assertFalse(values.isEmpty(), "Response list is empty");
        for (String v : values) {
            Assertions.assertEquals(value.toLowerCase(), v.toLowerCase());
        }
    }

    @And("the response should be an empty array")
    public void theResponseShouldBeAnEmptyArray() {
        List<?> list = response.jsonPath().getList("$");
        Assertions.assertTrue(list.isEmpty(), "Response should be empty but was not");
    }

    @Given("I have an order with city {string}, payment {string}, delivered {string}")
    public void iHaveAnOrderWith(String city, String payment, String delivered) {
        orderBody = new HashMap<>();
        orderBody.put("city", city);
        orderBody.put("paymentMethod", payment);
        orderBody.put("delivered", Boolean.parseBoolean(delivered));
    }

    @And("the response header {string} should contain {string}")
    public void theResponseHeaderShouldContain(String header, String value) {
        String headerValue = response.getHeader(header);
        Assertions.assertNotNull(headerValue, "Header '" + header + "' not found");
        Assertions.assertTrue(headerValue.contains(value));
    }

    @And("I save the returned orderId")
    public void iSaveTheReturnedOrderId() {
        savedOrderId = response.jsonPath().getInt("orderId");
    }

    @When("I send a GET request to the saved order")
    public void iSendAGetRequestToTheSavedOrder() {
        response = RestAssured
                .given()
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .get(baseUrl + "/api/orders/" + savedOrderId);
    }
}