
@SuppressWarnings("hiding")
public class DistancePair<String, Double> {

	private final String plate;
    private final double distance;
	
    public DistancePair(String plate, double distance) {
    	
    	this.plate = plate;
    	this.distance = distance;
    	
    }
    
    public void print() {
    	
    	System.out.println("Plate: " + plate + " Distance: " + distance);
    	
    }

	public String getPlate() {
		return plate;
	}

	public double getDistance() {
		return distance;
	}
    
}
