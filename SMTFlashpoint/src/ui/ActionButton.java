/**
 * 
 */
package ui;

import java.util.ArrayList;

import controller.GameEngine;
import controller.listener.IActionListener;
import model.Action;
import model.Actiontype;
import model.Block;
import model.SpecialistType;
import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class ActionButton extends Zone{
	
	private ArrayList<IActionListener> listeners=new ArrayList<IActionListener>();
	
	private Action type;
	private Block start, ziel;
	private PImage buttonImage;
	private int apcost;
	private int tsize;
	int textcolor;
	public ActionButton(int x, int y, int width, int height,int apcost, Action type,Block start, Block ziel)
	{
		super(x,y,width,height);
		this.apcost=apcost;
		this.type=type;
		this.start=start;
		this.ziel=ziel;
		buttonImage=Utility.getImage(type.getType().path);
		tsize=(int)(AppInjector.engine().getBlock_size()/90.0*25);
		if(type.getType()==Actiontype.HEAL_PERSON) //weiss auf weissem Hintergrund eher ungünstig
			textcolor=0;
		else
			textcolor=255;
	
		
		
	}
	public void addListener(IActionListener listener)
	{
			listeners.add(listener);
	}
	/* (non-Javadoc)
	 * @see vialab.SMT.Zone#draw()
	 */
	@Override
	public void draw() 
	{
		if(AppInjector.engine().isVisorTouched())
		{
			image(buttonImage,0,0,width,height);
			textAlign(CENTER);
			textSize(tsize);
			
			fill(textcolor);
			text(apcost,width/4,height/2+tsize/3);
		}
		

	}
	/* (non-Javadoc)
	 * @see vialab.SMT.Zone#touch()
	 */

	@Override
	public void touch() {
		if(AppInjector.engine().isVisorTouched()&&!AppInjector.engine().isActionTouched())
		{
			AppInjector.engine().setActionTouched(true);
			for(IActionListener al:listeners){
				al.actionSelected(type, start, ziel);
			}
		}
	}
	

}
