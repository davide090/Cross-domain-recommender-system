package uniroma3.it.sii;

public class NameDistanceRow {


	private String name;
	private double distance;

	public NameDistanceRow(String name , double distance) {

		this.name = name;
		this.distance = distance;
	}





	//getters & setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

}
