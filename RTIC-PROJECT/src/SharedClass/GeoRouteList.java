import java.util.ArrayList;


public class GeoRouteList extends ArrayList<GeoRoute> {
	
	private String plate;
	private double totalDis = 0.0;
	private double occDis = 0.0;
	private double vacDis = 0.0;
	
	//private SimulationTaxi st = new SimulationTaxi();
	
	private static final long serialVersionUID = 1L;

	public GeoRouteList(String plate) {
		
		super();
		this.plate = plate;
		
	}
	
	public void print() { 
		
		System.out.println("Plate: " + plate + "\t \t TotalDiatance: " + totalDis + "\t \t OccupyDiatance: " + occDis + "\t \t VacantDiatance: " + vacDis);
		  		 
	}
	
	@ Override
	public boolean add(GeoRoute gr) {
		
		if (this.isEmpty()) {
			
			super.add(gr);
			
		}
		
		else {
			
			GeoRoute temp = get(this.size() - 1);
			
			if(temp.getType() == gr.getType()) {
				
				temp.addDistance(gr);
				temp.setEnd(gr.getEnd());
				super.add(this.size() - 1, temp);
				
			}
			
			else {
				
				super.add(gr);
				
			}
			
		}
		
		totalDis += gr.getDis();
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

}
