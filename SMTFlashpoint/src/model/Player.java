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
public class Player extends Zone{
	
	private SpecialistType specialist;
	private Color color;
	private Integer xb,yb, ap, sp, apgain, spgain;
	private GameEngine g;
	

	 public Player(GameEngine g)	 
	{
		this(g, SpecialistType.DUMMY, Color.DEFAULT);
	}

	public Player(GameEngine g, SpecialistType specialist,Color color )
	{
		this.g=g;
		this.color=color;
		ap=0;
		sp=0;
		xb=100;		//Startwert für ausserhalb des Feldes
		yb=100;		//Startwert für ausserhalb des Feldes
		spgain=0;
		this.specialist=specialist;
		if(specialist==SpecialistType.ALLESKOENNER)
		{
			apgain=5;
			spgain=0;
		}
		else if(specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST)
		{
			apgain=3;
			spgain=3;
		}
		else
		{
			apgain=4;
			if(specialist==SpecialistType.EINSATZLEITER)
			{
				spgain=2;
			}
			else if(specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST||specialist==SpecialistType.RETTUNGSSPEZIALIST)
			{
				spgain=3;
			}			
		}	

	}
	public void setplayer(SpecialistType specialist,Color color,Integer ap,Integer sp,Integer xb ,Integer yb )
	{
		this.color=color;
		this.specialist=specialist;
		this.ap=ap;
		this.sp=sp;
		this.xb=xb;		
		this.yb=yb;		
		spgain=0;
		if(specialist==SpecialistType.ALLESKOENNER)
		{
			apgain=5;
			spgain=0;
		}
		else if(specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST)
		{
			apgain=3;
			spgain=3;
		}
		else
		{
			apgain=4;
			if(specialist==SpecialistType.EINSATZLEITER)
			{
				spgain=2;
			}
			else if(specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST||specialist==SpecialistType.RETTUNGSSPEZIALIST)
			{
				spgain=3;
			}			
		}

		
	}
	
	
	public boolean place_Firefighter(Integer x, Integer y)
	{
		//zu Spielbeginn ausführen
		//Integer possiblex=			benötigt Feldgröße aus Game Engine
		return false;
	}
	
	public void start_turn()
	{
		ap+=apgain;
		sp=spgain;
		
	}	
	
	public void end_turn()
	{
		//Abfrage zur Bestätigung, wenn Rest AP vorhanden sind
		
	}
	
	public boolean spend_ap(Integer anz)
	{
		if(ap>=anz)
		{
			ap-=anz;
			return true;
		}
		return false;
	}
	public boolean spend_sp(Integer anz)
	{
		if(sp>=anz)
		{
			sp-=anz;
			return true;
		}
		return false;
	}

	
	
	
	
	
	public void switch_Specialist()
	{
		SpecialistType new_specialist=SpecialistType.ALLESKOENNER;
		//Klassenauswahl anzeigen und new_specialist zuweisen
		this.setplayer(new_specialist,color,ap-2,sp,xb ,yb );
		
	}
	public void draw() {
		int size=g.getBlock_size();
		//noStroke();
		fill(0xAFEEEEEE);	
        //rect(x,y,this.width,this.height);
        ellipse(g.getX_offset()+(xb*size), g.getY_offset()+yb*size, size*80/100,size*80/100 );
		//image(picture,x,y,width,height);
	}

}
