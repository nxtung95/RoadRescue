package models;

import java.sql.Timestamp;

public class OtpModel {
    private int otpId;
    private String otpCode;
    private String mobileNo;
    private Timestamp createdTime;
    private Timestamp expiredTime;
    private int isUsed;
    private int wrongAttempts;

    public OtpModel() {
    }

    public OtpModel(int otpId, String otpCode, String mobileNo, Timestamp createdTime, Timestamp expiredTime, int isUsed, int wrongAttempts) {
        this.otpId = otpId;
        this.otpCode = otpCode;
        this.mobileNo = mobileNo;
        this.createdTime = createdTime;
        this.expiredTime = expiredTime;
        this.isUsed = isUsed;
        this.wrongAttempts = wrongAttempts;
    }

    public OtpModel(String otpCode, String mobileNo, Timestamp createdTime, Timestamp expiredTime, int isUsed, int wrongAttempts) {
        this.otpCode = otpCode;
        this.mobileNo = mobileNo;
        this.createdTime = createdTime;
        this.expiredTime = expiredTime;
        this.isUsed = isUsed;
        this.wrongAttempts = wrongAttempts;
    }

    public int getOtpId() {
        return otpId;
    }

    public void setOtpId(int otpId) {
        this.otpId = otpId;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getExpiredTime() {
        return expiredTime;
    }

    public void setExpiredTime(Timestamp expiredTime) {
        this.expiredTime = expiredTime;
    }

    public int getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(int isUsed) {
        this.isUsed = isUsed;
    }

    public int getWrongAttempts() {
        return wrongAttempts;
    }

    public void setWrongAttempts(int wrongAttempts) {
        this.wrongAttempts = wrongAttempts;
    }
}
