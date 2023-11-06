import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class Driver {
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		// read the data
		TripPoint.readFile("triplog.csv");
		
		// find data for heuristic 1
		System.out.println("Data for heuristic 1:");
		System.out.println("Number of Stops: " + TripPoint.h1StopDetection());
		System.out.println("Moving Time (hr): " + TripPoint.movingTime());
		System.out.println("Stopped Time (hr): " + TripPoint.stoppedTime());
		System.out.println("Average Moving Speed (km/hr): " + TripPoint.avgMovingSpeed());
		System.out.println();
		
		// find data for heuristic 2
		System.out.println("Data for heuristic 2:");
		System.out.println("Number of Stops: " + TripPoint.h2StopDetection());
		System.out.println("Moving Time (hr): " + TripPoint.movingTime());
		System.out.println("Stopped Time (hr): " + TripPoint.stoppedTime());
		System.out.println("Average Moving Speed (km/hr): " + TripPoint.avgMovingSpeed());
	}
	

//	public static int h2StopDetection() {
//		ArrayList<ArrayList<TripPoint>> numStops = new ArrayList<ArrayList<TripPoint>>();
//		movingTrip = new ArrayList<>(trip);
//		int count=0;
//		for(int i = 0; i < trip.size(); i++) {
//			TripPoint pt1 = trip.get(i);
//			ArrayList<TripPoint> stopZones = new ArrayList<TripPoint>();
//			stopZones.add(pt1);
//			
//		for(int j = i+1; j < trip.size(); j++) {
//			TripPoint pt2 = trip.get(j);
//			double distance = haversineDistance(pt1,pt2);
//			
//			if(distance <= 0.5) {
//				stopZones.add(pt2);
//				movingTrip.remove(pt2);
//			}
//			else {
//				break;
//			}
//		}
//			if(stopZones.size() >= 3) {
//				numStops.add(stopZones);
//				count++;
//			}
//		}
//		return count;
//	}
}
