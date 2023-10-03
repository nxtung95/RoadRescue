package controllers;

import dto.CrudUtil;
import models.CustomerModel;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class CustomerController {

    public JsonArray getAll(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT  * FROM customer");
        JsonArrayBuilder customerArray = Json.createArrayBuilder();
        while (rst.next()){
            String customerId = rst.getString("customerId");
            String fName = rst.getString("fName");
            String lName = rst.getString("lName");
            String contactNum = rst.getString("contactNum");
            String email = rst.getString("email");
            Timestamp timestamp = rst.getTimestamp("timestamp");

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("customerId", customerId);
            objectBuilder.add("fName", fName);
            objectBuilder.add("lName", lName);
            objectBuilder.add("contactNum", contactNum);
            objectBuilder.add("email", email);
            objectBuilder.add("timestamp", timestamp.toString());

            customerArray.add(objectBuilder.build());
        }
        return customerArray.build();
    }

    public JsonArray getCustomerId(Connection connection) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT customerId FROM customer");
        JsonArrayBuilder arrayBuilder2 = Json.createArrayBuilder();
        while (rst.next()) {
            String id = rst.getString(1);
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("id",id);
            arrayBuilder2.add(objectBuilder.build());
        }
        return arrayBuilder2.build();
    }


    public CustomerModel search(Connection connection, String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT * FROM customer WHERE id=?", id);
        if (rst.next()) {
            String customerId = rst.getString("customerId"); // Assuming the column name is "customerId"
            String fName = rst.getString("fName");           // Assuming the column name is "fName"
            String lName = rst.getString("lName");           // Assuming the column name is "lName"
            String contactNum = rst.getString("contactNum"); // Assuming the column name is "contactNum"
            String email = rst.getString("email");           // Assuming the column name is "email"

            return new CustomerModel(fName, lName, contactNum, email);
        }
        return null;
    }



    public boolean add(Connection connection,CustomerModel customer) throws SQLException, ClassNotFoundException {
        /*return  CrudUtil.executeUpdate(connection, "INSERT  into customer values(?,?,?,?)",customer.getId(),customer.getName(),customer.getAddress(),customer.getSalary());*/

        System.out.println("customer check two");

        return true;
    }

    public boolean delete(Connection connection,String id) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"Delete from Customer where id=?",id);
    }

    public boolean update(Connection connection,CustomerModel customer) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE Customer SET fName=?,lName=?,contactNum=?,email=? WHERE id=?",customer.getfName(),customer.getlName(),customer.getContactNum(),customer.getEmail());
    }

}
