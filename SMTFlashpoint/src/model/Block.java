package model;

public class Block {
	
	private boolean inside_Block;
	private boolean smoke;
	private boolean fire;
	private Integer danger; //Gefahrenstoff
	private boolean interest; //Einsatzmarker
	private boolean seat; //Brandherd
	private Integer people; //Anzahl verletzter Personen
	private Integer healed_people; //Anzahl verarzteter Personen
	private Wallblock north, east, south, west;
	private boolean firetruck;
	private boolean ambulance;
	
	public Block()
	{
		this(false, false, false,0,false,false,0,0,null, null, null, null);
	}
	
	
	public Block(boolean inside_Block,boolean smoke, boolean fire, Integer danger ,boolean interest, boolean seat,Integer people ,Integer healed_people,Wallblock north,Wallblock east,Wallblock south,Wallblock west )
	{
		this.inside_Block=inside_Block;
		this.smoke=smoke;
		this.fire=fire;
		this.danger=danger;
		this.interest=interest;
		this.seat=seat;
		this.people=people;
		this.healed_people=healed_people;
		this.north=north;
		this.east=east;
		this.south=south;
		this.west=west;
			
	}
	public void set_all(boolean inside_Block,boolean smoke, boolean fire, Integer danger ,boolean interest, boolean seat,Integer people ,Integer healed_people,Wallblock north,Wallblock east,Wallblock south,Wallblock west )
	{
		this.inside_Block=inside_Block;
		this.smoke=smoke;
		this.fire=fire;
		this.danger=danger;
		this.interest=interest;
		this.seat=seat;
		this.people=people;
		this.healed_people=healed_people;
		this.north=north;
		this.east=east;
		this.south=south;
		this.west=west;
			
	}

	public Integer getDanger() {
		return danger;
	}


	public void setDanger(Integer danger) {
		this.danger = danger;
	}


	public boolean isInside_Block() {
		return inside_Block;
	}


	public boolean isSmoke() {
		return smoke;
	}


	public boolean isFire() {
		return fire;
	}


	public boolean isInterest() {
		return interest;
	}


	public boolean isSeat() {
		return seat;
	}


	public Integer getPeople() {
		return people;
	}


	public Integer getHealed_people() {
		return healed_people;
	}


	public Wallblock getNorth() {
		return north;
	}


	public Wallblock getEast() {
		return east;
	}


	public Wallblock getSouth() {
		return south;
	}


	public Wallblock getWest() {
		return west;
	}


	public boolean isFiretruck() {
		return firetruck;
	}


	public void setFiretruck(boolean firetruck) {
		this.firetruck = firetruck;
	}


	public boolean isAmbulance() {
		return ambulance;
	}


	public void setAmbulance(boolean ambulance) {
		this.ambulance = ambulance;
	}
	
	public boolean checkneightbors_smoke(Integer x, Integer y, Block[][] board)
	{
		boolean test=false;
		if(fire)
		{

			if(north==null||north.passage_Wall())
			{
				if(board[x-1][y].smoke)
				{
					board[x-1][y].setSmoke(false);
					board[x-1][y].setFire(true);
					test=true;
				}
			}
			if(east==null||east.passage_Wall())
			{
				if(board[x][y+1].smoke)
				{
					board[x][y+1].setSmoke(false);
					board[x][y+1].setFire(true);
					test=true;
				}
			}
			if(south==null||south.passage_Wall())
			{
				if(board[x+1][y].smoke)
				{
					board[x+1][y].setSmoke(false);
					board[x+1][y].setFire(true);
					test=true;
				}
			}
			if(west==null||west.passage_Wall())
			{
				if(board[x][y-1].smoke)
				{
					board[x][y-1].setSmoke(false);
					board[x][y-1].setFire(true);
					test=true;
				}
			}
		
		}
			
				//, east, south, west;
		return test;
	}


	public void setSmoke(boolean smoke) {
		this.smoke = smoke;
	}


	public void setFire(boolean fire) {
		this.fire = fire;
	}


	public void setNorth(Wallblock north) {
		this.north = north;
	}


	public void setEast(Wallblock east) {
		this.east = east;
	}


	public void setSouth(Wallblock south) {
		this.south = south;
	}


	public void setWest(Wallblock west) {
		this.west = west;
	}
	
}
