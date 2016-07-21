/**
 * 
 */
package ui;

import controller.GameEngine;
import model.PlayerColor;
import vialab.SMT.ButtonZone;
import vialab.SMT.ButtonZone.Form;

/**
 * @author Relic
 *
 */
public class DriveButton extends ButtonZone{
	private int x,y,width,height,playerNumber;
	private int buttonColor;
	private static final String CMD_PLAYER_RIDE_0="player0_ride";
	private static final String CMD_PLAYER_NORIDE_0="player0_noride";
	private static final String CMD_PLAYER_RIDE_1="player1_ride";
	private static final String CMD_PLAYER_NORIDE_1="player1_noride";
	private static final String CMD_PLAYER_RIDE_2="player2_ride";
	private static final String CMD_PLAYER_NORIDE_2="player2_noride";
	private static final String CMD_PLAYER_RIDE_3="player3_ride";
	private static final String CMD_PLAYER_NORIDE_3="player3_noride";
	private static final String CMD_PLAYER_RIDE_4="player4_ride";
	private static final String CMD_PLAYER_NORIDE_4="player4_noride";
	private static final String CMD_PLAYER_RIDE_5="player5_ride";
	private static final String CMD_PLAYER_NORIDE_5="player5_noride";
	GameEngine g;
	String ride="mitfahren";
	String noride=" nicht\nmitfahren";
	
	public DriveButton(int x, int y, int width, int height,String text, int playerNumber, PlayerColor buttonColor,GameEngine g)
	{
		super(x,y,width,height,text);
		if(playerNumber==1)
		{
			rotateAbout(PI/2, 0,0);
		}
		else if(playerNumber>1&&playerNumber<4)
		{
			rotateAbout(PI, 0,0);
		}
		else if(playerNumber==4)
		{
			rotateAbout((float)(PI*1.5), 0,0);
		}
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.playerNumber=playerNumber;
		this.setForm(Form.RECTANGLE);
		this.setFontSize((int)(g.getBlock_size()/90.0*15));
		
		if(playerNumber==0)
		{
			if(text==ride)
				this.setButtonCommand(CMD_PLAYER_RIDE_0);
			else
				this.setButtonCommand(CMD_PLAYER_NORIDE_0);
		}
			
		else if(playerNumber==1)
		{
			if(text==ride)
				this.setButtonCommand(CMD_PLAYER_RIDE_1);
			else
				this.setButtonCommand(CMD_PLAYER_NORIDE_1);
		}
		else if(playerNumber==2)
		{
			if(text==ride)
				this.setButtonCommand(CMD_PLAYER_RIDE_2);
			else
				this.setButtonCommand(CMD_PLAYER_NORIDE_2);
		}
		else if(playerNumber==3)
		{
			if(text==ride)
				this.setButtonCommand(CMD_PLAYER_RIDE_3);
			else
				this.setButtonCommand(CMD_PLAYER_NORIDE_3);
		}
		else if(playerNumber==4)
		{
			if(text==ride)
				this.setButtonCommand(CMD_PLAYER_RIDE_4);
			else
				this.setButtonCommand(CMD_PLAYER_NORIDE_4);
		}
		else if(playerNumber==5)
		{
			if(text==ride)
				this.setButtonCommand(CMD_PLAYER_RIDE_5);
			else
				this.setButtonCommand(CMD_PLAYER_NORIDE_5);
		}
		
		if(buttonColor==PlayerColor.GREEN)
			this.color=0xFF1c5b2e;
		else if(buttonColor==PlayerColor.ORANGE)
			this.color=0xFFbd7212;
		else if(buttonColor==PlayerColor.BLUE)
			this.color=0xFF1a4a6e;
		else if(buttonColor==PlayerColor.RED)
			this.color=0xFF961e1f;
		else if(buttonColor==PlayerColor.YELLOW)
			this.color=0xFFeadb1c;
		else if(buttonColor==PlayerColor.WHITE)
			this.color=0xFFdadada;

		this.setMoveable(false);
	}
	
	
	

}
