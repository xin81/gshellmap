package shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
			FileReader fr=new FileReader(file);
			BufferedReader br=new BufferedReader(fr);
			try{
				String string="\0";
				while((string!=null)&&(string.length()>-1)){
					string=br.readLine();
					if(string!=null){
						String[]token = string.split("=");
						if(token!=null){
							if(token[0].isEmpty()==false){
								if(token[0].compareToIgnoreCase(property)==0){
								
									if(token[1].isEmpty()==false){
										value=token[1];
									}
								}
							}
						}
					}
				}
				br.close();
				fr.close();				
			}catch(IOException e){
				System.err.println(e.getMessage());
			}
		}catch(FileNotFoundException e){
			System.err.println(e.getMessage());
		}
		return value;
	}
}
