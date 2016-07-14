/**
 * 
 */
package ui;

import controller.GameEngine;
import model.Player;
import model.PlayerColor;
import model.SpecialistType;
import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class Playerzone extends Zone{
	
	
	private PImage picture;
	private Player ff;
	private int x,y,width,height;
	private int size,number;
	private String pic_path;
	private PImage specialistimage, colorimage;
	private GameEngine g;
	
	public Playerzone(String pic_path,Player ff,GameEngine g, int number, int x, int y, int width, int height)
	{
		super(x,y,width,height);
		
		size=g.getBlock_size();   //size=AppInjector.engine().getBlock_size(); erzeugt ewigen Aufruf
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.g=g;
		
		
		this.pic_path=pic_path;
		this.number=number;
		if(number==0)
		{
			this.ff=g.getFf0();
		}
		else
			this.ff=ff;		
		specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_dummy.gif"); 
		colorimage=Utility.getImage(pic_path+"/Farbkarte_schwarz.gif");
		if(ff!=null)
		{
			if(ff.getPlayerColor()==PlayerColor.GREEN)
				colorimage=Utility.getImage(pic_path+"/Farbkarte_gruen.gif");
			else if(ff.getPlayerColor()==PlayerColor.RED)
				colorimage=Utility.getImage(pic_path+"/Farbkarte_rot.gif");
			else if(ff.getPlayerColor()==PlayerColor.ORANGE)
				colorimage=Utility.getImage(pic_path+"/Farbkarte_orange.gif");
			else if(ff.getPlayerColor()==PlayerColor.BLUE)
				colorimage=Utility.getImage(pic_path+"/Farbkarte_blau.gif");
			else if(ff.getPlayerColor()==PlayerColor.YELLOW)
				colorimage=Utility.getImage(pic_path+"/Farbkarte_gelb.gif");
			else if(ff.getPlayerColor()==PlayerColor.WHITE)
				colorimage=Utility.getImage(pic_path+"/Farbkarte_weiss.gif");
			
			if(ff.getSpecialist()==SpecialistType.ALLESKOENNER)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Alleskoenner.gif");
			else if(ff.getSpecialist()==SpecialistType.GEFAHRSTOFFSPEZIALIST)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Gefahrstoffspezialist.gif");
			else if(ff.getSpecialist()==SpecialistType.EINSATZLEITER)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Einsatzleiter.gif");
			else if(ff.getSpecialist()==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Waermebildkamera.gif");
			else if(ff.getSpecialist()==SpecialistType.LOESCHSCHAUMSPEZIALIST)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Loesschaumspezialist.gif");
			else if(ff.getSpecialist()==SpecialistType.RETTUNGSSPEZIALIST)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Rettungsspezialist.gif");
			else if(ff.getSpecialist()==SpecialistType.RETTUNGSSANITAETER)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Rettungssanitaeter.gif");
			else if(ff.getSpecialist()==SpecialistType.FAHRZEUGMASCHINIST)
				specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Fahrzeugmaschinist.gif");
		
		}


		
		
		
	}

	public void refreshplayer()
	{
		//Spezialist und Farbe nach Wechsel anpassen

		
		if(ff.getSpecialist()==SpecialistType.ALLESKOENNER)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Alleskoenner.gif");
		else if(ff.getSpecialist()==SpecialistType.GEFAHRSTOFFSPEZIALIST)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Gefahrstoffspezialist.gif");
		else if(ff.getSpecialist()==SpecialistType.EINSATZLEITER)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Einsatzleiter.gif");
		else if(ff.getSpecialist()==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Waermebildkamera.gif");
		else if(ff.getSpecialist()==SpecialistType.LOESCHSCHAUMSPEZIALIST)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Loesschaumspezialist.gif");
		else if(ff.getSpecialist()==SpecialistType.RETTUNGSSPEZIALIST)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Rettungsspezialist.gif");
		else if(ff.getSpecialist()==SpecialistType.RETTUNGSSANITAETER)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Rettungssanitaeter.gif");
		else if(ff.getSpecialist()==SpecialistType.FAHRZEUGMASCHINIST)
			specialistimage=Utility.getImage(pic_path+"/Spezialistenkarten_Fahrzeugmaschinist.gif");
	
	}
	
	
	@Override
	public void draw() {
		//noStroke();
		//fill(0xAFEEEEEE);
        //rect(x,y,this.width,this.height);
		/*
		if(ff!=null)
			image(picture,x,y,width,height);
		else
		{
			
		}
		*/
		
		pushMatrix();
		translate(x,y);
		if(number==1)
		{
			rotate(PI/2);			
		}
		else if(number==2||number==3)
		{
			rotate(PI);
		}
		else if(number==4)
		{
			rotate((float)(PI*1.5));
		}
		//fill(100,0,0);
        //rect(0,0,this.width,this.height);
		image(specialistimage, 0,0,this.width,this.height);
		
        //Farbbild
        image(colorimage,(float) (1.2*size), (float) (0.2*size), (float) width/3, (float) height/6 );
        // fill(0,100,0);
        // rect((float) (1.2*size), (float) (0.2*size), (float) width/3, (float) height/6);
        fill(1);
        textSize((int)(size/90.0*22));
        text("AP: "+ff.getAp() , (float)(1.25*size),(float) (1*size));
        text("SP: "+ff.getSp() , (float)(1.25*size),(float) (1.2*size));

		
		popMatrix();

		
		/*
		if(number==1)
		{
			pushMatrix();
			translate(x,y);
			rotate(PI);
			
		}
		fill(100,0,0);
        rect(x,y,this.width,this.height);
        //Farbbild
        rect((float) (x+1.2*size), (float) (y+0.2*size), (float) width/3, (float) height/6);
        fill(0);
        text("AP:  "+ff.getAp() , (float)(x+1.25*size),(float) (y+1*size));
        text("SAP: "+ff.getSp() , (float)(x+1.25*size),(float) (y+1.2*size));
		if(number==1)
		{
			popMatrix();
		}
		
		*/
	}
	
	@Override
	public void pickDraw()
	{
		pushMatrix();
		translate(x,y);
		if(number==1)
		{
			rotate(PI/2);			
		}
		else if(number==2||number==3)
		{
			rotate(PI);
		}
		else if(number==4)
		{
			rotate((float)(PI*1.5));
		}
		//fill(100,0,0);
        rect(0,0,this.width,this.height);		
        //Farbbild
		
		popMatrix();		
	}
	
	@Override
	public void touch() {
		rst(true, true, true, true);
		//drag(); 
		//scale();		
	}

	
	
	
}