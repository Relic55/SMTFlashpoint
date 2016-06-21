/**
 * 
 */
package model;

/**
 * @author Relic
 * CONTROL_FIREFIGHTER fehlt
 * Zeile 108 USE_FIRETRUCK weitere Kriterien notwendig
 */
public class Action {
	Integer apcost;
	Actiontype type;
	public Action()
	{
		this(Actiontype.CANCEL);
	}
	
	public Action(Actiontype type)
	{
		this.type=type;
		if(type==Actiontype.MOVE||type==Actiontype.OPEN_DOOR||type==Actiontype.CLOSE_DOOR||type==Actiontype.IDENTIFY||type==Actiontype.HEAL_PERSON||type==Actiontype.MOVE_WITH_HEALED_PERSON||type==Actiontype.EXTINQUISH_FIRE||type==Actiontype.EXTINQUISH_SMOKE)
		{
			apcost=1;
		}
		if(type==Actiontype.CONTROL_FIREFIGHTER||type==Actiontype.CANCEL)
		{
			apcost=0;
		}
		if(type==Actiontype.MOVE_TO_FIRE||type==Actiontype.DAMAGE_WALL||type==Actiontype.MOVE_AMBULANCE||type==Actiontype.MOVE_FIRETRUCK||type==Actiontype.REMOVE_DANGER||type==Actiontype.TRANSPORT_DANGER||type==Actiontype.MOVE_CARRY_PERSON||type==Actiontype.MOVE_CARRY_AND_HEALED)
		{
			apcost=2;
		}
		if(type==Actiontype.USE_FIRETRUCK)
		{
			apcost=4;
		}
	}
	
	
	public boolean action_possible(Block current_block,Block next_block)
	{
		if(type==Actiontype.MOVE)
		{
			if(!next_block.isFire())
				return true;
		}
		if(type==Actiontype.MOVE_TO_FIRE)
		{
			if(next_block.isFire())
				return true;
		}		
		if(type==Actiontype.MOVE_CARRY_PERSON)
		{
			if(!next_block.isFire()&& current_block.getPeople()>0)
				return true;
		}
		if(type==Actiontype.MOVE_WITH_HEALED_PERSON)
		{
			if(!next_block.isFire()&& current_block.getHealed_people()>0)
				return true;
		}
		if(type==Actiontype.MOVE_CARRY_AND_HEALED)
		{
			if(!next_block.isFire()&& current_block.getHealed_people()>0 && current_block.getPeople()>0)
				return true;
		}
		if(type==Actiontype.TRANSPORT_DANGER)
		{
			if(!next_block.isFire()&& current_block.getDanger()>0)
				return true;
		}	
		if(type==Actiontype.EXTINQUISH_FIRE)
		{
			if(current_block==next_block)
			{
				if(current_block.isFire())
					return true;
			}			
			if(next_block.isFire())
				return true;
		}			
		if(type==Actiontype.EXTINQUISH_SMOKE)
		{
			if(current_block==next_block)
			{
				if(current_block.isSmoke())
					return true;
			}			
			if(next_block.isSmoke())
				return true;
		}			
		if(type==Actiontype.HEAL_PERSON)
		{
			if(current_block.getPeople()>0)
				return true;
		}	
		if(type==Actiontype.REMOVE_DANGER)
		{
			if(current_block.getDanger()>0)
				return true;
		}	
		if(type==Actiontype.MOVE_FIRETRUCK)
		{
			if(current_block.isFiretruck())
				return true;
		}			
		if(type==Actiontype.USE_FIRETRUCK)
		{
			if(current_block.isFiretruck())	//weitere Kriterien nötig
				return true;
		}			
		
		
		//global
		if(type==Actiontype.MOVE_AMBULANCE)
		{
			if(current_block.isAmbulance())
				return true;
		}
		if(type==Actiontype.IDENTIFY)
		{
			if(current_block.isInterest())
				return true;
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
			if(block.getWall()==Walltype.DOORCLOSED)
				return true;
		}
		if(type==Actiontype.CLOSE_DOOR)
		{
			if(block.getWall()==Walltype.DOOROPEN)
				return true;
		}		
		if(type==Actiontype.DAMAGE_WALL)
		{
			if(block.getWall()==Walltype.WALL||block.getWall()==Walltype.WALLDAMAGED)
				return true;
		}
		if(type==Actiontype.CANCEL)
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
