package vialab.SMT;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import processing.core.PFont;
import vialab.SMT.util.keyboard.KeyMapping;
import vialab.SMT.util.keyboard.TouchKey;
import vialab.SMT.util.keyboard.TouchKey.Modifiers;

/**
 * KeyboardZone is an implementation of an on-screen keyboard. Use its
 * addKeyListener(Zone) method to allow other zones, such as TextZone, to
 * listen/receive the keyboard input.
 */
public class KeyboardZone extends Zone {
	
	private IndirectDrawingChildZone indirectDrawingChild;
	
	//This inner Class is set indirect and draws the KeyZones into itself
	//allowing the keyboardZone to have children outside of itself, which is
	//used when we add Zones to the KeyboardZones to auto listen to its
	//key events
	class IndirectDrawingChildZone extends Zone{

		protected boolean updateOnlyWhenModified(){
			return true;
		}
		
		IndirectDrawingChildZone(int width, int height){
			super( 0, 0, width, height);
		}
		
		boolean warnDraw(){
			return false;
		}
		
		@Override
		public void touch(){}
		
		@Override
		public void pickDraw(){}

		@Override
		public void draw() {
			KeyboardZone kb = (KeyboardZone) getParent();
			smooth(8);
			fill(kb.backgroundColor, kb.alpha);
			rect(0, 0, this.getWidth(), this.getHeight());
			
			//update Modifiers from key state changes
//			updateModifiersFromKeys();
		}
	};

	public int keyColor = 200;

	public int keyPressedColor = 150;

	public int backgroundColor = 0;

	public int textColor;

	public int alpha = 255;

	public boolean capsLockOn = false;

	private int MODIFIERS = 0;
	
	// we will have only one active.
	private Modifiers activeModifier = Modifiers.NONE;
	
	private int keysPerRow = 11;
	private final int DEFAULT_KEY_WIDTH = (width * 9 / 10) / keysPerRow;

	boolean warnDraw() {
		return false;
	}
	
	boolean warnTouch(){
		return false;
	}

	private Component keyboardComponent = new Component() {
		private static final long serialVersionUID = -3237916182106172342L;
	};

	private static final int DEFAULT_HEIGHT = 250;
	private static final int DEFAULT_WIDTH = 750;

	private static final int DEFAULT_NUM_KEYBOARD_ROWS = 4;
	
	class KeyZone extends ButtonZone {
		private TouchKey key;
		private boolean keyDown;

		public KeyZone(int x, int y, int width, int height, TouchKey key, int fontSize, PFont font, int fontColor) {
			super(null, x, y, width, height, key.getDescriptor(activeModifier), fontSize, font);
			this.key = key;
			this.textColor = fontColor;
		}

		@Override
		public void drawImpl() {
			
			int width = this.getWidth();
			int height = this.getHeight();
			
			textMode(SHAPE);
			if (deactivated) {
				drawImpl(color(255, alpha), color(175, alpha));
			}
			else {
				if (isButtonDown()) {
					drawImpl(color(keyPressedColor, alpha), color(this.textColor, alpha));
				}
				else {
					drawImpl(color(keyColor, alpha), color(this.textColor, alpha));
				}
			}

			// drawCapsLock state
			if (key.getSignal() == KeyEvent.VK_CAPS_LOCK) {
				if (capsLockOn) {
					text("On", width / 2, height * 3 / 4);
				}
				else {
					text("Off", width / 2, height * 3 / 4);
				}
			}
		}

		@Override
		protected void drawImpl(int buttonColor, int textColor) {
			
			int width = getWidth();
			int height = getHeight();
			
			stroke(borderColor);
			strokeWeight(borderWeight);
			fill(buttonColor);
			rect(borderWeight, borderWeight, width - 2 * borderWeight, height - 2 * borderWeight);
			
			if (text != null) {
				if (font != null) {
					textFont(font);
				}
				textAlign(CENTER, CENTER);
				textSize(fontSize);
				fill(textColor);
				
				text(key.getDescriptor(activeModifier), width / 2- borderWeight, height / 2 - borderWeight);
//				text(key.getCharacter(activeModifier), width / 2- borderWeight, height / 2 - borderWeight);
			}
		}

		@Override
		public void touchDownImpl(Touch touch) {
			super.touchDownImpl(touch);
			if (!keyDown) {
				keyDown();
			}
		}

		@Override
		public void touchMovedImpl(Touch touch) {
			if (!keyDown) {
				keyDown();
			}
		}

