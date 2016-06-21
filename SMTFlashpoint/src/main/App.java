package main;

import util.AppInjector;
import util.io.Utility;
import model.Background;
import processing.core.PApplet;
import processing.core.PImage;
import vialab.SMT.SMT;
import vialab.SMT.TouchSource;

public class App extends PApplet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	

	public static void main(String args[]) {
		
		PApplet.main(new String[] { "main.App" });
	}
	
	/**
	 * Runs the app in full screen and removes the OS frame around it.
	 */
	public boolean sketchFullScreen() {
		return true;
	}
	
	public void setup() {
		
		
		
		
		size(displayWidth, displayHeight, SMT.RENDERER);
		SMT.init( this, TouchSource.AUTOMATIC);
		AppInjector.initFor(this);
		
	}
	
	public void draw() {
		background(30);
	}
}
