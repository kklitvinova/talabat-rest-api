package lt.viko.eif.klitvinova.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lt.viko.eif.klitvinova.model.CsvDataLoader;
import lt.viko.eif.klitvinova.model.Order;

import java.io.IOException;
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

    private final CsvDataLoader loader = new CsvDataLoader();

    /**
     * GET /api/orders
     * Returns all orders.
     *
     * @return list of all orders
     */
    @GET
    public Response getAllOrders() {
        try {
            List<Order> orders = loader.loadOrders();
            System.out.println("GET /api/orders -> " + orders.size());
            return Response.ok(orders).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    /**
     * GET /api/orders/{id}
     * Returns a single order by ID.
     *
     * @param id order ID
     * @return order or 404
     */
    @GET
    @Path("/{id}")
    public Response getOrderById(@PathParam("id") int id) {
        try {
            Order order = loader.loadOrders(100000).stream()
                .filter(o -> o.getOrderId() == id)
                .findFirst()
                .orElse(null);

            if (order == null) {
                return Response.status(Response.Status.NOT_FOUND)
                    .entity("Order not found: " + id).build();
            }
            return Response.ok(order).build();
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
    @GET
    @Path("/city/{city}")
    public Response getOrdersByCity(@PathParam("city") String city) {
        try {
            List<Order> result = loader.loadOrders(200).stream()
                .filter(o -> city.equalsIgnoreCase(o.getCity()))
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
    @GET
    @Path("/status/{delivered}")
    public Response getOrdersByStatus(@PathParam("delivered") boolean delivered) {
        try {
            List<Order> result = loader.loadOrders(200).stream()
                .filter(o -> delivered == o.isDelivered())
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
    @GET
    @Path("/payment/{method}")
    public Response getOrdersByPayment(@PathParam("method") String method) {
        try {
            List<Order> result = loader.loadOrders(200).stream()
                .filter(o -> method.equalsIgnoreCase(o.getPaymentMethod()))
                .collect(Collectors.toList());
            return Response.ok(result).build();
        } catch (IOException e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
