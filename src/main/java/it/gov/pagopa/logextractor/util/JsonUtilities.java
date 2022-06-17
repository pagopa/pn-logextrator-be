package it.gov.pagopa.logextractor.util;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class JsonUtilities {
	/** 
	 * Returns the value associated with the specified key.
	 * @param document the single document represented as a JSONObject containing the content to write in the output file (.txt, .csv) contained in the output zip archive
	 * @param key the name of the key whose associated value is to be returned
	 * @return the value associated with the specified key
	 */
	public static String getValue(JSONObject document, String key) {
		return document.has(key) ? document.getString(key) : null;
	}
	
	/** 
	 * Returns the value associated with the specified key.
	 * @param document the single document represented as a Json formatted string
	 * @param key the name of the key whose associated value is to be returned
	 * @return the value associated with the specified key
	 */
	public static String getValue(String document, String key) {
		return getValue(new JSONObject(document), key);
	}
	
	/** 
	 * Returns the edited document with the specified value replacement. It works for a single couple key,value.
	 * @param document the single document converted to JSONObject containing the content to write in the output file (.txt, .csv) contained in the output zip archive
	 * @param key the name of the key whose associated value is to be replaced
	 * @param newValue the new value associated with the key
	 * @return document the edited document with the specified value replacement
	 */
	public static JSONObject replaceValue(JSONObject document, String key, String newValue) {
			if(document.has(key)) {
				document.remove(key);
				document.put(key, newValue);
			}
		return document;
	}
	
	/** 
	 * Returns the edited document with the specified value replacement. It works for more couples key,value.
	 * @param document the single document represented as a JSONObject containing the content to write in the output file (.txt, .csv) contained in the output zip archive
	 * @param keyValue the map containing the keys and the new associated values
	 * @return document the edited document with the specified value replacement
	 */
	public static JSONObject replaceValues(JSONObject document, HashMap<String, String> keyValue ){
			for(Map.Entry<String, String> entry : keyValue.entrySet()) {
				replaceValue(document, entry.getKey(), entry.getValue());
			} 
		return document;
	}
	
	/** 
	 * Returns the edited document with the specified value replacement. It works for more couples key,value.
	 * @param document the single document represented as a Json formatted string
	 * @param keyValue the map containing the keys and the new associated values
	 * @return document the edited document with the specified value replacement
	 */
	public static String deanonymizeDocument(String document, HashMap<String, String> keyValue ){
		return replaceValues(new JSONObject(document),keyValue).toString();
	}
}
