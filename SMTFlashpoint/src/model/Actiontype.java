/**
 * 
 */
package model;

/**
 * @author Relic
 *
 */
public enum Actiontype {
	MOVE,
	MOVE_TO_FIRE, 
	MOVE_CARRY_PERSON, 
	MOVE_WITH_HEALED_PERSON, 
	MOVE_CARRY_AND_HEALED, 
	TRANSPORT_DANGER,
	EXTINQUISH_FIRE, 
	EXTINQUISH_SMOKE,	 	 
	HEAL_PERSON,				//Rettungssanitaeter
	REMOVE_DANGER, 				//Gefahrspezi
	IDENTIFY,					//Waermebildkamera
	CONTROL_FIREFIGHTER, //Einsatzleiter
	//Fahrzeuge
	MOVE_AMBULANCE, 
	MOVE_FIRETRUCK, 
	USE_FIRETRUCK, 	 
	//Walloperationen
	OPEN_DOOR, 
	CLOSE_DOOR,
	DAMAGE_WALL,	

	CANCEL; //Abbruch

}
