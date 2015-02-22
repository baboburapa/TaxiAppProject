import java.util.ArrayList;


public class GeoRouteList extends ArrayList<GeoRoute> {
	
	private String plate;
	private double totalDis = 0.0;
	private double occDis = 0.0;
	private double vacDis = 0.0;
	private long time = 0;						//In Seconds
	
	private Pair<Double, Double> startPoint;
	private Pair<Double, Double> endPoint;
	private MyDate startTime;
	private MyDate endTime;
	private int pointer = 0;

	private static final long serialVersionUID = 1L;

	public GeoRouteList(String plate) {
		
		super();
		this.plate = plate;
		
	}
	
	public void print() { 
		
		System.out.println("Plate: " + plate + "\t TotalDistance: " + totalDis + " km\t OccupiedDistance: " + occDis + 
									" km\t VacantDistance: " + vacDis + " km\t Time: " + (time/60/60) + " hours " + ((time/60)%60) + " minutes");
		  		 
	}
	
	public void findPassenger() {
		
		for(int i = pointer; i < this.size() ; i++) {
			
			GeoRoute gr = this.get(i);
			
			if(gr.getType() == 1) {
				
				startPoint = gr.getStart();
				endPoint = gr.getEnd();
				startTime = gr.getStartTime();
				endTime = gr.getEndTime();
				pointer = i;
				
			}
			
		}
		
	}
	
	public void reset() {
		
		startPoint = (this.get(0)).getStart();
		endPoint = (this.get(0)).getEnd();
		startTime = (this.get(0)).getStartTime();
		endTime = (this.get(0)).getEndTime();
		pointer = 0;
		
	}
	
	@ Override
	public boolean add(GeoRoute gr) {
		
		if (this.isEmpty()) {
			
			super.add(gr);
			startPoint = gr.getStart();
			endPoint = gr.getEnd();
			startTime = gr.getStartTime();
			endTime = gr.getEndTime();
			
		}
		
		else {
			
			GeoRoute temp = get(this.size() - 1);
			
			if(temp.getType() == gr.getType()) {
				
				temp.addDistance(gr);
				super.add(this.size() - 1, temp);
				
			}
			
			else {
				
				super.add(gr);
				
			}
			
		}
		
		totalDis += gr.getDis();
		time += gr.getTime();
		if(gr.getType() == 0) vacDis += gr.getDis();
		if(gr.getType() == 1) occDis += gr.getDis();
		
		return true;			
		
	}

	public String getPlate() {
		return plate;
	}

	public double getTotalDis() {
		return totalDis;
	}

	public double getOccDis() {
		return occDis;
	}

	public double getVacDis() {
		return vacDis;
	}

	public Pair<Double, Double> getStartPoint() {
		return startPoint;
	}
	
	public Pair<Double, Double> getEndPoint() {
		return endPoint;
	}

	public int getPointer() {
		return pointer;
	}

	public long getTime() {
		return time;
	}

	public MyDate getStartTime() {
		return startTime;
	}

	public MyDate getEndTime() {
		return endTime;
	}
	
}
