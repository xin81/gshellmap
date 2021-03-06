# gshellmap
## Synopsis
GShell Map executes web services by GoogleMap in the shell/terminal/command line (cmd). Currently, this application accesses four web services:
Directory API <https://developers.google.com/maps/documentation/directions>
Distance Matrix API <https://developers.google.com/maps/documentation/distancematrix>
Elevation API <https://developers.google.com/maps/documentation/elevation>
Geocoding API <https://developers.google.com/maps/documentation/geocoding>

Please, read doc/installation/ReadMe.html to learn more about the use of this application. If you want to try out this application right now without the need to understand it in details, type in:

- sh help.sh help
- sh gshellmap.sh
- sh gshellmetry.sh
- sh gphonebook.sh
 
## Code Example
shell.distance.GMapShell
Executes basic GoogleMap functions like calculating the distance between two places.

	// KEY = your own API-Key from Google
	// origin_place = start address
	// destination_place = end address
	// language = response language
	// instant = departure time (e.g. Instant.now())
	// mode = travel mode (e.g . DRIVING, WALKING, BICYCLING, TRANSIT)
	GMapInfo.retrieve(KEY, origin_place, destination_place,language, instant, unit, mode);
	
	// if true, all directions (routes) from origin_place to destination_place are fetched
	if(showRoutes==true){
		GMapInfo.retrieveDirections(KEY, origin_place, destination_place, mode);
	}

from GooMetryShell.java
Translates two geographic coordinate values into a human readable address.
In addition, also calculate the elevation

	GooMetry gmetry=new GooMetry();
	// KEY = YOUR_OWN_API-KEY
	GeoApiContext context=new GeoApiContext().setApiKey(KEY);
	
	try{
		// @return street address  
		gmetry.addAddressByLatLng(context, latitude, longitude);
		
		// in theory, there can be many street addresses with a longitude and a latitude value
		// in most cases, however, the very first address which GooMetry finds suffices completely
		String[] addresses=gmetry.getAddresses();
		for(int j=0; j < addresses.length; j++){
			if(addresses[j].isEmpty()==false){
				if(addresses[j].compareToIgnoreCase("nowhere")!=0){
					// print out the name, the latitude, the longitude, and the elevation value of the street address 
					System.out.println(addresses[j]+"("+latitude+", "+longitude+", "+gmetry.getElevation()+")");
				}
			}else{
				// found nothing
				System.err.println("unknown place at ("+latitude+", "+longitude+")");
			}
		}
	}catch(Exception e){
		System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

from GPhonebook.java
Looks up street addresses, phone numbers, and email address in a database

	try{
		// connect to the mysql database
		Connection connection=DriverManager.getConnection(url, user, password);
		Statement statement=connection.createStatement();

		// find addresses, phone numbers, or email address
		// query:= all|address|email|phone					
		if(query.isEmpty()==false){
			retrieve(query, statement);
		}
	
		// update the database
		if(addingContact==true){					
			insert(statement);
		}
	}catch(Exception){
	}


## Motivation
This application serves best as a big example how to use Google Java client library.
This simple application runs in most common shell systems (terminals or command lines - whatever you may call the black box window).
In addition to most common and well known GoogleMap functions, this application also displays latitude and longitude values, and provides
a phonebook application to store friends' and families' addresses and phone numbers.

## Installation
Please, read doc/installation/ReadMe.html

## API Reference
navigate to doc/

## Tests
coming soon!
Describe and show how to run the tests with code examples.

## Contributors
I am not sure yet whether I should let other people contribute to this very project or not. If you want to use my source codes to learn more about Google's Java client library, you are welcome to do so. If you want to make modifications to this project, I suggest to fork this project, and start a project of your own which builds upon this one. Please, don't remove my name from the source files.

## License
Copyright 2015 Nguyen Viet Tan (xin81, 阮越新)
Licensed under the Apache License, Version 2.0 (the "License")you may not use this file except in compliance with the License.You may obtain a copy of the License at
	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, softwaredistributed under the License is distributed on an "AS IS" BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.
