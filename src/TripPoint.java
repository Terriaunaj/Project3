import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Scanner;

/**
 * 
 * @author terriaunajames
 *
 */
public class TripPoint {

	/**
	 * instance variables
	 */
	private double lat;	// latitude
	private double lon;	// longitude
	private int time;	// time in minutes
	
	/**
	 * ArrayList of tripPoints for the entire trip and for the moving trip
	 */
	private static ArrayList<TripPoint> trip;	// ArrayList of every point in a trip
	private static ArrayList<TripPoint> movingTrip;
	
	/**
	 * default constructor if given no parameters
	 */
	// default constructor
	public TripPoint() {
		time = 0;
		lat = 0.0;
		lon = 0.0;
	}
	
	/**
	 * 
	 * @param time
	 * value of time
	 * @param lat
	 * latitude value
	 * @param lon
	 * longitude value
	 * 
	 * 1. constructor for the values of time, latitude, and longitude
	 */
	// constructor given time, latitude, and longitude
	public TripPoint(int time, double lat, double lon) {
		this.time = time;
		this.lat = lat;
		this.lon = lon;
	}
	
	/**
	 * 
	 * @return
	 * 1. returns the time
	 */
	// returns time
	public int getTime() {
		return time;
	}
	
	/**
	 * 
	 * @return
	 * 1. returns the latitude value
	 */
	// returns latitude
	public double getLat() {
		return lat;
	}
	
	/**
	 * 
	 * @return
	 * 1. returns the longitude value
	 */
	// returns longitude
	public double getLon() {
		return lon;
	}
	
	/**
	 * 
	 * @return
	 * 1. returns a copy of the trip arraylist
	 */
	// returns a copy of trip ArrayList
	public static ArrayList<TripPoint> getTrip() {
		return new ArrayList<>(trip);
	}
	
	/**
	 * 
	 * @return
	 * 1. returns a copy of the movingTrip arrayList
	 */
	public static ArrayList<TripPoint> getMovingTrip(){
		return new ArrayList<>(movingTrip);
	}
	
	/**
	 * 
	 * @param first
	 * The first tripPoint
	 * @param second
	 * The second tripPoint
	 * @return
	 * 1. Finds and returns distance between two points
	 */
	// uses the haversine formula for great sphere distance between two points
	public static double haversineDistance(TripPoint first, TripPoint second) {
		// distance between latitudes and longitudes
		double lat1 = first.getLat();
		double lat2 = second.getLat();
		double lon1 = first.getLon();
		double lon2 = second.getLon();
		
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
 
        // convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        // apply formulae
        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.pow(Math.sin(dLon / 2), 2) *
                   Math.cos(lat1) *
                   Math.cos(lat2);
        double rad = 6371;
        double c = 2 * Math.asin(Math.sqrt(a));
        return rad * c;
	}
	
	/**
	 * 
	 * @param a
	 * The first tripPoint
	 * @param b
	 * The second tripPoint
	 * @return
	 * Finds and returns the average speed between to tripPoints in the trip
	 */
	// finds the average speed between two TripPoints in km/hr
	public static double avgSpeed(TripPoint a, TripPoint b) {
		
		int timeInMin = Math.abs(a.getTime() - b.getTime());
		
		double dis = haversineDistance(a, b);
		
		double kmpmin = dis / timeInMin;
		
		return kmpmin*60;
	}
	
	/**
	 * 
	 * @return
	 * average moving speed
	 * @throws FileNotFoundException
	 * file is read in total distance
	 * @throws IOException
	 * file is read in total distance method
	 * 
	 * 1. finds and returns the average speed while moving in the entire trip
	 */
	public static double avgMovingSpeed() throws FileNotFoundException, IOException {
		return totalDistance()/movingTime();
	}
	
	/**
	 * 
	 * @return
	 * returns the total time of the entire trip
	 */
	// returns the total time of trip in hours
	public static double totalTime() {
		int minutes = trip.get(trip.size()-1).getTime();
		double hours = minutes / 60.0;
		return hours;
	}
	
	/**
	 * 
	 * @return
	 * 1. returns the amount of time spent moving over the entire trip
	 */
	public static double movingTime() {
		int minutes = movingTrip.get(movingTrip.size()-1).getTime();
		double hours = minutes / 60.0;
		return hours;
	}
	
