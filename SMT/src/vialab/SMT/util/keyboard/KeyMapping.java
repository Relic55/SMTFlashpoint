package vialab.SMT.util.keyboard;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import vialab.SMT.util.keyboard.TouchKey.Modifiers;

public class KeyMapping {

	private ArrayList<TouchKey> keyList;
	
	// TODO How is localization handled?
	private String encoding;
	private String language;
	
	private int keysPerRow = 11;
	
	/**
	 * Standard Key mapping
	 */
	public KeyMapping() {
		// position?
		keyList = new ArrayList<TouchKey>();
		
		TouchKey key = new TouchKey(KeyEvent.VK_Q);
		key.addCharacter('q', Modifiers.NONE);
		key.addCharacter('Q', Modifiers.SHIFT);
		key.addCharacter('1', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_W);
		key.addCharacter('w', Modifiers.NONE);
		key.addCharacter('W', Modifiers.SHIFT);
		key.addCharacter('2', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_E);
		key.addCharacter('e', Modifiers.NONE);
		key.addCharacter('E', Modifiers.SHIFT);
		key.addCharacter('3', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_R);
		key.addCharacter('r', Modifiers.NONE);
		key.addCharacter('R', Modifiers.SHIFT);
		key.addCharacter('4', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_T);
		key.addCharacter('t', Modifiers.NONE);
		key.addCharacter('T', Modifiers.SHIFT);
		key.addCharacter('5', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_Z);
		key.addCharacter('z', Modifiers.NONE);
		key.addCharacter('Z', Modifiers.SHIFT);
		key.addCharacter('6', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_U);
		key.addCharacter('u', Modifiers.NONE);
		key.addCharacter('U', Modifiers.SHIFT);
		key.addCharacter('7', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_I);
		key.addCharacter('i', Modifiers.NONE);
		key.addCharacter('I', Modifiers.SHIFT);
		key.addCharacter('8', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_O);
		key.addCharacter('o', Modifiers.NONE);
		key.addCharacter('O', Modifiers.SHIFT);
		key.addCharacter('9', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_P);
		key.addCharacter('p', Modifiers.NONE);
		key.addCharacter('P', Modifiers.SHIFT);
		key.addCharacter('0', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_MINUS);
		key.addCharacter('ü', Modifiers.NONE);
		key.addCharacter('Ü', Modifiers.SHIFT);
		key.addCharacter('ß', Modifiers.NUM);
		keyList.add(key);
		
		// 2nd line
		
		key = new TouchKey(KeyEvent.VK_A);
		key.addCharacter('a', Modifiers.NONE);
		key.addCharacter('A', Modifiers.SHIFT);
		key.addCharacter('@', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_S);
		key.addCharacter('s', Modifiers.NONE);
		key.addCharacter('S', Modifiers.SHIFT);
		key.addCharacter(':', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_D);
		key.addCharacter('d', Modifiers.NONE);
		key.addCharacter('D', Modifiers.SHIFT);
		key.addCharacter(';', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_F);
		key.addCharacter('f', Modifiers.NONE);
		key.addCharacter('F', Modifiers.SHIFT);
		key.addCharacter('_', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_G);
		key.addCharacter('g', Modifiers.NONE);
		key.addCharacter('G', Modifiers.SHIFT);
		key.addCharacter('-', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_H);
		key.addCharacter('h', Modifiers.NONE);
		key.addCharacter('H', Modifiers.SHIFT);
		key.addCharacter('#', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_J);
		key.addCharacter('j', Modifiers.NONE);
		key.addCharacter('J', Modifiers.SHIFT);
		key.addCharacter('(', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_K);
		key.addCharacter('k', Modifiers.NONE);
		key.addCharacter('K', Modifiers.SHIFT);
		key.addCharacter(')', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_L);
		key.addCharacter('l', Modifiers.NONE);
		key.addCharacter('L', Modifiers.SHIFT);
		key.addCharacter('+', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_SEMICOLON);
		key.addCharacter('ö', Modifiers.NONE);
		key.addCharacter('Ö', Modifiers.SHIFT);
		key.addCharacter('§', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_BACK_SPACE);
		key.addCharacter('\u232B', Modifiers.NONE, "<x");
		key.addCharacter('\u232B', Modifiers.SHIFT, "<x");
		key.addCharacter('\u232B', Modifiers.NUM, "<x");
//		key.setKeyWidthRatio(1.7f);
		keyList.add(key);
		
		// 3rd line
		
		key = new TouchKey(KeyEvent.VK_SHIFT);
		key.addCharacter(KeyEvent.CHAR_UNDEFINED, Modifiers.NONE, "\u21E7");
		key.addCharacter(KeyEvent.CHAR_UNDEFINED, Modifiers.SHIFT, "\u21E7");
		key.addCharacter(KeyEvent.CHAR_UNDEFINED, Modifiers.NUM, "\u21E7");
		key.setModifier(true);
		key.setKeyWidthRatio(1.3f);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_Y);
		key.addCharacter('y', Modifiers.NONE);
		key.addCharacter('Y', Modifiers.SHIFT);
		key.addCharacter('\u20AC', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_X);
		key.addCharacter('x', Modifiers.NONE);
		key.addCharacter('X', Modifiers.SHIFT);
		key.addCharacter('/', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_C);
		key.addCharacter('c', Modifiers.NONE);
		key.addCharacter('C', Modifiers.SHIFT);
		key.addCharacter('?', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_V);
		key.addCharacter('v', Modifiers.NONE);
		key.addCharacter('V', Modifiers.SHIFT);
		key.addCharacter('!', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_B);
		key.addCharacter('b', Modifiers.NONE);
		key.addCharacter('B', Modifiers.SHIFT);
		key.addCharacter('\'', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_N);
		key.addCharacter('n', Modifiers.NONE);
		key.addCharacter('N', Modifiers.SHIFT);
		key.addCharacter('\"', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_M);
		key.addCharacter('m', Modifiers.NONE);
		key.addCharacter('M', Modifiers.SHIFT);
		key.addCharacter('*', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_SUBTRACT);
		key.addCharacter('ä', Modifiers.NONE);
		key.addCharacter('Ä', Modifiers.SHIFT);
		key.addCharacter('%', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_ENTER);
		key.addCharacter('\u21B5', Modifiers.NONE, "\u21B5");
		key.addCharacter('\u21B5', Modifiers.SHIFT, "\u21B5");
		key.addCharacter('\u21B5', Modifiers.NUM, "\u21B5");
		key.setKeyWidthRatio(1.7f);
		keyList.add(key);
		
