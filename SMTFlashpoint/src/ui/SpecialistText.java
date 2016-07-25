/**
 * 
 */
package ui;

import model.SpecialistType;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class SpecialistText extends Zone {
	private int x,y,width,height,playerNumber;
	//SpecialistType specialist;
	private String specitext;
	
	public SpecialistText(int x, int y, int width, int height, int playerNumber, SpecialistType specialist, int blocksize)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.playerNumber=playerNumber;
		//TODO: Texte der Specialisten einfügen
		specitext= "Hier könnte\nIhre Werbung stehen";
		
		if(playerNumber==1)
		{
			rotateAbout(PI/2, 0,0);
		}
		else if(playerNumber>1&&playerNumber<4)
		{
			rotateAbout(PI, 0,0);
		}
		else if(playerNumber==4)
		{
			rotateAbout((float)(PI*1.5), 0,0);
		}
		
	}
	
	public void setSpecialist(SpecialistType specialist)
	{
		//TODO: Texte der Specialisten einfügen
		specitext= "Hier könnte\nIhre Werbung stehen 2";
	}
	
	@Override
	public void draw()
	{
		fill(0);
		rect(0,0,width,height);
		textAlign(CENTER);
		fill(255);
		text(specitext,width/2,height/2);

		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
}
