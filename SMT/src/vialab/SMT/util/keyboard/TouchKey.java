package vialab.SMT.util.keyboard;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * 
 * Specifies a Key for an OnscreenKeyboard from the SMT framework.
 * 
 * @author <a href="mailto:soeren@informatik.tu-chemnitz.de">soeren</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[soeren][26.05.2016] Created</LI>
 */
public class TouchKey {
	
	// should it be private?
	public enum Modifiers {
		NONE,
		SHIFT,
		NUM
	};
	
	private int signal; // The event from AWT which is sent to listeners by pressing the visual Key Zone
	private boolean isModifier; // specifies, if the key is a modifier rather than a character (eg. ALT, CTRL, CMD, Shift)
	private Map<Modifiers, Character> keyCharacter; // This is the character which is represented by the TouchKey.
	private Map<Modifiers, String> descriptor; // The String displayed on the TouchKey depending on the modifier.
	
	private float keyWidthRatio;
	
	// a mapping for one key would be e.g.
	//
	// { NONE } => a
	// { SHIFT } => A

	/**
	 * 
	 * @param signal - a legal KeyEvent.VK_ value to distinguish the key
	 */
	public TouchKey(int signal) {
		init(signal);
	}
	
	public TouchKey(int signal, boolean mod) {
		isModifier = mod;
		init(signal);
	}
	
	private void init(int signal) {
		this.signal = signal;
		keyWidthRatio = 1;
		keyCharacter = new HashMap<Modifiers, Character>();
		descriptor = new HashMap<Modifiers, String>();
		
	}

	public void addCharacter(Character character, Modifiers mod) {
		addCharacter(character,mod, character.toString());
	}
	
	public void addCharacter(Character character, Modifiers mod, String keyName) {
		keyCharacter.put(mod, character);
		descriptor.put(mod, keyName);
	}
	
	// TODO implement method
	public Character getCharacter(Modifiers mod) {
		return keyCharacter.get(mod);
	}
	
	public Character getCharacter() {
		return getCharacter(Modifiers.NONE);
	}
	
	/**
	 * Equality method for TouchKeys
	 * @param other - the TouchKey which is compared to this.
	 * @return true, if the TouchKey objects share the same Modifiers-Chararcter bindings;
	 *  else false.
	 */
	public boolean equalsTo(TouchKey other) {
		if(other == this) return true;
		
		return this.keyCharacter.equals(other.keyCharacter);
	}
	
	public String getDescriptor(Modifiers mod) {
		return descriptor.get(mod);
	}
	
	public void setDescriptor(String descriptor, Modifiers mod) {
		this.descriptor.put(mod, descriptor);
	}
	
	public void setKeyWidthRatio(float ratio) {
		this.keyWidthRatio = ratio;
	}
	
	public float getKeyWidthRatio() {
		return this.keyWidthRatio;
	}
	
	public int getSignal() {
		return this.signal;
	}

	public void setModifier(boolean flag) {
		isModifier=flag;
	}
	
	public boolean isModifier() {
		return isModifier;
	}
}
