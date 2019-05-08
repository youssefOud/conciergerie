/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author youss
 */
public class SynonymsFinder {
    final String endpoint = "http://thesaurus.altervista.org/thesaurus/v1"; 
    
    public SynonymsFinder(){
    }
    
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
            //System.out.println(list.get("category")+":"+list.get("synonyms"));
            res+= (String)list.get("synonyms") + "|";
//            String temp2 = temp.replaceAll("|","");
//            System.out.println(temp);
//            System.out.println(temp2);
//            
//            String[] parts = temp2.split("|");
//            for(int j = 0; j< parts.length; j++){
//                res.add(parts[j]);
//                System.out.println(parts[j]);
//            }            
          }
          res.replace("|", "");
          return res;
        } else System.out.println("HTTP error:"+rc); 
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