		@Override
		public void touchImpl(){}
		
		@Override
		public void assignTouch(Touch... touches) {
			//setModified on our indirect parent if this key transitioned from buttonUp to buttondown
			boolean prevDown = isButtonDown();
			super.assignTouch(touches);
			if(prevDown != isButtonDown()){
				indirectDrawingChild.setModified(true);
			}
		}
		
		@Override
		public void unassignTouch(long id) {
			//setModified on our indirect parent if this key transitioned from buttonDown to buttonUp
			boolean prevDown = isButtonDown();
			super.unassignTouch(id);
			if(prevDown != isButtonDown()){
				indirectDrawingChild.setModified(true);
			}
		}

		private void keyDown() {
			keyDown = true;
			char k = key.getCharacter();
			// if not undefined char and shift is on, set to upper case
			if (key.isModifier()) {
				
				if(key.getSignal() == KeyEvent.VK_SHIFT) {
					if(activeModifier == Modifiers.NONE) {
						activeModifier = Modifiers.SHIFT;
					}
					
				} else if(key.getSignal() == KeyEvent.VK_ALT) {
				
					if(activeModifier == Modifiers.NUM) {
						activeModifier = Modifiers.NONE;
					} else {
						activeModifier = Modifiers.NUM;
					}
					// hack: Use ALT
				}
			}
			// send key press using KeyEvent to listeners
			for (KeyListener l : keyListeners) {
				l.keyPressed(new KeyEvent(keyboardComponent, KeyEvent.KEY_PRESSED, System
						.currentTimeMillis(), MODIFIERS, key.getSignal(), k));
			}
		}

		@Override
		protected void pressImpl(Touch t) {
			// toggle Caps Lock
			if (key.getSignal() == KeyEvent.VK_CAPS_LOCK) {
				capsLockOn = !capsLockOn;
			}
			else {
				if (keyDown) {
					keyUp();
				}
			}
		}

		private void keyUp() {
			keyDown = false;
			char k = key.getCharacter(activeModifier);
			// if not undefined char and shift is on, set to upper case
			
			if(key.getSignal() == KeyEvent.VK_SHIFT) {
				if(activeModifier == Modifiers.SHIFT) {
					activeModifier = Modifiers.NONE;
				}
			}
			
//			if (key.keyChar != KeyEvent.CHAR_UNDEFINED
//					&& ((((MODIFIERS >> 6) % 2 == 1) && !capsLockOn) || (((MODIFIERS >> 6) % 2 == 0) && capsLockOn))) {
//				k = Character.toUpperCase(key.keyChar);
//				// if key has a different value for when shift is down, set it
//				// to that
//				if (key.keyChar != key.shiftKeyChar) {
//					k = key.shiftKeyChar;
//				}
//			}
			
			// send key release and typed using KeyEvent to listeners
			for (KeyListener l : keyListeners) {
				l.keyReleased(new KeyEvent(keyboardComponent, KeyEvent.KEY_RELEASED, System
						.currentTimeMillis(), MODIFIERS, key.getSignal(), k));
				if (!key.isModifier()) {
					l.keyTyped(new KeyEvent(keyboardComponent, KeyEvent.KEY_TYPED, System
							.currentTimeMillis(), MODIFIERS, KeyEvent.VK_UNDEFINED, k));
				}
			}
		}
	}
	
	private ArrayList<KeyListener> keyListeners = new ArrayList<KeyListener>();

	/**
	 * [KeyboardZone description]
	 */
	public KeyboardZone( ) { // #1
		this(null, true);
	}
	
	public KeyboardZone(KeyMapping keys) {
		this(null, true, keys);
	}

	/**
	 * [KeyboardZone description]
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 */
	public KeyboardZone( boolean keysSentToApplet) {
		this(null, keysSentToApplet);
	}

