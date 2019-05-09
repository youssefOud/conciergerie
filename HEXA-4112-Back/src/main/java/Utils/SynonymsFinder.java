package Utils;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Class allowing to make an API request to identify synonyms of words entered 
 * by the user in order to perform matchmaking
 * 
 * src : http://thesaurus.altervista.org/testjava
 * 
 * @author HEXA-4112
 */
public class SynonymsFinder {
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1"; 
    
    public SynonymsFinder(){
    }
    
    /**
     * Send an email to the API with the required settings to find synonyms of 
     * the parameter word
     * 
     * @param word
     * @param language
     * @param key
     * @param output
     * @return a String with the synonyms
     */
    public String SendRequest(String word, String language, String key, String output) { 
    try { 
        URL serverAddress = new URL(endpoint + "?word="+URLEncoder.encode(word, "UTF-8")+"&language="+language+"&key="+key+"&output="+output); 
        HttpURLConnection connection = (HttpURLConnection)serverAddress.openConnection(); 
        connection.connect(); 
        int rc = connection.getResponseCode(); 
        if (rc == 200) { 
            String line = null; 
            BufferedReader br = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream())); 
            StringBuilder sb = new StringBuilder(); 
            while ((line = br.readLine()) != null) 
                sb.append(line + '\n'); 
            JSONObject obj = (JSONObject) JSONValue.parse(sb.toString()); 
            JSONArray array = (JSONArray)obj.get("response"); 
            String res = "";
            for (int i=0; i < array.size(); i++) {
                JSONObject list = (JSONObject) ((JSONObject)array.get(i)).get("list");

                res+= (String)list.get("synonyms") + "|";
            }
            res.replace("|", "");
            return res;
        } else {
            System.out.println("HTTP error:"+rc);
        } 
        connection.disconnect(); 
      } catch (java.net.MalformedURLException e) { 
        e.printStackTrace(); 
      } catch (java.net.ProtocolException e) { 
        e.printStackTrace(); 
      } catch (java.io.IOException e) { 
        e.printStackTrace(); 
      } 
        return "";
      }
    
}
