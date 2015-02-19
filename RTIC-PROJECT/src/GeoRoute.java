public class GeoRoute {
	
	private String plate;
	private int type;                         //Occupy = 0, Vacant = 1
	private double lat;
	private double lng;
	private double Nlat;
	private double Nlng;
	
	
	public void printGeoRoute() {
		
		System.out.println("Plate: " + plate + ", Type: " + type + ", Lat: " + lat + ", Lng: " + lng + ", NLat: " + Nlat + ", NLng: " + Nlng);
		
	}
	
	public GeoRoute(String plate, int type, double lat, double lng, double Nlat, double Nlng) {
		
		if (type == 0 || type == 1) {
			
			this.plate = plate;
			this.type = type;
			this.lat = lat;
			this.lng = lng;
			this.Nlat = Nlat;
			this.Nlng = Nlng;
			
		}
	}

	public String getPlate() {
		return plate;
	}
	
	public void setPlate(String plate) {
		this.plate = plate;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	public double getNlat() {
		return Nlat;
	}

	public void setNlat(double nlat) {
		Nlat = nlat;
	}

	public double getNlng() {
		return Nlng;
	}

	public void setNlng(double nlng) {
		Nlng = nlng;
	}
	
}