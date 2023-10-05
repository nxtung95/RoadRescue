package controllers;

import dto.CrudUtil;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleModelController {
	public JsonArray getAllByMakeId(Connection connection, int makeId) throws SQLException, ClassNotFoundException {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM vehicle_model WHERE make_id = ? ");
		ResultSet rst = CrudUtil.executeQuery(connection, query.toString(), makeId);
		JsonArrayBuilder vehicleArray = Json.createArrayBuilder();
		while (rst.next()) {
			int id = rst.getInt("id");
			String modelName = rst.getString("model_name");

			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add("id", id);
			objectBuilder.add("modelName", modelName);

			vehicleArray.add(objectBuilder.build());
		}
		return vehicleArray.build();
	}
}
