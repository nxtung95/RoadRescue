package controllers;

import dto.CrudUtil;
import models.VehicleModel;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleController {
    public JsonArray getAllByCustomerId(Connection connection, int customerId) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT  * FROM vehicle WHERE owner_id = ?", customerId);
        JsonArrayBuilder vehicleArray = Json.createArrayBuilder();
        while (rst.next()) {
            String plateNum = rst.getString("plate_num");
            String make = rst.getString("make");
            int year = rst.getInt("year");
            String model = rst.getString("model");
            String type = rst.getString("type");

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("plateNum", plateNum);
            objectBuilder.add("make", make);
            objectBuilder.add("year", year);
            objectBuilder.add("model", model);
            objectBuilder.add("type", type);

            vehicleArray.add(objectBuilder.build());
        }
        return vehicleArray.build();
    }

    public boolean add(Connection connection, VehicleModel vehicle) throws SQLException, ClassNotFoundException {
        return  CrudUtil.executeUpdate(connection, "INSERT into vehicle(plate_num, make, year, model, type, owner_id) values(?,?,?,?)",
                vehicle.getPlateNum(), vehicle.getMake(), vehicle.getYear(), vehicle.getModel(), vehicle.getType(), vehicle.getOwnerId());
    }

    public boolean delete(Connection connection, String plateNum, int ownerId) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"Delete from vehicle where plate_num = ? and owner_id = ?", plateNum, ownerId);
    }

    public boolean update(Connection connection, VehicleModel vehicle) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE vehicle SET make=?, year=?, model=?, type=?, owner_id=? WHERE plate_num=? and owner_id = ?",
                vehicle.getMake(), vehicle.getYear(), vehicle.getModel() ,vehicle.getType(), vehicle.getPlateNum(), vehicle.getOwnerId());
    }
}
