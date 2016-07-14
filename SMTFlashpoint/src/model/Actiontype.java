/**
 * 
 */
package model;

import processing.core.PImage;
import util.io.Utility;

/**
 * @author Relic
 *
 */
public enum Actiontype {
	MOVE("./pictures/AktionLaufen.png"),
	MOVE_TO_FIRE("./pictures/AktionLaufen.png"), 
	MOVE_CARRY_PERSON("./pictures/AktionPersonTragen.png"), 
	MOVE_WITH_HEALED_PERSON("./pictures/AktionLaufenMitGeheilt.png"), 
	MOVE_CARRY_AND_HEALED("./pictures/AktionPersonTragenMitGeheilt.png"), 
	TRANSPORT_DANGER("./pictures/AktionHazmatTragen.png"),
	TRANSPORT_DANGER_AND_HEALED("./pictures/AktionHazmatTragenMitGeheilt.png"), 
	EXTINQUISH_FIRE("./pictures/AktionLoeschen.png"), 
	EXTINQUISH_SMOKE("./pictures/AktionLoeschen.png"),
	EXTINQUISH_STEP("./pictures/AktionLoeschen.png"),
	HEAL_PERSON("./pictures/AktionHeilen.png"),				//Rettungssanitaeter
	REMOVE_DANGER("./pictures/AktionHazmatDispose.png"), 				//Gefahrspezi
	IDENTIFY("./pictures/AktionScannen.png"),					//Waermebildkamera
	CONTROL_FIREFIGHTER("./pictures/AktionKommandieren.png"), //Einsatzleiter
	//Fahrzeuge
	MOVE_AMBULANCE("./pictures/AktionNichts.png"), 
	MOVE_FIRETRUCK("./pictures/AktionNichts.png"), 
	USE_FIRETRUCK("./pictures/AktionNichts.png"), 	 
	//Walloperationen
	OPEN_DOOR("./pictures/AktionTuerWechseln.png"), 
	CLOSE_DOOR("./pictures/AktionTuerWechseln.png"),
	DAMAGE_WALL("./pictures/AktionMauerHacken.png"),	

	CANCEL("./pictures/AktionNichts.png"); //Abbruch
	
	public String path;

	Actiontype(String path)
	{
		this.path=path;
	}

}
