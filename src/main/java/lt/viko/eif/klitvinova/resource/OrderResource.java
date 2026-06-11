package lt.viko.eif.klitvinova.resource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriInfo;
import lt.viko.eif.klitvinova.model.Order;
import lt.viko.eif.klitvinova.model.OrderResponse;
import lt.viko.eif.klitvinova.service.OrderService;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * JAX-RS REST resource for order operations.
 * Base path: /orders (full path: /api/orders)
 *
 * @author Klitvinova
 * @version 1.0
 */
@Tag(name = "Orders", description = "Talabat food delivery order management API")
@Component
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Autowired
    private OrderService service;

    @Context
    private UriInfo uriInfo;

    /**
     * Returns base URL from request context.
     *
     * @return base URL string
     */
    private String getBaseUrl() {
        return "http://localhost:8080";
    }

    /**
     * GET /api/orders
     * Returns all orders with HATEOAS links.
     *
     * @return list of all orders
     */
    @Operation(summary = "Get all orders",
            description = "Returns all delivery orders with HATEOAS links")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of orders returned"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GET
    public Response getAllOrders() {
        try {
            List<OrderResponse> result = service.getAllOrders().stream()
                    .map(o -> new OrderResponse(o, getBaseUrl()))
                    .collect(Collectors.toList());
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * GET /api/orders/{id}
     * Returns a single order by ID with HATEOAS links.
     *
     * @param id order ID
     * @return order or 404
     */
    @Operation(summary = "Get order by ID",
            description = "Returns a single order by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @GET
    @Path("/{id}")
    public Response getOrderById(
            @Parameter(description = "Order ID") @PathParam("id") int id) {
        try {
            Order order = service.getOrderById(id);
            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Order not found: " + id).build();
            }
            return Response.ok(new OrderResponse(order, getBaseUrl())).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * GET /api/orders/city/{city}
     * Returns orders filtered by city.
     *
     * @param city city name
     * @return list of orders from that city
     */
    @Operation(summary = "Get orders by city",
            description = "Returns orders filtered by city name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered orders returned")
    })
    @GET
    @Path("/city/{city}")
    public Response getOrdersByCity(
            @Parameter(description = "City name") @PathParam("city") String city) {
        try {
            List<OrderResponse> result = service.getByCity(city).stream()
                    .map(o -> new OrderResponse(o, getBaseUrl()))
                    .collect(Collectors.toList());
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * GET /api/orders/status/{delivered}
     * Returns orders filtered by delivery status.
     *
     * @param delivered true or false
     * @return filtered list of orders
     */
    @Operation(summary = "Get orders by delivery status",
            description = "Returns orders filtered by delivered true or false")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered orders returned")
    })
    @GET
    @Path("/status/{delivered}")
    public Response getOrdersByStatus(
            @Parameter(description = "Delivery status: true or false")
            @PathParam("delivered") boolean delivered) {
        try {
            List<OrderResponse> result = service.getByStatus(delivered).stream()
                    .map(o -> new OrderResponse(o, getBaseUrl()))
                    .collect(Collectors.toList());
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * GET /api/orders/payment/{method}
     * Returns orders filtered by payment method.
     *
     * @param method payment method
     * @return filtered list of orders
     */
    @Operation(summary = "Get orders by payment method",
            description = "Returns orders filtered by payment method")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Filtered orders returned")
    })
    @GET
    @Path("/payment/{method}")
    public Response getOrdersByPayment(
            @Parameter(description = "Payment method e.g. Cash, Wallet, Credit Card")
            @PathParam("method") String method) {
        try {
            List<OrderResponse> result = service.getByPayment(method).stream()
                    .map(o -> new OrderResponse(o, getBaseUrl()))
                    .collect(Collectors.toList());
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * POST /api/orders
     * Creates a new order.
     *
     * @param order order to create
     * @return created order with 201 status
     */
    @Operation(summary = "Create new order",
            description = "Creates a new delivery order")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully")
    })
    @POST
    public Response createOrder(Order order) {
        Order created = service.createOrder(order);
        URI location = UriBuilder.fromResource(OrderResource.class)
                .path(String.valueOf(created.getOrderId()))
                .build();
        return Response.created(location)
                .entity(new OrderResponse(created, getBaseUrl()))
                .build();
    }

    /**
     * PUT /api/orders/{id}
     * Updates an existing order.
     *
     * @param id order ID
     * @param order updated order data
     * @return updated order or 404
     */
    @Operation(summary = "Update order",
            description = "Updates an existing order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PUT
    @Path("/{id}")
    public Response updateOrder(
            @Parameter(description = "Order ID") @PathParam("id") int id,
            Order order) {
        Order updated = service.updateOrder(id, order);
        if (updated == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order not found: " + id).build();
        }
        return Response.ok(new OrderResponse(updated, getBaseUrl())).build();
    }

    /**
     * DELETE /api/orders/{id}
     * Deletes an order by ID.
     *
     * @param id order ID
     * @return 204 or 404
     */
    @Operation(summary = "Delete order",
            description = "Deletes an order by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(
            @Parameter(description = "Order ID") @PathParam("id") int id) {
        boolean deleted = service.deleteOrder(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order not found: " + id).build();
        }
        return Response.noContent().build();
    }
}