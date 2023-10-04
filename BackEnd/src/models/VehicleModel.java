package models;

public class VehicleModel {
    private String plateNum;
    private String make;
    private int year;
    private String model;
    private String type;
    private int ownerId;

    public VehicleModel() {
    }

    public VehicleModel(String plateNum, String make, int year, String model, String type, int ownerId) {
        this.plateNum = plateNum;
        this.make = make;
        this.year = year;
        this.model = model;
        this.type = type;
        this.ownerId = ownerId;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }
}
