package servlet;

import controllers.OtpController;
import models.OtpModel;
import utils.CommonUtils;

import javax.annotation.Resource;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@WebServlet(urlPatterns = "/otp")
public class OTPServlet extends HttpServlet {
    @Resource(name = "java:comp/env/roadRescue")
    DataSource ds;

    private OtpController otpController = new OtpController();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        Connection connection = null;
        try {
            JsonReader reader = Json.createReader(req.getReader());
            JsonObject jsonObject = reader.readObject();
            String option = jsonObject.getString("option");
            connection = ds.getConnection();
            switch (option) {
                case "SEND_OTP":
                    String mobileNo = jsonObject.getString("mobileNo");
                    String otp = CommonUtils.generateRandomNum(6);
                    System.out.println("OTP: " + otp);
                    Timestamp now = Timestamp.valueOf(LocalDateTime.now());
                    Timestamp expireDate = Timestamp.valueOf(LocalDateTime.now().plus(5, ChronoUnit.MINUTES));
                    OtpModel otpModel = new OtpModel(otp, mobileNo, now, expireDate, 0, 0);
                    boolean result = otpController.add(connection, otpModel);
                    if (result) {
                        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                        resp.setStatus(HttpServletResponse.SC_CREATED);
                        objectBuilder.add("status",200);
                        objectBuilder.add("message","Send otp successfully!");
                        objectBuilder.add("data","");
                        writer.print(objectBuilder.build());
                    }
                    break;
                case "CONFIRM_OTP":
                    mobileNo = jsonObject.getString("mobileNo");
                    otp = jsonObject.getString("otp");
                    String code = otpController.confirmOTP(connection,mobileNo, otp);
                    JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
                    resp.setStatus(HttpServletResponse.SC_OK);
                    objectBuilder.add("status",200);
                    if (code == null || "".equals(code)) {
                        objectBuilder.add("message","The system cannot verify OTP");
                    } else if ("01".equals(code)) {
                        objectBuilder.add("message","Expired OTP. Please request for new OTP");
                    } else if ("02".equals(code)) {
                        objectBuilder.add("message","Wrong attempts OTP more than three times.");
                    } else if ("03".equals(code)) {
                        objectBuilder.add("message","Incorrect OTP");
                    } else if ("00".equals(code)) {
                        // Success confirm OTP
                        objectBuilder.add("message","Confirm OTP successfully");
                    }
                    objectBuilder.add("code", code);
                    objectBuilder.add("data","");
                    writer.print(objectBuilder.build());
                    break;
                default:
                    break;
            }
        } catch (SQLException throwables) {
            JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectBuilder.add("status",400);
            objectBuilder.add("message","Error");
            objectBuilder.add("data",throwables.getLocalizedMessage());
            writer.print(objectBuilder.build());
            throwables.printStackTrace();
        } catch (ClassNotFoundException a) {
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
}
