package lt.viko.eif.klitvinova.model;

import java.util.List;

/**
 * Delivery order from the Talabat dataset.
 *
 * @author Klitvinova
 * @version 1.0
 */
public class Order {

    private Integer orderId;
    private String userId;
    private String city;
    private String paymentMethod;
    private Boolean delivered;
    private Integer deliveryDurationMinutes;
    private Float deliveryDistanceKm;
    private String statusCode;

    // list of food items in this order
    private List<Product> products;

    // default constructor required by Jackson
    public Order() {
    }

    public Order(int orderId, String userId, String city, String paymentMethod,
                 boolean delivered, int deliveryDurationMinutes,
                 float deliveryDistanceKm, char statusCode, List<Product> products) {
        this.orderId = orderId;
        this.userId = userId;
        this.city = city;
        this.paymentMethod = paymentMethod;
        this.delivered = delivered;
        this.deliveryDurationMinutes = deliveryDurationMinutes;
        this.deliveryDistanceKm = deliveryDistanceKm;
        this.statusCode = String.valueOf(statusCode);
        this.products = products;
    }

    public Integer getOrderId() { return orderId; }
    public void setOrderId(Integer orderId) { this.orderId = orderId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public Boolean isDelivered() { return delivered; }
    public void setDelivered(Boolean delivered) { this.delivered = delivered; }

    public Integer getDeliveryDurationMinutes() { return deliveryDurationMinutes; }
    public void setDeliveryDurationMinutes(Integer v) { this.deliveryDurationMinutes = v; }

    public Float getDeliveryDistanceKm() { return deliveryDistanceKm; }
    public void setDeliveryDistanceKm(Float v) { this.deliveryDistanceKm = v; }

    public String getStatusCode() { return statusCode; }
    public void setStatusCode(String statusCode) { this.statusCode = statusCode; }

    public List<Product> getProducts() { return products; }
    public void setProducts(List<Product> products) { this.products = products; }

    @Override
    public String toString() {
        return String.format(
            "Order{id=%d, user='%s', city='%s', payment='%s', delivered=%b, items=%d}",
            orderId, userId, city, paymentMethod, delivered,
            products != null ? products.size() : 0
        );
    }
}
