package shell.geometry;
/*
Copyright 2015 Nguyen Viet Tan (xin81，　阮越新)

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
import java.io.PrintStream;

import shell.Constants;
import shell.PropertyReader;

import com.google.maps.GeoApiContext;

/**
 * Sends one query (containing latitude and longitude values) to GeocodingAPI,
 * and retrieves all addresses (by given latitude and longitude values) in return.
 * @see shell.distance.GMapShell GMapShell
 * for information on how to get this class compiled.
 * @author Nguyen Viet Tan
 * @version 25/02/2015
 */
public class GooMetryShell {
	private GooMetryShell(){
		
	}
	// Explains how to use this program
	private static void printHelp(final PrintStream out){
		// out.println("help");
		// java -cp gmapshell_lib -jar gmapshell.jar shell.GMapShell --PARAMETERS VALUES
		String jarfile="gshellmap.jar";
		String cp="$CLASSPATH";
		out.println("usage: java -cp "+cp+":"+jarfile+" "+GooMetryShell.class.getName()+" PARAMETERS|[Optional Parameters]");
		out.println("PARAMETERS:");
		// out.println("--apiKey\t[Your_Google_API_KEY]");
		out.println("--latitude\t[-90.000000, 90.000000]");
		out.println("--longitude\t[-179.999999, 180.000000]");
		
		out.println("\nOptional Parameters:");
		out.println("--help\tprints this help information");
		out.println(Constants.getLicense());
	}
	
	/**
	 * Excutes this program by this following command:
	 * <code>java -cp $CLASSPATH:$main_jarfile shell.geometry.GooMetryShell --latitude $v1 --longitude $v2</code>,
	 * whereas <em>$v1</em> and <em>$v2</em> are degree values.<br />
	 * <em>v1</em>=[-90.000000, 90.000000]<br />
	 * <em>v2</em>=[-179.999999, 180.000000]<br />
	 * 
	 * If you need help, type --help right after the class name (in this case "shell.geometry.GooMetryShell")
	 * @see shell.distance.GMapShell#main(String[])
	 */
	public static void main(String[] args) {
		if(args.length <= 0){
			printHelp(System.err);
			System.exit(-1);
		}else{
			int i=0;
			// TODO Auto-generated method stub
			String KEY="******************";
			KEY=PropertyReader.getPropertyValue(Constants.getPropertyFilePath(), "apiKey");
			double latitude=0.0;
			double longitude=0.0;
			
			do{
				if(args[i].startsWith("--")==true){
					if(args[i].compareToIgnoreCase("--help")==0){
						printHelp(System.out);
						System.exit(0);
					}else if(args[i].compareToIgnoreCase("--latitude")==0){
						try{
							latitude=Double.parseDouble(args[i+1]);
						}catch(NumberFormatException e){
							System.err.println(e.getMessage());
						}
					}else if(args[i].compareToIgnoreCase("--longitude")==0){
						try{
							longitude=Double.parseDouble(args[i+1]);
						}catch(NumberFormatException e){
							System.err.println(e.getMessage());
						}
					}else{
						System.err.println("unknown option: "+args[i]);
					}
				}
				i++;
			}while(i < args.length);
			
			/* 
			 * Translate a location point (latitude, longitude)
			 * into a human readable address (i. e. simple string)
			 * In addition, also calculate the elevation
			 */
			GooMetry gmetry=new GooMetry();
			GeoApiContext context=new GeoApiContext().setApiKey(KEY);
			try{
				gmetry.addAddressByLatLng(context, latitude, longitude);
				String[] addresses=gmetry.getAddresses();
				System.out.print("\n");
				for(int j=0; j < addresses.length; j++){
					if(addresses[j].isEmpty()==false){
						if(addresses[j].compareToIgnoreCase("nowhere")!=0){
							System.out.println(addresses[j]+"("+latitude+", "+longitude+", "+gmetry.getElevation()+")");
						}
					}else{
						System.err.println("unknown place at ("+latitude+", "+longitude+")");
					}
				}
			}catch(Exception e){
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
