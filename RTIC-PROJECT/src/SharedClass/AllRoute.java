import java.util.ArrayList;


public class AllRoute extends ArrayList<GeoRouteList> {
	
	private static final long serialVersionUID = 1L;

	public AllRoute() {
		
		super();
		
	}
	
	public boolean isAllNull() {
		
		for(GeoRouteList grl : this) {
			
			if(grl != null) return false;
			
		}
		
		return true;
		
	}
	
	public void findTaxi(Pair<String, GeoRoute> pair) {
		
		if(pair != null) {
			
			//pair.print();
			GeoRoute gr = pair.getRight();
			Pair<Double, Double> passengerPoint = gr.getStart();
			MyDate passengerTime = gr.getStartTime();
			double distance = Double.POSITIVE_INFINITY;
			int temp = 0;
			
			Pair<Double, Double> startWay = passengerPoint;
			MyDate beginTime = passengerTime;
			
			for(int i = 0 ; i < this.size() ; i++) {
				
				GeoRouteList grl  = this.get(i);
				if(grl.getTotalDis() == 0) continue;
				
				Pair<Pair<Double, Double>, MyDate> current = grl.getCurrentPosition();
				//System.out.println(grl.getPlate());
				
				if(passengerTime.isAfter(current.getRight())) {
	
					double dis = SharedMethod.haversinDistance(current.getLeft(), passengerPoint);
					double time = SharedMethod.dateDiff(passengerTime, current.getRight(), 0) / 60.0;
					
					if(dis/time  < 2.0 && dis < distance) {
						
						distance = dis;
						temp = i;
						
						startWay = current.getLeft();
						beginTime = current.getRight();
						
					}
					
				}
				
			} // End for
			
			//Add GeoRoute to Passenger
			//System.out.println(get(temp).getPlate());
			GeoRoute toPassenger = new GeoRoute(0, startWay.getLeft(), startWay.getRight(), passengerPoint.getLeft(), passengerPoint.getRight(), beginTime.toString(), passengerTime.toString());
			addGeoRoute(new Pair<String, GeoRoute> (get(temp).getPlate(), toPassenger));
			
			Pair<String, GeoRoute> ans = new Pair<String, GeoRoute> (get(temp).getPlate(), pair.getRight());
			addGeoRoute(ans);
			//ans.print();

			addEmptyGeoRoute(get(temp).getPlate());
			
		}  //End if null
		
	}
	
	public void addGeoRoute(Pair<String, GeoRoute> pair) {
		
		String plate = pair.getLeft();
		GeoRoute gr = pair.getRight();
		
		for(int i = 0 ; i < this.size() ; i++) {
			
			if((this.get(i).getPlate()).equals(plate)) this.get(i).add(gr);
			
		}
		
	}
	
	public void addEmptyGeoRoute(String plate) {
		
		for(int i = 0 ; i < this.size() ; i++) {
			
			GeoRouteList grl = get(i);
			
			if((grl.getPlate()).equals(plate)) {
				
				Pair<Pair<Double, Double>, MyDate> pair = grl.getCurrentPosition();
				
				MyDate start = pair.getRight();				
				Pair<Double, Double> begin = pair.getLeft();				
				GeoRoute empty = new GeoRoute(0, begin.getLeft(), begin.getRight(), begin.getLeft(),  begin.getRight(), start.toString(), start.toString());	
				
				this.get(i).add(empty);
				
			}
			
		}
		
	}
	/*
	public Pair<String, GeoRoute> getGeoRoute(int index) {
		
		//if(plate == null) return null;
		
		GeoRoute gr = null;
		
		for(int i = 0 ; i < this.size() ; i++) {
			
			if(this.get(i) != null) {
				
				if(i == index)  {
					
					gr = this.get(i).getPassenger();
					this.get(i).addPassengerPointer();
					
				}
				
			}
			
		} // End for
		
		//if(gr == null) gr = new GeoRoute(0, 13.73695, 100.61773, 13.73695, 100.61773, "2020-11-11 11:11:11", "2020-11-11 11:11:11");

		return new Pair<String, GeoRoute>(this.get(index).getPlate(), gr);
		
	}
	*/
	public Pair<String, GeoRoute> findFirstPassenger() {
		
		GeoRoute passenger = null;
		int index = 0;
		
		for(int i = 0 ; i < this.size() ; i++) {
			
			if(get(i).getTotalDis() != 0) {
				
				GeoRoute gr = get(i).getPassenger();
	
				if(passenger == null) {
					
					passenger = gr;
					index = i;
					
				}
				
				else {
					
					if(gr != null && passenger.getStartTime().isAfter(gr.getStartTime())) {
						
						passenger = gr;
						index = i;
						
					}
					
				}
				
			}
			
		}  // End for
		
		if(passenger == null) {
			
			//for(GeoRouteList grl : this) if(grl.getTotalDis() != 0) grl.get(grl.size() - 1).print();
			return null;
			
		}
		
		get(index).addPassengerPointer();
		
		return new Pair<String, GeoRoute>(get(index).getPlate(), passenger);
		
	}

}  // End Class
