package shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PropertyResourceBundle;

/**
 * Reads a property file, and returns the value of the property
 * @param property string
 */
public class PropertyReader {
	private PropertyReader(){
		// don't instantiate this class
	}	
	/**
	 * @see PropertyReader
	 */
	public static String getPropertyValue(String filename, String property){
		File file=new File(filename);
		String value="";
		try{
			try{
				FileReader fr=new FileReader(file);
				BufferedReader br=new BufferedReader(fr);
				PropertyResourceBundle rb=new PropertyResourceBundle(br);
				value=rb.getString(property);
				br.close();
				fr.close();				
			}catch(FileNotFoundException e){
				System.err.println(e.getMessage());
			}
		}catch(IOException e){
			System.err.println(e.getMessage());
		}
		return value;
	}
}
