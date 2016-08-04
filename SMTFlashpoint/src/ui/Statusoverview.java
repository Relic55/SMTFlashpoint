/**
 * 
 */
package ui;

import controller.GameEngine;
import processing.core.PImage;
import util.AppInjector;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class Statusoverview extends Zone{
		
	private int maxbuildingdamage=24;
	private int leftmarker=12;				//Anzahl uebriger Einsatzmarker
	private int x1, y1, x2, y2;			//Position fuer die Anzeige
	private int size;
	private GameEngine g;
	private String pic_path;
	private PImage seatimage, catimage, savedimage, deadimage;
	
	


	public Statusoverview(GameEngine g)
	{
		
		this.g=g;
		this.leftmarker=g.getInterest_left();
		this.size=g.getBlock_size();
		x1=g.getX_offset()-size;
		y1=g.getY_offset()+2*size;
		x2=x1+11*size;
		pic_path=g.getPic_path();
		seatimage=Utility.getImage(pic_path+"/Brandherd.gif");
		catimage=Utility.getImage(pic_path+"/Marker_Person_Katze.gif");
		savedimage=Utility.getImage(pic_path+"/Gerettet.gif");
		deadimage=Utility.getImage(pic_path+"/Gestorben.gif");
	}
	
	@Override
	public void draw() {
		//noStroke();
		fill(0xFFFFD700);
        rect(x1,y1,size,size*4);
        
        textSize((int)(size/90.0*14));
        
        if(g.getBuildingdamage()<18)
        	fill(0);
        else
        	fill(100,0,0);
        text("Dmg:"+g.getBuildingdamage()+"/24", (float)(x1+0.15*size),(float)(y1+0.8*size));
        image(catimage,x1+size*8/90,y1+(int)(size*14/90*8.5),size*40/90,size*40/90);
        image(savedimage,x1+size*8/90,y1+(int)(size*14/90*8.5),size*40/90,size*40/90);
        fill(0);
        text("         "+g.getSaved_person()+"/7", (float)(x1+0.15*size),(float)(y1+1.6*size));
        
        image(catimage,x1+size*8/90,y1+(int)(size*14/90*13.5),size*40/90,size*40/90);
        image(deadimage,x1+size*8/90,y1+(int)(size*14/90*13.5),size*40/90,size*40/90);
        if(g.getDead_person()<3)
        	fill(0);
        else
        	fill(100,0,0);
        
        text("         "+g.getDead_person()+"/4", (float)(x1+0.15*size),(float)(y1+2.4*size));
        fill(0);
        image(seatimage,x1+size*8/90,y1+(int)(size*14/90*18.5),size*40/90,size*40/90);
        text("         "+g.getInactive_seats(), (float)(x1+0.15*size),(float)(y1+3.2*size));
        
        //rotate(PI/2);
        
		fill(0xFFFFD700);
        rect(x2,y1,size,size*4);
        
        this.pushMatrix();
        fill(0);
        translate(x2,y1);
        rotate(PI);
        if(g.getBuildingdamage()<18)
        	fill(0);
        else
        	fill(100,0,0);
        text("Dmg: "+g.getBuildingdamage()+"/"+g.getMaxbuildingdamage(),(float)(-size+ 0.15*size),(float)(-4*size+0.8*size));
        image(catimage,-10*size*8/90,(int)(-size*14/90*17),size*40/90,size*40/90);
        image(savedimage,-10*size*8/90,(int)(-size*14/90*17),size*40/90,size*40/90);
        fill(0);
        text("         "+g.getSaved_person()+"/7", (float)(-size+ 0.15*size),(float)(-4*size+1.6*size));
        if(g.getDead_person()<3)
        	fill(0);
        else
        	fill(100,0,0);
        image(catimage,-10*size*8/90,(int)(-size*15/8),size*40/90,size*40/90);
        image(deadimage,-10*size*8/90,(int)(-size*15/8),size*40/90,size*40/90);
        text("         "+g.getDead_person()+"/4", (float)(-size+ 0.15*size),(float)(-4*size+2.4*size));
        fill(0);
        image(seatimage,-10*size*8/90,(int)(-size*9/8),size*40/90,size*40/90);
        text("         "+g.getInactive_seats(), (float)(-size+ 0.15*size),(float)(-4*size+3.2*size));
        this.popMatrix();
		
	}
	@Override
	public void touch()
	{
		
	}
	
}
