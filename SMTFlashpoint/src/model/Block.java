package model;

import controller.GameEngine;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.SMT;
import vialab.SMT.Zone;

public class Block extends Zone{
	
	private int xb,yb;
	private GameEngine g;
	private int size, x_offset, y_offset;
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
	private String pic_path;
	
	private PImage fireimage;
	private PImage smokeimage;
	private PImage interestimage;
	
	
	public Block(GameEngine g,String pic_path)
	{
		
		this(g, pic_path,false, false, false,0,false,false,0,0,null, null, null, null);
		
	}
	
	
	public Block(GameEngine g,String pic_path, boolean inside_Block,boolean smoke, boolean fire, Integer danger ,boolean interest, boolean seat,Integer people ,Integer healed_people,Wallblock north,Wallblock east,Wallblock south,Wallblock west )
	{
		this.g=g;
		this.x=0;
		this.y=0;
		this.pic_path=pic_path;
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
		size=g.getBlock_size();
		x_offset=g.getX_offset();
		y_offset=g.getY_offset();
			
	}
	public void set_all(int xb, int yb, boolean inside_Block,boolean smoke, boolean fire, Integer danger ,boolean interest, boolean seat,Integer people ,Integer healed_people,Wallblock north,Wallblock east,Wallblock south,Wallblock west )
	{
		this.xb=xb;
		this.yb=yb;
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

		//System.out.println(this.north+" "+this.east);
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


	/**
	 * @param interest the interest to set
	 */
	public void setInterest(boolean interest) {
		this.interest = interest;
	}


	/**
	 * @param seat the seat to set
	 */
	public void setSeat(boolean seat) {
		this.seat = seat;
	}


	/**
	 * @param people the people to set
	 */
	public void setPeople(Integer people) {
		this.people = people;
	}


	/**
	 * @param healed_people the healed_people to set
	 */
	public void setHealed_people(Integer healed_people) {
		this.healed_people = healed_people;
	}
	
	
	@Override
	public void draw()
	{
		if (fire)
		{
			//fireimage=Utility.getImage(pic_path+"/Feuer");
			//image(fireimage,x_offset+(xb*size),y_offset+yb*size,size,size);
			fill(100,0,0);
			ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.5*size), (size*60/100),(size*60/100) );
		}
		if (smoke)
		{
			//fireimage=Utility.getImage(pic_path+"/Feuer");
			//image(fireimage,x_offset+(xb*size),y_offset+yb*size,size,size);
			fill(102,102,102);
			ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.5*size), (size*60/100),(size*60/100) );
		}	
		
		if(east!=null&&(east.getWall()==Walltype.DOORCLOSED))
		{
				//vertikal
			fill(80,80,80);
			ellipse((float)(x_offset+(yb*size)+size),(float)(y_offset+(xb*size)+0.5*size), (size*40/100),(size*40/100) );
		
		}
		
		if(south!=null&&(south.getWall()==Walltype.DOORCLOSED))
		{
			fill(80,80,80);
			ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+(xb*size)+size), (size*40/100),(size*40/100) );
		
		}
		if(interest)
		{
			fill(30,144,255);
			ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.25*size), (size*30/100),(size*30/100) );
					
		}
		
		
		return;
	
	}
	
}
