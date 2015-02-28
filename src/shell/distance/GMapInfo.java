package shell.distance;

import org.joda.time.Instant;

import shell.geometry.GooMetry;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
/*
 * URL:
 ** https://github.com/googlemaps/google-maps-services-java
 ** http://grepcode.com/snapshot/repo1.maven.org/maven2/com.google.code.gson/gson/2.3.1
 *
 * projects:
 ** google-maps-services-java-master
 ** google-gson
 * dependencies: gson-2.3.1.jar
 */
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;

import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Distance;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.DistanceMatrixRow;
import com.google.maps.model.Duration;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

/**
 * Retrieves all requested information on two location points.
 * During the retrieval, this class also takes predefined request settings into consideration 
 */
public class GMapInfo {
	private GMapInfo(){
		//
	}
	public static void retrieveDirections(String apiKey, String origin, String destination, TravelMode mode){
		GeoApiContext context=new GeoApiContext().setApiKey(apiKey);
		DirectionsApiRequest diRequest=
				DirectionsApi.getDirections(context, origin, destination).optimizeWaypoints(true).mode(mode);
		try{
			DirectionsRoute[] routes=diRequest.await();
			GooMetry gmetry=new GooMetry();
			System.out.println(origin);
			for(int r=0; r < routes.length; r++){
				DirectionsLeg[] legs=routes[r].legs;
				for(int l=0; l < legs.length; l++){
					DirectionsStep[]steps=legs[l].steps;
					for(int s=0; s < steps.length; s++){
						LatLng startLatLng=steps[s].startLocation;
						LatLng endLatLng=steps[s].endLocation;
					
						gmetry.addAddressByLatLng(context, startLatLng.lat, endLatLng.lng);
						String[] startAddressess=gmetry.getAddresses();
						if(startAddressess[0].compareToIgnoreCase("nowhere")!=0){
							System.out.println("\n"+s+". "+startAddressess[0]);
						}
					
						gmetry.addAddressByLatLng(context, endLatLng.lat, endLatLng.lng);
						String[] endAddressess=gmetry.getAddresses();
						if(endAddressess[0].compareToIgnoreCase("nowhere")!=0){
							System.out.println("\n"+s+". "+endAddressess[0]);
						}
					}// end for(s)
				}//end for(l)
			} //end for(r)
			System.out.println(destination);
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * As stated before (in the beginning of this document,
	 * you need one API-key from Google API Console (https://code.google.com/apis/console).
	 * After passing your API-key to this method,
	 * GeocodingApi fetches the latitude and longitude values of both the origin address, and the destination address.
	 * These values are evaluated by DistanceMatrixApi in order to calculate the distance between two location points,
	 * and how long it takes to get from one location point to another.
	 * In addition, ElevationApi calculates the elevation value.
	 * This value might be interesting for mountaineers, and strollers who like go for a walk in a forest.
	 */
	public static void retrieve(final String KEY, final String origin, final String destination,
			final String language, final Instant instant, final Unit unit, final TravelMode mode){
		GeoApiContext context=new GeoApiContext().setApiKey(KEY);
		try{
			final int MAX=2;
			GooMetry[] gmetry=new GooMetry[MAX];
			gmetry[0]=new GooMetry(origin);
			gmetry[1]=new GooMetry(destination);
			for(int i=0; i < MAX; i++){
				gmetry[i].retrieve(context);
			}
			
			int i=0;
			int j=1;
			// origin: address(latitude, longitude, elevation)
			System.out.println("\norigin: "+gmetry[i].getAddress()+"("+gmetry[i].getLatitude()+", "+gmetry[i].getLongitude()+", "+gmetry[i].getElevation()+")");
			
			// destination: address(latitude, longitude, elevation)
			System.out.println("destination: "+gmetry[j].getAddress()+"("+gmetry[j].getLatitude()+", "+gmetry[j].getLongitude()+", "+gmetry[j].getElevation()+")\n");
			
			// @return DistanceMatrixApiRequest
			LatLng origin_latlng=gmetry[i].getLocation();
			LatLng destination_latlng=gmetry[j].getLocation();
			DistanceMatrixApiRequest request=
					DistanceMatrixApi.newRequest(context).origins(origin_latlng).destinations(destination_latlng);
			request.language(language);
			request.departureTime(instant);
			request.units(unit); // or IMPERIAL
			request.mode(mode);
			
			// @return distance and duration values
			DistanceMatrix matrix=request.await();
			DistanceMatrixRow[] rows=matrix.rows;			
			DistanceMatrixElement[] elements=rows[0].elements;
			DistanceMatrixElementStatus status=elements[0].status;
			if(status.compareTo(DistanceMatrixElementStatus.OK)==0){
				Distance distance=elements[0].distance;
				Duration duration=elements[0].duration;
				System.out.println("\ndistance: "+distance.humanReadable);
				System.out.println("duration: "+duration.humanReadable);
			}
		}catch(Exception e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}		
	}
}
