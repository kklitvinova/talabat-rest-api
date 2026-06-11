package lt.viko.eif.klitvinova;

import lt.viko.eif.klitvinova.model.Order;
import lt.viko.eif.klitvinova.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Order and Product model classes.
 *
 * @author Klitvinova
 * @version 1.0
 */
class OrderTest {

    private Order order;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product("Fried Chicken", 3, 273.72, "Motorbike", "High");
        order = new Order(1, "U3522", "Alexandria", "Wallet",
                true, 39, 1.67f, 'D', Arrays.asList(product));
    }

    @Test
    @DisplayName("Order ID is set correctly")
    void testOrderId() { assertEquals(1, order.getOrderId()); }

    @Test
    @DisplayName("User ID is set correctly")
    void testUserId() { assertEquals("U3522", order.getUserId()); }

    @Test
    @DisplayName("City is set correctly")
    void testCity() { assertEquals("Alexandria", order.getCity()); }

    @Test
    @DisplayName("Payment method is set correctly")
    void testPayment() { assertEquals("Wallet", order.getPaymentMethod()); }

    @Test
    @DisplayName("Delivered is true")
    void testDelivered() { assertTrue(order.isDelivered()); }

    @Test
    @DisplayName("Duration is set correctly")
    void testDuration() { assertEquals(39, order.getDeliveryDurationMinutes()); }

    @Test
    @DisplayName("Distance is set correctly")
    void testDistance() { assertEquals(1.67f, order.getDeliveryDistanceKm(), 0.01f); }

    @Test
    @DisplayName("Status code is D")
    void testStatusCode() { assertEquals("D", order.getStatusCode()); }

    @Test
    @DisplayName("Order has 1 product")
    void testProducts() { assertEquals(1, order.getProducts().size()); }

    @Test
    @DisplayName("Setter updates city")
    void testSetCity() {
        order.setCity("Cairo");
        assertEquals("Cairo", order.getCity());
    }

    @Test
    @DisplayName("No-arg constructor works")
    void testNoArgConstructor() { assertNotNull(new Order()); }

    @Test
    @DisplayName("Product item name is set correctly")
    void testProductName() { assertEquals("Fried Chicken", product.getItemName()); }

    @Test
    @DisplayName("Product quantity is set correctly")
    void testProductQuantity() { assertEquals(3, product.getQuantity()); }

    @Test
    @DisplayName("Product price is set correctly")
    void testProductPrice() { assertEquals(273.72, product.getTotalPrice(), 0.001); }

    @Test
    @DisplayName("Product vehicle is set correctly")
    void testProductVehicle() { assertEquals("Motorbike", product.getDriverVehicle()); }

    @Test
    @DisplayName("Product traffic is set correctly")
    void testProductTraffic() { assertEquals("High", product.getTrafficLevel()); }

    @Test
    @DisplayName("Product no-arg constructor works")
    void testProductNoArg() { assertNotNull(new Product()); }

    @Test
    @DisplayName("Product toString contains name")
    void testProductToString() { assertTrue(product.toString().contains("Fried Chicken")); }
}
