package lt.viko.eif.klitvinova;

import lt.viko.eif.klitvinova.model.CsvDataLoader;
import lt.viko.eif.klitvinova.model.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for order filtering logic used in OrderResource.
 *
 * @author Klitvinova
 * @version 1.0
 */
class OrderResourceTest {

    private CsvDataLoader loader;
    private List<Order> orders;

    @BeforeEach
    void setUp() throws Exception {
        loader = new CsvDataLoader();
        orders = loader.loadOrders(200);
    }

    @Test
    @DisplayName("Can filter delivered orders")
    void testFilterDelivered() {
        List<Order> delivered = orders.stream()
            .filter(o -> o.isDelivered())
            .collect(Collectors.toList());
        assertFalse(delivered.isEmpty());
        delivered.forEach(o -> assertTrue(o.isDelivered()));
    }

    @Test
    @DisplayName("Can filter by city Alexandria")
    void testFilterByCity() {
        List<Order> result = orders.stream()
            .filter(o -> "Alexandria".equalsIgnoreCase(o.getCity()))
            .collect(Collectors.toList());
        assertFalse(result.isEmpty());
        result.forEach(o -> assertEquals("Alexandria", o.getCity()));
    }

    @Test
    @DisplayName("Can filter by payment Wallet")
    void testFilterByPayment() {
        List<Order> result = orders.stream()
            .filter(o -> "Wallet".equalsIgnoreCase(o.getPaymentMethod()))
            .collect(Collectors.toList());
        assertFalse(result.isEmpty());
    }

    @Test
    @DisplayName("Find order by ID 1")
    void testFindById() throws Exception {
        Order order = loader.loadOrders(100000).stream()
            .filter(o -> o.getOrderId() == 1)
            .findFirst().orElse(null);
        assertNotNull(order);
        assertEquals(1, order.getOrderId());
    }

    @Test
    @DisplayName("Unknown city returns empty list")
    void testUnknownCity() {
        List<Order> result = orders.stream()
            .filter(o -> "UnknownCity".equalsIgnoreCase(o.getCity()))
            .collect(Collectors.toList());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("All orders have non-null city")
    void testAllCitiesNotNull() {
        orders.forEach(o -> assertNotNull(o.getCity()));
    }

    @Test
    @DisplayName("All orders have non-null payment method")
    void testAllPaymentsNotNull() {
        orders.forEach(o -> assertNotNull(o.getPaymentMethod()));
    }

    @Test
    @DisplayName("All orders have products list")
    void testAllOrdersHaveProducts() {
        orders.forEach(o -> assertNotNull(o.getProducts()));
    }
}
