package servlet;

import controllers.VehicleController;
import models.VehicleModel;

import javax.annotation.Resource;
import javax.json.*;
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

@WebServlet(urlPatterns = "/vehicle")
public class VehicleServlet extends HttpServlet {
    @Resource(name = "java:comp/env/roadRescue")
    DataSource ds;

    private VehicleController vehicleController = new VehicleController();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        Connection connection = null;
        try {
            connection = ds.getConnection();
            // Get customerId when sign in
            HttpSession session = req.getSession();
            int customerId = (int) session.getAttribute("customerId");
            JsonArray allVehicles = vehicleController.getAllByCustomerId(connection, customerId);
            JsonObjectBuilder response = Json.createObjectBuilder();
            response.add("status",200);
            response.add("message","Done");
            response.add("data", allVehicles);
            writer.print(response.build());
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String plateNum = jsonObject.getString("plateNum");
        String make = jsonObject.getString("make");
        int year = jsonObject.getInt("year");
        String model = jsonObject.getString("model");
        String type = jsonObject.getString("type");

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        Connection connection = null;
        try {
            connection = ds.getConnection();
            HttpSession session = req.getSession();
            int customerId = (int) session.getAttribute("customerId");
            VehicleModel vehicleModel = new VehicleModel(plateNum, make, year, model, type, customerId);

            if (vehicleController.checkExistPlateNum(connection, vehicleModel.getOwnerId(), vehicleModel.getPlateNum())) {
                //Exist plate num
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "The plate num have existed!");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
                return;
            }

            boolean result = vehicleController.add(connection, vehicleModel);

            if (result) {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_CREATED);
                objectBuilder.add("status",200);
                objectBuilder.add("message","Successfully Added...!");
                objectBuilder.add("data","");
                writer.print(objectBuilder.build());
            } else {
                JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                objectBuilder.add("status", 400);
                objectBuilder.add("message", "Added Failed");
                objectBuilder.add("data", "");
                writer.print(objectBuilder.build());
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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }

    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        Connection connection = null;
        try {
            connection = ds.getConnection();
            String plateNum = req.getParameter("plateNum");
            HttpSession session = req.getSession();
            int customerId = (int) session.getAttribute("customerId");
            boolean result = vehicleController.delete(connection, plateNum, customerId);
            if (result) {
                JsonObjectBuilder response = Json.createObjectBuilder();
                response.add("status",200);
                response.add("message","Vehicle Deleted!");
                response.add("data","");
                writer.print(response.build());
            } else {
                JsonObjectBuilder response = Json.createObjectBuilder();
                resp.setStatus(HttpServletResponse.SC_OK);
                response.add("status",400);
                response.add("message","Wrong ID!");
                response.add("data","");
                writer.print(response.build());
            }
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
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JsonReader reader = Json.createReader(req.getReader());
        JsonObject jsonObject = reader.readObject();
        String plateNum = jsonObject.getString("plateNum");
        String make = jsonObject.getString("make");
        int year = jsonObject.getInt("year");
        String model = jsonObject.getString("model");
        String type = jsonObject.getString("type");

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        Connection connection = null;
        try {
            connection = ds.getConnection();
            HttpSession session = req.getSession();
            int customerId = (int) session.getAttribute("customerId");
            VehicleModel vehicleModel = new VehicleModel(plateNum, make, year, model, type, customerId);
            boolean result = vehicleController.update(connection, vehicleModel);

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
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {

                }
            }
        }
    }
}
