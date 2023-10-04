package models;

import java.sql.Timestamp;

public class CustomerModel {
    private int customerId;
    private String fName;
    private String lName;
    private String contactNum;
    private String email;
    private Timestamp timestamp;

    public CustomerModel(int customerId, String fName, String lName, String contactNum, String email) {
        this.customerId = customerId;
        this.setfName(fName);
        this.setlName(lName);
        this.setContactNum(contactNum);
        this.setEmail(email);
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public CustomerModel(String fName, String lName, String contactNum, String email) {
        this.setfName(fName);
        this.setlName(lName);
        this.setContactNum(contactNum);
        this.setEmail(email);
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public CustomerModel() {
    }

    public int getCustomerId() {
        return this.customerId;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getContactNum() {
        return contactNum;
    }

    public void setContactNum(String contactNum) {
        this.contactNum = contactNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CustomerModel{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", contactNum='" + contactNum + '\'' +
                ", email='" + email + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
