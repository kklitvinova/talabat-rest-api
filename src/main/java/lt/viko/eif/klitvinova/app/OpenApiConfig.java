package lt.viko.eif.klitvinova.app;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for Talabat REST API.
 * Manually registers all JAX-RS endpoints for Swagger UI display.
 *
 * @author Klitvinova
 * @version 1.0
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures OpenAPI documentation with all endpoints.
     *
     * @return OpenAPI instance
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Talabat Order REST API")
                        .version("1.0")
                        .description("RESTful Web Service for Talabat food delivery orders. " +
                                "Conforms to Richardson Maturity Level 3 (HATEOAS).")
                        .contact(new Contact()
                                .name("Karyna Litvinova")
                                .email("klitvinova@stud.viko.lt")))
                .paths(buildPaths());
    }

    /**
     * Builds all API paths manually for JAX-RS compatibility.
     *
     * @return Paths object with all endpoints
     */
    private Paths buildPaths() {
        Paths paths = new Paths();

        // GET /api/orders
        paths.addPathItem("/api/orders", new PathItem()
                .get(new Operation()
                        .summary("Get all orders")
                        .description("Returns all delivery orders with HATEOAS links")
                        .addTagsItem("Orders")
                        .responses(okResponse("List of all orders")))
                .post(new Operation()
                        .summary("Create new order")
                        .description("Creates a new delivery order")
                        .addTagsItem("Orders")
                        .responses(new ApiResponses()
                                .addApiResponse("201", new ApiResponse()
                                        .description("Order created successfully"))
                                .addApiResponse("400", new ApiResponse()
                                        .description("Invalid input")))));

        // GET /api/orders/{id}
        paths.addPathItem("/api/orders/{id}", new PathItem()
                .get(new Operation()
                        .summary("Get order by ID")
                        .description("Returns a single order by its ID")
                        .addTagsItem("Orders")
                        .addParametersItem(pathParam("id", "Order ID"))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Order found"))
                                .addApiResponse("404", new ApiResponse()
                                        .description("Order not found"))))
                .put(new Operation()
                        .summary("Update order")
                        .description("Updates an existing order by ID")
                        .addTagsItem("Orders")
                        .addParametersItem(pathParam("id", "Order ID"))
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Order updated"))
                                .addApiResponse("404", new ApiResponse()
                                        .description("Order not found"))))
                .delete(new Operation()
                        .summary("Delete order")
                        .description("Deletes an order by ID")
                        .addTagsItem("Orders")
                        .addParametersItem(pathParam("id", "Order ID"))
                        .responses(new ApiResponses()
                                .addApiResponse("204", new ApiResponse()
                                        .description("Order deleted"))
                                .addApiResponse("404", new ApiResponse()
                                        .description("Order not found")))));

        // GET /api/orders/city/{city}
        paths.addPathItem("/api/orders/city/{city}", new PathItem()
                .get(new Operation()
                        .summary("Get orders by city")
                        .description("Returns orders filtered by city name")
                        .addTagsItem("Orders")
                        .addParametersItem(pathParam("city", "City name e.g. Cairo, Alexandria"))
                        .responses(okResponse("Filtered orders by city"))));

        // GET /api/orders/status/{delivered}
        paths.addPathItem("/api/orders/status/{delivered}", new PathItem()
                .get(new Operation()
                        .summary("Get orders by delivery status")
                        .description("Returns orders filtered by delivered true or false")
                        .addTagsItem("Orders")
                        .addParametersItem(pathParam("delivered", "true or false"))
                        .responses(okResponse("Filtered orders by status"))));

        // GET /api/orders/payment/{method}
        paths.addPathItem("/api/orders/payment/{method}", new PathItem()
                .get(new Operation()
                        .summary("Get orders by payment method")
                        .description("Returns orders filtered by payment method")
                        .addTagsItem("Orders")
                        .addParametersItem(pathParam("method",
                                "Payment method e.g. Cash, Wallet, Credit Card"))
                        .responses(okResponse("Filtered orders by payment method"))));

        // POST /api/auth/login
        paths.addPathItem("/api/auth/login", new PathItem()
                .post(new Operation()
                        .summary("Manager login")
                        .description("Authenticates manager and returns JWT token")
                        .addTagsItem("Authentication")
                        .responses(new ApiResponses()
                                .addApiResponse("200", new ApiResponse()
                                        .description("Login successful, JWT token returned"))
                                .addApiResponse("401", new ApiResponse()
                                        .description("Invalid credentials")))));
        return paths;
    }

    /**
     * Creates a standard 200 OK response.
     *
     * @param description response description
     * @return ApiResponses with 200 OK
     */
    private ApiResponses okResponse(String description) {
        return new ApiResponses()
                .addApiResponse("200", new ApiResponse()
                        .description(description));
    }

    /**
     * Creates a path parameter.
     *
     * @param name parameter name
     * @param description parameter description
     * @return Parameter object
     */
    private Parameter pathParam(String name, String description) {
        return new Parameter()
                .name(name)
                .in("path")
                .required(true)
                .description(description)
                .schema(new Schema<>().type("string"));
    }
}