	/**
	 * 
	 * @return
	 * 1. returns the total time stopped throughout the entire trip
	 */
	public static double stoppedTime() {
		return h1StopDetection()/60.0;	
	}
	
	/**
	 * 
	 * @return
	 * total distance
	 * @throws FileNotFoundException
	 * If file is null
	 * @throws IOException
	 * If fill is null
	 * 
	 * 1. finds and returns the total distance of the trip
	 */
	// finds the total distance traveled over the trip
	public static double totalDistance() throws FileNotFoundException, IOException {
		
		double distance = 0.0;
		
		if (trip.isEmpty()) {
			readFile("triplog.csv");
		}
		
		for (int i = 1; i < trip.size(); ++i) {
			distance += haversineDistance(trip.get(i-1), trip.get(i));
		}
		
		return distance;
	}
	
/**
 * 
 * @param filename
 * Given file
 * @throws FileNotFoundException
 * If file is null
 * @throws IOException
 * if file is null
 * 
 * 1. reads the triplog file
 * 2. gets data from file
 * 3. adds data to trip arraylist
 */
	public static void readFile(String filename) throws FileNotFoundException, IOException {

		// construct a file object for the file with the given name.
		File file = new File(filename);

		// construct a scanner to read the file.
		Scanner fileScanner = new Scanner(file);
		
		// initiliaze trip
		trip = new ArrayList<TripPoint>();

		// create the Array that will store each lines data so we can grab the time, lat, and lon
		String[] fileData = null;

		// grab the next line
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();

			// split each line along the commas
			fileData = line.split(",");

			// only write relevant lines
			if (!line.contains("Time")) {
				// fileData[0] corresponds to time, fileData[1] to lat, fileData[2] to lon
				trip.add(new TripPoint(Integer.parseInt(fileData[0]), Double.parseDouble(fileData[1]), Double.parseDouble(fileData[2])));
				//movingTrip.add(new TripPoint(Integer.parseInt(fileData[0]), Double.parseDouble(fileData[1]), Double.parseDouble(fileData[2])));

			}
		}

		// close scanner
		fileScanner.close();
	}

	/**
	 * 
	 * @return
	 * 1.Number of stops where one point and its previous point
	 * are within 0.6km distance
	 * 2.Removes the stops from the moving trip arraylist
	 */
	public static int h1StopDetection() {		
		ArrayList<TripPoint> numStops = new ArrayList<TripPoint>();
		movingTrip = new ArrayList<>(trip);
		for(int i = 1; i < trip.size(); i++) {
			TripPoint pt1 = trip.get(i-1);
			TripPoint pt2 = trip.get(i);
			
			double distance = haversineDistance(pt1,pt2);
			
			if(distance <= 0.6) {
				numStops.add(pt1);
				movingTrip.remove(pt2);
			}
		}
		return numStops.size();	
	}
	
	/**
	 * 
	 * @return
	 * 1.Three or more points that are within the same stop radius of 0.5km
	 * 2.Removes stops from moving trip arraylist
	 */
	public static int h2StopDetection() {
		ArrayList<ArrayList<TripPoint>> numStops = new ArrayList<ArrayList<TripPoint>>();
		ArrayList<TripPoint> stopZones = new ArrayList<>();
		ArrayList<TripPoint> newStopZone = new ArrayList<>();
		HashSet<TripPoint> ptRemove = new LinkedHashSet<>();
		boolean added = false;
		movingTrip = new ArrayList<>(trip);
		int count=0;
		
		for(TripPoint current: movingTrip) {
			added = false;
			
			for(ArrayList<TripPoint> next : numStops) {
			
				for(TripPoint point : stopZones) {
					
				double newDist = haversineDistance(current,point);
			
				if(newDist <= 0.5) {
					added = true;
					stopZones.add(current);
					break;
				}
			}
		}
			if(added == false) {
				newStopZone.add(current);
				numStops.add(newStopZone);
			}
		}
		for(ArrayList<TripPoint> pt : numStops) {
			if(numStops.size() >= 3) {
				ptRemove.addAll(pt);
				count+=pt.size();
			}
		}
		movingTrip.removeAll(ptRemove);
		return count;
	}
}
