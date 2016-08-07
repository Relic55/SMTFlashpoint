/**
 * 
 */
package ui;

import controller.GameEngine;
import model.AnimationKind;
import model.Block;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class AnimationToken extends Zone{
	
	private GameEngine g;
	private Block block;
	private int x,y,width,height, block_size;
	private AnimationKind type;
	private PImage image1, image2;
	private long starttime, actualtime,animationDuration,endtime;
	
	
	public AnimationToken(int x, int y, int width, int height, GameEngine g, AnimationKind type, String pic_path)
	{
		super(x,y,width, height);
		this.x=x;
		this.y=y;
		this.width=width;
		this.height=height;
		this.g=g;
		//this.block=block;
		this.type=type;
		this.block_size=g.getBlock_size();
		starttime=System.currentTimeMillis();
		animationDuration=g.getAnimationDuration();
		actualtime=starttime;
		endtime=starttime+animationDuration;
		
		image1=null;
		image2=null;
		if(type==AnimationKind.NEWSMOKE)
		{
			image1=Utility.getImage(pic_path+"/Rauch.gif") ;
		}
		else if(type==AnimationKind.NEWFIREONSMOKE)
		{
			image1=Utility.getImage(pic_path+"/Rauch.gif") ;
			image2=Utility.getImage(pic_path+"/Feuer.gif");
		}
		else if(type==AnimationKind.EXPLOSION)
		{
			image1=Utility.getImage(pic_path+"/Feuer.gif");
		}
		else if(type==AnimationKind.FALSEALARM)
		{
			image1=Utility.getImage(pic_path+"/Marker_Einsatzmarker.gif");
			image2=Utility.getImage(pic_path+"/Marker_Fehlalarm.gif");
		}
		else if(type==AnimationKind.PERSONFOUND)
		{
			image1=Utility.getImage(pic_path+"/Marker_Einsatzmarker.gif");
			image2=Utility.getImage(pic_path+"/Marker_Person_Katze.gif");
		}
//		image1=Utility.getImage(pic_path+"/Feuer.gif");
//		
//		image1=Utility.getImage(pic_path+"/Marker_Einsatzmarker.gif");
//		image1=Utility.getImage(pic_path+"/Marker_Gefahrenstoff.gif");
//		image1=Utility.getImage(pic_path+"/Brandherd.gif");
		
		
		
	}

	
	
	
	
	
	@Override
	public void draw()
	{
		if(g.isAnimationInProgess())
		{
			actualtime=System.currentTimeMillis();
			if(actualtime>=endtime)
			{
				g.setAnimationInProgess(false);
			}
			else if(type==AnimationKind.NEWSMOKE)
			{			
				image(image1,-block_size*3/4+ ((actualtime-starttime)/block_size)*2,-block_size*3/4+ ((actualtime-starttime)/block_size)*2, width*3-((actualtime-starttime)/block_size)*6,height*3-((actualtime-starttime)/block_size)*6);
				
			}
			else if(type==AnimationKind.NEWFIREONSMOKE)
			{		
				if(actualtime<starttime+this.animationDuration/2) //vorhandener Rauch wird größer
					image(image1,-((actualtime-starttime)/block_size)*2,-((actualtime-starttime)/block_size)*2, width+((actualtime-starttime)/block_size)*6,height+((actualtime-starttime)/block_size)*6);
				else //Rauch wird zu Feuer und wieder kleiner
					image(image2,-block_size*3/4+ ((actualtime-starttime)/block_size)*2,-block_size*3/4+ ((actualtime-starttime)/block_size)*2, width*3-((actualtime-starttime)/block_size)*6,height*3-((actualtime-starttime)/block_size)*6);
									
			}
			else if(type==AnimationKind.EXPLOSION)
			{			
				//TODO: aktuell Platzhalter
				image(image1,0,0, width,height);
				if(actualtime>starttime+this.animationDuration/4)
				{
					image(image1,-block_size/2+width/4,0+height/4, width/2,height/2);
					image(image1,+width/4,-block_size/2+height/4, width/2,height/2);
					image(image1,+block_size/2+width/4,0+height/4, width/2,height/2);
					image(image1,0+width/4,+block_size/2+height/4, width/2,height/2);
				}
				if(actualtime>starttime+this.animationDuration/2)
				{
					image(image1,-block_size+width/4,0+height/4, width/2,height/2);
					image(image1,+width/4,-block_size+height/4, width/2,height/2);
					image(image1,+block_size+width/4,0+height/4, width/2,height/2);
					image(image1,0+width/4,+block_size+height/4, width/2,height/2);
				}
//				
			}
			else if(type==AnimationKind.FALSEALARM||type==AnimationKind.PERSONFOUND)
			{		
				if(actualtime<starttime+this.animationDuration/2) //vorhandener Rauch wird größer
					image(image1,-((actualtime-starttime)/block_size)*2,-((actualtime-starttime)/block_size)*2, width+((actualtime-starttime)/block_size)*6,height+((actualtime-starttime)/block_size)*6);
				else //Rauch wird zu Feuer und wieder kleiner
					image(image2,-block_size*3/4+ ((actualtime-starttime)/block_size)*2,-block_size*3/4+ ((actualtime-starttime)/block_size)*2, width*3-((actualtime-starttime)/block_size)*6,height*3-((actualtime-starttime)/block_size)*6);
									
			}
		}
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
}
