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
		super(x,y,width,height);
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
			screentext= "Leider sind zuviele Leute grauenhaft verbrannt! \nDer Einsatz war ein Fehlschlag \nViel Erfolg beim n�chsten Versuch";
		}
		else
		{
			screenimage=Utility.getImage(pic_path+"/Verloren.gif");
			screentext= "Das Geb�ude ist eingest�rzt! \nDer Einsatz war ein Fehlschlag \nViel Erfolg beim n�chsten Versuch";
		}
	}
	
	@Override
	public void draw()
	{
		image(screenimage,x,y,width,height);
		
		textSize(height/12);
		fill(0,255,255);
		text(screentext,width/10,height/3);
	}
	@Override
	public void touch()
	{
		
	}
	

}
