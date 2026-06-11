package lt.viko.eif.klitvinova;

import lt.viko.eif.klitvinova.model.Order;
import lt.viko.eif.klitvinova.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for OrderService.
 *
 * @author Klitvinova
 */
class OrderServiceTest {

    private OrderService service;

    @BeforeEach
    void setUp() {
        service = new OrderService();
    }

    @Test
    void getAllOrders_returnsNonEmptyList() throws IOException {
        List<Order> orders = service.getAllOrders();
        assertNotNull(orders);
        assertFalse(orders.isEmpty());
    }

    @Test
    void getOrderById_existingId_returnsOrder() throws IOException {
        Order order = service.getOrderById(1);
        assertNotNull(order);
        assertEquals(1, order.getOrderId());
    }

    @Test
    void getOrderById_nonExistingId_returnsNull() throws IOException {
        Order order = service.getOrderById(99999);
        assertNull(order);
    }

    @Test
    void getByCity_returnsFilteredOrders() throws IOException {
        List<Order> orders = service.getByCity("Cairo");
        assertNotNull(orders);
        orders.forEach(o -> assertEquals("Cairo", o.getCity()));
    }

    @Test
    void getByCity_nonExistingCity_returnsEmptyList() throws IOException {
        List<Order> orders = service.getByCity("Atlantis");
        assertNotNull(orders);
        assertTrue(orders.isEmpty());
    }

    @Test
    void getByStatus_delivered_returnsOnlyDelivered() throws IOException {
        List<Order> orders = service.getByStatus(true);
        assertNotNull(orders);
        orders.forEach(o -> assertTrue(o.isDelivered()));
    }

    @Test
    void getByPayment_cash_returnsOnlyCash() throws IOException {
        List<Order> orders = service.getByPayment("Cash");
        assertNotNull(orders);
        orders.forEach(o -> assertEquals("Cash", o.getPaymentMethod()));
    }

    @Test
    void createOrder_assignsId_andReturnsOrder() {
        Order order = new Order();
        order.setCity("Cairo");
        order.setPaymentMethod("Cash");
        Order created = service.createOrder(order);
        assertNotNull(created);
        assertEquals(100000, created.getOrderId());
    }

    @Test
    void updateOrder_existingId_updatesOrder() {
        Order order = new Order();
        order.setCity("Cairo");
        service.createOrder(order);

        Order updated = new Order();
        updated.setCity("Alexandria");
        Order result = service.updateOrder(100000, updated);

        assertNotNull(result);
        assertEquals("Alexandria", result.getCity());
    }

    @Test
    void deleteOrder_existingId_returnsTrue() {
        Order order = new Order();
        service.createOrder(order);
        assertTrue(service.deleteOrder(100000));
    }

    @Test
    void deleteOrder_nonExistingId_returnsFalse() {
        assertFalse(service.deleteOrder(99999));
    }
}