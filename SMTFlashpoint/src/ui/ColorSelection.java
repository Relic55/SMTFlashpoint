/**
 * 
 */
package ui;

import model.PlayerColor;
import model.SpecialistType;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class ColorSelection extends Zone{
	private PlayerColor chosencolor;
	private int x,y,width,height,playerNumber;
	private PImage chosencolorimage;
	private String pic_path;
	
	public ColorSelection(int x, int y, int width, int height, String pic_path,int playerNumber)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.pic_path=pic_path;
		this.playerNumber=playerNumber;
		chosencolor=PlayerColor.DEFAULT;
		chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_schwarz.gif");
		
		
		
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
	public void changeColorSelection(PlayerColor chosencolor)
	{
		this.chosencolor=chosencolor;
		if(chosencolor==PlayerColor.DEFAULT)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_schwarz.gif");
		else if(chosencolor==PlayerColor.BLUE)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_blau.gif");
		else if(chosencolor==PlayerColor.YELLOW)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_gelb.gif");
		else if(chosencolor==PlayerColor.GREEN)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_gruen.gif");
		else if(chosencolor==PlayerColor.ORANGE)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_orange.gif");
		else if(chosencolor==PlayerColor.RED)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_rot.gif");
		else if(chosencolor==PlayerColor.WHITE)
			chosencolorimage=Utility.getImage(pic_path+"/Farbkarte_weiss.gif");	
	}
	
	@Override
	public void draw()
	{
		image(chosencolorimage,0,0,width,height);

		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
	/**
	 * @return the chosencolor
	 */
	public PlayerColor getChosencolor() {
		return chosencolor;
	}
	/**
	 * @param chosencolor the chosencolor to set
	 */
	public void setChosencolor(PlayerColor chosencolor) {
		this.chosencolor = chosencolor;
	}
}
