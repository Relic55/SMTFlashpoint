/**
 * This file is part of the BMBF funded project <B>Nebeneinander wird Miteinander</B>
 * at Technische Universität Chemnitz.
 *
 * All Copyrights reserved @year.
 */
package util.io;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Iterator;

import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import util.AppInjector;

/**
 * Test <BR>
 * 
 * @author <a href="mailto:storz@informatik.tu-chemnitz.de">storz</a>
 * @version 0.2 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[storz][29.08.2013] Created inside a MT4J Project</LI>
 *          <LI>[storz][27.08.2015] Adapted and imported into this SMT Project</LI>
 */
public class Utility {
	private static HashMap<String,PImage> imageMap = new HashMap<String,PImage>();
	
	/**
	 * method returns the image from the given imagePath (if available)
	 * loaded images are stored in a map, 
	 * already loaded and stored images are directly returned
	 * 
	 * @param app
	 * @param imagePath
	 * @return
	 */
	public static PImage getImage(String imagePath) {
		if (imageMap.get(imagePath)!=null) {
			return imageMap.get(imagePath);
		}
		else {
			PImage image = null;
			try {
				 image = AppInjector.application().loadImage(imagePath);
			}catch(NullPointerException ex) {
				ex.printStackTrace();
			}
			if (image!=null) imageMap.put(imagePath, image);
			return image;
		}
	}
	
	public static void clearImageStore() {
		
		Iterator<String> iter = imageMap.keySet().iterator();
		while(iter.hasNext()) {
			AppInjector.application().removeCache(imageMap.get(iter.next()));
			iter.remove();
		}
	}
	
	
	
	//json:
	
	public static JSONObject getJSONObjectFromPath(String path) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonObject;
    }
    
    public static JSONArray getJSONArrayFromPath(String path) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(new BufferedReader(new FileReader(path)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jsonArray;
        
    }
}
