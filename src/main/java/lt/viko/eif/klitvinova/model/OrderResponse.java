package lt.viko.eif.klitvinova.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * HATEOAS wrapper for Order responses.
 * Adds hypermedia links to conform to Richardson Maturity Level 3.
 *
 * @author Klitvinova
 * @version 1.0
 */
public class OrderResponse {

    private Integer orderId;
    private String userId;
    private String city;
    private String paymentMethod;
    private Boolean delivered;
    private Integer deliveryDurationMinutes;
    private Float deliveryDistanceKm;
    private String statusCode;
    private List<Product> products;
    private Map<String, Map<String, String>> _links;

    /**
     * Creates HATEOAS response from Order.
     *
     * @param order source order
     * @param baseUrl base URL e.g. http://localhost:8080
     */
    public OrderResponse(Order order, String baseUrl) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.city = order.getCity();
        this.paymentMethod = order.getPaymentMethod();
        this.delivered = order.isDelivered();
        this.deliveryDurationMinutes = order.getDeliveryDurationMinutes();
        this.deliveryDistanceKm = order.getDeliveryDistanceKm();
        this.statusCode = order.getStatusCode();
        this.products = order.getProducts();

        this._links = new HashMap<>();

        Map<String, String> self = new HashMap<>();
        self.put("href", baseUrl + "/api/orders/" + order.getOrderId());
        _links.put("self", self);

        Map<String, String> collection = new HashMap<>();
        collection.put("href", baseUrl + "/api/orders");
        _links.put("collection", collection);
    }

    public Integer getOrderId() { return orderId; }
    public String getUserId() { return userId; }
    public String getCity() { return city; }
    public String getPaymentMethod() { return paymentMethod; }
    public Boolean isDelivered() { return delivered; }
    public Integer getDeliveryDurationMinutes() { return deliveryDurationMinutes; }
    public Float getDeliveryDistanceKm() { return deliveryDistanceKm; }
    public String getStatusCode() { return statusCode; }
    public List<Product> getProducts() { return products; }
    public Map<String, Map<String, String>> get_links() { return _links; }
}