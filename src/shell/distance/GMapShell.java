package shell.distance;
/*
Copyright [2015] [Nguyen Viet Tan]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License. 
*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

// URL: https://github.com/JodaOrg/joda-time
// dependencies: joda-time-2.7.jar
import org.joda.time.Instant;

import shell.PropertyReader;

/*
 *  URL:
 ** https://github.com/googlemaps/google-maps-services-java
 ** http://grepcode.com/snapshot/repo1.maven.org/maven2/com.google.code.gson/gson/2.3.1
*/
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

/**
 * Sends queries to <a href="https://developers.google.com/maps/">Google Distance Matrix API</a>
 * and <a href="https://developers.google.com/maps/">Google Geocoding API</a>,
 * and receives response values in return.
 * In order to use this program, you need a developer account on
 * <a href="https://developers.google.com">Google API Console</a>,
 * and one API key to pass as a value for the <em>apiKey</em> parameter.
 *  
 * To get this source code compiled, follow the instructions in <a href="../../installation/ReadMe.html">ReadMe.html</a>
 * @author Nguyen V. Tan
 * @since 24/02/2015
 * @version 1.0
 */
public class GMapShell{
	private GMapShell(){
		
	}
	/* information on how to use this program */
	private static void printHelp(final PrintStream out){
		// java -cp gmapshell_lib -jar gmapshell.jar shell.GMapShell --PARAMETERS VALUES
		String jarfile="gshellmap.jar";
		String cp="$CLASSPATH";
		out.println("usage: java -cp "+cp+":"+jarfile+" "+GMapShell.class.getName()+" PARAMETERS|[Optional Parameters]");
		out.println("PARAMETERS:");
		// out.println("--apiKey\t[Your_Google_API_KEY]");		
		out.println("\nOptional Parameters:");
		out.println("--help\tprints this help information");
		out.println("--language\t[de, en, en-GB, en-AU, es, fr, ...]");
		
		String url="https://developers.google.com/maps/faq#languagesupport";
		out.println("\tsee also "+url);
		
		out.println("--unit\t[IMPERIAL (in feet, or miles)|METRIC (in metres, or kilometres)]");
		out.println("--mode\t[driving|bicycling|walking|transit]");
		out.println("--fuzzy\t[false|true]; if true, simple string queries can be performed  (e. g. origin address: England, destination address: Germany");
		out.println("--routes\t[false|true]; if set true, all routes (directions) from the start address to the end address are listed");
	}
	

	
	/**
	 * Executes this program by this following command:
	 * <code>java -cp $CLASSPATH:$main_jarfile shell.distance.GMapShell --apiKey AIza..YOUR_OWN_API-KEY...</code>;
	 * whereas $CLASSPATH contains all required .jar files
	 * <ul>
	 * <li><a href="https://android.googlesource.com/platform/external/okhttp/+/master">okhttp-2.2.0.jar</a></li>
	 * <li><a href="http://grepcode.com/snapshot/repo1.maven.org/maven2/com.google.code.gson/gson/2.3.1">gson-2.3.1.jar</a></li>
	 * <li><a href="https://github.com/JodaOrg/joda-time">joda-time-2.7.jar</a></li>
	 * <li>gmapservice.jar</li>
	 * <li>okio.jar</li>
	 * <li>$main_jarfile</li>
	 * </ul>
	 * and $main_jarfile is the actual .jar file (in the current version (since February 2015,
	 * it's called <b>gshellmap.jar</b>)
	 * which contains shell/GMapShell.class. As this document implies (-cp $CLASSPATH<b>:$main_jarfile</b>),
	 * $main_jarfile must be added to the $CLASSPATH, since this .jar file contains more than one main class,
	 * and it's not a directly executable .jar file.   
	 * For help, type --help right after the class name (in this case "shell.distance.GMapShell")
	 * 
	 * If you want to, you can use these following options:<br />
	 * --help<br />
	 * --language determines the response language<br />
	 * --unit IMPERIAL (in feet or miles)|METRIC (in metres or kilometres)<br />
	 * --mode determines the way of travel (driving|walking|bicycling|transit)
	 * --fuzzy false|true:<br /> 
	 * determines whether the user wants to enter
	 * simple names of places (details like street number, or postal code are not required),
	 * or detailed address information.
	 * if fuzzy is false, the user will have to give geographic data like
	 * <ul>
	 * <li>country</li>
	 * <li>town</li>
	 * <li>postal code</li>
	 * <li>street</li>
	 * <li>street number</li>
	 * </ul>
	 * --routes false|true:<br />
	 * if set to true, all routes from the start address to the end address are listed.
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// requirements
		String KEY="************************************";
		KEY=PropertyReader.getPropertyValue("txt/edit_me.txt", "apiKey");
		String language="en-GB";
		Instant instant=Instant.now();
		Unit unit=Unit.METRIC;
		TravelMode mode=TravelMode.DRIVING;
		
		boolean fuzzy=false;
		boolean modulo2=false;
		boolean showRoutes=false;
		
		if(args.length <= 0){
			printHelp(System.err);
			System.exit(-1);
		}else{
			int i=0;
			if(args[0].compareToIgnoreCase(GMapShell.class.getName())==0){
				// executing outside of eclipse (or any other IDE)
				i=1;
				modulo2=((i%2)!=0);
			}else{
				// executing inside of eclipse (or any other IDE)
				i=0;
				modulo2=((i%2)==0);
			}
			
			do{
				// System.out.println("args["+i+"]: "+args[i]);
				if(args[i].compareToIgnoreCase("--help")==0){
					printHelp(System.out);
					System.exit(0);
				}else if(args[i].compareToIgnoreCase("--language")==0){
					language=args[i+1];
				}else if(args[i].compareToIgnoreCase("--departure")==0){
					// more about DateTimeFormatter??
					instant=Instant.parse(args[i+1]);
				}
				else if(args[i].compareToIgnoreCase("--unit")==0){
					if(args[i+1].compareToIgnoreCase("imperial")==0){
						unit=Unit.IMPERIAL;
					}else{
						unit=Unit.METRIC;
					}
				}else if(args[i].compareToIgnoreCase("--fuzzy")==0){
					if((args[i+1].compareToIgnoreCase("true")==0)){
						fuzzy=true;
					}else{
						fuzzy=false;
					}
				}else if(args[i].compareToIgnoreCase("--mode")==0){
					if(args[i+1].compareToIgnoreCase("bicycling")==0){
						mode=TravelMode.BICYCLING;
					}else if(args[i+1].compareToIgnoreCase("driving")==0){
						mode=TravelMode.DRIVING;
					}else if(args[i+1].compareToIgnoreCase("transit")==0){
						mode=TravelMode.TRANSIT;
					}else if(args[i+1].compareToIgnoreCase("walking")==0){
						mode=TravelMode.WALKING;
					}else{
						System.err.println("unknown travel mode");
					}						
				}else if(args[i].compareToIgnoreCase("--routes")==0){
					if(args[i+1].compareToIgnoreCase("true")==0){
						showRoutes=true;
					}else{
						showRoutes=false;
					}
				}else{
					if(modulo2==false){
						System.err.println("unknown option: "+args[i]);
					}
				}
				i++;
			}while(i < (args.length));
		}
		
		// user inputs
		int MAX_INPUTS=4;

		// declaration of input streams, and readers
		// for origin data
		InputStream[] origin_is=new InputStream[MAX_INPUTS];
		InputStreamReader[] origin_isr=new InputStreamReader[MAX_INPUTS];
		BufferedReader[] origin_br=new BufferedReader[MAX_INPUTS];
		
		// for destination data
		InputStream[] destination_is=new InputStream[MAX_INPUTS];
		InputStreamReader[] destination_isr=new InputStreamReader[MAX_INPUTS];
		BufferedReader[] destination_br=new BufferedReader[MAX_INPUTS];
		
		// instantiate all streams and readers
		for(int i=0; i < MAX_INPUTS; i++){
			origin_is[i]=System.in;
			origin_isr[i]=new InputStreamReader(origin_is[i]);
			origin_br[i]=new BufferedReader(origin_isr[i]);
			
			destination_is[i]=System.in;
			destination_isr[i]=new InputStreamReader(destination_is[i]);
			destination_br[i]=new BufferedReader(destination_isr[i]);
		}
		
		String origin_street="Spandauer Straße 1";
		StringBuffer origin=new StringBuffer("");
		String origin_pcode="10178";
		String origin_town="Berlin";
		String origin_country="Germany";
		String origin_place=origin_country;
		System.out.println("origin data:");
		try{
			String string="";
			if(fuzzy==true){
				System.out.print("origin address: ");
				string=origin_br[0].readLine();
				if(string.isEmpty()==false){
					origin_place=string;
				}else{
					origin_place="England";
				}
			}else{
				System.out.print("country: ");
				string=origin_br[0].readLine();
				if(string.isEmpty()==false){
					origin_country=string;
				}
			
				System.out.print("town/city: ");
				string=origin_br[1].readLine();
				if(string.isEmpty()==false){
					origin_town=string;
				}
			
				System.out.print("postal code: ");
				string=origin_br[2].readLine();
				if(string.isEmpty()==false){
					origin_pcode=string;
				}else{
					origin_pcode="0";
				}
			
				System.out.print("street: ");
				string=origin_br[3].readLine();
				if(string.isEmpty()==false){
					origin_street=string;
				}else{
					origin_street="";
				}
				
				if(origin_street.isEmpty()==false){
					origin.append(origin_street);
				}
				if(origin_pcode.compareTo("0")==0){
					origin.append(" "+origin_pcode);
				}
				if(origin_town.isEmpty()==false){
					origin.append(" "+origin_town);
				}
				if(origin_country.isEmpty()==false){
					origin.append(" "+origin_country);
				}
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		
		String destination_street=origin_street;
		StringBuffer destination=new StringBuffer("");
		String destination_pcode=origin_pcode;
		String destination_town=origin_town;
		String destination_country=origin_country;
		String destination_place="";
		System.out.println("\ndestination data:");		
		try{			
			String string="";
			if(fuzzy==true){
				System.out.print("destination address: ");
				string=destination_br[0].readLine();
				if(string.isEmpty()==false){
					destination_place=string;
				}else{
					destination_place="Germany";
				}
			}else{
				System.out.print("country: ");
				string=destination_br[0].readLine();
				if(string.isEmpty()==false){
					destination_country=string;
				}
			
				System.out.print("town/city: ");
				string=destination_br[1].readLine();
				if(string.isEmpty()==false){
					destination_town=string;
				}
			
				System.out.print("postal code: ");
				string=destination_br[2].readLine();
				if(string.isEmpty()==false){
					destination_pcode=string;
				}else{
					destination_pcode="0";
				}
			
				System.out.print("street: ");
				string=destination_br[3].readLine();
				if(string.isEmpty()==false){
					destination_street=string;
				}else{
					destination_street="";
				}
				
				if(destination_street.isEmpty()==false){
					destination.append(destination_street);
				}
				if(destination_pcode.compareTo("0")==0){
					destination.append(" "+destination_pcode);
				}
				if(destination_town.isEmpty()==false){
					destination.append(" "+destination_town);
				}
				if(destination_country.isEmpty()==false){
					destination.append(" "+destination_country);
				}				
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		// close all streams and readers		
		try{
			for(int i=0; i < MAX_INPUTS; i++){
				destination_br[i].close();
				destination_isr[i].close();
				destination_is[i].close();
				origin_br[i].close();
				origin_isr[i].close();
				origin_is[i].close();
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
		if(fuzzy==true){
			GMapInfo.retrieve(KEY, origin_place, destination_place,
					language, instant, unit, mode);
			
			if(showRoutes==true){
				GMapInfo.retrieveDirections(KEY, origin_place, destination_place, mode);
			}
		}else{
			GMapInfo.retrieve(KEY, origin.toString(),
					destination.toString(), language, instant, unit, mode);
			
			if(showRoutes==true){
				GMapInfo.retrieveDirections(KEY, origin.toString(),
						destination.toString(), mode);
			}
		}
	}
}