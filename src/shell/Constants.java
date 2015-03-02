package shell;

public final class Constants {
private static final String PROPERTY_FILE="settings.properties";
private Constants(){
	// no instantiation allowed
}
private static String getCopyright(){
	int year=2015;
	String author="Nguyen Viet Tan (xin81, 阮越新)";
	return "\nCopyright "+year+" "+author;
}
public static String getLicense(){
	float version=2.0f;
	String url="http://www.apache.org/licenses/LICENSE";
	return getCopyright()+"\nLicensed under the Apache License, Version "+version+" (the \"License\")"
			+"you may not use this file except in compliance with the License."
			+"You may obtain a copy of the License at"
			+"\n\t"+url+"-"+version+""
			+"\nUnless required by applicable law or agreed to in writing, software"
			+"distributed under the License is distributed on an \"AS IS\" BASIS,"
			+"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied."
			+"See the License for the specific language governing permissions and limitations under the License.";			
}

public static String getPropertyFilePath(){
	return PROPERTY_FILE;
}
}
