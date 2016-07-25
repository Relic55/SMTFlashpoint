/**
 * 
 */
package ui;

import java.util.ArrayList;

import controller.listener.IActionListener;
import controller.listener.ISwitchListener;
import processing.core.PImage;
import util.io.Utility;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class SwitchButton extends Zone {
	
	private ArrayList<ISwitchListener> listeners=new ArrayList<ISwitchListener>();
	private int x,y,width,height,playerNumber,what,direction;
	private PImage arrowimage;
	private String command;
	private int timer=0;
	
	
	//Button für Schwierigkeitsgrad ändern
	private static final String CMD_NEXT_DIFFICULTY="nextdifficulty";
	private static final String CMD_PREVIOUS_DIFFICULTY="prevdifficulty";
	
	//Button für Spielerauswahl
	private static final String CMD_PLAYER0_NEXT_SPECIALIST="player0_nextspecialist";
	private static final String CMD_PLAYER0_PREVIOUS_SPECIALIST="player0_prevspecialist";
	private static final String CMD_PLAYER0_NEXT_COLOR="player0_nextcolor";
	private static final String CMD_PLAYER0_PREVIOUS_COLOR="player0_prevcolor";
	
	private static final String CMD_PLAYER1_NEXT_SPECIALIST="player1_nextspecialist";
	private static final String CMD_PLAYER1_PREVIOUS_SPECIALIST="player1_prevspecialist";
	private static final String CMD_PLAYER1_NEXT_COLOR="player1_nextcolor";
	private static final String CMD_PLAYER1_PREVIOUS_COLOR="player1_prevcolor";
	
	private static final String CMD_PLAYER2_NEXT_SPECIALIST="player2_nextspecialist";
	private static final String CMD_PLAYER2_PREVIOUS_SPECIALIST="player2_prevspecialist";
	private static final String CMD_PLAYER2_NEXT_COLOR="player2_nextcolor";
	private static final String CMD_PLAYER2_PREVIOUS_COLOR="player2_prevcolor";
	
	private static final String CMD_PLAYER3_NEXT_SPECIALIST="player3_nextspecialist";
	private static final String CMD_PLAYER3_PREVIOUS_SPECIALIST="player3_prevspecialist";
	private static final String CMD_PLAYER3_NEXT_COLOR="player3_nextcolor";
	private static final String CMD_PLAYER3_PREVIOUS_COLOR="player3_prevcolor";
	
	private static final String CMD_PLAYER4_NEXT_SPECIALIST="player4_nextspecialist";
	private static final String CMD_PLAYER4_PREVIOUS_SPECIALIST="player4_prevspecialist";
	private static final String CMD_PLAYER4_NEXT_COLOR="player4_nextcolor";
	private static final String CMD_PLAYER4_PREVIOUS_COLOR="player4_prevcolor";
	
	private static final String CMD_PLAYER5_NEXT_SPECIALIST="player5_nextspecialist";
	private static final String CMD_PLAYER5_PREVIOUS_SPECIALIST="player5_prevspecialist";
	private static final String CMD_PLAYER5_NEXT_COLOR="player5_nextcolor";
	private static final String CMD_PLAYER5_PREVIOUS_COLOR="player5_prevcolor";
	
	
	public SwitchButton(int x, int y, int width, int height, String pic_path, int playerNumber, int what, int direction)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.playerNumber=playerNumber;
		this.direction=direction;   //1= weiter,   2= vorher
		this.what=what;
		
		arrowimage=Utility.getImage(pic_path+"/Pfeil_rechts.png");
		
		 //direction: 1= weiter,   2= vorher
		if(direction==1)
		{
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
		}
		else //Pfeil muss in die andere Richtung zeigen
		{
			if(playerNumber==1)
			{
				rotateAbout((float)(PI*1.5), 0,0);
			}
			else if(playerNumber==0||playerNumber==5)
			{
				rotateAbout(PI, 0,0);
			}
			else if(playerNumber==4)
			{
				rotateAbout((float)(PI/2), 0,0);
			}
		}
		
		//command bestimmen
		//what: 1= Spezialist 2= Farbe 3= Schwierigkeitsgrad
		  
		if(playerNumber==0)
		{
			if(what==1)
			{
				if(direction==1)
					command=CMD_PLAYER0_NEXT_SPECIALIST;
				else
					command=CMD_PLAYER0_PREVIOUS_SPECIALIST;			
			}
			else if(what==2)
			{
				if(direction==1)
					command=CMD_PLAYER0_NEXT_COLOR;
				else
					command=CMD_PLAYER0_PREVIOUS_COLOR;
			}		
		}
		else if(playerNumber==1)
		{
			if(what==1)
			{
				if(direction==1)
					command=CMD_PLAYER1_NEXT_SPECIALIST;
				else
					command=CMD_PLAYER1_PREVIOUS_SPECIALIST;			
			}
			else if(what==2)
			{
				if(direction==1)
					command=CMD_PLAYER1_NEXT_COLOR;
				else
					command=CMD_PLAYER1_PREVIOUS_COLOR;
			}		
		}
		else if(playerNumber==2)
		{
			if(what==1)
			{
				if(direction==1)
					command=CMD_PLAYER2_NEXT_SPECIALIST;
				else
					command=CMD_PLAYER2_PREVIOUS_SPECIALIST;			
			}
			else if(what==2)
			{
				if(direction==1)
					command=CMD_PLAYER2_NEXT_COLOR;
				else
					command=CMD_PLAYER2_PREVIOUS_COLOR;
			}		
		}
		else if(playerNumber==3)
		{
			if(what==1)
			{
				if(direction==1)
					command=CMD_PLAYER3_NEXT_SPECIALIST;
				else
					command=CMD_PLAYER3_PREVIOUS_SPECIALIST;			
			}
			else if(what==2)
			{
				if(direction==1)
					command=CMD_PLAYER3_NEXT_COLOR;
				else
					command=CMD_PLAYER3_PREVIOUS_COLOR;
			}		
		}
		else if(playerNumber==4)
		{
			if(what==1)
			{
				if(direction==1)
					command=CMD_PLAYER4_NEXT_SPECIALIST;
				else
					command=CMD_PLAYER4_PREVIOUS_SPECIALIST;			
			}
			else if(what==2)
			{
				if(direction==1)
					command=CMD_PLAYER4_NEXT_COLOR;
				else
					command=CMD_PLAYER4_PREVIOUS_COLOR;
			}		
		}
		else if(playerNumber==5)
		{
			if(what==1)
			{
				if(direction==1)
					command=CMD_PLAYER5_NEXT_SPECIALIST;
				else
					command=CMD_PLAYER5_PREVIOUS_SPECIALIST;			
			}
			else if(what==2)
			{
				if(direction==1)
					command=CMD_PLAYER5_NEXT_COLOR;
				else
					command=CMD_PLAYER5_PREVIOUS_COLOR;
			}		
		}
		
	}
	
	
	
	public void addListener(ISwitchListener listener)
	{
			listeners.add(listener);
	}
	
	@Override
	public void draw()
	{
		image(arrowimage,0,0,width,height);
		if(timer>0)
			timer--;

		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
		if(timer<1) //verzögerung einbauen bis zum erneuten Drücken
		{
			timer=30;
			for(ISwitchListener al:listeners){
				al.switchButtonPressed(playerNumber, what, direction );
			}
		}
	}
}
