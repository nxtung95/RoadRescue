package models;

public class VehicleModel {
	private int id;
	private int makeId;
	private String modelName;

	public VehicleModel() {
	}

	public VehicleModel(int id, int makeId, String modelName) {
		this.id = id;
		this.makeId = makeId;
		this.modelName = modelName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMakeId() {
		return makeId;
	}

	public void setMakeId(int makeId) {
		this.makeId = makeId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
