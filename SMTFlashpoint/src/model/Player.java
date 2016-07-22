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
	//private int fillColor;
	private Integer xb,yb, ap, sp, apgain, spgain; 
	//float xpos,ypos;
	private GameEngine g;
	private boolean noapcost=false; //keine Kosten zu Testzwecken
	//TODO: entfernen
	
	
	 public Player(GameEngine g)	 
	{
		this(g, SpecialistType.DUMMY, PlayerColor.DEFAULT);
	}

	public Player(GameEngine g, SpecialistType specialist,PlayerColor playerColor )
	{
		this.g=g;
		
		this.playerColor=playerColor;
		//fillColor = new Color(100,100,100);
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
	public void setplayer(SpecialistType specialist,PlayerColor playerColor,Integer ap,Integer xb ,Integer yb )
	{
		sp=0;
		this.playerColor=playerColor;
		this.specialist=specialist;
		this.ap=ap;
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
		sp=0;
		
	}
	
	
	//Test, ob Aktion mit den verbliebenen Aktionspunkten moeglich ist
	public boolean enoughPoints(Actiontype type)
	{
		if(this.noapcost)
			return true;
		//Einsatzleiter Sonderaktionen fehlen
		if(type==Actiontype.MOVE||type==Actiontype.MOVE_WITH_HEALED_PERSON)
		{
			if(ap>0)
				return true;
			else if (this.specialist==SpecialistType.RETTUNGSSPEZIALIST&&sp>0)
				return true;
		}	
		else if(type==Actiontype.MOVE_TO_FIRE||type==Actiontype.MOVE_CARRY_PERSON||type==Actiontype.TRANSPORT_DANGER||type==Actiontype.TRANSPORT_DANGER_AND_HEALED||type==Actiontype.MOVE_CARRY_AND_HEALED)
		{
			if(ap>1)
				return true;
			else if (this.specialist==SpecialistType.RETTUNGSSPEZIALIST&&(sp+ap)>1)
				return true;
		}
		else if(type==Actiontype.EXTINQUISH_FIRE)
		{
			if(this.specialist==SpecialistType.RETTUNGSSANITAETER||this.specialist==SpecialistType.RETTUNGSSPEZIALIST)
			{
				if(ap>3)
					return true;
				else
					return false;
			}
			
			else if(ap>1)
				return true;
			else if (this.specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST&&(sp+ap)>1)
				return true;
		}		
		else if(type==Actiontype.EXTINQUISH_SMOKE||type==Actiontype.EXTINQUISH_STEP)
		{
			if(this.specialist==SpecialistType.RETTUNGSSANITAETER||this.specialist==SpecialistType.RETTUNGSSPEZIALIST)
			{
				if(ap>1)
					return true;
				else
					return false;
			}
			
			else if(ap>0)
				return true;
			else if (this.specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST&&(sp+ap)>0)
				return true;
		}
		else if(type==Actiontype.OPEN_DOOR||type==Actiontype.CLOSE_DOOR)
		{
			if(ap>0)
				return true;
		}
		else if(type==Actiontype.DAMAGE_WALL)
		{
			if(ap>1)
				return true;
			else if(this.specialist==SpecialistType.RETTUNGSSPEZIALIST&&ap>0)
				return true;
		}
		else if(type==Actiontype.MOVE_AMBULANCE||type==Actiontype.MOVE_FIRETRUCK)
		{
			if(ap>1)
				return true;
		}
		else if(type==Actiontype.USE_FIRETRUCK)
		{
			if(ap>3)
				return true;
			else if(this.specialist==SpecialistType.FAHRZEUGMASCHINIST&&ap>1)
				return true;
		}
		else if(type==Actiontype.HEAL_PERSON)
		{
			if(ap>0)
				return true;
		}
		else if(type==Actiontype.IDENTIFY)
		{
			if(ap>0)
				return true;
		}
		else if(type==Actiontype.REMOVE_DANGER)
		{
			if(ap>1)
				return true;
		}
		else if(type==Actiontype.CONTROL_FIREFIGHTER)
		{
				return true;
		}
		else if(type==Actiontype.CANCEL)
			return true;
		
		return false;
	}
	
	//Aufsplitten in AP und SP Kosten
	public void spendPoints(Actiontype type)
	{
		if(this.noapcost)
			return;
		//Einsatzleiter Sonderaktionen fehlen
		if(type==Actiontype.MOVE||type==Actiontype.MOVE_WITH_HEALED_PERSON)
		{
			if (this.specialist==SpecialistType.RETTUNGSSPEZIALIST&&sp>0)
				this.spend_sp(1);
			else if(ap>0)
				this.spend_ap(1);
			 
		}	
		else if(type==Actiontype.MOVE_TO_FIRE||type==Actiontype.MOVE_CARRY_PERSON||type==Actiontype.TRANSPORT_DANGER||type==Actiontype.TRANSPORT_DANGER_AND_HEALED||type==Actiontype.MOVE_CARRY_AND_HEALED)
		{
			if (this.specialist==SpecialistType.RETTUNGSSPEZIALIST&&(sp+ap)>1)
			{
				if(sp>1)
					this.spend_sp(2);
				else if (sp>0)
				{
					this.spend_sp(1);
					this.spend_ap(1);
				}
				else 
					this.spend_ap(2);
			}
			else if(ap>1)
				this.spend_ap(2);

		}
		else if(type==Actiontype.EXTINQUISH_FIRE)
		{
			if(this.specialist==SpecialistType.RETTUNGSSANITAETER||this.specialist==SpecialistType.RETTUNGSSPEZIALIST)
			{
				if(ap>3)
					this.spend_ap(4);
			}
			else if (this.specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST&&(sp+ap)>1)
			{
				if(sp>1)
					this.spend_sp(2);
				else if(sp>0)
				{
					this.spend_ap(1);
					this.spend_sp(1);
				}
				else
					this.spend_ap(2);
				
			}
			else if(ap>1)
				this.spend_ap(2);
			
		}		
		else if(type==Actiontype.EXTINQUISH_SMOKE||type==Actiontype.EXTINQUISH_STEP)
		{
			if(this.specialist==SpecialistType.RETTUNGSSANITAETER||this.specialist==SpecialistType.RETTUNGSSPEZIALIST)
			{
				if(ap>1)
					this.spend_ap(2);
			}
			else if (this.specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST&&(sp+ap)>0)
			{
				if(sp>0)
					this.spend_sp(1);
				else
					this.spend_ap(1);
				
			}
			else if(ap>0)
				this.spend_ap(1);
			
			
		}
		else if(type==Actiontype.OPEN_DOOR||type==Actiontype.CLOSE_DOOR)
		{
			if(ap>0)
				this.spend_ap(1);
		}
		else if(type==Actiontype.DAMAGE_WALL)
		{
			if(this.specialist==SpecialistType.RETTUNGSSPEZIALIST&&ap>0)
				this.spend_ap(1);
			else if(ap>1)
				this.spend_ap(2);
		}
		else if(type==Actiontype.MOVE_AMBULANCE||type==Actiontype.MOVE_FIRETRUCK)
		{
			if(ap>1)
				this.spend_ap(2);
		}
		else if(type==Actiontype.USE_FIRETRUCK)
		{
			if(this.specialist==SpecialistType.FAHRZEUGMASCHINIST&&ap>1)
				this.spend_ap(2);
			else if(ap>3)
				this.spend_ap(4);
		}
		else if(type==Actiontype.HEAL_PERSON)
		{
			if(ap>0)
				this.spend_ap(1);
		}
		else if(type==Actiontype.IDENTIFY)
		{
			if(ap>0)
				this.spend_ap(1);
		}
		else if(type==Actiontype.REMOVE_DANGER)
		{
			if(ap>1)
				this.spend_ap(2);
		}

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
		SpecialistType new_specialist=SpecialistType.DUMMY;
		//Klassenauswahl anzeigen und new_specialist zuweisen
		//Position in GameEngine festlegen--> Feuerwehrwagen suchen
		int lastap=ap;
		this.setplayer(new_specialist,playerColor,ap,xb ,yb );
		this.start_turn();  //SP bekommen
		ap=lastap-2;
		
		
		
	}
	
	@Override
	public void draw() {
		/*
		if(xb==100||yb==100)
			return;
		int size=g.getBlock_size();
		//noStroke();

		fill(fillColor);
              ellipse(g.getX_offset()+(yb*size)+(xpos),g.getY_offset()+xb*size+(ypos), (size*80/100/3),(size*80/100/3) );
		//image(picture,x,y,width,height);
		 
		 */
	}
	@Override
	public void touch() {
		//drag();
	}
	/**
	 * @return the ap
	 */
	public Integer getAp() {
		return ap;
	}

	/**
	 * @param ap the ap to set
	 */
	public void setAp(Integer ap) {
		this.ap = ap;
	}

	/**
	 * @return the sp
	 */
	public Integer getSp() {
		return sp;
	}

	/**
	 * @param sp the sp to set
	 */
	public void setSp(Integer sp) {
		this.sp = sp;
	}

	/**
	 * @return the playerColor
	 */
	public PlayerColor getPlayerColor() {
		return playerColor;
	}

	/**
	 * @return the specialist
	 */
	public SpecialistType getSpecialist() {
		return specialist;
	}

	/**
	 * @return the xb
	 */
	public Integer getXb() {
		return xb;
	}

	/**
	 * @return the yb
	 */
	public Integer getYb() {
		return yb;
	}

	/**
	 * @param xb the xb to set
	 */
	public void setXb(Integer xb) {
		this.xb = xb;
	}

	/**
	 * @param yb the yb to set
	 */
	public void setYb(Integer yb) {
		this.yb = yb;
	}
	
}
