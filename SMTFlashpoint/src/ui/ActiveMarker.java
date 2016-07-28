/**
 * 
 */
package ui;

import controller.GameEngine;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class ActiveMarker extends Zone{
	private int x,y,width,height,playernumber;
	private PImage activeMarkerimage;
	private PImage switchimage;
	private String pic_path;
	private GameEngine g;
	private int timer=0, delay;
	
	public ActiveMarker(int x, int y, int width, int height, int playerNumber,String pic_path,GameEngine g)
	{
		super(x,y,width,height);
		this.delay=g.getDelay();
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
		this.g=g;
		this.pic_path=pic_path;
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.playernumber=playerNumber;
		activeMarkerimage=Utility.getImage(this.pic_path+"/AktiverSpieler.png");
		switchimage=Utility.getImage(this.pic_path+"/VisierAusblenden.gif");
		timer=0;
		
	}
	@Override
	public void draw()
	{
		if(playernumber==g.getActivePlayer())
		{
			if(g.isVisorshown())
				image(switchimage, (float)0,(float)0,(float)(this.width),(float)(this.height));
			else
				image(activeMarkerimage, (float)0,(float)0,(float)(this.width),(float)(this.height));
			if(timer>0)
				timer--;
		}
	}
	@Override
	public void touch()
	{
		if(playernumber==g.getActivePlayer()&&timer<1)
		{
			rst(false,false,false,false);
			g.setVisorshown(!g.isVisorshown());
			timer=delay;
		}
	}

}
