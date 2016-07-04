/**
 * 
 */
package model;

import java.awt.Color;

import controller.GameEngine;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class Player extends Zone{
	
	private SpecialistType specialist;
	private PlayerColor playerColor;
	java.awt.Color fillColor;
	private Integer xb,yb, ap, sp, apgain, spgain; 
	float xpos,ypos;
	private GameEngine g;
	
	
	 public Player(GameEngine g)	 
	{
		this(g, SpecialistType.DUMMY, PlayerColor.DEFAULT);
	}

	public Player(GameEngine g, SpecialistType specialist,PlayerColor playerColor )
	{
		this.g=g;
		
		this.playerColor=playerColor;
		//fillColor = new Color(100,100,100);
		int size=g.getBlock_size();
		if(playerColor==playerColor.GREEN)
		{
			xpos=size/6;
			ypos=size/6;
		}
		else if(playerColor==playerColor.BLUE)
		{			
			xpos=size/6*5;
			ypos=size/6*3;
		}
		
		else if(playerColor==playerColor.RED)
		{
			xpos=size/6;
			ypos=size/6*5;	
		}
		else if(playerColor==playerColor.ORANGE)
		{
			xpos=size/6*5;
			ypos=size/6*5;
		}
		else if(playerColor==playerColor.WHITE)
		{
			xpos=size/6;
			ypos=size/6*3;
		}
		else if(playerColor==playerColor.YELLOW)
		{
			xpos=size/6*5;
			ypos=size/6;
		}
		ap=0;
		sp=0;
		xb=100;		//Startwert f�r ausserhalb des Feldes
		yb=100;		//Startwert f�r ausserhalb des Feldes
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
	public void setplayer(SpecialistType specialist,PlayerColor playerColor,Integer ap,Integer sp,Integer xb ,Integer yb )
	{
		this.playerColor=playerColor;
		int size=g.getBlock_size();
		if(playerColor==playerColor.GREEN)
		{
			xpos=size/6;
			ypos=size/6;
		}
		else if(playerColor==playerColor.BLUE)
		{			
			xpos=size/6*5;
			ypos=size/6*3;
		}
		
		else if(playerColor==playerColor.RED)
		{
			xpos=size/6;
			ypos=size/6*5;	
		}
		else if(playerColor==playerColor.ORANGE)
		{
			xpos=size/6*5;
			ypos=size/6*5;
		}
		else if(playerColor==playerColor.WHITE)
		{
			xpos=size/6;
			ypos=size/6*3;
		}
		else if(playerColor==playerColor.YELLOW)
		{
			xpos=size/6*5;
			ypos=size/6;
		}
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
		//zu Spielbeginn ausf�hren
		//Integer possiblex=			ben�tigt Feldgr��e aus Game Engine
		return false;
	}
	
	public void start_turn()
	{
		ap+=apgain;
		sp=spgain;
		
	}	
	
	public void end_turn()
	{
		//Abfrage zur Best�tigung, wenn Rest AP vorhanden sind
		
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
		this.setplayer(new_specialist,playerColor,ap-2,sp,xb ,yb );
		
	}
	public void draw() {
		if(xb==100||yb==100)
			return;
		int size=g.getBlock_size();
		//noStroke();
		if(playerColor==playerColor.GREEN)
		{
			fill(0,100,0);
			
		}
		else if(playerColor==playerColor.BLUE)
		{			
			fill(0,0,100);
		}
		
		else if(playerColor==playerColor.RED)
		{
			fill(100,0,0);	
		}
		else if(playerColor==playerColor.ORANGE)
		{
			fill(255,140,0);
		}
		else if(playerColor==playerColor.WHITE)
		{
			fill(255,255,255);
		}
		else if(playerColor==playerColor.YELLOW)
		{
			fill(238,238,0);
		}
        //rect(x,y,this.width,this.height);
        //ellipse(g.getX_offset()+(xb*size)+((float)0.5*size),g.getY_offset()+yb*size+((float)0.5*size), (size*80/100),(size*80/100) );
        ellipse(g.getX_offset()+(xb*size)+(xpos),g.getY_offset()+yb*size+(ypos), (size*80/100/3),(size*80/100/3) );
		//image(picture,x,y,width,height);
	}
	
}
