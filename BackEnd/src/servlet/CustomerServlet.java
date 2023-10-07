package servlet;


import controllers.CustomerController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import models.CustomerModel;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = "/customer")
public class CustomerServlet extends HttpServlet {

    @Resource(name = "java:comp/env/roadRescue")
    DataSource ds;

    CustomerController customerController = new CustomerController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        Connection connection = null;
        try {
            String option = req.getParameter("option");
            connection = ds.getConnection();
            switch (option) {
                case "VIEW":
                    HttpSession session = req.getSession();
                    int currentCusId = (int) session.getAttribute("customerId");
                    JsonObject customer = customerController.getCustomerById(connection, currentCusId);
                    if (customer == null) {
                        // Not exist customer in the system
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectBuilder.add("status",400);
                        objectBuilder.add("message","Error");
                        objectBuilder.add("data", "This customer is not registered!");
                        writer.print(objectBuilder.build());
                        return;
                    }
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectBuilder.add("status",200);
                    objectBuilder.add("message","Successfully view...!");
                    objectBuilder.add("data", customer);
                    writer.print(objectBuilder.build());
                    break;
                default:
                    break;
            }
            connection.close();
        } catch (SQLException throwables) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            response.add("status",400);
            response.add("message","Error");
            response.add("data",throwables.getLocalizedMessage());
            writer.print(response.build());
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            response.add("status",400);
            response.add("message","Error");
            response.add("data",e.getLocalizedMessage());
            writer.print(response.build());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        Connection connection = null;
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();
            String option = jsonObject.getString("option");
            switch (option) {
                case "REGISTRATION":
                    String fName = jsonObject.getString("firstName");
                    String lName = jsonObject.getString("lastName");
                    String contactNum = jsonObject.getString("mobileNo");
                    String email = "";

                    connection = ds.getConnection();
                    boolean checkExistMobileNo = customerController.checkExistMobileNo(connection, contactNum);
                    if (checkExistMobileNo) {
                        // Exist mobile no
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectBuilder.add("status",400);
                        objectBuilder.add("message","Error");
                        objectBuilder.add("data", "This phone number have existed!");
                        writer.print(objectBuilder.build());
                        return;
                    }
                    CustomerModel customerDTO = new CustomerModel(fName, lName, contactNum, email);
                    boolean result = customerController.add(connection, customerDTO);
                    if (result){
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_CREATED);
                        objectBuilder.add("status",200);
                        objectBuilder.add("message","Successfully Added...!");
                        objectBuilder.add("data","");
                        writer.print(objectBuilder.build());
                    }
                    break;
                case "LOGIN":
                    contactNum = jsonObject.getString("mobileNo");
                    connection = ds.getConnection();
                    JsonObject customer = customerController.getCustomerByMobileNo(connection, contactNum);
                    if (customer == null) {
                        // Not exist customer in the system
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectBuilder.add("status",400);
                        objectBuilder.add("message","Error");
                        objectBuilder.add("data", "This customer is not registered!");
                        writer.print(objectBuilder.build());
                        return;
                    }
                    //Generate a JWT token
                    Map<String, Object> claims = new HashMap<>();
                    String jwtToken = Jwts.builder().setClaims(claims).setSubject(customer.toString()).setIssuedAt(new Date(System.currentTimeMillis()))
                            .setExpiration(new Date(System.currentTimeMillis() + 18000 * 1000))
                            .signWith(SignatureAlgorithm.HS512, "roadRescue@key123").compact();

                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectBuilder.add("status",200);
                    objectBuilder.add("message","Successfully Login...!");

                    JsonObjectBuilder dataObjectBuilder = Json.createObjectBuilder();
                    dataObjectBuilder.add("token", jwtToken);
                    dataObjectBuilder.add("customer", customer);
                    objectBuilder.add("data", dataObjectBuilder.build());
                    writer.print(objectBuilder.build());
                    break;
                default:
                    break;
            }
        } catch (SQLException throwables) {
            System.out.println("customer check error one");
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectBuilder.add("status",400);
            objectBuilder.add("message","Error");
            objectBuilder.add("data",throwables.getLocalizedMessage());
            writer.print(objectBuilder.build());
            throwables.printStackTrace();
        } catch (ClassNotFoundException a) {
            System.out.println("customer check error two");
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectBuilder.add("status",400);
            objectBuilder.add("message","Error");
            objectBuilder.add("data",a.getLocalizedMessage());
            writer.print(objectBuilder.build());
            a.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }

    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Connection connection = null;
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        try {
            connection = ds.getConnection();
            HttpSession session = req.getSession();
            int customerId = (int) session.getAttribute("customerId");

            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();
            String option = jsonObject.getString("option");
            switch (option) {
                case "UPDATE":
                    String fName = jsonObject.getString("firstName");
                    String lName = jsonObject.getString("lastName");
                    String email = jsonObject.getString("email");
                    CustomerModel customerDTO = new CustomerModel(customerId, fName, lName, email);
                    boolean result = customerController.update(connection, customerDTO);
                    if (result) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("status", 200);
                        objectBuilder.add("message", "Successfully Updated");
                        objectBuilder.add("data", "");
                        writer.print(objectBuilder.build());
                    } else {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectBuilder.add("status", 400);
                        objectBuilder.add("message", "Update Failed");
                        objectBuilder.add("data", "");
                        writer.print(objectBuilder.build());
                    }
                    break;
                case "CHANGE_PHONE":
                    String phoneNumber = jsonObject.getString("mobileNo");
                    result = customerController.updatePhoneNumber(connection, phoneNumber, customerId);
                    if (result) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("status", 200);
                        objectBuilder.add("message", "Successfully Updated");
                        objectBuilder.add("data", "");
                        writer.print(objectBuilder.build());
                    } else {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        objectBuilder.add("status", 400);
                        objectBuilder.add("message", "Update Failed");
                        objectBuilder.add("data", "");
                        writer.print(objectBuilder.build());
                    }
                    break;
                default:
                    break;
            }
        } catch (SQLException throwables) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Exception Error");
            objectBuilder.add("data", throwables.getLocalizedMessage());
            writer.print(objectBuilder.build());
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectBuilder.add("status", 500);
            objectBuilder.add("message", "Exception Error");
            objectBuilder.add("data", e.getLocalizedMessage());
            writer.print(objectBuilder.build());
            e.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException e) {

            }
        }
    }
}
