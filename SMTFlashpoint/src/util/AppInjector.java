package util;

import controller.GameController;
import controller.GameEngine;
import controller.GameOptions;
import controller.ZoneManager;
import processing.core.PApplet;
import controller.GameEngine.GameStates;

/**
 * 
 * This class holds (and supplies) Singleton objects for all application-dependant
 * objects, such as GameOptions. <BR>
 * 
 * @author <a href="mailto:soeren@informatik.tu-chemnitz.de">soeren</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[soeren][11.08.2015] Created</LI>
 */
public class AppInjector {

	private static volatile GameOptions gameOptions = null;
	private static volatile GameEngine gameEngine = null;
	private static volatile GameController gameController = null;
	private static volatile ZoneManager zoneManager;
	private static volatile PApplet applet = null;
	
	private static volatile boolean isInit=false;
	
	private AppInjector() {}
	
	/**
	 * Initializes all components for the applications.
	 * 
	 * Calls and creates objects in this order:
	 * <ul>
	 * 	<li>GameOptions</li>
	 * 	<li>GameEngine</li>
	 * 	<li>GameController</li>
	 * 	<li>ZoneManager</li>
	 * </ul>
	 * 
	 * @param app - The Processing applet linked to this Injector
	 */
	public static void initFor(PApplet app) {
		if(isInit) {
//			System.err.println("AppInjector is initialized more than once.");
			return;
		}
		
		applet = app;
		
		options();
		engine();
		controller();
		zoneManager();
		
		isInit = true;
	}
	
	/**
	 * This method (initializes and) returns the GameOptions for the game.
	 * The GameOptions holds all values for settings, mainly visualisation.
	 * @return
	 */
	public static GameOptions options() {
		if(gameOptions == null) {
			synchronized (GameOptions.class) {
				if(gameOptions == null) {// Doppelcheck für Threads
					gameOptions = new GameOptions();
					gameOptions.setResolution(applet.displayWidth, applet.displayHeight);
				}
			}
		}
		return gameOptions;
	}
	
	/**
	 * This method (initializes and) returns the GameEngine for the game.
	 * The GameEngine coordinates the game state and game variables.
	 * @return The GameEngine object of the application
	 */
	public static GameEngine engine() {
		if(gameEngine == null) {
			synchronized (GameEngine.class) {
				if(gameEngine == null) {// Doppelcheck für Threads
					gameEngine = new GameEngine();
					gameEngine.setCurrentGameState(GameStates.STATE_MENU);
				}
			}
		}
		return gameEngine;
	}
	
	public static GameController controller() {
		if(gameController == null) {
			synchronized (GameController.class) {
				if(gameController == null) {// Doppelcheck für Threads
					gameController = new GameController();
				}
			}
		}
		return gameController;
	}

	public static ZoneManager zoneManager() {
		
		if(zoneManager == null) {
			synchronized (ZoneManager.class) {
				if(zoneManager == null) {// Doppelcheck für Threads
					zoneManager = new ZoneManager();
				}
			}
		}
		return zoneManager;
	}
	
	public static PApplet application() {
		return applet;
	}
	
}