		// 4th line
		
		key = new TouchKey(KeyEvent.VK_ALT);
		key.addCharacter(KeyEvent.CHAR_UNDEFINED, Modifiers.NONE, "123#,.");
		key.addCharacter(KeyEvent.CHAR_UNDEFINED, Modifiers.SHIFT, "123#,.");
		key.addCharacter(KeyEvent.CHAR_UNDEFINED, Modifiers.NUM, "Abc");
		key.setModifier(true);
		key.setKeyWidthRatio(1.5f);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_COMMA);
		key.addCharacter(',', Modifiers.NONE);
		key.addCharacter(',', Modifiers.SHIFT);
		key.addCharacter(',', Modifiers.NUM);
		keyList.add(key);

		key = new TouchKey(KeyEvent.VK_SPACE);
		key.addCharacter(' ', Modifiers.NONE);
		key.addCharacter(' ', Modifiers.SHIFT);
		key.addCharacter(' ', Modifiers.NUM);
		key.setKeyWidthRatio(5);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_PERIOD);
		key.addCharacter('.', Modifiers.NONE);
		key.addCharacter('.', Modifiers.SHIFT);
		key.addCharacter('.', Modifiers.NUM);
		keyList.add(key);
		
		key = new TouchKey(KeyEvent.VK_HOME);
		key.addCharacter('\u2302', Modifiers.NONE, "\u2302");
		key.addCharacter('\u2302', Modifiers.SHIFT, "\u2302");
		key.addCharacter('\u2302', Modifiers.NUM, "\u2302");
		key.setKeyWidthRatio(1.9f);
		keyList.add(key);
		
	}

	/*
		// KEY_DELETE('\u007F', KeyEvent.VK_DELETE, false, "Delete");

		private final boolean isModifier;
		private final char shiftKeyChar;
		private final char keyChar;
		private final int keyCode;
		private final String text;
		private final float keyWidthRatio;

		Keys(char keyChar, int keyCode, boolean isModifier) {
			this(keyChar, keyCode, isModifier, Character.toString(keyChar));
		}

		Keys(char keyChar, int keyCode, boolean isModifier, String text) {
			this(keyChar, keyChar, keyCode, isModifier, text, 1.0f);
		}

		Keys(char keyChar, int keyCode, boolean isModifier, String text, float keyWidthRatio) {
			this(keyChar, keyChar, keyCode, isModifier, text, keyWidthRatio);
		}

		Keys(char shiftKeyChar, char keyChar, int keyCode, boolean isModifier) {
			this(shiftKeyChar, keyChar, keyCode, isModifier, 1.0f);
		}

		Keys(char shiftKeyChar, char keyChar, int keyCode, boolean isModifier, float keyWidthRatio) {
			this(shiftKeyChar, keyChar, keyCode, isModifier, shiftKeyChar + "\n" + keyChar,
					keyWidthRatio);
		}

		Keys(char shiftKeyChar, char keyChar, int keyCode, boolean isModifier, String text,
				float keyWidthRatio) {
			this.shiftKeyChar = shiftKeyChar;
			this.keyChar = keyChar;
			this.keyCode = keyCode;
			this.isModifier = isModifier;
			this.text = text;
			this.keyWidthRatio = keyWidthRatio;
		}

		public String toString() {
			return String.valueOf( keyChar);
		}
	}

	 */
	
	public ArrayList<TouchKey> getKeys() {
		return keyList;
	}
	
	public void addKey(TouchKey key) {
		if(!keyList.contains(key)) {
			keyList.add(key);
		}
	}
	
	public int getKeysPerRow() {
		return keysPerRow;
	}
}
