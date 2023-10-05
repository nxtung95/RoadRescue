package models;

public class VehicleMakeModel {
	private int id;
	private String makeName;

	public VehicleMakeModel() {
	}

	public VehicleMakeModel(int id, String makeName) {
		this.id = id;
		this.makeName = makeName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMakeName() {
		return makeName;
	}

	public void setMakeName(String makeName) {
		this.makeName = makeName;
	}
}
