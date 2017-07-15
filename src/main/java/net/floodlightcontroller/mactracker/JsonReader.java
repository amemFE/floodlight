package net.floodlightcontroller.mactracker;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.io.FileWriter;




    
public class JsonReader {
	
	String jsonPath1 = "/home/asanka/map.json"; //put the json file path here
	String jsonPath2 = "/home/asanka/ipmap.json"; //put the json file path here
	String jsonPath3 = "/home/asanka/macmap.json";
	
	Map<String, String> map = new HashMap<>();
	Map<String, String> ipmap = new HashMap<>();
	Map<String, String> macmap = new HashMap<>();
	String rval = "noValue";
	
public void initialize() {
		
		JSONParser parser = new JSONParser();
		JSONParser parser2 = new JSONParser();
		JSONParser parser3 = new JSONParser();
        JSONObject obj = new JSONObject();
//        obj.put("35395198515", "Library");
//        obj.put("12492181241", "CC");

//        JSONArray list = new JSONArray();
//        list.add("msg 1");
//        list.add("msg 2");
//        list.add("msg 3");

//		obj.put("messages", list);

        try  {
            
            Object tempobj = parser.parse(new FileReader(jsonPath1));
            JSONObject jsonObject = (JSONObject) tempobj;
            
            Object tempobj2 = parser2.parse(new FileReader(jsonPath2));
            JSONObject jsonObject2 = (JSONObject) tempobj2;
            
            Object tempobj3 = parser3.parse(new FileReader(jsonPath3));
            JSONObject jsonObject3 = (JSONObject) tempobj3;
            
            
            for (Object key : jsonObject.keySet()) {
                //based on you key types
                String mac = (String)key;
                String location = (String) jsonObject.get(mac);
                map.put(mac,location);
                //Print key and value  
            }
            
            for (Object key : jsonObject2.keySet()) {
                //based on you key types
                String ip = (String)key;
                String mac = (String) jsonObject2.get(ip);
                ipmap.put(ip,mac);
                //Print key and value  
            }
            
            for (Object key : jsonObject3.keySet()) {
                //based on you key types
                String host = (String)key;
                String sw = (String) jsonObject2.get(host);
                macmap.put(host,sw);
                //Print key and value  
            }
            
           

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        

	}
	
	public void putToList(String key) {
		
		JSONParser parser = new JSONParser();
	    JSONObject obj = new JSONObject();
	    map.put(key,"unknown");
	    map.forEach((k,v)->{
        	obj.put(k, v);
	    });
	    
	//    obj.put("35395198515", "Library");
	//    obj.put("12492181241", "CC");
	
	//    JSONArray list = new JSONArray();
	//    list.add("msg 1");
	//    list.add("msg 2");
	//    list.add("msg 3");
	
	//	obj.put("messages", list);
	
	    try (FileWriter file = new FileWriter(jsonPath1)) {

            file.write(obj.toJSONString());
            file.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	    
	    public void putIpToList(String key,String value) {
			
			JSONParser parser = new JSONParser();
		    JSONObject obj = new JSONObject();
		    ipmap.put(key,value);
		    ipmap.forEach((k,v)->{
	        	obj.put(k, v);
		    });
		    
		//    obj.put("35395198515", "Library");
		//    obj.put("12492181241", "CC");
		
		//    JSONArray list = new JSONArray();
		//    list.add("msg 1");
		//    list.add("msg 2");
		//    list.add("msg 3");
		
		//	obj.put("messages", list);
		
		    try (FileWriter file = new FileWriter(jsonPath2)) {

	            file.write(obj.toJSONString());
	            file.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		    
		
	}
	    
public void putMacToList(String key,String value) {
			
			JSONParser parser = new JSONParser();
		    JSONObject obj = new JSONObject();
		    macmap.put(key,value);
		    macmap.forEach((k,v)->{
	        	obj.put(k, v);
		    });
		    
		//    obj.put("35395198515", "Library");
		//    obj.put("12492181241", "CC");
		
		//    JSONArray list = new JSONArray();
		//    list.add("msg 1");
		//    list.add("msg 2");
		//    list.add("msg 3");
		
		//	obj.put("messages", list);
		
		    try (FileWriter file = new FileWriter(jsonPath3)) {

	            file.write(obj.toJSONString());
	            file.flush();

	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		    
		
	}
		
	public String getLocation(String macAdd) {
		
		
		rval = "noValue";
		
		
//        JSONArray list = new JSONArray();
//        list.add("msg 1");
//        list.add("msg 2");
//        list.add("msg 3");
       
        
        map.forEach((k,v)->{
        	if(k.equals(macAdd)){
        		rval = v;	
        	}
    });
     
     return rval.equals("noValue")? "0":rval;

	}
public String getSwitchLocation(String ip) {
		
		
		rval = "noValue";
		
		
//        JSONArray list = new JSONArray();
//        list.add("msg 1");
//        list.add("msg 2");
//        list.add("msg 3");
       
        
        ipmap.forEach((k,v)->{
        	if(k.equals(ip)){
        		rval = v;	
        	}
    });
     
     return rval.equals("noValue")? "0":rval;

	}

public String getSwitchLocationFromMac(String Mac) {
	
	
	rval = "noValue";
	
	
//    JSONArray list = new JSONArray();
//    list.add("msg 1");
//    list.add("msg 2");
//    list.add("msg 3");
   
    
    macmap.forEach((k,v)->{
    	if(k.equals(Mac)){
    		rval = v;	
    	}
});
 
 return rval.equals("noValue")? "0":rval;

}


}
