package model;

import java.util.Random;

import controller.GameEngine;
import controller.GameEngine.PhaseStates;
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
	
	private boolean firetruckplace=false;
	private boolean ambulanceplace=false;
	
	private PImage fireimage;
	private PImage smokeimage;
	private PImage interestimage;
	private PImage dangerimage;
	private PImage seatimage;
	private PImage closeddoorimage;
	private PImage opendoorimage;
	private PImage catimage;
	private PImage healoverlayimage;
	private PImage firetruckimage;
	private PImage ambulanceimage;
	
	
	
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
		fireimage=Utility.getImage(pic_path+"/Feuer.gif");
		smokeimage=Utility.getImage(pic_path+"/Rauch.gif") ;
		interestimage=Utility.getImage(pic_path+"/Marker_Einsatzmarker.gif");
		dangerimage=Utility.getImage(pic_path+"/Marker_Gefahrenstoff.gif");
		seatimage=Utility.getImage(pic_path+"/Brandherd.gif");
		closeddoorimage=Utility.getImage(pic_path+"/Marker_Tuer_geschlossen.gif");
		opendoorimage=Utility.getImage(pic_path+"/Marker_Tuer_offen.gif");
		catimage=Utility.getImage(pic_path+"/Marker_Person_Katze.gif");
		healoverlayimage=Utility.getImage(pic_path+"/Marker_Person_geheilt.gif");
		firetruckimage= Utility.getImage(pic_path+"/Feuerwehrwagen.gif");
		ambulanceimage=Utility.getImage(pic_path+"/Krankenwagen.gif");
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
	public void setAmbulanceplace()
	{
		this.ambulanceplace=true;
	}
	public void setFiretruckplace()
	{
		this.firetruckplace=true;
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
	
	//Schaut, ob Nachbarn Feuer haben und eigenes Feld Rauch
	public boolean checkneightbors_Fire(Integer x, Integer y)
	{
		boolean test=false;
		if(smoke)
		{

			if(north==null||north.passage_Wall())
			{
				if(g.board[x-1][y].fire)
				{
					g.board[x][y].setSmoke(false);
					g.board[x][y].setFire(true);
					test=true;
				}
			}
			if(east==null||east.passage_Wall())
			{
				if(g.board[x][y+1].fire)
				{
					g.board[x][y].setSmoke(false);
					g.board[x][y].setFire(true);
					test=true;
				}
			}
			if(south==null||south.passage_Wall())
			{
				if(g.board[x+1][y].fire)
				{
					g.board[x][y].setSmoke(false);
					g.board[x][y].setFire(true);
					test=true;
				}
			}
			if(west==null||west.passage_Wall())
			{
				if(g.board[x][y-1].fire)
				{
					g.board[x][y].setSmoke(false);
					g.board[x][y].setFire(true);
					test=true;
				}
			}
		
		}
			
				//, east, south, west;
		return test;
	}
	//Schaut, ob Nachbarn Rauch haben und eigenes Feld Feuer
	public boolean checkneightbors_Smoke(Integer x, Integer y)
	{
		boolean test=false;
		if(fire)
		{

			if(north==null||north.passage_Wall())
			{
				if(g.board[x-1][y].smoke)
				{
					g.board[x-1][y].setSmoke(false);
					g.board[x-1][y].setFire(true);
					test=true;
				}
			}
			if(east==null||east.passage_Wall())
			{
				if(g.board[x][y+1].smoke)
				{
					g.board[x][y+1].setSmoke(false);
					g.board[x][y+1].setFire(true);
					test=true;
				}
			}
			if(south==null||south.passage_Wall())
			{
				if(g.board[x+1][y].smoke)
				{
					g.board[x+1][y].setSmoke(false);
					g.board[x+1][y].setFire(true);
					test=true;
				}
			}
			if(west==null||west.passage_Wall())
			{
				if(g.board[x][y-1].smoke)
				{
					g.board[x][y-1].setSmoke(false);
					g.board[x][y-1].setFire(true);
					test=true;
				}
			}
		
		}
			
				//, east, south, west;
		return test;
	}
	
	public void scanInterest()
	{
		this.interest=false;
		Random rand=new Random();
		int pm=g.getPerson_marker();
		int fam=g.getFalse_alarm_marker();
		if(fam==0)
		{
			this.people++;
			
			//g.setNewInterest();
		}
		else
		{
			
			int randomvalue = rand.nextInt(pm+fam);
			if(randomvalue<fam)		//Fehlalarme sind die ersten fam Ziffern --> Gleichverteilung der Werte
			{
						//TODO: Fehlalarm animieren
				System.out.println("Fehlalarm" + xb + ":" +yb);
				g.setFalse_alarm_marker(fam-1);
				g.decreaseInterest_onboard();
			}
			else
			{
				System.out.println("Person gefunden" + xb + ":" +yb);
				this.people++;
			}
			
			
			
		}
	}

	
	public void reducePeople()
	{
		this.people--;
	}
	
	public void reduceHealedPeople()
	{
		this.healed_people--;
	}
	
	public void reduceDanger()
	{
		this.danger--;
	}
	public void increasePeople()
	{
		this.people++;
	}
	
	public void increaseHealedPeople()
	{
		this.healed_people++;
	}
	
	public void increaseDanger()
	{
		this.danger++;
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
			
			image(fireimage,(float)(x_offset+yb*size+0.2*size),(float)(y_offset+xb*size+0.2*size),size*60/100,size*60/100);
			//fill(100,0,0);
			//ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.5*size), (size*60/100),(size*60/100) );
		}
		if (smoke)
		{
			
			image(smokeimage,(float)(x_offset+yb*size+0.2*size),(float)(y_offset+xb*size+0.2*size),size*60/100,size*60/100);
			//fill(102,102,102);
			//ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.5*size), (size*60/100),(size*60/100) );
		}	
		
		if(east!=null)//vertikal
		{
			if(east.getWall()==Walltype.DOORCLOSED)
			{	
				image(closeddoorimage, (float)(x_offset+(yb*size)+0.8*size),(float)(y_offset+(xb*size)+0.4*size), (size*40/100),(size*40/100) );
				//fill(80,80,80);
				//ellipse((float)(x_offset+(yb*size)+size),(float)(y_offset+(xb*size)+0.5*size), (size*40/100),(size*40/100) );
			}
			else if(east.getWall()==Walltype.DOOROPEN)
			{	
				image(opendoorimage, (float)(x_offset+(yb*size)+0.8*size),(float)(y_offset+(xb*size)+0.4*size), (size*40/100),(size*40/100) );
				//fill(150,150,150);
				//ellipse((float)(x_offset+(yb*size)+size),(float)(y_offset+(xb*size)+0.5*size), (size*40/100),(size*40/100) );
			}
			else if(east.getWall()==Walltype.WALLDAMAGED)
			{
				
				fill(43,43,43);
				rect((float)(x_offset+(yb*size)+0.9*size),(float)(y_offset+(xb*size)+0.3*size),(size*15/100),(size*15/100));
			}
			else if(east.getWall()==Walltype.WALLDESTROYED)
			{
				
				fill(43,43,43);
				rect((float)(x_offset+(yb*size)+0.9*size),(float)(y_offset+(xb*size)+0.3*size),(size*15/100),(size*15/100));
				rect((float)(x_offset+(yb*size)+0.9*size),(float)(y_offset+(xb*size)+0.6*size),(size*15/100),(size*15/100));
			}
		}
		
		if(south!=null)	
		{
			if(south.getWall()==Walltype.DOORCLOSED)
			{
				pushMatrix();
				translate((float)(x_offset+(yb*size)+0.2*size),(float)(y_offset+(xb*size)+1*size));
				rotate(PI/2);
				image(closeddoorimage, (float)-0.15*size,(float)-0.4*size, (size*40/100),(size*40/100) );
				//fill(80,80,80);
				//ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+(xb*size)+size), (size*40/100),(size*40/100) );
				
				popMatrix();
			}
			else if(south.getWall()==Walltype.DOOROPEN)
			{
				pushMatrix();
				translate((float)(x_offset+(yb*size)+0.2*size),(float)(y_offset+(xb*size)+1*size));
				rotate(PI/2);
				image(opendoorimage, (float)-0.15*size,(float)-0.4*size, (size*40/100),(size*40/100) );
				//fill(150,150,150);
				//ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+(xb*size)+size), (size*40/100),(size*40/100) );
				popMatrix();
			}
			else if(south.getWall()==Walltype.WALLDAMAGED)
			{
				fill(43,43,43);
				rect((float)(x_offset+(yb*size)+0.3*size),(float)(y_offset+(xb*size)+size),(size*15/100),(size*15/100));
			}
			else if(south.getWall()==Walltype.WALLDESTROYED)
			{
				fill(43,43,43);
				rect((float)(x_offset+(yb*size)+0.3*size),(float)(y_offset+(xb*size)+size),(size*15/100),(size*15/100));
				rect((float)(x_offset+(yb*size)+0.6*size),(float)(y_offset+(xb*size)+size),(size*15/100),(size*15/100));
			}
			
		}

		if(this.xb==0&&ambulance&&g.board[0][yb+1].ambulance) //Krankenwagen oben
		{
			image(ambulanceimage,(float)(x_offset+(yb*size)+(0.4*size)),(float)(y_offset+xb*size+0.1*size) ,size*120/100,size*80/100);
		}
		else if(this.xb==7&&ambulance&&g.board[7][yb-1].ambulance) //Krankenwagen unten
		{
			pushMatrix();
			translate((float)(x_offset+(yb*size)),(float)(y_offset+xb*size));
			rotate(PI);
			image(ambulanceimage,(float)(0.4*size-size),(float)(0.1*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.yb==9&&ambulance&&g.board[xb+1][9].ambulance) //Krankenwagen rechts
		{
			pushMatrix();
			translate((float)(x_offset+(yb*size)),(float)(y_offset+xb*size));
			rotate(PI/2);
			image(ambulanceimage,(float)(0.4*size),(float)(0.05*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.yb==0&&ambulance&&g.board[xb-1][0].ambulance) //Krankenwagen links
		{
			pushMatrix();
			translate((float)(x_offset+(yb*size)),(float)(y_offset+xb*size));
			rotate(PI/2*3);
			image(ambulanceimage,(float)(0.4*size-size),(float)(0.05*size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		
		
		
		if(this.xb==0&&firetruck&&g.board[0][yb+1].firetruck) //Feuerwehrwagen oben
		{
			image(firetruckimage,(float)(x_offset+(yb*size)+(0.4*size)),(float)(y_offset+xb*size+0.1*size) ,size*120/100,size*80/100);
		}
		else if(this.xb==7&&firetruck&&g.board[7][yb-1].firetruck) //Feuerwehrwagen unten
		{
			pushMatrix();
			translate((float)(x_offset+(yb*size)),(float)(y_offset+xb*size));
			rotate(PI);
			image(firetruckimage,(float)(0.4*size-size),(float)(0.1*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.yb==9&&firetruck&&g.board[xb+1][9].firetruck) //Feuerwehrwagen rechts
		{
			pushMatrix();
			translate((float)(x_offset+(yb*size)),(float)(y_offset+xb*size));
			rotate(PI/2);
			image(firetruckimage,(float)(0.4*size),(float)(0.05*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.yb==0&&firetruck&&g.board[xb-1][0].firetruck) //Feuerwehrwagen links
		{
			pushMatrix();
			translate((float)(x_offset+(yb*size)),(float)(y_offset+xb*size));
			rotate(PI/2*3);
			image(firetruckimage,(float)(0.4*size-size),(float)(0.05*size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		
		if(interest&&this.people==0&&this.healed_people==0)
		{
			image(interestimage,(float)(x_offset+(yb*size)+(0.35*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			//fill(30,144,255);
			//ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.25*size), (size*30/100),(size*30/100) );
					
		}
		if(!interest&&this.people>0&&this.healed_people==0)
		{
			image(catimage,(float)(x_offset+(yb*size)+(0.35*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			if(people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(people, (float)(x_offset+(yb*size)+0.5*size), (float)(y_offset+xb*size+0.25*size));

			}	
		}
		if(!interest&&this.people==0&&this.healed_people>0)
		{
			image(catimage,(float)(x_offset+(yb*size)+(0.35*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(yb*size)+(0.35*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			
			if(healed_people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(healed_people, (float)(x_offset+(yb*size)+0.5*size), (float)(y_offset+xb*size+0.25*size));

			}	
		}
		if(interest&&this.people>0&&this.healed_people==0)
		{
			image(interestimage,(float)(x_offset+(yb*size)+(0.2*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			if(people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(people, (float)(x_offset+(yb*size)+0.65*size), (float)(y_offset+xb*size+0.25*size));

			}				
		}
		if(interest&&this.people==0&&this.healed_people>0)
		{
			image(interestimage,(float)(x_offset+(yb*size)+(0.2*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			if(healed_people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(healed_people, (float)(x_offset+(yb*size)+0.65*size), (float)(y_offset+xb*size+0.25*size));

			}		
		}
		if(!interest&&this.people>0&&this.healed_people>0)
		{
			image(catimage,(float)(x_offset+(yb*size)+(0.2*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			if(people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(people, (float)(x_offset+(yb*size)+0.35*size), (float)(y_offset+xb*size+0.25*size));

			}	
			if(healed_people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(healed_people, (float)(x_offset+(yb*size)+0.65*size), (float)(y_offset+xb*size+0.25*size));

			}
		}
		if(interest&&this.people>0&&this.healed_people>0)
		{
			image(catimage,(float)(x_offset+(yb*size)+(0.2*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.1*size) ,size*30/100,size*30/100);
			if(people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(people, (float)(x_offset+(yb*size)+0.35*size), (float)(y_offset+xb*size+0.25*size));

			}	
			if(healed_people>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(healed_people, (float)(x_offset+(yb*size)+0.65*size), (float)(y_offset+xb*size+0.25*size));

			}
			image(interestimage,(float)(x_offset+(yb*size)+(0.2*size)),(float)(y_offset+xb*size+0.4*size) ,size*30/100,size*30/100);
			
			if(seat)
				image(seatimage,(float)(x_offset+(yb*size)+(0.5*size)),(float)(y_offset+xb*size+0.4*size) ,size*30/100,size*30/100);
			
		}
		else if(seat)
		{
			image(seatimage,(float)(x_offset+(yb*size)+(0.35*size)),(float)(y_offset+xb*size+0.4*size) ,size*30/100,size*30/100);
		}
		if(danger>0)
		{
			image(dangerimage, (float)(x_offset+(yb*size)+(0.35*size)),(float)(y_offset+xb*size+0.65*size) ,size*30/100,size*30/100);
			//fill(69,139,0);
			//ellipse((float)(x_offset+(yb*size)+0.5*size),(float)(y_offset+xb*size+0.75*size), (size*30/100),(size*30/100) );			
			if(danger>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(danger, (float)(x_offset+(yb*size)+0.5*size), (float)(y_offset+xb*size+0.85*size));

			}
		}

		
		return;
	
	}
	@Override
	public void touch() {
		//drag();
	}


	/**
	 * @return the xb
	 */
	public int getXb() {
		return xb;
	}


	/**
	 * @return the yb
	 */
	public int getYb() {
		return yb;
	}


	/**
	 * @return the firetruckplace
	 */
	public boolean isFiretruckplace() {
		return firetruckplace;
	}


	/**
	 * @return the ambulanceplace
	 */
	public boolean isAmbulanceplace() {
		return ambulanceplace;
	}
}
