package controllers;

import dto.CrudUtil;
import models.OtpModel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class OtpController {

    public boolean add(Connection connection, OtpModel otpModel) throws SQLException, ClassNotFoundException {
        return  CrudUtil.executeUpdate(connection, "INSERT into otp(otp_code, mobile_no, timestamp, expire_timestamp, is_used, wrong_attempts) values(?,?,?,?,?,?)",
                otpModel.getOtpCode(), otpModel.getMobileNo(), otpModel.getCreatedTime(), otpModel.getExpiredTime(), otpModel.getIsUsed(), otpModel.getWrongAttempts());
    }

    public String confirmOTP(Connection connection, String mobileNo, String otp) throws SQLException, ClassNotFoundException {
        ResultSet rst = CrudUtil.executeQuery(connection, "SELECT * FROM otp WHERE mobile_no = ?", mobileNo);
        List<OtpModel> otpList = new ArrayList<>();
        while (rst.next()){
            int otpId = rst.getInt("otp_id");
            String mobileNoOtp = rst.getString("mobile_no");
            String otpCode = rst.getString("otp_code");
            Timestamp createdDate = rst.getTimestamp("timestamp");
            Timestamp expiredDate = rst.getTimestamp("expire_timestamp");
            int isUsed = rst.getInt("is_used");
            int wrongAttempts = rst.getInt("wrong_attempts");
            otpList.add(new OtpModel(otpId, otpCode, mobileNoOtp, createdDate, expiredDate, isUsed, wrongAttempts));
        }

        if (otpList.isEmpty()) {
            return "";
        }

        // Get the lasted verify number
        LocalDateTime currentTime = LocalDateTime.now();
        otpList = otpList.stream().filter(f -> f.getIsUsed() == 0
                        && currentTime.minusMinutes(10).isBefore(f.getCreatedTime().toLocalDateTime()))
                .collect(Collectors.toList());
        otpList.sort(Comparator.comparing(OtpModel::getCreatedTime).reversed());
        OtpModel otpModel = otpList.stream().findFirst().orElse(null);
        if (Objects.isNull(otpModel)) {
            return "";
        }
        if (otpModel != null && otp.equalsIgnoreCase(otpModel.getOtpCode())) {
            if (otpModel.getExpiredTime().toLocalDateTime().isBefore(LocalDateTime.now())) {
                // otp number expire
                return "01";
            } else {
                // success
                otpModel.setIsUsed(1);
                update(connection, otpModel);
                return "00";
            }
        } else {
            if (otpModel.getWrongAttempts() + 1 >= 3) {
                // Get the 10 minutes before verify otp
                List<OtpModel> listVerifyWith10Min = otpList.stream()
                        .filter(f -> currentTime.minusMinutes(10).isBefore(f.getExpiredTime().toLocalDateTime()))
                        .collect(Collectors.toList());
                listVerifyWith10Min.forEach(f -> f.setIsUsed(1));
                for (OtpModel otpVerify : listVerifyWith10Min) {
                    update(connection, otpVerify);
                }
                return "02"; // Wrong OTP exceed max allow
            } else {
                otpModel.setWrongAttempts(otpModel.getWrongAttempts() + 1);
                update(connection, otpModel);
                return "03"; // Wrong OTP
            }
        }
    }

    private void update(Connection connection, OtpModel otpModel) throws SQLException, ClassNotFoundException {
        CrudUtil.executeUpdate(connection, "UPDATE otp SET otp_code = ?, mobile_no = ?, timestamp = ?, expire_timestamp = ?, is_used = ?, wrong_attempts = ? WHERE otp_id = ?",
                otpModel.getOtpCode(), otpModel.getMobileNo(), otpModel.getCreatedTime(), otpModel.getExpiredTime(), otpModel.getIsUsed(), otpModel.getWrongAttempts(), otpModel.getOtpId());
    }
}
