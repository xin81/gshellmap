package shell.geometry;

/*
 *  URL:
 ** https://github.com/googlemaps/google-maps-services-java
 ** http://grepcode.com/snapshot/repo1.maven.org/maven2/com.google.code.gson/gson/2.3.1
*/
import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

/**
 * Calculates latitude, longitude and elevation values by a given address or location point.
 * dlat=1°=111 km
 * dl=1°=60 min
 * @author Nguyen Viet Tan
 */
public class GooMetry {
	private String address;
	private String[] addresses;
	private double latitude; // measure north-south position between the poles; equator=0°
	private double longitude; // measure east-west position, regarding Greenwich, England; prime meridian=0°
	private double elevation; //
	private LatLng location; // location point as latitude/longitude values
	
	// private set functions
	private void setAddress(String s){
		address=s;
	}
	private void setLatitude(double d){
		latitude=d;
	}
	private void setLongitude(double d){
		longitude=d;
	}
	private void setElevation(double d){
		elevation=d;
	}
	private void setLocation(LatLng ll){
		location=ll;
	}
	
	public GooMetry(){
		this("");
		final int MAX=10;
		addresses=new String[MAX];
		for(int i=0; i < MAX; i++){
			addresses[i]=new String("nowhere");
		}
	}
	
	/**
	 * Sets some default values, and specifies the address
	 * @param address location point as string
	 */
	public GooMetry(String address){
		setAddress(address);
		setLatitude(0.0);
		setLongitude(0.0);
		setElevation(0.0);
	}
	
	/**
	 * Translates a combination of a latitude -, and longitude value into a human readable address,
	 * and stores each address in a string array
	 */
	public void addAddressByLatLng(final GeoApiContext context,
			final double latitude, final double longitude)throws Exception{
		LatLng location=new LatLng(latitude, longitude);
		GeocodingResult[] result=GeocodingApi.reverseGeocode(context, location).await();
		ElevationResult eresult=ElevationApi.getByPoint(context, location).await();
		
		if(eresult!=null){
			setElevation(eresult.elevation);
		}
		
		int i=0;
		do{
			if(result.length > 0){
				int j=i;
				address=result[i].formattedAddress;
				if(j < addresses.length){
					if(address.isEmpty()==false){
						addresses[j]=address;						
					}
					// j++;
				}
			}
		i++;
		}while(i < result.length);
	}
	
	/**
	 * @return an array of all addresses found by GeocodingApi
	 */
	public String[] getAddresses(){
		return addresses;
	}
	
	/**
	 * Retrieves
	 * <ul>
	 * <li>latitude/longitude values</li>
	 * <li>elevation values</li>
	 * </ul>
	 * 
	 * and updates all corresponding data
	 */
	public void retrieve(final GeoApiContext context){
		try{
			GeocodingResult[] result=GeocodingApi.geocode(context, address).await();
			Geometry geometry=result[0].geometry;
			LatLng latlng=geometry.location;
			ElevationResult eresult=ElevationApi.getByPoint(context, latlng).await();
			setLocation(geometry.location);
			setLatitude(latlng.lat);
			setLongitude(latlng.lng);
			setElevation(eresult.elevation);			
			setAddress(result[0].formattedAddress);
		}catch(Exception e){
			System.err.println(e.getMessage());
		}
	}
	
	// public get functions
	/**
	 * @return address as string
	 */
	public String getAddress(){
		return address;
	}
	
	/**
	 * @return location as a location point
	 */
	public LatLng getLocation(){
		return location;
	}
	
	/**
	 * @return latitude as a double
	 */
	public double getLatitude(){
		return latitude;
	}
	
	/**
	 * @return longitude as a double
	 */
	public double getLongitude(){
		return longitude;
	}
	
	/**
	 * @return elevation as a double
	 */
	public double getElevation(){
		return elevation;
	}
}
