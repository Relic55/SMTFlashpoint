/**
 * 
 */
package ui;

import controller.GameEngine;
import model.PlayerColor;
import vialab.SMT.ButtonZone;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class EndTurnButton  extends ButtonZone{ 
	private int x,y,width,height,playerNumber;
	private int buttonColor;
	private static final String CMD_PLAYER_TURNEND_0="player0_turnend";
	private static final String CMD_PLAYER_TURNEND_1="player1_turnend";
	private static final String CMD_PLAYER_TURNEND_2="player2_turnend";
	private static final String CMD_PLAYER_TURNEND_3="player3_turnend";
	private static final String CMD_PLAYER_TURNEND_4="player4_turnend";
	private static final String CMD_PLAYER_TURNEND_5="player5_turnend";
	
	GameEngine g;
	
	public EndTurnButton(int x, int y, int width, int height,int playerNumber, PlayerColor buttonColor,GameEngine g)
	{
		super(x,y,width,height,"Zug beenden");
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
		this.setFontSize((int)(g.getBlock_size()/90.0*27));
		
		if(playerNumber==0)
			this.setButtonCommand(CMD_PLAYER_TURNEND_0);
		else if(playerNumber==1)
			this.setButtonCommand(CMD_PLAYER_TURNEND_1);
		else if(playerNumber==2)
			this.setButtonCommand(CMD_PLAYER_TURNEND_2);
		else if(playerNumber==3)
			this.setButtonCommand(CMD_PLAYER_TURNEND_3);
		else if(playerNumber==4)
			this.setButtonCommand(CMD_PLAYER_TURNEND_4);
		else if(playerNumber==5)
			this.setButtonCommand(CMD_PLAYER_TURNEND_5);
		
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
		this.setVisible(false);
			
	}
	
	
	
}

