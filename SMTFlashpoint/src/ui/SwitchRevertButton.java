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
public class SwitchRevertButton extends ButtonZone{ 
	private int x,y,width,height,playerNumber;
	private int buttonColor;
	private static final String CMD_PLAYER_REVERT_SWITCH_0="player0_revertswitch";
	private static final String CMD_PLAYER_REVERT_SWITCH_1="player1_revertswitch";
	private static final String CMD_PLAYER_REVERT_SWITCH_2="player2_revertswitch";
	private static final String CMD_PLAYER_REVERT_SWITCH_3="player3_revertswitch";
	private static final String CMD_PLAYER_REVERT_SWITCH_4="player4_revertswitch";
	private static final String CMD_PLAYER_REVERT_SWITCH_5="player5_revertswitch";
	
	GameEngine g;
	

	public SwitchRevertButton(int x, int y, int width, int height,int playerNumber, PlayerColor buttonColor,GameEngine g)
	{
		super(x,y,width,height,"Spezialist\nwechseln");
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
		this.setFontSize((int)(g.getBlock_size()/90.0*22));
		
		if(playerNumber==0)
			this.setButtonCommand(CMD_PLAYER_REVERT_SWITCH_0);
		else if(playerNumber==1)
			this.setButtonCommand(CMD_PLAYER_REVERT_SWITCH_1);
		else if(playerNumber==2)
			this.setButtonCommand(CMD_PLAYER_REVERT_SWITCH_2);
		else if(playerNumber==3)
			this.setButtonCommand(CMD_PLAYER_REVERT_SWITCH_3);
		else if(playerNumber==4)
			this.setButtonCommand(CMD_PLAYER_REVERT_SWITCH_4);
		else if(playerNumber==5)
			this.setButtonCommand(CMD_PLAYER_REVERT_SWITCH_5);
		
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
