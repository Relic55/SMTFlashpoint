/**
 * 
 */
package ui;

import model.SpecialistType;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class SpecialistSelection extends Zone{
	private SpecialistType specialist;
	private int x,y,width,height,playerNumber;
	private PImage chosenimage;
	private String pic_path;
	private boolean chosable;
	
	
	public SpecialistSelection(int x, int y, int width, int height, String pic_path,int playerNumber)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		specialist=SpecialistType.NONE;
		this.pic_path=pic_path;
		this.playerNumber=playerNumber;
		chosable=false;
		chosenimage=Utility.getImage(pic_path+"/Spezialisten_waehlen.gif");
		
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
	public void setSpecialistSelect(SpecialistType specialist, boolean chosable)
	{
		this.specialist=specialist;
		this.chosable=chosable;
		if(specialist==SpecialistType.ALLESKOENNER)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Alleskoenner.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Alleskoenner.gif");
		}
		else if(specialist==SpecialistType.EINSATZLEITER)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Einsatzleiter.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Einsatzleiter.gif");
		}
		else if(specialist==SpecialistType.FAHRZEUGMASCHINIST)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Fahrzeugmaschinist.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Fahrzeugmaschinist.gif");
		}
		else if(specialist==SpecialistType.GEFAHRSTOFFSPEZIALIST)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Gefahrstoffspezialist.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Gefahrstoffspezialist.gif");
		}
		else if(specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Loesschaumspezialist.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Loesschaumspezialist.gif");
		}
		else if(specialist==SpecialistType.RETTUNGSSANITAETER)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Rettungssanitaeter.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Rettungssanitaeter.gif");
		}
		else if(specialist==SpecialistType.RETTUNGSSPEZIALIST)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Rettungsspezialist.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Rettungsspezialist.gif");
		}
		else if(specialist==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
		{
			if(chosable)
				chosenimage=Utility.getImage(pic_path+"/Spezialistenkarten_Waermebildkamera.gif");
			else 
				chosenimage=Utility.getImage(pic_path+"/Used_Spezialistenkarten_Waermebildkamera.gif");
		}
		else
			chosenimage=Utility.getImage(pic_path+"/Spezialisten_waehlen.gif");
	}

	@Override
	public void draw()
	{
		image(chosenimage,0,0,width,height);

		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
	/**
	 * @return the specialist
	 */
	public SpecialistType getSpecialist() {
		return specialist;
	}
	/**
	 * @param specialist the specialist to set
	 */
	public void setSpecialist(SpecialistType specialist) {
		this.specialist = specialist;
	}
	/**
	 * @return the chosable
	 */
	public boolean isChosable() {
		return chosable;
	}
	/**
	 * @param chosable the chosable to set
	 */
	public void setChosable(boolean chosable) {
		this.chosable = chosable;
	}
}
