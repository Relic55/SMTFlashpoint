/**
 * 
 */
package model;

import vialab.SMT.Zone;

/**
 * @author Relic
 * CONTROL_FIREFIGHTER fehlt
 * Zeile 108 USE_FIRETRUCK weitere Kriterien notwendig
 */
public class Action extends Zone { // 
	private int apcost;
	private Actiontype type;

	public Action()
	{
		this(Actiontype.CANCEL);
	}
	
	public Action(Actiontype type)
	{
		this.type=type;
		if(type==Actiontype.MOVE||type==Actiontype.OPEN_DOOR||type==Actiontype.CLOSE_DOOR||type==Actiontype.IDENTIFY||type==Actiontype.HEAL_PERSON||type==Actiontype.MOVE_WITH_HEALED_PERSON||type==Actiontype.EXTINQUISH_STEP||type==Actiontype.EXTINQUISH_SMOKE)
		{
			apcost=1;
		}
		else if(type==Actiontype.CONTROL_FIREFIGHTER||type==Actiontype.CANCEL)
		{
			apcost=0;
		}
		else if(type==Actiontype.MOVE_TO_FIRE||type==Actiontype.EXTINQUISH_FIRE||type==Actiontype.DAMAGE_WALL||type==Actiontype.MOVE_AMBULANCE||type==Actiontype.MOVE_FIRETRUCK||type==Actiontype.REMOVE_DANGER||type==Actiontype.TRANSPORT_DANGER||type==Actiontype.MOVE_CARRY_PERSON||type==Actiontype.MOVE_CARRY_AND_HEALED||type==Actiontype.TRANSPORT_DANGER_AND_HEALED)
		{
			apcost=2;
		}
		else if(type==Actiontype.USE_FIRETRUCK)
		{
			apcost=4;
		}
	}

	
	public boolean action_possible(Block current_block,Block next_block)
	{
		
		Wallblock wall=null;
		if(current_block.getXb()<next_block.getXb())
		{
				wall=current_block.getSouth();					
		}
		else if(current_block.getXb()>next_block.getXb())
		{
				wall=current_block.getNorth();					
		}
		else if(current_block.getYb()<next_block.getYb())
		{
				wall=current_block.getEast();					
		}
		else if(current_block.getYb()>next_block.getYb())
		{
				wall=current_block.getWest();					
		}
		
		
		if((wall==null||(wall!=null&&wall.passage_Wall()))&&current_block!=next_block)
		{			
			if(type==Actiontype.MOVE)
			{
				if(!next_block.isFire())
					return true;
			}
			else if(type==Actiontype.MOVE_TO_FIRE)
			{
				if(next_block.isFire())
					return true;
			}		
			else if(type==Actiontype.MOVE_CARRY_PERSON)
			{
				if(!next_block.isFire()&& current_block.getPeople()>0)
					return true;
			}
			else if(type==Actiontype.MOVE_WITH_HEALED_PERSON)
			{
				if(!next_block.isFire()&& current_block.getHealed_people()>0)
					return true;
			}
			else if(type==Actiontype.MOVE_CARRY_AND_HEALED)
			{
				if(!next_block.isFire()&& current_block.getHealed_people()>0 && current_block.getPeople()>0)
					return true;
			}
			else if(type==Actiontype.TRANSPORT_DANGER)
			{
				if(!next_block.isFire()&& current_block.getDanger()>0)
					return true;
			}	
			else if(type==Actiontype.TRANSPORT_DANGER_AND_HEALED)
			{
				if(!next_block.isFire()&& current_block.getDanger()>0&&current_block.getHealed_people()>0)
					return true;
			}
			else if(type==Actiontype.EXTINQUISH_FIRE||type==Actiontype.EXTINQUISH_STEP)
			{
				if(current_block==next_block)
				{
					if(current_block.isFire())
						return true;
				}			
				if(next_block.isFire())
					return true;
			}			
			else if(type==Actiontype.EXTINQUISH_SMOKE)
			{
				if(current_block==next_block)
				{
					if(current_block.isSmoke())
						return true;
				}			
				if(next_block.isSmoke())
					return true;
			}			
		}
		
		
		if(current_block==next_block)
		{
			//nur auf aktuelles Feld ausfuehren
			if(type==Actiontype.IDENTIFY)		//global
			{
				if(current_block.isInterest())
					return true;
			}
			else if(type==Actiontype.MOVE_AMBULANCE)   //global
			{
				if(current_block.isAmbulance())
					return true;
			}
			else if(type==Actiontype.EXTINQUISH_FIRE||type==Actiontype.EXTINQUISH_STEP)
			{
				if(current_block.isFire())
					return true;

			}
			else if(type==Actiontype.EXTINQUISH_SMOKE)
			{
				if(current_block.isSmoke())
					return true;

			}
			
			//control_firefighter
			else if(type==Actiontype.HEAL_PERSON)
			{
				if(current_block.getPeople()>0)
					return true;
			}	
			else if(type==Actiontype.REMOVE_DANGER)
			{
				if(current_block.getDanger()>0)
					return true;
			}	
			else if(type==Actiontype.MOVE_FIRETRUCK)
			{
				if(current_block.isFiretruck())
					return true;
			}			
			else if(type==Actiontype.USE_FIRETRUCK)
			{
				if(current_block.isFiretruck())	//weitere Kriterien nötig
					return true;
			}					
			

		
		}
		
		if(type==Actiontype.CANCEL)
		{
				return true;
		}			
		
		return false;
	}
	public boolean wallaction_possible(Wallblock block)
	{
		if(type==Actiontype.OPEN_DOOR)
		{
			if(block!=null&&block.getWall()==Walltype.DOORCLOSED)
				return true;
		}
		else if(type==Actiontype.CLOSE_DOOR)
		{
			if(block!=null&&block.getWall()==Walltype.DOOROPEN)
				return true;
		}		
		else if(type==Actiontype.DAMAGE_WALL)
		{
			if(block!=null&&(block.getWall()==Walltype.WALL||block.getWall()==Walltype.WALLDAMAGED))
				return true;
		}
		else if(type==Actiontype.CANCEL)
		{
			return true;
		}
		
		return false;
	}
	

	public Integer getApcost() {
		return apcost;
	}

	public Actiontype getType() {
		return type;
	}

	
}
