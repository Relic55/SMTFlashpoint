/**
 * 
 */
package ui;

import model.GameDifficulty;
import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class StartGameButton extends Zone{
	private int x,y,width,height;
	private PImage startimage;
	private String pic_path;
	private boolean possible;
	
	public StartGameButton(int x, int y,int width,int height, String pic_path)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.pic_path=pic_path;
		startimage=Utility.getImage(pic_path+"/start_notpossible.gif");
		possible=false;
	}
	
	public void setPossible(boolean possible)
	{
		this.possible=possible;
		if(possible)
			startimage=Utility.getImage(pic_path+"/start_possible.gif");
		else
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
		if(possible)
			AppInjector.engine().letTheGameBegin();
	}
}
