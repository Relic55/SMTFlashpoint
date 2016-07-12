package model;

import vialab.SMT.Zone;

public class Wallblock extends Zone {
	
	
	private Integer direction; //0=horizontal, 1=vertikal
	private Walltype wall;
	
	
	public Wallblock()
	{
		this(0, Walltype.DOORDESTROYED);
	}
	
	public Wallblock(Integer direction, Walltype wall)
	{
		this.direction=direction;
		this.wall=wall;
	}
	public Wallblock(Integer direction, int wallint)  //Int Konstruktor
	{
		this.direction=direction;
		if(wallint==0)
			this.wall=Walltype.BOARDEND;
		else if(wallint==1)
			this.wall=Walltype.WALL;
		else if(wallint==2)
			this.wall=Walltype.WALLDAMAGED;
		else if(wallint==3)
			this.wall=Walltype.WALLDESTROYED;
		else if(wallint==4)
			this.wall=Walltype.DOORCLOSED;
		else if(wallint==5)
			this.wall=Walltype.DOOROPEN;
		else if(wallint==6)
			this.wall=Walltype.DOORDESTROYED;
		else if(wallint==7)
			this.wall=Walltype.OUTDOOROPEN;
		
	}
	
	public boolean switch_Door()
	{
		if(wall==Walltype.DOORCLOSED)
		{
			wall= Walltype.DOOROPEN;
			return true;
		}
		if(wall==Walltype.DOOROPEN)
		{
			wall= Walltype.DOORCLOSED;
			return true;
		}
		return false;
	}
	public Integer damage_Wall()		//gibt Anzahl der neu hinzugefügten Schadensmarker zurück
	{
		if(wall==Walltype.DOORCLOSED)
		{
			wall=Walltype.DOORDESTROYED;
			return 0;							
		}
		if(wall==Walltype.WALL)
		{
			wall=Walltype.WALLDAMAGED;
			return 1;
		}		
		if(wall==Walltype.WALLDAMAGED)
		{
			wall=Walltype.WALLDESTROYED;
			return 1;
		}				
		return 0;
	}
	public boolean passage_Wall() //ist der Wallabschnitt begehbar
	{
		if(wall==Walltype.DOOROPEN||wall==Walltype.DOORDESTROYED||wall==Walltype.WALLDESTROYED||wall==Walltype.OUTDOOROPEN)
			return true;
		
		return false;
	}

	public Walltype getWall() {
		return wall;
	}
	
	@Override
	public void draw()
	{
		return;
	}
	
	@Override
	public void touch()
	{
		
	}
	
	
}
