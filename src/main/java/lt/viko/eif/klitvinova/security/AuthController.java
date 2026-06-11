package lt.viko.eif.klitvinova.security;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * JAX-RS resource for authentication.
 * Handles login and JWT token generation.
 *
 * @author Klitvinova
 * @version 1.0
 */
@Component
@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    private static final String MANAGER_EMAIL = "manager@talabat.com";
    private static final String MANAGER_PASSWORD = "manager123";

    /**
     * Authenticates manager and returns JWT token.
     *
     * @param credentials login credentials
     * @return JWT token or 401
     */
    @POST
    @Path("/login")
    public Response login(Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        if (MANAGER_EMAIL.equals(email) && MANAGER_PASSWORD.equals(password)) {
            String token = jwtUtil.generateToken(email);
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("email", email);
            response.put("role", "MANAGER");
            return Response.ok(response).build();
        }

        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid credentials");
        return Response.status(401).entity(error).build();
    }
}