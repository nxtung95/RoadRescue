package models;

public class Vehicle {
    private String plateNum;
    private int makeId;
    private int year;
    private int modelId;
    private String type;
    private int ownerId;

    public Vehicle() {
    }

    public Vehicle(String plateNum, int makeId, int year, int modelId, String type, int ownerId) {
        this.plateNum = plateNum;
        this.makeId = makeId;
        this.year = year;
        this.modelId = modelId;
        this.type = type;
        this.ownerId = ownerId;
    }

    public int getMakeId() {
        return makeId;
    }

    public void setMakeId(int makeId) {
        this.makeId = makeId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
