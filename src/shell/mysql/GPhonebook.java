package shell.mysql;
/*
Copyright 2015 Nguyen Viet Tan

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
import java.math.BigDecimal;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import shell.PropertyReader;
import shell.geometry.GooMetry;

import com.google.maps.GeoApiContext;
import com.mysql.jdbc.Driver;

/**
 * Looks up addresses, email addresses, phone numbers, and full names of people you're looking for
 * @author Nguyen Viet Tan
 * @version 26/02/2015
 */
public class GPhonebook {
	private GPhonebook(){
		
	}
	private static void printHelp(final PrintStream out){
		out.println("Help");
		String jarfile="gshellmap.jar";
		String cp="$CLASSPATH";
		String className=GPhonebook.class.getName();
		
		out.println("usage: java -cp "+cp+":"+jarfile+" "+className+" PARAMATERS|[OPTIONAL_PARAMETERS]");
		out.println("PARAMETERS:");
		out.println("--new\t[false|true] add a new contact to your database");
		out.println("--query\t[all|address|email|phone]");
		
		out.println("OPTIONAL_PARAMETERS");
		out.println("--help\tprint this help information");
	}
	
	/*
	 * Retrieves addresses by given latitude and longitude values.
	 * First, the user enters two names (last name, and first name (including the midle name as one string)).
	 * This application queries for these names in the MySQL database, and returns a latitude and a longitude value.
	 * Afterwards, these two geographic values are sent to Geocoding API, and receives full address in return.
	 * @param apiKey your API-Key from Google
	 * @param result set of results returned by MySQL
	 * @throw SQLException and Exception something fails (either the MySQL query, or the query sent to Geocoding API)
	 */
	private static void retrieveAddress(final String apiKey, ResultSet result)throws SQLException{
		GeoApiContext context=new GeoApiContext().setApiKey(apiKey);
		GooMetry gmetry=new GooMetry();
		
		int row=0;		
		while(result.next()){
			String lastname=result.getString(1);
			String name=result.getString(2);
			double latitude=result.getDouble(3);
			double longitude=result.getDouble(4);
			try{
				gmetry.addAddressByLatLng(context, latitude, longitude);
			}catch(Exception e){
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
			
			if((lastname.isEmpty()==false)&&(name.isEmpty()==false)){
				System.out.println("\n"+name+" "+lastname);
				String[] address=gmetry.getAddresses();
				if(address[0].isEmpty()==false){
					if(address[0].compareToIgnoreCase("nowhere")!=0){
						System.out.println(address[0]);
						row++;
					}
				}
			}
		}
		
		System.out.println(row+" result(s)");
	}
	
	/*
	 * Retrieves people's email addresses by two names (last name, and a first name (including the middle name))
	 * @param result set of results returned by MySQL
	 * @throw SQLException
	 */
	private static void retrieveMailAddress(ResultSet result)throws SQLException{
		int row=0;
		while(result.next()){
			String lastname=result.getString(1);
			String name=result.getString(2);
			String email=result.getString(3);
			if((lastname.isEmpty()==false)&&(name.isEmpty()==false)){				
				System.out.println("\n"+name+" "+lastname);
				if(email.isEmpty()==false){
					System.out.println(email);
					row++;
				}
			}
		}
		
		System.out.println(row+" result(s)");
	}
	
	/*
	 * Retrieves people's phone numbers
	 * @param result set of results from MySQL
	 * @throw SQLException
	 */
	private static void retrievePhoneNumber(ResultSet result)throws SQLException{
		int area_code=0;
		BigDecimal number=new BigDecimal(0);
		
		int row=0;
		while(result.next()){
			String lastname=result.getString(1);
			String name=result.getString(2);
			area_code=result.getInt(3);
			number=result.getBigDecimal(4);
			if((lastname.isEmpty()==false)&&(name.isEmpty()==false)){
				System.out.println("\n"+name+" "+lastname);
				if((area_code!=0)&&(number.compareTo(new BigDecimal(0))!=0)){
					System.out.println("+"+area_code+" (0)"+number);
					row++;
				}								
			}
		}
		
		System.out.println(row+" result(s)");
	}
	
	/* Find someone's address, phone numbers, and email addresses */
	private static void retrieve(final String query, Statement statement)throws SQLException{
		System.out.println("Find someone's addresses, phone numbers, and email address");
		String sqlAddress="";
		String sqlEmail="";
		String sqlPhone="";
		String lastname="";
		String name="";
		
		final int MAX=2;
		try{
			InputStream[]is=new InputStream[MAX];
			InputStreamReader[] isr=new InputStreamReader[MAX];
			BufferedReader[]br=new BufferedReader[MAX];
			for(int j=0; j < MAX; j++){
				is[j]=System.in;
				isr[j]=new InputStreamReader(is[j]);
				br[j]=new BufferedReader(isr[j]);
			}
			
			String string="";
			System.out.print("last name: ");
			string=br[0].readLine();
			if(string.isEmpty()==false){
				lastname=string;
			}
			
			System.out.print("first and/or middle name: ");
			string=br[1].readLine();
			if(string.isEmpty()==false){
				name=string;
			}
			
			for(int j=0; j < MAX; j++){
				br[j].close();
				isr[j].close();
				is[j].close();
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
			
		}
		
		String condition="(LOWER(person.lastname)=LOWER('"+lastname+"'))AND(LOWER(person.name) LIKE LOWER('%"+name+"%'))";
		String pselection="person.lastname, person.name";
		String KEY="**************";
		KEY=PropertyReader.getPropertyValue("txt/edit_me.txt", "apiKey");
		ResultSet result=null;
		/****************************************************************************************************************************************/
		if(query.compareToIgnoreCase("address")==0){
			sqlAddress="\nSELECT "+pselection+", person.latitude, person.longitude FROM person WHERE("+condition+")";
			result=statement.executeQuery(sqlAddress);
			retrieveAddress(KEY, result);
		}else if(query.compareToIgnoreCase("email")==0){
			sqlEmail="\nSELECT "+pselection+", email.address FROM person INNER JOIN email ON person.id=email.pid WHERE("+condition+")";
			result=statement.executeQuery(sqlEmail);
			retrieveMailAddress(result);
		}else if(query.compareToIgnoreCase("phone")==0){
			sqlPhone="\nSELECT "+pselection+", phone.area_code, phone.number FROM person INNER JOIN phone ON person.id=phone.pid WHERE("+condition+")";
			result=statement.executeQuery(sqlPhone);
			retrievePhoneNumber(result);
		}else if(query.compareToIgnoreCase("all")==0){
			sqlAddress="\nSELECT "+pselection+", person.latitude, person.longitude FROM person WHERE("+condition+")";
			result=statement.executeQuery(sqlAddress);
			retrieveAddress(KEY, result);
			sqlEmail="\nSELECT "+pselection+", email.address FROM person INNER JOIN email ON person.id=email.pid WHERE("+condition+")";
			result=statement.executeQuery(sqlEmail);
			retrieveMailAddress(result);
			sqlPhone="\nSELECT "+pselection+", phone.area_code, phone.number FROM person INNER JOIN phone ON person.id=phone.pid WHERE("+condition+")";
			result=statement.executeQuery(sqlPhone);
			retrievePhoneNumber(result);
		}else{
			System.err.println("unknown query type: "+query);
		}
	/*******************************************************************************************************************************/
		if(result!=null){
			result.close();
		}
		if(statement!=null){
			statement.close();
		}
	}
	
	/* Adds new contacts to the database
	 * @param statement instance of Statement)
	 */
	private static void insert(Statement statement)throws SQLException{
		System.out.println("Add new contact");
		String lastname="Doe";
		String name="Jone";
		String address="The Mall, SW14 7EN London, UK";
		String email="john.doe@predestination.com";		
		int area_code=0;
		BigDecimal number=new BigDecimal(0);
		
		/* create input -, and buffer streams */
		final int MAX=6;
		InputStream[] is=new InputStream[MAX];
		InputStreamReader[] isr=new InputStreamReader[MAX];
		BufferedReader[] br=new BufferedReader[MAX];
		for(int i=0; i < MAX; i++){
			is[i]=System.in;
			isr[i]=new InputStreamReader(is[i]);
			br[i]=new BufferedReader(isr[i]);
		}
		try{
			// user inputs
			System.out.print("last name: ");
			String string=br[0].readLine();
			if(string.isEmpty()==false){
				lastname=string;
			}
			System.out.print("name (first and middle names): ");
			string=br[1].readLine();
			if(string.isEmpty()==false){
				name=string;
			}
			System.out.print("address: ");
			string=br[2].readLine();
			if(string.isEmpty()==false){
				address=string;
			}
			
			System.out.print("email: ");
			string=br[3].readLine();
			if(string.isEmpty()==false){
				email=string;
			}
			
			System.out.print("area code: ");
			string=br[4].readLine();
			if(string.isEmpty()==false){
				try{
					area_code=Integer.parseInt(string);
				}catch(NumberFormatException e){
					System.err.println(e.getMessage());
				}
			}
			
			System.out.print("phone number: ");
			string=br[5].readLine();
			if(string.isEmpty()==false){
				long l=Long.parseLong(string);
				try{
					number=BigDecimal.valueOf(l);
				}catch(NumberFormatException e){
					System.err.println(e.getMessage());
				}
			}
			/* retrieve latitude and longitude values for the given address */
			GooMetry gmetry=new GooMetry(address);
			String apiKey="****************************************";
			apiKey=PropertyReader.getPropertyValue("txt/edit_me.txt", "apiKey");
			GeoApiContext context=new GeoApiContext().setApiKey(apiKey);
			gmetry.retrieve(context);
			double latitude=90.0;
			double longitude=0.0;
			latitude=gmetry.getLatitude();
			longitude=gmetry.getLongitude();
			

			int row=0;
			// query the id by given last name, and a name
			String sql="SELECT id FROM person WHERE((lastname='"+lastname+"')AND(name='"+name+"'))";			
			
			// insert statements
			String sql_person="INSERT INTO person(lastname, name, latitude, longitude)VALUES('"+lastname+"', '"+name+"', "+latitude+", "+longitude+");";
			row=statement.executeUpdate(sql_person);
			System.out.println("insert into person: "+row);
			
			String sql_email="INSERT INTO email(address, pid) VALUES('"+email+"', ("+sql+"))";
			row=statement.executeUpdate(sql_email);
			System.out.println("insert into email: "+row);
			
			String sql_phone="INSERT INTO phone(area_code, number, pid)VALUES("+area_code+", "+number+", ("+sql+"))";
			row=statement.executeUpdate(sql_phone);
			System.out.println("insert into phone: "+row);
			
			/* close all streams */
			for(int i=0; i < MAX; i++){
				br[i].close();
				isr[i].close();
				is[i].close();
			}
			
			statement.close();
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Executes this program by this following command:
	 * <code>java -cp $CLASSPATH:$main_jarfile shell.mysql.GPhonebook
	 * --new [false|true]
	 * --query [all|address|mail|phone]
	 * </code>
	 * $CLASSPATH contains:
	 * <ul>
	 * <li><a href="https://android.googlesource.com/platform/external/okhttp/+/master">okhttp-2.2.0.jar</a></li>
	 * <li><a href="http://grepcode.com/snapshot/repo1.maven.org/maven2/com.google.code.gson/gson/2.3.1">gson-2.3.1.jar</a></li>
	 * <li><a href="https://github.com/JodaOrg/joda-time">joda-time-2.7.jar</a></li>
	 * <li><a href="http://dev.mysql.com/downloads/connector/j/">mysql-connector-java-5.1.34-bin.jar</a></li>
	 * <li>gmapservice.jar</li>
	 * <li>okio.jar</li>
	 * <li>$main_jarfile</li>
	 * </ul> 
	 * Type --help for help information
	 * @see shell.distance.GMapShell#main(String[])
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length <= 0){
			printHelp(System.err);
			System.exit(-1);
		}else{
			String query="";
			boolean addingContact=false;
			
			int i=0;
			do{
				final String str="--";
				if(args[i].startsWith("--")==true){
					if(args[i].compareToIgnoreCase(str+"help")==0){
						printHelp(System.out);
						System.exit(0);
					}else if(args[i].compareToIgnoreCase(str+"query")==0){
						if(args[i+1].isEmpty()==false){
							query=args[i+1];
						}
					}else if(args[i].compareToIgnoreCase(str+"new")==0){
						if(args[i+1].isEmpty()==false){
							if(args[i+1].compareToIgnoreCase("true")==0){
								addingContact=true;
							}else{
								addingContact=false;
							}
						}
					}
				}
				i++;
			}while(i < (args.length));

			
			String driverName=Driver.class.getName();
			try{
				Class.forName(driverName);
				
				/* Feel free to create your own database, and to change the user name, and the password */
				String db="phonebook";
				String host="localhost";
				String url="jdbc:mysql://"+host+"/"+db;
				String user=PropertyReader.getPropertyValue("txt/edit_me.txt", "mysql_user");
				String password=PropertyReader.getPropertyValue("txt/edit_me.txt", "mysql_password");
				
				try{
					Connection connection=DriverManager.getConnection(url, user, password);
					Statement statement=connection.createStatement();
					if(query.isEmpty()==false){
						retrieve(query, statement);
					}
					
					if(addingContact==true){
						insert(statement);
					}
					
					connection.close();
				}catch(SQLException e){
					System.err.println(e.getMessage());
				}			
			}catch(ClassNotFoundException e){
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}
}
