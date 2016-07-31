/**
 * 
 */
package ui;

import controller.GameEngine;
import model.Player;
import model.PlayerColor;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class PlayerVisual extends Zone{
	private Player player;
	private GameEngine g;
	private int size, x_offset, y_offset;
	private String pic_path;
	private PImage figureimage;
	float xpos,ypos;
	
	
	public PlayerVisual(GameEngine g, Player player, String pic_path)
	{
		this.g=g;
		this.pic_path=pic_path;
		size=g.getBlock_size();
		x_offset=g.getX_offset();
		y_offset=g.getY_offset();
		this.player=player;
		if(player.getPlayerColor()==PlayerColor.RED)
		{
			xpos=size/6;
			ypos=size/6*5-size/12;
			figureimage=Utility.getImage(pic_path+"/FireFighterRed.gif");
		}
		else if(player.getPlayerColor()==PlayerColor.BLUE)
		{
			xpos=size/6*5-size/12;
			ypos=size/6*3;
			figureimage=Utility.getImage(pic_path+"/FireFighterBlue.gif");
		}
		else if(player.getPlayerColor()==PlayerColor.WHITE)
		{
			xpos=size/6;
			ypos=size/6*3;
			figureimage=Utility.getImage(pic_path+"/FireFighterWhite.gif");
		}
		else if(player.getPlayerColor()==PlayerColor.GREEN)
		{
			xpos=size/6;
			ypos=size/6+size/12;
			figureimage=Utility.getImage(pic_path+"/FireFighterGreen.gif");
		}
		else if(player.getPlayerColor()==PlayerColor.ORANGE)
		{
			xpos=size/6*5-size/12;
			ypos=size/6*5-size/12;
			figureimage=Utility.getImage(pic_path+"/FireFighterOrange.gif");
		}
		else //if(player.getPlayerColor()==PlayerColor.YELLOW)
		{
			xpos=size/6*5-size/12;
			ypos=size/6+size/12;
			figureimage=Utility.getImage(pic_path+"/FireFighterYellow.gif");
		}
		
		xpos-=(size/6);
		ypos-=(size/6);
		
	}
	@Override
	public void draw() {
		if(player.getXb()==100||player.getYb()==100)
		{
			return;
			//TODO: Figuren beim Spieler anzeigen vorm Platzieren
		}
		

		//fill(fillColor);
              //ellipse(x_offset+(yb*size)+(xpos),y_offset+xb*size+(ypos), (size*80/100/3),(size*80/100/3) );
		image(figureimage,x_offset+(player.getYb()*size)+(xpos),y_offset+player.getXb()*size+(ypos),(size*80/100/2),(size*80/100/2));
	}
	
	@Override
	public void touch(){
		
	}
	

}
