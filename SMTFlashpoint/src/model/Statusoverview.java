/**
 * 
 */
package model;

import controller.GameEngine;
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
	GameEngine g;
	


	public Statusoverview(GameEngine g)
	{
		this.g=g;

		this.leftmarker=g.getInterest_left();
		this.size=g.getBlock_size();
		x1=g.getX_offset()-size;
		y1=g.getY_offset()+2*size;
		x2=x1+11*size;
	}
	
	@Override
	public void draw() {
		//noStroke();
		fill(0xFFFFD700);
        rect(x1,y1,size,size*4);
        fill(0);
        textSize((int)(size/90.0*14));
        
        
        text("Dmg: "+g.getBuildingdamage()+"/24", (float)(x1+0.15*size),(float)(y1+0.8*size));
        text("Save: "+g.getSaved_person()+"/7", (float)(x1+0.15*size),(float)(y1+1.6*size));
        text("Dead: "+g.getDead_person()+"/4", (float)(x1+0.15*size),(float)(y1+2.4*size));
        text("Seat: "+g.getInactive_seats(), (float)(x1+0.15*size),(float)(y1+3.2*size));
        
        //rotate(PI/2);
        
		fill(0xFFFFD700);
        rect(x2,y1,size,size*4);
        
        this.pushMatrix();
        fill(0);
        translate(x2,y1);
        rotate(PI);
        text("Dmg: "+g.getBuildingdamage()+"/24",(float)(-size+ 0.15*size),(float)(-4*size+0.8*size));
        text("Save: "+g.getSaved_person()+"/7", (float)(-size+ 0.15*size),(float)(-4*size+1.6*size));
        text("Dead: "+g.getDead_person()+"/4", (float)(-size+ 0.15*size),(float)(-4*size+2.4*size));
        text("Seat: "+g.getInactive_seats(), (float)(-size+ 0.15*size),(float)(-4*size+3.2*size));
        this.popMatrix();
		
	}
	@Override
	public void touch()
	{
		
	}
	
}