	/**
	 * [KeyboardZone description]
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 */
	public KeyboardZone( int x, int y) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, true);
	}

	/**
	 * [KeyboardZone description]
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 */
	public KeyboardZone( int x, int y, boolean keysSentToApplet) {
		this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, keysSentToApplet);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 */
	public KeyboardZone( String name) {
		this(name, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, true, 255);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 */
	public KeyboardZone( String name, boolean keysSentToApplet) {	// #2
		this(name, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, keysSentToApplet, 255);
	}
	
	public KeyboardZone( String name, boolean keysSentToApplet, KeyMapping keys) {	// #2
		this(name, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, keysSentToApplet, 255, keys);
	}

	/**
	 * [KeyboardZone description]
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 */
	public KeyboardZone( int x, int y, int width, int height) {
		this(null, x, y, width, height, true, 255);
	}

	/**
	 * [KeyboardZone description]
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 */
	public KeyboardZone( int x, int y, int width, int height, boolean keysSentToApplet) {
		this(null, x, y, width, height, keysSentToApplet, 255);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 */
	public KeyboardZone( String name, int x, int y, int width, int height) {
		this(name, x, y, width, height, true, 255);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param alpha	Transparency level for keyboard
	 */
	public KeyboardZone( String name, int alpha) {
		this(name, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, true, alpha);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 * @param alpha	Transparency level for keyboard
	 */
	public KeyboardZone( String name, boolean keysSentToApplet, int alpha) {
		this(name, 0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, keysSentToApplet, alpha);
	}

	/**
	 * [KeyboardZone description]
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param alpha	Transparency level for keyboard
	 */
	public KeyboardZone( int x, int y, int width, int height, int alpha) {
		this(null, x, y, width, height, true, alpha);
	}

	/**
	 * [KeyboardZone description]
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 * @param alpha	Transparency level for keyboard
	 */
	public KeyboardZone( int x, int y, int width, int height, boolean keysSentToApplet, int alpha) {
		this(null, x, y, width, height, keysSentToApplet, alpha);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param alpha	Transparency level for keyboard
	 */
	public KeyboardZone( String name, int x, int y, int width, int height, int alpha) {
		this( name, x, y, width, height, true, alpha);
	}

	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param alpha	Transparency level for keyboard
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 */
	public KeyboardZone( String name, int x, int y, int width, int height,
			boolean keysSentToApplet, int alpha) {	// #3
		this( name, x, y, width, height, keysSentToApplet, alpha, 0, 200, 150, 0);
	}
	
	public KeyboardZone( String name, int x, int y, int width, int height,
			boolean keysSentToApplet, int alpha, KeyMapping keys) {	// #3
		this( name, x, y, width, height, keysSentToApplet, alpha, 0, 200, 150, 0, keys);
	}
	
	/**
	 * [KeyboardZone description]
	 * @param name	The name of the zone
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 * @param alpha	Transparency level for keyboard
	 * @param backgroundColor	Background colour of the keyboard
	 * @param keyColor	Colour of the keyboard keys
	 * @param keyPressedColor	Colour of the keyboard keys when they are pressed down
	 * @param textColor	Colour of the text in the keyboard keys
	 */
	public KeyboardZone( String name, int x, int y, int width, int height,
			boolean keysSentToApplet, int alpha, int backgroundColor, int keyColor,
			int keyPressedColor, int textColor) {
		this( name, x, y, width, height, keysSentToApplet, alpha, backgroundColor,
			keyColor, keyPressedColor, textColor, null, null);
	}
	
	public KeyboardZone( String name, int x, int y, int width, int height,
			boolean keysSentToApplet, int alpha, int backgroundColor, int keyColor,
			int keyPressedColor, int textColor, KeyMapping keys) {
		this( name, x, y, width, height, keysSentToApplet, alpha, backgroundColor,
			keyColor, keyPressedColor, textColor, null, keys);
	}

	/**
	 * [KeyboardZone description]
	 * 
	 * @param name	The name of the zone
	 * @param x	X-coordinate of the upper left corner of the zone
	 * @param y	Y-coordinate of the upper left corner of the zone
	 * @param width	Width of the zone
	 * @param height	Height of the zone
	 * @param keysSentToApplet	Sets if the key input is sent to Applet
	 * @param alpha	Transparency level for keyboard
	 * @param backgroundColor	Background colour of the keyboard
	 * @param keyColor	Colour of the keyboard keys
	 * @param keyPressedColor	Colour of the keyboard keys when they are pressed down
	 * @param textColor	Colour of the text in the keyboard keys
	 * @param font	Font used to display text in the keyboard keys
	 */
	public KeyboardZone( String name, int x, int y, int width, int height, boolean keysSentToApplet,
			int alpha, int backgroundColor, int keyColor, int keyPressedColor, int textColor, PFont font, KeyMapping keys) {
		super(name, x, y, width, height);

		indirectDrawingChild = new IndirectDrawingChildZone(width, height);
		this.add(indirectDrawingChild);
		
		if(keys == null) { // no mapping specified, use standard instead.
			keys = new KeyMapping();
		}
		
		keysPerRow = keys.getKeysPerRow(); 
		int fontSize = (this.getHeight() / DEFAULT_NUM_KEYBOARD_ROWS) * 16 / 50;
		
		for (TouchKey k : keys.getKeys()) {
			
			indirectDrawingChild.add(new KeyZone(0, 0, (int) (k.getKeyWidthRatio() * DEFAULT_KEY_WIDTH),
					(this.getHeight() * 9 / 10) / DEFAULT_NUM_KEYBOARD_ROWS, k, fontSize, font, textColor));
			
		}

		SMT.grid(width / 20, height / 20, (width * 9 / 10), 0, 0,
				indirectDrawingChild.getChildren());

		
		indirectDrawingChild.setDirect(false);
		for (Zone zone : indirectDrawingChild.children) {
			zone.setDirect(true);
		}

		if (keysSentToApplet) {
			// add the processing applet as a KeyListener by default
			this.addKeyListener(applet);
		}

		this.alpha = alpha;
		this.backgroundColor = backgroundColor;
		this.keyColor = keyColor;
		this.keyPressedColor = keyPressedColor;
		this.textColor = textColor;
	}
	
	public void setTextColor(int color) {
		this.textColor = color;
		for(Zone z : this.getChildren()) {
			if(z.getClass() == IndirectDrawingChildZone.class) {
				for(Zone zone : z.getChildren()) {
					if(zone.getClass() == KeyZone.class) {
						KeyZone k = (KeyZone) zone;
						k.textColor = color;
					}
				}
			}
		}

	}
	
	/**
	 * Set the size of the keyboard zone
	 * @param w width
	 * @param h height
	 */
	@Override
	public void setSize(int w, int h) {
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		super.setSize(w, h);
		for (Zone child : this.children) {
			if (child instanceof KeyZone) {
				KeyZone keyZone = (KeyZone) child;
				keyZone.setSize((int) (keyZone.key.getKeyWidthRatio() * DEFAULT_KEY_WIDTH),
						(height * 9 / 10) / DEFAULT_NUM_KEYBOARD_ROWS);
				keyZone.fontSize = ((height * 9 / 10) / DEFAULT_NUM_KEYBOARD_ROWS) * 16 / 50;
			}
		}
		if(indirectDrawingChild != null){
			SMT.grid(width / 20, height / 20, (width * 9 / 10), 0, 0,
					indirectDrawingChild.getChildren());
		}
	}

	protected void updateModifiersFromKeys() {
		// make sure modifiers have the correct setting as they act
		// differently than normal keys and should be unset even without a
		// touchUp event, although really just a hack, as touchUp should be
		// generated whenever a touch is unassigned from a zone
		boolean shiftDown = false;
		boolean altDown = false;
		boolean ctrlDown = false;
		boolean metaDown = false;
		for (Zone k : indirectDrawingChild.children) {
			if (k instanceof KeyZone) {
				KeyZone key = (KeyZone) k;
				if (key.isButtonDown()) {
					if (key.key.getSignal() == KeyEvent.VK_SHIFT) {
						shiftDown = true;
					}
					else if (key.key.getSignal() == KeyEvent.VK_CONTROL) {
						ctrlDown = true;
					}
					else if (key.key.getSignal() == KeyEvent.VK_ALT) {
						altDown = true;
					}
					else if (key.key.getSignal() == KeyEvent.VK_META) {
						metaDown = true;
					}
				}
			}
		}
		if (shiftDown) {
			modifierDown(KeyEvent.VK_SHIFT);
		}
		else {
			modifierUp(KeyEvent.VK_SHIFT);
		}
		if (ctrlDown) {
			modifierDown(KeyEvent.VK_CONTROL);
		}
		else {
			modifierUp(KeyEvent.VK_CONTROL);
		}
		if (altDown) {
			modifierDown(KeyEvent.VK_ALT);
		}
		else {
			modifierUp(KeyEvent.VK_ALT);
		}
		if (metaDown) {
			modifierDown(KeyEvent.VK_META);
		}
		else {
			modifierUp(KeyEvent.VK_META);
		}
	}
	
	/**
	 * [add description]
	 * @param  z [description]
	 * @return   [description]
	 */
	@Override
	public boolean add(Zone z){
		if(!(z instanceof KeyZone)){
			this.addKeyListener(z);
		}
		return super.add(z);
	}
	
	/**
	 * [remove description]
	 * @param  z [description]
	 * @return   [description]
	 */
	@Override
	public boolean remove(Zone z){
		if(!(z instanceof KeyZone)){
			this.removeKeyListener(z);
		}
		return super.remove(z);
	}

	/**
	 * This adds a KeyListener to listen to this keyboard implementation. Since
	 * Zone implements the KeyListener interface, Zone can be passed to this
	 * method
	 * 
	 * @param listener
	 *            The KeyListener to add to the keyboard, usually a Zone, which
	 *            implements the KeyListener interface
	 */
	public void addKeyListener(KeyListener listener) {
		this.keyListeners.add(listener);
	}

	/**
	 * This removes a KeyListener from this keyboard
	 * 
	 * @param listener The KeyListener to remove from the keyboard,
	 *  usually a Zone that implements the KeyListener interface
	 */
	public void removeKeyListener(KeyListener listener) {
		this.keyListeners.remove(listener);
	}

	/**
	 * This clears all KeyListeners from this keyboard
	 */
	public void clearKeyListeners(){
		this.keyListeners.clear();
	}

	/**
	 * This returns an array of all KeyListeners on this keyboard that are Zones
	 * 
	 * @return A Zone[] containing all zones that are KeyListeners on this
	 *         keyboard
	 */
	public Zone[] getZoneKeyListeners() {
		ArrayList<Zone> zones = new ArrayList<Zone>();
		for (KeyListener k : this.keyListeners) {
			if (k instanceof Zone) {
				zones.add((Zone) k);
			}
		}
		return zones.toArray(new Zone[zones.size()]);
	}

	/**
	 * This returns an array of all KeyListeners on this keyboard
	 * 
	 * @return A KeyListener[] containing all KeyListeners on this keyboard
	 */
	public KeyListener[] getKeyListeners() {
		return keyListeners.toArray(new Zone[keyListeners.size()]);
	}

	/**
	 * [modifierDown description]
	 * @param keyCode [description]
	 */
	private void modifierDown(int keyCode) {
		switch (keyCode) {
			case KeyEvent.VK_SHIFT:
				if ((MODIFIERS >> 6) % 2 == 0) {
					// add modifier only if not down already
					MODIFIERS += KeyEvent.SHIFT_DOWN_MASK;
					activeModifier = Modifiers.SHIFT;
				}
				break;
			case KeyEvent.VK_CONTROL:
				if ((MODIFIERS >> 7) % 2 == 0) {
					// add modifier only if not down already
					MODIFIERS += KeyEvent.CTRL_DOWN_MASK;
				}
				break;
			case KeyEvent.VK_ALT:
				if ((MODIFIERS >> 8) % 2 == 0) {
					// add modifier only if not down already
					MODIFIERS += KeyEvent.ALT_DOWN_MASK;
					activeModifier = Modifiers.NUM;
				}
				break;
			default:
				break;
		}
	}

	/**
	 * [modifierDown description]
	 * @param keyCode [description]
	 */
	private void modifierUp(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_SHIFT:
			if ((MODIFIERS >> 6) % 2 == 1) {
				// remove modifier only if down already
				MODIFIERS -= KeyEvent.SHIFT_DOWN_MASK;
			}
			break;
		case KeyEvent.VK_CONTROL:
			if ((MODIFIERS >> 7) % 2 == 1) {
				// remove modifier only if down already
				MODIFIERS -= KeyEvent.CTRL_DOWN_MASK;

			}
			break;
		case KeyEvent.VK_ALT:
			if ((MODIFIERS >> 8) % 2 == 1) {
				// remove modifier only if down already
				MODIFIERS -= KeyEvent.ALT_DOWN_MASK;

			}
			break;
		default:
			break;
		}
	}

	/**
	 * [getAlpha description]
	 * @return [description]
	 */
	public int getAlpha() {
		return alpha;
	}

	/**
	 * [getAlpha description]
	 * @param alpha [description]
	 */
	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	/**
	 * [isModifierDown description]
	 * @param  keyCode [description]
	 * @return         [description]
	 */
	public boolean isModifierDown(int keyCode) {
		switch (keyCode) {
		case KeyEvent.VK_SHIFT:
			if ((MODIFIERS >> 6) % 2 == 1) {
				return true;
			}
			break;
		case KeyEvent.VK_CONTROL:
			if ((MODIFIERS >> 7) % 2 == 1) {
				return true;
			}
			break;
		case KeyEvent.VK_ALT:
			if ((MODIFIERS >> 8) % 2 == 1) {
				return true;
			}
			break;
		default:
			break;
		}

		return false;
	}

}
