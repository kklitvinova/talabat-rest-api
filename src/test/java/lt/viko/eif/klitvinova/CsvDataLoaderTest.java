package lt.viko.eif.klitvinova;

import lt.viko.eif.klitvinova.model.CsvDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for CsvDataLoader.
 *
 * @author Klitvinova
 * @version 1.0
 */
class CsvDataLoaderTest {

    private CsvDataLoader loader;

    @BeforeEach
    void setUp() { loader = new CsvDataLoader(); }

    @Test
    @DisplayName("Returns non-null list")
    void testNotNull() throws Exception { assertNotNull(loader.loadOrders()); }

    @Test
    @DisplayName("Default limit is 50")
    void testDefaultLimit() throws Exception { assertEquals(50, loader.loadOrders().size()); }

    @Test
    @DisplayName("Custom limit is respected")
    void testCustomLimit() throws Exception { assertEquals(10, loader.loadOrders(10).size()); }

    @Test
    @DisplayName("First order has valid ID")
    void testFirstOrderId() throws Exception {
        assertTrue(loader.loadOrders(1).get(0).getOrderId() > 0);
    }

    @Test
    @DisplayName("userId is not null")
    void testUserIdNotNull() throws Exception {
        loader.loadOrders(5).forEach(o -> assertNotNull(o.getUserId()));
    }

    @Test
    @DisplayName("City is not null")
    void testCityNotNull() throws Exception {
        loader.loadOrders(5).forEach(o -> assertNotNull(o.getCity()));
    }

    @Test
    @DisplayName("Each order has at least one product")
    void testHasProducts() throws Exception {
        loader.loadOrders(10).forEach(o -> assertFalse(o.getProducts().isEmpty()));
    }

    @Test
    @DisplayName("Product price is positive")
    void testPricePositive() throws Exception {
        loader.loadOrders(10).forEach(o ->
            o.getProducts().forEach(p -> assertTrue(p.getTotalPrice() > 0)));
    }

    @Test
    @DisplayName("Delivery duration is positive")
    void testDurationPositive() throws Exception {
        loader.loadOrders(10).forEach(o ->
            assertTrue(o.getDeliveryDurationMinutes() > 0));
    }
}
