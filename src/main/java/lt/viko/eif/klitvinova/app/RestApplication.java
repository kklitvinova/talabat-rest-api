package lt.viko.eif.klitvinova.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class. Starts Spring Boot with JAX-RS Jersey endpoint.
 *
 * Endpoints:
 *   GET http://localhost:8080/api/orders
 *   GET http://localhost:8080/api/orders/{id}
 *   GET http://localhost:8080/api/orders/city/{city}
 *   GET http://localhost:8080/api/orders/status/{delivered}
 *   GET http://localhost:8080/api/orders/payment/{method}
 *
 * WADL: http://localhost:8080/api/application.wadl
 *
 * @author Klitvinova
 * @version 1.0
 */
@SpringBootApplication(scanBasePackages = "lt.viko.eif.klitvinova")
public class RestApplication {

    /**
     * Application entry point.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(RestApplication.class, args);
        System.out.println("==========================================");
        System.out.println("Service started!");
        System.out.println("Endpoints:");
        System.out.println("  GET http://localhost:8080/api/orders");
        System.out.println("  GET http://localhost:8080/api/orders/{id}");
        System.out.println("  GET http://localhost:8080/api/orders/city/{city}");
        System.out.println("  GET http://localhost:8080/api/orders/status/{delivered}");
        System.out.println("  GET http://localhost:8080/api/orders/payment/{method}");
        System.out.println("WADL: http://localhost:8080/api/application.wadl");
        System.out.println("==========================================");
    }
}
