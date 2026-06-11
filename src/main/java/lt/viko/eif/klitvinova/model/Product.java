package lt.viko.eif.klitvinova.model;

/**
 * Food item in a delivery order.
 *
 * @author Klitvinova
 * @version 1.0
 */
public class Product {

    private String itemName;
    private Integer quantity;
    private Double totalPrice;
    private String driverVehicle;
    private String trafficLevel;

    // default constructor required by Jackson
    public Product() {
    }

    public Product(String itemName, Integer quantity, Double totalPrice,
                   String driverVehicle, String trafficLevel) {
        this.itemName = itemName;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.driverVehicle = driverVehicle;
        this.trafficLevel = trafficLevel;
    }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(Double totalPrice) { this.totalPrice = totalPrice; }

    public String getDriverVehicle() { return driverVehicle; }
    public void setDriverVehicle(String driverVehicle) { this.driverVehicle = driverVehicle; }

    public String getTrafficLevel() { return trafficLevel; }
    public void setTrafficLevel(String trafficLevel) { this.trafficLevel = trafficLevel; }

    @Override
    public String toString() {
        return String.format("Product{item='%s', qty=%d, price=%.2f, vehicle='%s', traffic='%s'}",
                itemName, quantity, totalPrice, driverVehicle, trafficLevel);
    }
}
