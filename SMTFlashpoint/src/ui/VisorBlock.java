/**
 * 
 */
package ui;

import controller.GameEngine;
import model.Block;
import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class VisorBlock extends Zone{
	private int x,y,height, width; 
	private PImage visorimage;
	private String pic_path;
	private int size, x_offset, y_offset,type;
	private Block start, ziel;
	private GameEngine g;
	
	
	public VisorBlock( String pic_path,int type,Block start, Block ziel,float x, float y,int height,int  width,GameEngine g )
	{
		super((int)x,(int)y,width,height);
		this.g=g;
		this.x=(int)x;
		this.y=(int)y;
		this.start=start;
		this.ziel=ziel;
		
		this.height=height;
		this.width=width;
		this.pic_path=pic_path;
		if(type==1)
			visorimage=Utility.getImage(this.pic_path+"/VisierRahmenFeld.gif");
		else if(type==2)
			visorimage= Utility.getImage(this.pic_path+"/VisierRahmenMauerHorizontal.gif");
		else if(type==3)
			visorimage= Utility.getImage(this.pic_path+"/VisierRahmenMauerVertikal.gif");
		else 
			visorimage=Utility.getImage(this.pic_path+"/VisierPlaceField.gif");
		size=g.getBlock_size();
		x_offset=g.getX_offset();
		y_offset=g.getY_offset();
		this.type=type; //1= Block, 2=horizontaler Wall, 3=vertikaler Wall
	}
	
	@Override
	public void draw()
	{	
		if(!g.isVisorTouched())
			image(visorimage, (float)0,(float)0,(float)(this.width),(float)(this.height));

		
		
	}
	/*@Override
	public void Press()
	*/
	@Override
	public void touch() 
	{
		rst(false,false,false,false);
		if(!g.isVisorTouched())
		{
			System.out.println("Visor gedrueckt");
			g.removePossibleActions();
			if(type==1)
			{
				g.showPossibleActions(1, start,ziel, null);
				g.setVisorTouched(true);
			}
			else if(type==2)
			{
				if(start.getXb()<ziel.getXb())
				{
					g.showPossibleActions(2, start,ziel, start.getSouth());
					System.out.println("South");
				}
				else
				{
					g.showPossibleActions(2, start,ziel, start.getNorth());
					System.out.println("North");
				}
				g.setVisorTouched(true);
			}
			else if(type==3)
			{
				if(start.getYb()<ziel.getYb())
				{
					g.showPossibleActions(3, start,ziel, start.getEast());
					System.out.println("East");
	
				}
				else
				{
	
					g.showPossibleActions(3, start,ziel, start.getWest());
					System.out.println("West");
				}
				g.setVisorTouched(true);
			}
			else if(type==4||type==5)
			{
				g.canYouRide(type, start, ziel);

			}
			else 
			{
				g.placesomething(type, start, start);

			}
			
			
		}
		
	}


}
