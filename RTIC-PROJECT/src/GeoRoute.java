public class GeoRoute {
	
	private int type;                         //Occupy = 0, Vacant = 1
	private double lat;
	private double lng;
	private double Nlat;
	private double Nlng;
	
	public GeoRoute(int type, double lat, double lng, double Nlat, double Nlng) {
		
		if (type == 0 || type == 1) {
			
			this.type = type;
			this.lat = lat;
			this.lng = lng;
			this.Nlat = Nlat;
			this.Nlng = Nlng;
			
		}
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