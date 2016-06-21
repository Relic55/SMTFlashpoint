package controller;

import java.util.HashMap;
import java.util.Map;

import util.AppInjector;
import processing.core.PFont;

/**
 * 
 * This class contains all GameOptions, also options dealing with visualisation, such as
 * card measures, stencil measures and others. <BR>
 * 
 * @author <a href="mailto:soeren@informatik.tu-chemnitz.de">soeren</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[soeren][30.07.2015] Created</LI>
 */
public class GameOptions {

	public static final int CARD_WIDTH = 98;
	public static final int CARD_HEIGHT = 144;
	
	public static final int STENCIL_WIDTH = 280;
	public static final int STENCIL_HEIGHT = 220;
	public static final int STENCIL_HORIZONTAL_SPACING = 20;
	public static final int STENCIL_VERTICAL_SPACING = 40;
	public static float FONT_SIZE = 48.0f;		// Standard Font size for which fonts are rendered by createFont();
	
	private int displayWidth;
	private int displayHeight;
	
	private Map<String, PFont> fonts;
	
	private PFont currentFont;
	private float currentFontSize;
	
	public GameOptions() {
		if(fonts == null) {
			fonts = new HashMap<String, PFont>();
		}
	}
	
	public int getDisplayWidth() {
		return displayWidth;
	}
	
	public int getDisplayHeight() {
		return displayHeight;
	}
	
	public void setResolution(int width, int height) {
		this.displayWidth = width;
		this.displayHeight = height;
	}
	
	public void setDisplayWidth(int width) {
		this.displayWidth = width;
	}
	
	public void setDisplayHeight(int height) {
		this.displayHeight = height;
	}
	
	public boolean addFont(String fontName, PFont font) {
		if(!fonts.containsKey(fontName)) {
			fonts.put(fontName, font);
			return true;
		}
		return false;
	}
	
	public PFont getFont(String fontName) {
		return fonts.get(fontName);
	}
	
	public PFont getCurrentFont() {
		return currentFont;
	}
	
	public float getCurrentFontSize() {
		return currentFontSize;
	}
	
	public void setFont(PFont font, float size) {
		currentFont = font;
		currentFontSize = size;
		AppInjector.application().textFont(font, size);
	}
}
