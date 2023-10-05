package controllers;

import dto.CrudUtil;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class VehicleMakeController {
	public JsonArray getAll(Connection connection) throws SQLException, ClassNotFoundException {
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM vehicle_make ");
		ResultSet rst = CrudUtil.executeQuery(connection, query.toString());
		JsonArrayBuilder vehicleArray = Json.createArrayBuilder();
		while (rst.next()) {
			int id = rst.getInt("id");
			String makeName = rst.getString("make_name");

			JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
			objectBuilder.add("id", id);
			objectBuilder.add("makeName", makeName);

			vehicleArray.add(objectBuilder.build());
		}
		return vehicleArray.build();
	}
}
