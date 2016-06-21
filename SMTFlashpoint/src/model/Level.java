package model;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

import controller.listener.ILevelListener;

import processing.core.PImage;

/**
 * This object depicts a level of the game.
 * Test <BR>
 * 
 * @author <a href="mailto:soeren@informatik.tu-chemnitz.de">soeren</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[soeren][01.09.2015] Created</LI>
 */
@XmlRootElement
public class Level {


	private int levelNumber;
	private String levelTitle;
	
	private ArrayList<ILevelListener> listeners;
	
	private PImage backgroundImage;
	
	public Level() {
		init(1, "", null);
	}
	
	public Level(int levelNumber) {
		init(levelNumber, "", null);
	}
	
	public Level(int levelNumber, String levelTitle) {
		init(levelNumber, levelTitle, null);
	}
	
	public Level(int levelNumber, String levelTitle, PImage levelBackground) {
		init(levelNumber, levelTitle, levelBackground);
	}
	
	/**
	 * 
	 * Initializes a level and its data structure. Gets called by every Constructor.
	 * 
	 * @param levelNumber - The cardinal number of this level
	 * @param levelTitle - The title of the level
	 * @param background - a specified background image for this level
	 */
	private void init(int levelNumber, String levelTitle, PImage background) {
		this.levelNumber = levelNumber;
		this.levelTitle = levelTitle;
		this.backgroundImage = background;
		listeners = new ArrayList<ILevelListener>();

		// Additional initialization (e.g. creation and placement of zones) goes here
	}
	
	/**
	 * One Level equals another if they both have the same level number.
	 * A game can only ever have one (incarnation of) Level of a certain number.
	 * @param comparedLevel
	 * @return true, if level numbers match; else false
	 */
	public boolean equals(Level comparedLevel) {
		if(this.levelNumber == comparedLevel.levelNumber) {
			return true;
		}
		return false;
	}
	
	/**
	 * Add a listener to its list to check if the level is completed
	 * @param listener An object implementing the ILevelListener interface
	 */
	public void addListener(ILevelListener listener) {
		if(!listeners.contains(listener))
			this.listeners.add(listener);
	}
	
	/**
	 * Gets the number of level. Normally Levels are cardinally counted from 1 to n,
	 * however no sequence is strictly enforced.
	 * @return The level number from the resource file
	 */
	public int getLevelNr() {
		return this.levelNumber;
	}
	
	/**
	 * Gets the title of the level
	 * 
	 * @return A String depicting the name of the current level
	 */
	public String getLevelTitle() {
		return this.levelTitle;
	}
	
	/**
	 * This method checks if our level condition is met by the player(s). If so
	 * we send an event to every listener that it is indeed completed.
	 * 
	 * @return
	 */
	public boolean checkIfCompleted() {
		
		//Pseudo-Variable. Should be replaced by actual game conditions.
		boolean isLevelConditionFulfilled = false;
		
		// uses early-return-pattern
		if(!isLevelConditionFulfilled) {
			return false;
		}
		
		// at this point the required level condition is fulfilled
		
		// Tell all listeners
		for(ILevelListener levelListeners : listeners) {
			levelListeners.levelCompleted();
		}
		
		// we are done with this level and it is completed
		return true;
	}
	
	// Implement additional methods for level representation, e.g. background
	
}
