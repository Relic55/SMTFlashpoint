package model;

public class Wallblock {
	
	
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
	
	
	
	
}
