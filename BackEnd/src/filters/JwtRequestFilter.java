package filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import controllers.CustomerController;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.function.Function;

@WebFilter(urlPatterns = "/*")
public class JwtRequestFilter implements Filter {
    private static final String REGEX_DEAULT_URL = "/RoadRescue/$";
    private CustomerController customerController = new CustomerController();

    @Resource(name = "java:comp/env/roadRescue")
    DataSource ds;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        HttpServletResponse servletResponse = (HttpServletResponse) response;
        String uri = servletRequest.getRequestURI();
        if (uri.contains("/otp") || uri.matches(REGEX_DEAULT_URL)) {
            chain.doFilter(request, response);
            return;
        }
        if (uri.contains("/customer") && "POST".equalsIgnoreCase(servletRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
        String jwtToken = extractTokenFromRequest(servletRequest);
        JsonObject customer = null;
        if (jwtToken != null && !jwtToken.isEmpty()) {
            try {
                if (!isTokenExpired(jwtToken)) {
                    customer = getUserFromToken(jwtToken);
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            System.out.println("JWT Token does not begin with Bearer String");
        }
        if (customer == null) {
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json");
            servletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            JsonObjectBuilder resp = Json.createObjectBuilder();
            resp.add("status",401);
            resp.add("message","Unauthorized");
            resp.add("data", "JWT token failed");
            writer.print(resp.build());
            return;
        }

        // Once we get the token validate it.
        HttpSession session = servletRequest.getSession();
        if (customer != null && session.isNew()) {
            Connection connection = null;
            try {
                connection = ds.getConnection();
                JsonObject validCus = customerController.getCustomerById(connection, customer.getInt("customerId"));
                if (validCus.getString("mobileNo").equals(customer.getString("mobileNo"))) {
                    session.setAttribute("customerId", validCus.getInt("customerId"));
                }
            } catch (SQLException e) {
                System.out.println(e.getLocalizedMessage());
            } catch (ClassNotFoundException e) {
                System.out.println(e.getLocalizedMessage());
            } finally {
                if (connection != null) {
                    try {
                        connection.close();
                    } catch (SQLException e) {

                    }
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    private boolean isTokenExpired(String jwtToken) {
        final Date expiration = getClaimFromToken(jwtToken, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private JsonObject getUserFromToken(String jwtToken) throws JsonProcessingException {
        String obj = getClaimFromToken(jwtToken, Claims::getSubject);
        JsonReader jsonReader = Json.createReader(new StringReader(obj));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        return object;
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser().setSigningKey("roadRescue@key123").parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = "";
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
        }
        return jwtToken;
    }
}
