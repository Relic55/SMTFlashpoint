/**
 * 
 */
package ui;

import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class EndingScreen extends Zone {
	private int x,y,width, height;
	private PImage screenimage;
	private String screentext;
	
	public EndingScreen(int x,int y,int width,int  height, int ending,String pic_path)
	{
		super(x,y,1600,900);
		System.out.println("Breite: " + width + " Hoehe: "+ height);
		//super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		if(ending==1)
		{
			screenimage=Utility.getImage(pic_path+"/Gewonnen.gif");
			screentext= "Ihr habt gewonnen!";
		}
		else if(ending==2)
		{
			screenimage=Utility.getImage(pic_path+"/Verloren.gif");
			screentext= "Leider sind zuviele Leute grauenhaft verbrannt! \nDer Einsatz war ein Fehlschlag \nViel Erfolg beim nächsten Versuch";
		}
		else
		{
			screenimage=Utility.getImage(pic_path+"/Verloren.gif");
			screentext= "Das Gebäude ist eingestürzt! \nDer Einsatz war ein Fehlschlag \nViel Erfolg beim nächsten Versuch";
		}
	}
	
	@Override
	public void draw()
	{
		image(screenimage,x,y,width,height);
//		image(screenimage,x,y,1600,900);
		//textAlign(CENTER);
//		textSize(900/12);
		
		textSize(height/12);
		fill(0,255,255);
		//textStroke (0,0,0);
		text(screentext,width/10,height/3);
//		text(screentext,1600/10,900/5);
	}
	@Override
	public void touch()
	{
		
	}
	

}
