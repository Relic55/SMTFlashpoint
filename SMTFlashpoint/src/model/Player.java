/**
 * 
 */
package model;

/**
 * @author Relic
 *
 */
public class Player {
	
	private SpecialistType specialist;
	private Color color;
	private Integer x,y, ap, sp, apgain, spgain;
	
	public Player()
	{
		this(SpecialistType.DUMMY, Color.DEFAULT);
	}
	
	public Player(SpecialistType specialist,Color color )
	{
		this.color=color;
		ap=0;
		sp=0;
		x=100;		//Startwert für ausserhalb des Feldes
		y=100;		//Startwert für ausserhalb des Feldes
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
	public void setplayer(SpecialistType specialist,Color color,Integer ap,Integer sp,Integer x ,Integer y )
	{
		this.color=color;
		this.specialist=specialist;
		this.ap=ap;
		this.sp=sp;
		this.x=x;		
		this.y=y;		
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
		this.setplayer(new_specialist,color,ap-2,sp,x ,y );
		
	}

}
