package lt.viko.eif.klitvinova.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads orders from the Talabat CSV dataset.
 *
 * @author Klitvinova
 * @version 1.0
 */
public class CsvDataLoader {

    private static final String CSV_PATH = "/data/orders.csv";
    private static final int MAX_ORDERS = 50;

    /**
     * Loads orders with default limit of 50.
     *
     * @return list of orders
     * @throws IOException if file cannot be read
     */
    public List<Order> loadOrders() throws IOException {
        return loadOrders(MAX_ORDERS);
    }

    /**
     * Loads orders up to given limit.
     *
     * @param limit max number of orders
     * @return list of orders
     * @throws IOException if file cannot be read
     */
    public List<Order> loadOrders(int limit) throws IOException {
        List<Order> orders = new ArrayList<>();

        InputStream is = getClass().getResourceAsStream(CSV_PATH);
        if (is == null) {
            throw new IOException("Dataset not found: " + CSV_PATH);
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, "UTF-8"))) {

            String line;
            boolean header = true;

            while ((line = reader.readLine()) != null && orders.size() < limit) {
                // skip header row
                if (header) { header = false; continue; }

                String[] c = line.split(",", -1);
                if (c.length < 23) continue;

                try {
                    int orderId     = Integer.parseInt(c[0].trim());
                    String userId   = c[1].trim();
                    String itemName = c[4].trim();
                    int quantity    = Integer.parseInt(c[5].trim());
                    double price    = Double.parseDouble(c[6].trim());
                    int duration    = Integer.parseInt(c[9].trim());
                    String city     = c[10].trim();
                    String payment  = c[11].trim();
                    String status   = c[12].trim();
                    String vehicle  = c[13].trim();
                    float distance  = Float.parseFloat(c[20].trim());
                    String traffic  = c[21].trim();

                    boolean delivered = "Delivered".equalsIgnoreCase(status);
                    char statusCode   = status.isEmpty() ? 'U' : status.charAt(0);

                    // create product from this row
                    Product product = new Product(itemName, quantity, price, vehicle, traffic);
                    List<Product> products = new ArrayList<>();
                    products.add(product);

                    orders.add(new Order(orderId, userId, city, payment,
                            delivered, duration, distance, statusCode, products));

                } catch (NumberFormatException e) {
                    // skip malformed rows
                }
            }
        }
        return orders;
    }
}
