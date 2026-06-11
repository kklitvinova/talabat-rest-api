package lt.viko.eif.klitvinova.resource;

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

/**
 * JAX-RS REST resource for order operations.
 * Base path: /orders (full path: /api/orders)
 *
 * @author Klitvinova
 * @version 1.0
 */
@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {

    private final OrderService service = new OrderService();

    @Context
    private UriInfo uriInfo;

    /**
     * Returns base URL from request context.
     *
     * @return base URL string
     */
    private String getBaseUrl() {
        return uriInfo.getBaseUri().toString().replaceAll("/$", "");
    }

    /**
     * GET /api/orders
     * Returns all orders with HATEOAS links.
     *
     * @return list of all orders
     */
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
    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") int id) {
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
     * Returns orders filtered by city with HATEOAS links.
     *
     * @param city city name
     * @return list of orders from that city
     */
    @GET
    @Path("/city/{city}")
    public Response getOrdersByCity(@PathParam("city") String city) {
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
     * Returns orders filtered by delivery status with HATEOAS links.
     *
     * @param delivered true or false
     * @return filtered list of orders
     */
    @GET
    @Path("/status/{delivered}")
    public Response getOrdersByStatus(@PathParam("delivered") boolean delivered) {
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
     * Returns orders filtered by payment method with HATEOAS links.
     *
     * @param method payment method
     * @return filtered list of orders
     */
    @GET
    @Path("/payment/{method}")
    public Response getOrdersByPayment(@PathParam("method") String method) {
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
    @PUT
    @Path("/{id}")
    public Response updateOrder(@PathParam("id") int id, Order order) {
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
    @DELETE
    @Path("/{id}")
    public Response deleteOrder(@PathParam("id") int id) {
        boolean deleted = service.deleteOrder(id);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order not found: " + id).build();
        }
        return Response.noContent().build();
    }
}