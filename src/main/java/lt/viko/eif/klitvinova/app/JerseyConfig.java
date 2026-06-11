package lt.viko.eif.klitvinova.app;

import lt.viko.eif.klitvinova.resource.OrderResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

/**
 * Jersey configuration class.
 * Registers REST resources and enables WADL.
 *
 * @author Klitvinova
 * @version 1.0
 */
@Component
public class JerseyConfig extends ResourceConfig {

    /**
     * Registers all JAX-RS resources.
     */
    public JerseyConfig() {
        // register resource class
        register(OrderResource.class);
        // enable WADL generation
        property("jersey.config.server.wadl.disableWadl", false);
    }
}
