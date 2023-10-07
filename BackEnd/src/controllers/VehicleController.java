package controllers;

import dto.CrudUtil;
import models.Vehicle;

import javax.json.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleController {
    public JsonArray getAllByCustomerId(Connection connection, int customerId) throws SQLException, ClassNotFoundException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.plate_num, a.year, a.type, a.make_id, b.make_name, a.model_id, c.model_name FROM vehicle a ");
        query.append("INNER JOIN vehicle_make b ON a.make_id = b.id ");
        query.append("INNER JOIN vehicle_model c ON a.model_id = c.id ");
        query.append("WHERE a.owner_id = ?");
        ResultSet rst = CrudUtil.executeQuery(connection, query.toString(), customerId);
        JsonArrayBuilder vehicleArray = Json.createArrayBuilder();
        while (rst.next()) {
            String plateNum = rst.getString("plate_num");
            int makeId = rst.getInt("make_id");
            String makeName = rst.getString("make_name");
            int year = rst.getInt("year");
            int modelId = rst.getInt("model_id");
            String modelName = rst.getString("model_name");
            String type = rst.getString("type");

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("plateNum", plateNum);
            objectBuilder.add("makeName", makeName);
            objectBuilder.add("makeId", makeId);
            objectBuilder.add("year", year);
            objectBuilder.add("modelId", modelId);
            objectBuilder.add("modelName", modelName);
            objectBuilder.add("type", type);

            vehicleArray.add(objectBuilder.build());
        }
        return vehicleArray.build();
    }

    public boolean checkExistPlateNum(Connection connection, int customerId, String plateNum) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT  * FROM vehicle WHERE owner_id = ? and plate_num = ?", customerId, plateNum);
        return rst.next();
    }

    public boolean add(Connection connection, Vehicle vehicle) throws SQLException, ClassNotFoundException {
        return  CrudUtil.executeUpdate(connection, "INSERT into vehicle(plate_num, make_id, year, model_id, type, owner_id) values(?,?,?,?,?,?)",
                vehicle.getPlateNum(), vehicle.getMakeId(), vehicle.getYear(), vehicle.getModelId(), vehicle.getType(), vehicle.getOwnerId());
    }

    public boolean delete(Connection connection, String plateNum, int ownerId) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"Delete from vehicle where plate_num = ? and owner_id = ?", plateNum, ownerId);
    }

    public boolean update(Connection connection, Vehicle vehicle) throws SQLException, ClassNotFoundException {
        return CrudUtil.executeUpdate(connection,"UPDATE vehicle SET make_id=?, year=?, model_id=?, type=? WHERE plate_num=? and owner_id = ?",
                vehicle.getMakeId(), vehicle.getYear(), vehicle.getModelId() ,vehicle.getType(), vehicle.getPlateNum(), vehicle.getOwnerId());
    }

    public JsonObject getByCustomerIdAndPlateNum(Connection connection, int customerId, String plateNum) throws SQLException, ClassNotFoundException {
        StringBuilder query = new StringBuilder();
        query.append("SELECT a.plate_num, a.year, a.type, a.make_id, b.make_name, a.model_id, c.model_name FROM vehicle a ");
        query.append("INNER JOIN vehicle_make b ON a.make_id = b.id ");
        query.append("INNER JOIN vehicle_model c ON a.model_id = c.id ");
        query.append("WHERE a.owner_id = ? AND a.plate_num = ?");
        ResultSet rst = CrudUtil.executeQuery(connection, query.toString(), customerId, plateNum);
        if (rst.next()) {
            int makeId = rst.getInt("make_id");
            String makeName = rst.getString("make_name");
            int year = rst.getInt("year");
            int modelId = rst.getInt("model_id");
            String modelName = rst.getString("model_name");
            String type = rst.getString("type");

            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            objectBuilder.add("plateNum", plateNum);
            objectBuilder.add("makeName", makeName);
            objectBuilder.add("makeId", makeId);
            objectBuilder.add("year", year);
            objectBuilder.add("modelId", modelId);
            objectBuilder.add("modelName", modelName);
            objectBuilder.add("type", type);

            return objectBuilder.build();
        }
        return null;
    }
}
