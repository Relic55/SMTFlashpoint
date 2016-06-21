package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import vialab.SMT.SMT;
import vialab.SMT.Zone;

/**
 * The ZoneManager handles all Zones for the current displayed Screen. It stores
 * the zones according to their Class in an ArrayList, which is in turn stored and retrieved
 * in a HashMap. You can specify the Class of Zones you wish to retrieve and then work with the resulting
 * ArrayList of Zones.
 * 
 * @author <a href="mailto:soeren@informatik.tu-chemnitz.de">soeren</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[soeren][27.08.2015] Created</LI>
 */
public class ZoneManager {
	
	private Map<String, ArrayList<Zone>> zoneMap =
			Collections.synchronizedMap(
				new HashMap<String, ArrayList<Zone>>());
	
	public ZoneManager() {}
	
	/**
	 * Adds Zones to the ZoneManager for quicker finding as well as to the SMT class
	 * in order to display them. As such you should *not* add the Zone to SMT but to your ZoneManager.
	 * 
	 * The function also adds children zones recursively to the lists and SMT.
	 * @param zones
	 */
	public void add(Zone ... zones) {
		
		for(Zone z : zones) {
		
			for(Zone child : z.getChildren()) {
				this.add(child);
			}
			
			ArrayList<Zone> list = zoneMap.get(z.getClass().getName());
			if(list == null)
				list = new ArrayList<Zone>();
			
			if(!list.contains(z)) {
				list.add(z);
			}
			
			zoneMap.put(z.getClass().getName(), list);
			
			if(z.getParent() == null) {
				SMT.add(z);
			}
		}
	}
	
	/**
	 * Removes Zones from the ZoneManager
	 * 
	 * @param zones - Zones to remove
	 */
	public void remove(Zone ... zones) {
		deleteZonesFromMap(zones);
		SMT.remove(zones);
	}
	
	private void deleteZonesFromMap(Zone ... zones) {
		
		for(Zone z : zones) {
			
			for(Zone child : z.getChildren()) {
				this.remove(child);
			}
			
			ArrayList<Zone> list = zoneMap.get(z.getClass().getName());
			if(list != null) {
				if(list.remove(z) == true) {
					// We actually removed something
					zoneMap.put(z.getClass().getName(), list);
				}
			}
		}
	}
	
	/**
	 * Returns all zones of class &lt;Class&gt;
	 * @param zoneType The actual class
	 * @return An ArrayList&lt;Zone&gt; containing all Zones of the specified class.
	 */
	public ArrayList<Zone> getZonesByClass(Class<?> zoneType) {
		return getZonesByClassName(zoneType.getName());
	}
	
	/**
	 * Returns the Zones of class &lt;className&gt; or null if there are no zones
	 * of such class in the ZoneManager.
	 * @param className The name of the class obtained via class.getName();
	 * @return An ArrayList&lt;Zone&gt; containing all the Zones of class &lt;Class&gt; or null if no such zones are present.
	 */
	public ArrayList<Zone> getZonesByClassName(String className) {
		
		return zoneMap.get(className);
		
	}
	
	/**
	 * Gets all Zones added to the SMT
	 * @return An ArrayList<Zone>, containing all Zones
	 */
	public ArrayList<Zone> getZones() {
		return new ArrayList<Zone>(Arrays.asList(SMT.getZones()));
	}
	
	/**
	 * Returns how many zones are in the ZoneManager.
	 * @return The number of zones in the ZoneManager
	 */
	public int getZoneCount() {
		return getZones().size();
	}
	
	/**
	 * Returns the number of Zones in the ZoneManager corresponding to Class &lt;className&gt;
	 * @param className The String returned by &lt;Class&gt;.getName();
	 * @return The number of Zones of a certain Class in the ZoneManager
	 */
	public int getZoneCountFor(String className) {
		ArrayList<Zone> list = zoneMap.get(className);
		
		if(list == null) return 0;
		else return list.size();
	}
	
	/**
	 * Wrapper method for getZoneCountFor() useable with a class instead of String.
	 * @param zoneType
	 * @return The number of Zones of a certain Class in the ZoneManager
	 */
	public int getZoneCountFor(Class<?> zoneType) {
		return getZoneCountFor(zoneType.getName());
	}
}
