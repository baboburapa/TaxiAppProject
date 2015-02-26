public class GeoRoute {
	
	private int type;                         //Occupy = 1, Vacant = 0
	private double dis;
	private long time;                     //In Seconds

	private Pair<Double, Double> start;
	private Pair<Double, Double>  end;	
	
	private MyDate startTime;
	private MyDate endTime;
	
	public GeoRoute(int type, double lat, double lng, double Nlat, double Nlng,String beginTime, String stopTime) {
		
		if (type == 0 || type == 1) {
			
			this.type = type;
			this.dis = SharedMethod.haversinDistance(lat, lng, Nlat, Nlng);
			this.start = new Pair<Double, Double>(lat, lng);
			this.end = new Pair<Double, Double>(Nlat, Nlng);
			this.startTime = new MyDate(beginTime);
			this.endTime = new MyDate(stopTime);
			this.time = SharedMethod.dateDiff(endTime, startTime, 0);
			if(time < 0) print();
			
		}
		
	}
	
	public boolean isEmptyGeoRoute() {
		
		if(type == 0 && dis == 0.0 && time == 0) return true;
		
		return false;
		
	}
	
	public void print() {
		
		System.out.println(toString());
		
	}
	
	public String toString() {
		
		return "Start: " + start.toString() + " " + startTime.toString() + " End: " + end.toString() + 
					" " + endTime.toString() + " Type: " + type + " Distance: " + dis + " km";
		
	}
	
	/**
	 * Add Distance and Also Change End Point for same Type
	 * @param gr
	 */
	public void addDistance(GeoRoute gr) {
		
		if(this.getType() == gr.getType()) {
			
			dis += gr.getDis();
			time += gr.getTime();
			end = gr.getEnd();
			endTime = gr.getEndTime();
			
		}
		
	}
	
	public long getTime() {
		return time;
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

	public Pair<Double, Double> getEnd() {
		return end;
	}

	public MyDate getStartTime() {
		return startTime;
	}

	public MyDate getEndTime() {
		return endTime;
	}
	
}