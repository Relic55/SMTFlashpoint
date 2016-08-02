/**
 * 
 */
package ui;

import model.PlayerColor;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class PlacementWindow extends Zone {
	private int x_offset,y_offset,width,height,blocksize, playerNumber,textsize;
	private int fillColor, textColor;
	private String windowText;
	
	public PlacementWindow(int x_offset, int y_offset, int blocksize, int playerNumber, PlayerColor buttonColor)
	{

		this.x_offset=x_offset;
		this.y_offset=y_offset;
		this.width=3*blocksize;
		this.height=blocksize;
		this.playerNumber=playerNumber;
		this.blocksize=blocksize;
		textColor=0;
		if(buttonColor==PlayerColor.GREEN)
			fillColor=0xFF1c5b2e;
		else if(buttonColor==PlayerColor.ORANGE)
			fillColor=0xFFbd7212;
		else if(buttonColor==PlayerColor.BLUE)
			fillColor=0xFF1a4a6e;
		else if(buttonColor==PlayerColor.RED)
			fillColor=0xFF961e1f;
		else if(buttonColor==PlayerColor.YELLOW)
			fillColor=0xFFeadb1c;
		else if(buttonColor==PlayerColor.WHITE)
			fillColor=0xFFdadada;
		else
			fillColor=0xFF000000;
		
		textsize=(int)(blocksize/90.0*20);
		
			
		//Text bestimmen
		if(playerNumber==6)
			windowText="Krankenwagen \nplatzieren";
		else if(playerNumber==7)
			windowText="Feuerwehr \nplatzieren";
		else
			windowText="Platziere deine\nSpielfigur";
	}
	
	@Override
	public void draw()
	{
		if(playerNumber==6||playerNumber==7) //Krankenwagen oder Feuerwehrwagen
		{
			
			pushMatrix();
			//Anzeige unten
			translate(x_offset+blocksize*7/2,y_offset+blocksize*9/2);
			fill(fillColor);
			rect(0,0,this.width,this.height/2);
			textAlign(CENTER);
			fill(255);
	        textSize(textsize);
	        text(windowText,width/2,height/7);
		popMatrix();
		pushMatrix();
			//Anzeige links
			translate(x_offset+blocksize*7/2,y_offset+blocksize*5/2);
			rotate(PI/2);
			fill(fillColor);
			rect(0,0,this.width,this.height/2);
			textAlign(CENTER);
			fill(255);
	        textSize(textsize);
	        text(windowText,width/2,height/7);
		popMatrix();
		pushMatrix();
			//Anzeige oben
			translate(x_offset+blocksize*13/2,y_offset+blocksize*7/2);
			rotate(PI);
			fill(fillColor);
			rect(0,0,this.width,this.height/2);
			textAlign(CENTER);
			fill(255);
	        textSize(textsize);
	        text(windowText,width/2,height/7);
		popMatrix();
		pushMatrix();
			//Anzeige rechts
			translate(x_offset+blocksize*13/2,y_offset+blocksize*11/2);
			rotate(PI*3/2);
			fill(fillColor);
			rect(0,0,this.width,this.height/2);
			textAlign(CENTER);
			fill(255);
	        textSize(textsize);
	        text(windowText,width/2,height/7);
		popMatrix();
			
			
		}
		else if(playerNumber==0)
		{			
			pushMatrix();
			translate(x_offset-blocksize*3,y_offset+blocksize*13/2);
			fill(fillColor);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(textColor);
	        textSize(textsize);
	        text(windowText,width/2,height/3);
	        popMatrix();
		}
		else if(playerNumber==5)
		{			
			pushMatrix();
			translate(x_offset+blocksize*10,y_offset+blocksize*13/2);
			fill(fillColor);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(textColor);
	        textSize(textsize);
	        text(windowText,width/2,height/3);
	        popMatrix();
		}
		else if(playerNumber==2)
		{
			pushMatrix();
			translate(x_offset,y_offset+blocksize*3/2);
			rotate(PI);
			fill(fillColor);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(textColor);
	        textSize(textsize);
	        text(windowText,width/2,height/3);
	        popMatrix();
		}
		else if(playerNumber==3)
		{
			pushMatrix();
			translate(x_offset+blocksize*13,y_offset+blocksize*3/2);
			rotate(PI);
			fill(fillColor);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(textColor);
	        textSize(textsize);
	        text(windowText,width/2,height/3);
	        popMatrix();
		}
		else if(playerNumber==1)
		{
			pushMatrix();
			translate(x_offset-blocksize*3/2,y_offset+blocksize*5/2);
			rotate(PI/2);
			fill(fillColor);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(textColor);
	        textSize(textsize);
	        text(windowText,width/2,height/3);
	        popMatrix();
		}
		else if(playerNumber==4)
		{
			pushMatrix();
			//Anzeige rechts
			translate(x_offset+blocksize*23/2,y_offset+blocksize*11/2);
			rotate(PI*3/2);
			fill(fillColor);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(textColor);
	        textSize(textsize);
	        text(windowText,width/2,height/3);
	        popMatrix();
		}
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
}
