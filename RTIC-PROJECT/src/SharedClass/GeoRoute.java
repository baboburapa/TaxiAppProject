public class GeoRoute {
	
	private int type;                         //Occupy = 1, Vacant = 0
	private double dis;
	
	private Pair<Double, Double> start;
	private Pair<Double, Double>  end;	

	private SimulationTaxi st = new SimulationTaxi();
	
	public GeoRoute(int type, double lat, double lng, double Nlat, double Nlng) {
		
		if (type == 0 || type == 1) {
			
			this.type = type;
			this.dis = st.haversinDistance(lat, lng, Nlat, Nlng);
			this.start = new Pair<Double, Double>(lat, lng);
			this.end = new Pair<Double, Double>(Nlat, Nlng);
			
		}
		
	}
	
	public void addDistance(GeoRoute gr) {
		
		dis += gr.getDis();
		
	}
	
	public double getDis() {
		return dis;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Pair<Double, Double> getStart() {
		return start;
	}

	public void setStart(Pair<Double, Double> start) {
		this.start = start;
	}

	public Pair<Double, Double> getEnd() {
		return end;
	}

	public void setEnd(Pair<Double, Double> end) {
		this.end = end;
	}
	
}