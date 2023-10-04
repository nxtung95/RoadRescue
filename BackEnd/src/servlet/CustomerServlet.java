package servlet;


import controllers.CustomerController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import models.CustomerModel;

import javax.annotation.Resource;
import javax.json.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        try {
            String option = req.getParameter("option");
            String searchId = req.getParameter("searchId");
            Connection connection = ds.getConnection();

            switch (option) {

                case "SEARCH":

                    CustomerModel searchCustomer = customerController.search(connection, searchId);
                    if (searchCustomer!=null) {
                        int customerId = searchCustomer.getCustomerId();
                        String fName = searchCustomer.getfName();
                        String lName = searchCustomer.getlName();
                        String contactNum = searchCustomer.getContactNum();
                        String email = searchCustomer.getEmail();
                        Timestamp timestamp = searchCustomer.getTimestamp();

                        JsonObjectBuilder customerData = Json.createObjectBuilder();
                        customerData.add("customerId",customerId);
                        customerData.add("fName",fName);
                        customerData.add("lName",lName);
                        customerData.add("contactNum",contactNum);
                        customerData.add("email", email);
                        customerData.add("timestamp", timestamp.toString());

                        JsonObjectBuilder searchResponse = Json.createObjectBuilder();
                        searchResponse.add("status",200);
                        searchResponse.add("message","Customer found");
                        searchResponse.add("data",customerData.build());

                        writer.print(searchResponse.build());
                    }else {
                        JsonObjectBuilder searchResponse = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_OK);
                        searchResponse.add("status",404);
                        searchResponse.add("message","Customer is not found");
                        searchResponse.add("data","");
                        writer.print(searchResponse.build());
                    }

                    break;
                case "GETALL":

                    /*ResultSet rst = connection.prepareStatement("SELECT  * FROM customer").executeQuery();
                    JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
                    while (rst.next()){
                        String id = rst.getString(1);
                        String name = rst.getString(2);
                        String address = rst.getString(3);
                        double salary = rst.getDouble(4);

                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        objectBuilder.add("id",id);
                        objectBuilder.add("name",name);
                        objectBuilder.add("address",address);
                        objectBuilder.add("salary",salary);
                        arrayBuilder.add(objectBuilder.build());
                    }*/


                    JsonArray allCustomers = customerController.getAll(connection);
                    JsonObjectBuilder response = Json.createObjectBuilder();
                    response.add("status",200);
                    response.add("message","Done");
                    response.add("data",allCustomers);
                    writer.print(response.build());
                    break;

                case "GetCustomerID":
                    /*ResultSet result = connection.prepareStatement("SELECT  id FROM customer").executeQuery();
                    JsonArrayBuilder cstIdArray = Json.createArrayBuilder();
                    while (result.next()){
                        String cstId = result.getString(1);

                        JsonObjectBuilder cstIdObject = Json.createObjectBuilder();
                        cstIdObject.add("id",cstId);
                        cstIdArray.add(cstIdObject.build());
                    }*/


                    JsonArray allCustomersId = customerController.getCustomerId(connection);
                    JsonObjectBuilder responseGetCstId = Json.createObjectBuilder();
                    responseGetCstId.add("status",200);
                    responseGetCstId.add("message","Done");
                    responseGetCstId.add("data",allCustomersId);
                    writer.print(responseGetCstId.build());
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String cstId = req.getParameter("customerId");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        try {
            Connection connection = ds.getConnection();

            boolean result = customerController.delete(connection, cstId);
            /*PreparedStatement pst = connection.prepareStatement("Delete from Customer where id=?");
            pst.setObject(1, cstId);*/

            if (result) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status",200);
                response.add("message","Customer Deleted!");
                response.add("data","");
                writer.print(response.build());
            }else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                response.add("status",400);
                response.add("message","Wrong ID!");
                response.add("data","");
                writer.print(response.build());
            }
            connection.close();
        } catch (SQLException throwables) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            response.add("status",500);
            response.add("message","Error");
            response.add("data",throwables.getLocalizedMessage());
            writer.print(response.build());
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            JsonObjectBuilder response = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            response.add("status",500);
            response.add("message","Error");
            response.add("data",e.getLocalizedMessage());
            writer.print(response.build());
            e.printStackTrace();
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String fName = jsonObject.getString("fName");
        String lName = jsonObject.getString("lName");
        String contactNum = jsonObject.getString("contactNum");
        String email = jsonObject.getString("email");

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        try {
            Connection connection = ds.getConnection();
            CustomerModel customerDTO = new CustomerModel(fName, lName, contactNum, email);
            boolean result = customerController.update(connection, customerDTO);

            /*PreparedStatement stm = connection.prepareStatement("UPDATE customer SET name=?,address=?,salary=? where id=?");
            stm.setString(1,customerName);
            stm.setString(2,customerAddress);
            stm.setDouble(3,customerSalary);
            stm.setString(4,customerId);*/

            if (result) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                objectBuilder.add("status", 200);
                objectBuilder.add("message", "Successfully Updated");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Update Failed");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
            }
            connection.close();
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
        }
    }
}
