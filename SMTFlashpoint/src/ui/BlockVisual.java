/**
 * 
 */
package ui;

import controller.GameEngine;
import model.Block;
import model.Walltype;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class BlockVisual extends Zone{
	private Block block;
	private GameEngine g;
	private int size, x_offset, y_offset;
	private String pic_path;
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
	
	public BlockVisual(GameEngine g,String pic_path, Block block)
	{
		this.g=g;
		this.pic_path=pic_path;
		this.block=block;
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
	
	@Override
	public void draw()
	{
		if (block.isFire())
		{
			
			image(fireimage,(float)(x_offset+block.getYb()*size+0.2*size),(float)(y_offset+block.getXb()*size+0.2*size),size*60/100,size*60/100);
		}
		if (block.isSmoke())
		{	
			image(smokeimage,(float)(x_offset+block.getYb()*size+0.2*size),(float)(y_offset+block.getXb()*size+0.2*size),size*60/100,size*60/100);
		}			
		if(block.getEast()!=null)//vertikal
		{
			if(block.getEast().getWall()==Walltype.DOORCLOSED)
			{	
				image(closeddoorimage, (float)(x_offset+(block.getYb()*size)+0.8*size),(float)(y_offset+(block.getXb()*size)+0.4*size), (size*40/100),(size*40/100) );
			}
			else if(block.getEast().getWall()==Walltype.DOOROPEN)
			{	
				image(opendoorimage, (float)(x_offset+(block.getYb()*size)+0.8*size),(float)(y_offset+(block.getXb()*size)+0.4*size), (size*40/100),(size*40/100) );
			}
			else if(block.getEast().getWall()==Walltype.WALLDAMAGED)
			{			
				fill(43,43,43);
				rect((float)(x_offset+(block.getYb()*size)+0.9*size),(float)(y_offset+(block.getXb()*size)+0.3*size),(size*15/100),(size*15/100));
			}
			else if(block.getEast().getWall()==Walltype.WALLDESTROYED)
			{			
				fill(43,43,43);
				rect((float)(x_offset+(block.getYb()*size)+0.9*size),(float)(y_offset+(block.getXb()*size)+0.3*size),(size*15/100),(size*15/100));
				rect((float)(x_offset+(block.getYb()*size)+0.9*size),(float)(y_offset+(block.getXb()*size)+0.6*size),(size*15/100),(size*15/100));
			}
		}
		
		if(block.getSouth()!=null)	
		{
			if(block.getSouth().getWall()==Walltype.DOORCLOSED)
			{
				pushMatrix();
				translate((float)(x_offset+(block.getYb()*size)+0.2*size),(float)(y_offset+(block.getXb()*size)+1*size));
				rotate(PI/2);
				image(closeddoorimage, (float)-0.15*size,(float)-0.4*size, (size*40/100),(size*40/100) );
				popMatrix();
			}
			else if(block.getSouth().getWall()==Walltype.DOOROPEN)
			{
				pushMatrix();
				translate((float)(x_offset+(block.getYb()*size)+0.2*size),(float)(y_offset+(block.getXb()*size)+1*size));
				rotate(PI/2);
				image(opendoorimage, (float)-0.15*size,(float)-0.4*size, (size*40/100),(size*40/100) );
				popMatrix();
			}
			else if(block.getSouth().getWall()==Walltype.WALLDAMAGED)
			{
				fill(43,43,43);
				rect((float)(x_offset+(block.getYb()*size)+0.3*size),(float)(y_offset+(block.getXb()*size)+size),(size*15/100),(size*15/100));
			}
			else if(block.getSouth().getWall()==Walltype.WALLDESTROYED)
			{
				fill(43,43,43);
				rect((float)(x_offset+(block.getYb()*size)+0.3*size),(float)(y_offset+(block.getXb()*size)+size),(size*15/100),(size*15/100));
				rect((float)(x_offset+(block.getYb()*size)+0.6*size),(float)(y_offset+(block.getXb()*size)+size),(size*15/100),(size*15/100));
			}
			
		}

		if(this.block.getXb()==0&&block.isAmbulance()&&g.board[0][block.getYb()+1].isAmbulance()) //Krankenwagen oben
		{
			image(ambulanceimage,(float)(x_offset+(block.getYb()*size)+(0.4*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*120/100,size*80/100);
		}
		else if(this.block.getXb()==7&&block.isAmbulance()&&g.board[7][block.getYb()-1].isAmbulance()) //Krankenwagen unten
		{
			pushMatrix();
			translate((float)(x_offset+(block.getYb()*size)),(float)(y_offset+block.getXb()*size));
			rotate(PI);
			image(ambulanceimage,(float)(0.4*size-size),(float)(0.1*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.block.getYb()==9&&block.isAmbulance()&&g.board[block.getXb()+1][9].isAmbulance()) //Krankenwagen rechts
		{
			pushMatrix();
			translate((float)(x_offset+(block.getYb()*size)),(float)(y_offset+block.getXb()*size));
			rotate(PI/2);
			image(ambulanceimage,(float)(0.4*size),(float)(0.05*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.block.getYb()==0&&block.isAmbulance()&&g.board[block.getXb()-1][0].isAmbulance()) //Krankenwagen links
		{
			pushMatrix();
			translate((float)(x_offset+(block.getYb()*size)),(float)(y_offset+block.getXb()*size));
			rotate(PI/2*3);
			image(ambulanceimage,(float)(0.4*size-size),(float)(0.05*size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		
		
		
		if(this.block.getXb()==0&&block.isFiretruck()&&g.board[0][block.getYb()+1].isFiretruck()) //Feuerwehrwagen oben
		{
			image(firetruckimage,(float)(x_offset+(block.getYb()*size)+(0.4*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*120/100,size*80/100);
		}
		else if(this.block.getXb()==7&&block.isFiretruck()&&g.board[7][block.getYb()-1].isFiretruck()) //Feuerwehrwagen unten
		{
			pushMatrix();
			translate((float)(x_offset+(block.getYb()*size)),(float)(y_offset+block.getXb()*size));
			rotate(PI);
			image(firetruckimage,(float)(0.4*size-size),(float)(0.1*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.block.getYb()==9&&block.isFiretruck()&&g.board[block.getXb()+1][9].isFiretruck()) //Feuerwehrwagen rechts
		{
			pushMatrix();
			translate((float)(x_offset+(block.getYb()*size)),(float)(y_offset+block.getXb()*size));
			rotate(PI/2);
			image(firetruckimage,(float)(0.4*size),(float)(0.05*size-size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		else if(this.block.getYb()==0&&block.isFiretruck()&&g.board[block.getXb()-1][0].isFiretruck()) //Feuerwehrwagen links
		{
			pushMatrix();
			translate((float)(x_offset+(block.getYb()*size)),(float)(y_offset+block.getXb()*size));
			rotate(PI/2*3);
			image(firetruckimage,(float)(0.4*size-size),(float)(0.05*size) ,size*120/100,size*80/100);		
			popMatrix();
		}
		
		if(block.isInterest()&&block.getPeople()==0&&block.getHealed_people()==0)
		{
			image(interestimage,(float)(x_offset+(block.getYb()*size)+(0.35*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			//fill(30,144,255);
			//ellipse((float)(x_offset+(block.getYb()*size)+0.5*size),(float)(y_offset+block.getXb()*size+0.25*size), (size*30/100),(size*30/100) );
					
		}
		if(!block.isInterest()&&block.getPeople()>0&&block.getHealed_people()==0)
		{
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.35*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			if(block.getPeople()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getPeople(), (float)(x_offset+(block.getYb()*size)+0.5*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}	
		}
		if(!block.isInterest()&&block.getPeople()==0&&block.getHealed_people()>0)
		{
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.35*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(block.getYb()*size)+(0.35*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			
			if(block.getHealed_people()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getHealed_people(), (float)(x_offset+(block.getYb()*size)+0.5*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}	
		}
		if(block.isInterest()&&block.getPeople()>0&&block.getHealed_people()==0)
		{
			image(interestimage,(float)(x_offset+(block.getYb()*size)+(0.2*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			if(block.getPeople()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getPeople(), (float)(x_offset+(block.getYb()*size)+0.65*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}				
		}
		if(block.isInterest()&&block.getPeople()==0&&block.getHealed_people()>0)
		{
			image(interestimage,(float)(x_offset+(block.getYb()*size)+(0.2*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			if(block.getHealed_people()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getHealed_people(), (float)(x_offset+(block.getYb()*size)+0.65*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}		
		}
		if(!block.isInterest()&&block.getPeople()>0&&block.getHealed_people()>0)
		{
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.2*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			if(block.getPeople()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getPeople(), (float)(x_offset+(block.getYb()*size)+0.35*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}	
			if(block.getHealed_people()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getHealed_people(), (float)(x_offset+(block.getYb()*size)+0.65*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}
		}
		if(block.isInterest()&&block.getPeople()>0&&block.getHealed_people()>0)
		{
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.2*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(catimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			image(healoverlayimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.1*size) ,size*30/100,size*30/100);
			if(block.getPeople()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getPeople(), (float)(x_offset+(block.getYb()*size)+0.35*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}	
			if(block.getHealed_people()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getHealed_people(), (float)(x_offset+(block.getYb()*size)+0.65*size), (float)(y_offset+block.getXb()*size+0.25*size));

			}
			image(interestimage,(float)(x_offset+(block.getYb()*size)+(0.2*size)),(float)(y_offset+block.getXb()*size+0.4*size) ,size*30/100,size*30/100);
			
			if(block.isSeat())
				image(seatimage,(float)(x_offset+(block.getYb()*size)+(0.5*size)),(float)(y_offset+block.getXb()*size+0.4*size) ,size*30/100,size*30/100);
			
		}
		else if(block.isSeat())
		{
			image(seatimage,(float)(x_offset+(block.getYb()*size)+(0.35*size)),(float)(y_offset+block.getXb()*size+0.4*size) ,size*30/100,size*30/100);
		}
		if(block.getDanger()>0)
		{
			image(dangerimage, (float)(x_offset+(block.getYb()*size)+(0.35*size)),(float)(y_offset+block.getXb()*size+0.65*size) ,size*30/100,size*30/100);
			if(block.getDanger()>1)
			{

				textAlign(CENTER);
				fill(1);
				textSize((int)(size/90.0*18));
				text(block.getDanger(), (float)(x_offset+(block.getYb()*size)+0.5*size), (float)(y_offset+block.getXb()*size+0.85*size));

			}
		}

		
		return;
	
	}
	@Override
	public void touch()
	{
		
	}
}
