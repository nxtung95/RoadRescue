package controllers;

import dto.CrudUtil;
import models.CustomerModel;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


public class CustomerController {
    public JsonObject getCustomerById(Connection connection, int customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT * FROM customer WHERE customer_id=?", customerId);
        if (rst.next()) {
            String fName = rst.getString("f_name");           // Assuming the column name is "fName"
            String lName = rst.getString("l_name");           // Assuming the column name is "lName"
            String contactNum = rst.getString("contact_num"); // Assuming the column name is "contactNum"
            String email = rst.getString("email");           // Assuming the column name is "email"

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("customerId", customerId);
            objectBuilder.add("firstName", fName);
            objectBuilder.add("lastName", lName);
            objectBuilder.add("mobileNo", contactNum);
            objectBuilder.add("email", email);

            return objectBuilder.build();
        }
        return null;
    }

    public boolean add(Connection connection,CustomerModel customer) throws SQLException, ClassNotFoundException {
        return  CrudUtil.executeUpdate(connection, "INSERT into customer(f_name, l_name, contact_num, email, timestamp) values(?,?,?,?,?)",
                customer.getfName(), customer.getlName(), customer.getContactNum(), customer.getEmail(), customer.getTimestamp());
    }

    public boolean update(Connection connection,CustomerModel customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE Customer SET f_name=?,l_name=?,email=? WHERE customer_id=?",customer.getfName(),customer.getlName(),customer.getEmail(), customer.getCustomerId());
    }

    public boolean updatePhoneNumber(Connection connection, String newPhoneNumber, int customerId) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE Customer SET contact_num=? WHERE customer_id=?",newPhoneNumber, customerId);
    }

    public boolean checkExistMobileNo(Connection connection, String mobileNo) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT * FROM customer WHERE contact_num=?", mobileNo);
        return rst.next();
    }

    public JsonObject getCustomerByMobileNo(Connection connection, String mobileNo) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT * FROM customer WHERE contact_num=?", mobileNo);
        if (rst.next()) {
            int customerId = rst.getInt("customer_id"); // Assuming the column name is "customerId"
            String fName = rst.getString("f_name");           // Assuming the column name is "fName"
            String lName = rst.getString("l_name");           // Assuming the column name is "lName"
            String contactNum = rst.getString("contact_num"); // Assuming the column name is "contactNum"
            String email = rst.getString("email");           // Assuming the column name is "email"

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("customerId", customerId);
            objectBuilder.add("firstName", fName);
            objectBuilder.add("lastName", lName);
            objectBuilder.add("mobileNo", contactNum);
            objectBuilder.add("email", email);

            return objectBuilder.build();
        }
        return null;
    }
}
