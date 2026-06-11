package lt.viko.eif.klitvinova.service;

import lt.viko.eif.klitvinova.model.CsvDataLoader;
import lt.viko.eif.klitvinova.model.Order;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service layer for order business logic.
 * Follows SOLID Single Responsibility Principle.
 *
 * @author Klitvinova
 * @version 1.0
 */
public class OrderService {

    private final CsvDataLoader loader = new CsvDataLoader();
    private final List<Order> createdOrders = new ArrayList<>();
    private int nextId = 100000;

    /**
     * Returns all orders from dataset.
     *
     * @return list of all orders
     * @throws IOException if dataset cannot be read
     */
    public List<Order> getAllOrders() throws IOException {
        List<Order> all = new ArrayList<>(loader.loadOrders(200));
        all.addAll(createdOrders);
        return all;
    }

    /**
     * Returns order by ID.
     *
     * @param id order ID
     * @return order or null if not found
     * @throws IOException if dataset cannot be read
     */
    public Order getOrderById(int id) throws IOException {
        return getAllOrders().stream()
                .filter(o -> o.getOrderId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns orders filtered by city.
     *
     * @param city city name
     * @return filtered list
     * @throws IOException if dataset cannot be read
     */
    public List<Order> getByCity(String city) throws IOException {
        return getAllOrders().stream()
                .filter(o -> city.equalsIgnoreCase(o.getCity()))
                .collect(Collectors.toList());
    }

    /**
     * Returns orders filtered by delivery status.
     *
     * @param delivered delivery status
     * @return filtered list
     * @throws IOException if dataset cannot be read
     */
    public List<Order> getByStatus(boolean delivered) throws IOException {
        return getAllOrders().stream()
                .filter(o -> delivered == o.isDelivered())
                .collect(Collectors.toList());
    }

    /**
     * Returns orders filtered by payment method.
     *
     * @param method payment method
     * @return filtered list
     * @throws IOException if dataset cannot be read
     */
    public List<Order> getByPayment(String method) throws IOException {
        return getAllOrders().stream()
                .filter(o -> method.equalsIgnoreCase(o.getPaymentMethod()))
                .collect(Collectors.toList());
    }

    /**
     * Creates a new order.
     *
     * @param order order to create
     * @return created order with assigned ID
     */
    public Order createOrder(Order order) {
        order.setOrderId(nextId++);
        createdOrders.add(order);
        return order;
    }

    /**
     * Updates an existing order.
     *
     * @param id order ID
     * @param updated updated order data
     * @return updated order or null if not found
     */
    public Order updateOrder(int id, Order updated) {
        for (int i = 0; i < createdOrders.size(); i++) {
            if (createdOrders.get(i).getOrderId() == id) {
                updated.setOrderId(id);
                createdOrders.set(i, updated);
                return updated;
            }
        }
        return null;
    }

    /**
     * Deletes an order by ID.
     *
     * @param id order ID
     * @return true if deleted, false if not found
     */
    public boolean deleteOrder(int id) {
        return createdOrders.removeIf(o -> o.getOrderId() == id);
    }
}