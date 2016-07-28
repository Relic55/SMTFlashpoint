/**
 * 
 */
package ui;

import java.util.ArrayList;

import controller.GameEngine;
import controller.listener.IActionListener;
import controller.listener.ISwitchListener;
import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class SwitchButton extends Zone {
	
	private ArrayList<ISwitchListener> listeners=new ArrayList<ISwitchListener>();
	private int x,y,width,height,playerNumber,what,direction;
	private PImage arrowimage;
	private String command;
	private int timer=0,delay;
	
	
	
	public SwitchButton(int x, int y, int width, int height, String pic_path, int playerNumber, int what, int direction,GameEngine g)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.playerNumber=playerNumber;
		this.direction=direction;   //1= weiter,   2= vorher
		this.what=what;
		delay=g.getDelay();
		arrowimage=Utility.getImage(pic_path+"/Pfeil_rechts.png");
		
		 //direction: 1= weiter,   2= vorher
		if(direction==1)
		{
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
		else //Pfeil muss in die andere Richtung zeigen
		{
			if(playerNumber==1)
			{
				rotateAbout((float)(PI*1.5), 0,0);
			}
			else if(playerNumber==0||playerNumber==5)
			{
				rotateAbout(PI, 0,0);
			}
			else if(playerNumber==4)
			{
				rotateAbout((float)(PI/2), 0,0);
			}
		}
		
	}
	
	
	
	public void addListener(ISwitchListener listener)
	{
			listeners.add(listener);
	}
	
	@Override
	public void draw()
	{
		image(arrowimage,0,0,width,height);
		if(timer>0)
			timer--;

		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
		if(timer<1) //verzögerung einbauen bis zum erneuten Drücken
		{
			timer=delay;
			for(ISwitchListener al:listeners){
				al.switchButtonPressed(playerNumber, what, direction );
			}
		}
	}
}
