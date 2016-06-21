/**
 * This file is part of the BMBF funded project <B>Nebeneinander wird Miteinander</B>
 * at Technische Universität Chemnitz.
 *
 * All Copyrights reserved @year.
 */
package controller;

import processing.core.PVector;
import vialab.SMT.Zone;

import util.AppInjector;

/**
 * The game controller creates the link between model and visualization. Mainly it is responsible for creating GUI elements dictated by the GameEngine. <BR>
 * Whereas the ZoneManager adds, deletes and retrieves Zones from SMT the GameController manages and assigns game-relevant information to such Zones,
 * such as placement, orientation and more.
 * 
 * @author <a href="mailto:storz@informatik.tu-chemnitz.de">storz</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[storz][31.08.2015] Created</LI>
 */
public class GameController {
	
	/**
	 * Add a new card at the specified center position with specified rotation angle.
	 * @param cardContent - card information
	 * @param centerPos - where the card center will be placed
	 * @param rotAngle - rotation angle in rad
	 */
	public void addNewCard(Zone zoneToBringIntoPlay, PVector centerPos, float rotAngle) {
		//
//		Zone itemCard = new Zone((int)centerPos.x-GameOptions.CARD_WIDTH/2,(int)centerPos.y-GameOptions.CARD_HEIGHT/2,zoneToBringIntoPlay);
//		itemCard.rotate(rotAngle);
//		AppInjector.zoneManager().add(itemCard);
		
	}

	/**
	 * Use this remove function if a zone is removed due to a game event such as death, destruction, evolution or else.
	 * Aside from removing the Zone from SMT (which should be done via ZoneManager) additional events may trigger here,
	 * e.g. notifying the AI of the death of its units, checking win/lose conditions, etc.
	 * @param stencilContent
	 */
	public void removeZone(Zone zoneToRemoveFromPlay) {

		if (zoneToRemoveFromPlay!=null) {
			AppInjector.zoneManager().remove(zoneToRemoveFromPlay);
		}
		
	}
	
}
