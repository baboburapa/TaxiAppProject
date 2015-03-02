import java.util.ArrayList;
import java.util.List;


public class GeoRouteList extends ArrayList<GeoRoute> {
	
	private String plate;
	private double totalDis = 0.0;
	private double occDis = 0.0;
	private double vacDis = 0.0;
	private long totalTime = 0;						//In Seconds
	private long occTime = 0;						//In Seconds
	private long vacTime = 0;						//In Seconds
	
	private List<Integer> passengerIndex = new ArrayList<Integer>();
	private int passengerPointer = 0;

	private static final long serialVersionUID = 1L;

	public GeoRouteList(String plate) {
		
		super();
		this.plate = plate;
		
	}
	
	public String toString() {
		
		return "Plate: " + plate + " \t Passengers: " + passengerIndex.size() + 
				"\n TotalDistance: " + totalDis + " km\t OccupiedDistance: " + occDis + " km\t VacantDistance: " + vacDis + " km" + 
				"\n TotalTime: " + (totalTime/60) + " minutes\t OccupiedTime: " + (occTime/60) + " minutes\t VacantTime: " + (vacTime/60) + " minutes";
		
	}
	
	public void print() { 
		
		System.out.println(toString());
		  		 
	}
	
	public boolean isVacant(MyDate date) {
		
		for(GeoRoute gr : this) {
			
			if(date.isBetween(gr.getStartTime(), gr.getEndTime()) && (gr.getType() == 0)) return true;
			
		}
		
		return false;
		
	}
	
	public Pair<Pair<Double, Double>, MyDate>  getCurrentPosition() {
		
		return new Pair<Pair<Double, Double>, MyDate>(get(size() - 1).getEnd(), get(size() - 1).getEndTime());
		
	}
	
	public Pair<Double, Double> getPosition(MyDate date) {
		
		GeoRoute temp = null;
		
		for(int i = 0 ; i < this.size() ; i++) {
			
			GeoRoute gr = this.get(i);
			
			if(date.isBetween(gr.getStartTime(), gr.getEndTime())) temp = gr;
			
		}
		
		return temp.getStart();
		
	}
	
	public GeoRoute getPassenger() {
		
		if(passengerPointer >= passengerIndex.size()) {
			
			return null;
			
		}
		
		int index = passengerIndex.get(passengerPointer);
		
		if(index == 0) {
			
			this.passengerPointer++;
			if(passengerPointer >= passengerIndex.size()) {
				
				return null;
				
			}
			index = passengerIndex.get(passengerPointer);
			
		}
		
		return this.get(index);
		
	}
	
	public void addPassengerPointer() {
		
		this.passengerPointer++;
		
	}

	@ Override
	public boolean add(GeoRoute gr) {
		
		if (this.isEmpty()) {
			
			super.add(gr);
			if(gr.getType() == 1) passengerIndex.add(0);
			
		}
		
		else {
			
			GeoRoute temp = get(this.size() - 1);
			
			if(temp.getType() == gr.getType() && temp.getType() == 1) {
				
				temp.addDistance(gr);
				super.add(this.size() - 1, temp);
				
			}
			
			else if(temp.getType() == 0 && gr.getType() == 1) {
				
				super.add(gr);
				passengerIndex.add(this.size() - 1);
				
			}
			
			else {
				
				super.add(gr);
				
			}
			
		}
		
		totalDis += gr.getDis();
		totalTime += gr.getTime();
		if(gr.getType() == 0) {
			
			vacDis += gr.getDis();
			vacTime += gr.getTime();
			
		}
		if(gr.getType() == 1) {
			
			occDis += gr.getDis();
			occTime += gr.getTime();
			
		}
		
		return true;			
		
	}
	
	public List<Integer> getPassengerIndex() {
		return passengerIndex;
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

	public long getTotalTime() {
		return totalTime;
	}

	public long getOccTime() {
		return occTime;
	}

	public long getVacTime() {
		return vacTime;
	}

	public int getPassengerPointer() {
		return passengerPointer;
	}

}
