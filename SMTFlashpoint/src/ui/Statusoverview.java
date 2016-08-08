/**
 * 
 */
package ui;

import controller.GameEngine;
import model.GameDifficulty;
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
	private int x1, y1, x2, y2, x3,y3,x4,y4;			//Position fuer die Anzeige
	private int size;
	private GameEngine g;
	private String pic_path;
	private GameDifficulty difficulty;
	private PImage seatimage, catimage, savedimage, deadimage, interestimage,falsealarmimage,dangerimage;
	
	


	public Statusoverview(GameEngine g)
	{
		
		this.g=g;
		this.leftmarker=g.getInterest_left();
		this.size=g.getBlock_size();
		x1=g.getX_offset()-size;  //Positionlinks
		y1=g.getY_offset()+2*size;
		x2=x1+11*size;				//Position rechts
		x3=g.getX_offset()+size*5/2;  //Unten
		y3=g.getY_offset()+8*size;
		x4=g.getX_offset()+size*15/2; //oben
		y4=g.getY_offset();
		
		
		pic_path=g.getPic_path();
		difficulty=g.getDifficulty();
		seatimage=Utility.getImage(pic_path+"/Brandherd.gif");
		catimage=Utility.getImage(pic_path+"/Marker_Person_Katze.gif");
		savedimage=Utility.getImage(pic_path+"/Gerettet.gif");
		deadimage=Utility.getImage(pic_path+"/Gestorben.gif");
		interestimage=Utility.getImage(pic_path+"/Marker_Einsatzmarker.gif");
		falsealarmimage=Utility.getImage(pic_path+"/Marker_Fehlalarm.gif");
		dangerimage=Utility.getImage(pic_path+"/Marker_Gefahrenstoff.gif");
	}
	
	@Override
	public void draw() {
		//noStroke();
		
		//Anzeige links
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
        if(difficulty!=GameDifficulty.BEGINNER) //uninteressant auf leicht
        {
	        fill(0);
	        image(seatimage,x1+size*8/90,y1+(int)(size*14/90*18.5),size*40/90,size*40/90);
	        text("         "+g.getInactive_seats(), (float)(x1+0.15*size),(float)(y1+3.2*size));
        }
        
        //Anzeige rechts
        
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
        if(difficulty!=GameDifficulty.BEGINNER)
        {
	        fill(0);
	        image(seatimage,-10*size*8/90,(int)(-size*9/8),size*40/90,size*40/90);
	        text("         "+g.getInactive_seats(), (float)(-size+ 0.15*size),(float)(-4*size+3.2*size));
        }
        this.popMatrix();
        
        
        //Anzeige unten
        
        fill(0xFFFFD700);
        rect(x3,y3,size*5,size);
        this.pushMatrix();
        translate(x3,y3);
        if(difficulty==GameDifficulty.BEGINNER)
        {
        	textSize((int)(size/90.0*10));
        	image(interestimage, size/10,size/10,size*25/90,size*25/90 );
        	fill(0);
        	text("         Einsatzmarker; wird beim \n         Betreten zu Person/Falschalarm" ,(float)(0.15*size),(float)(0.15*size) );
        	image(falsealarmimage, size/10+size*5/2,size/10,size*25/90,size*25/90 );
        	fill(0);
        	text("         Falschalarm" ,(float)(0.15*size+size*5/2),(float)(0.25*size) );
        	
        	image(catimage, size/10,size/10+size/3,size*25/90,size*25/90 );
        	fill(0);
        	text("         identifizierte Person; Nach draussen\n         tragen, um sie zu retten" ,(float)(0.15*size),(float)(0.55*size) );
        	
        	
        }
        else
        {
        	textSize((int)(size/90.0*9));
        	image(interestimage, size/10,size/10,size*25/90,size*25/90 );
        	fill(0);
        	text("         Einsatzmarker; wird beim \n         Betreten zu Person/Falschalarm" ,(float)(0.15*size),(float)(0.15*size) );
        	image(falsealarmimage, size/10+size*5/2,size/10,size*25/90,size*25/90 );
        	text("         Falschalarm" ,(float)(0.15*size+size*5/2),(float)(0.25*size) );
        	
        	image(catimage, size/10,size/10+size/3,size*25/90,size*25/90 );
        	text("         identifizierte Person; zum Kranken-\n         wagen tragen, um sie zu retten" ,(float)(0.15*size),(float)(0.5*size) );
        	
        	image(dangerimage, size/10+size*5/2,size/10+size/3,size*25/90,size*25/90 );
        	text("         Gefahrenstoff;Explodiert bei Feuer-\n         kontakt; nach draussen tragen" ,(float)(0.15*size+size*5/2),(float)(0.5*size) );
        	    	
        	image(seatimage, size/10,size/10+size/3*2,size*25/90,size*25/90 );
        	text("         Brandherd; nicht löschbar; erzeugt weitere Feuer, wenn getroffen" ,(float)(0.15*size),(float)(0.9*size) );
        	
        }
        
        this.popMatrix();
        
        this.pushMatrix();
        fill(0xFFFFD700);
        translate(x4,y4);
        rotate(PI);
        rect(0,0,size*5,size);
        
        
        if(difficulty==GameDifficulty.BEGINNER)
        {
        	textSize((int)(size/90.0*10));
        	image(interestimage, size/10,size/10,size*25/90,size*25/90 );
        	fill(0);
        	text("         Einsatzmarker; wird beim \n         Betreten zu Person/Falschalarm" ,(float)(0.15*size),(float)(0.15*size) );
        	image(falsealarmimage, size/10+size*5/2,size/10,size*25/90,size*25/90 );
        	fill(0);
        	text("         Falschalarm" ,(float)(0.15*size+size*5/2),(float)(0.25*size) );
        	
        	image(catimage, size/10,size/10+size/3,size*25/90,size*25/90 );
        	fill(0);
        	text("         identifizierte Person; Nach draussen\n         tragen, um sie zu retten" ,(float)(0.15*size),(float)(0.55*size) );
        	
        	
        }
        else
        {
        	textSize((int)(size/90.0*9));
        	image(interestimage, size/10,size/10,size*25/90,size*25/90 );
        	fill(0);
        	text("         Einsatzmarker; wird beim \n         Betreten zu Person/Falschalarm" ,(float)(0.15*size),(float)(0.15*size) );
        	image(falsealarmimage, size/10+size*5/2,size/10,size*25/90,size*25/90 );
        	text("         Falschalarm" ,(float)(0.15*size+size*5/2),(float)(0.25*size) );
        	
        	image(catimage, size/10,size/10+size/3,size*25/90,size*25/90 );
        	text("         identifizierte Person; zum Kranken-\n         wagen tragen, um sie zu retten" ,(float)(0.15*size),(float)(0.5*size) );
        	
        	image(dangerimage, size/10+size*5/2,size/10+size/3,size*25/90,size*25/90 );
        	text("         Gefahrenstoff;Explodiert bei Feuer-\n         kontakt; nach draussen tragen" ,(float)(0.15*size+size*5/2),(float)(0.5*size) );
        	    	
        	image(seatimage, size/10,size/10+size/3*2,size*25/90,size*25/90 );
        	text("         Brandherd; nicht löschbar; erzeugt weitere Feuer, wenn getroffen" ,(float)(0.15*size),(float)(0.9*size) );
        	
        }
        
        this.popMatrix();
        
        
        //Anzeige oben
		
	}
	@Override
	public void touch()
	{
		
	}
	
}
