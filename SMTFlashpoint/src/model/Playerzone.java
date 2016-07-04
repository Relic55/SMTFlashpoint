/**
 * 
 */
package model;

import processing.core.PImage;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class Playerzone extends Zone{
	
	
	private PImage picture;
	public Playerzone(PImage picture, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		this.picture=picture;
		
		
		
	}

	@Override
	public void draw() {
		//noStroke();
		//fill(0xAFEEEEEE);
        //rect(x,y,this.width,this.height);
		image(picture,x,y,width,height);
	}

	@Override
	public void touch() {
		//drag();
	}
	
	
	
}