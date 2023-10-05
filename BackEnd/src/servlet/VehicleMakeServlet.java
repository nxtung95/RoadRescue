package servlet;

import controllers.VehicleMakeController;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObjectBuilder;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet(urlPatterns = "/vehicle/make")
public class VehicleMakeServlet extends HttpServlet {
	@Resource(name = "java:comp/env/roadRescue")
	DataSource ds;

	private VehicleMakeController vehicleController = new VehicleMakeController();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		PrintWriter writer = resp.getWriter();
		resp.setContentType("application/json");
		Connection connection = null;
		try {
			connection = ds.getConnection();
			JsonArray allMakeVehicles = vehicleController.getAll(connection);
			JsonObjectBuilder response = Json.createObjectBuilder();
			response.add("status",200);
			response.add("message","Done");
			response.add("data", allMakeVehicles);
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
}
