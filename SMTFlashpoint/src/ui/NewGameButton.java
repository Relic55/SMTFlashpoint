/**
 * 
 */
package ui;

import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class NewGameButton extends Zone{
	private int x,y,width,height;
	private PImage startimage;
	private String pic_path;
	
	public NewGameButton(int x, int y,int width,int height, String pic_path)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.pic_path=pic_path;
		startimage=Utility.getImage(pic_path+"/start_notpossible.gif");
	}
	
	
	
	
	
	@Override
	public void draw()
	{	
			image(startimage,0,0,width,height);
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
		AppInjector.engine().init_Gamestart();
	}

}
