/**
 * This file is part of the BMBF funded project <B>Nebeneinander wird Miteinander</B>
 * at Technische UniversitÃ¤t Chemnitz.
 *
 * All Copyrights reserved @year.
 */
package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import vialab.SMT.*;
import vialab.SMT.event.ButtonEvent;
import vialab.SMT.event.ButtonZoneListener;

import java.util.Random; //Zufallszahlen

import controller.GameEngine.PhaseStates;
import controller.listener.IActionListener;
import controller.listener.ILevelListener;
import model.Actiontype;
import model.Action;
import model.Block;
import model.GameDifficulty;
import model.PlayerColor;
import model.Level;
import model.Player;
import model.SpecialistType;
import model.Wallblock;
import model.Walltype;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import ui.ActionButton;
import ui.ActiveMarker;
import ui.Background;
import ui.BlockVisual;
import ui.DriveButton;
import ui.EndTurnButton;
import ui.EndingScreen;
import ui.PlayerVisual;
import ui.Playerzone;
import ui.Statusoverview;
import ui.VisorBlock;
import util.Constants;
import util.io.Utility;
import util.AppInjector;

/**
 * This is the GameEngine. The GameEngine is responsible for the "flow" of a game. It manages
 * objects which are currently active in the application and also directs the flow of events,
 * such as reacting to the completion of a level.
 * 
 * @author <a href="mailto:storz@informatik.tu-chemnitz.de">Michael Storz</a>
 * @version 0.1 <BR>
 * <BR>
 *          History:<BR>
 *          <LI>[storz][28.07.2015] Created</LI>
 */
//public class GameEngine implements IStencilListener, ILevelListener {
public class GameEngine implements IActionListener, ButtonZoneListener {
	
	//Beginn Flashpoint Umsetzung
	
	
	private int vertical_blocks = 8;
	private int block_size;
	private int horizontal_blocks = 10;
	private int x_offset;			//Verschiebung des Spielbretts in X Richtung
	private int y_offset;			//Verschiebung des Spielbretts in X Richtung
	
	public String pic_path="./pictures";
	public String json_path="./jsonfiles";
	private Integer mapnumber;				//falls weitere Maps eingebunden werden
	public Block[][] board =new Block[vertical_blocks][horizontal_blocks];
	public BlockVisual[][] boardvisual =new BlockVisual[vertical_blocks][horizontal_blocks];

	
	private Statusoverview stat;
	
	PImage backgroundpic= Utility.getImage(pic_path+ "/Spielplan_groß.gif");
	
	private Background background;
	//background.draw();
	
	
	//TODO: ursprüngliche Panelgrößen entfernen
	private int panel_width;
	private int panel_height;
	private int board_width;
	private int board_height;
	
	private int activePlayer;

	
	public Player[] playerbase=new Player[6];								//Feld der Spieler
	public PlayerVisual[] playervisualbase=new PlayerVisual[6];
	private Playerzone[] playerzonebase=new Playerzone[6];					//Feld der Spielerzonen
	private EndTurnButton[] etbbase=new EndTurnButton[6];					//Feld für die jeweiligen Runde Beenden Buttons
	private ActiveMarker[] activemarkerfield=new ActiveMarker[6];			//Feld für die Anzeige, wer grade aktiv ist, gleichzeitig Schalter für Visor
	private int playercount;			//Anzahl aktiver Spieler
	private int saved_person=0;			//Anzahl geretteter Personen
	private int dead_person=0;			//Anzahl verstorbener Personen
	private int saved_person_to_win=7;
	private int dead_person_to_lose=4;
	private int buildingdamage=0;		
	private int maxbuildingdamage=24;
	private int person_marker=10;			//Anzahl Personen im Spiel insgesamt
	private int false_alarm_marker=5;		//Anzahl falscher Alarme insgesamt
	private int saved_danger=0;
	private int interestleft;
	private int active_seats;			//aktive Brandherde
	private int inactive_seats;			//weitere Brandherde
	private int interest_onboard=0;		//Anzahl Einsatzmarker auf dem Spielfeld
	private boolean hittedseat=false; 	//Brandherd getroffen bei true, neuer Brandherd muss gesetzt werden
	private Random rand;				//Objekt fuer Zufallszahlen
	
	private GameDifficulty difficulty;
	public boolean GameActive=true;
	
	
	private VisorBlock[] visorfield=new VisorBlock[32];		//Bei Positionswahl müssen bis zu 32 Visor angezeigt werden
	private int visorFill=0;
	//0=N, 1=O, 2=S, 3=W, 4=eigenes Feld
	
	EndingScreen ending=null;
	private Player active_firefighter;
	
	private int actionnumber=21;
	private Action[] Actionfield=new Action[actionnumber];
	private boolean[] possibleactions=new boolean[actionnumber];
	//private Action[] possibleactions=new Action[actionnumber];
	
	ArrayList<ActionButton> actionButtons = new ArrayList<ActionButton>();

	private boolean visorTouched=false;
	private boolean visorshown=true;
	private boolean actionTouched=false;
	private boolean waiting=false;
	private int buttoncount=0;
	private Block cargoal=null;
	private int carride=4;
	private DriveButton[] driveButtonField=new DriveButton[2];
	private boolean canYouDriveWithMe[]=new boolean[6];
	private boolean wannaDriveWithMe[]=new boolean[6];
	
	private GameStates currentGameState;
	private PhaseStates currentPhaseState;
	
	private ArrayList<Object> resources;
	
	// Verwaltet alle aktiven Objekte des Spiels.
	// Als aktive Objekte gelten (derzeit) das Level und alle im Level vorkommenden Objekte
	
	private Map<Integer, Level> levelMap;
	
	private int currentLevel = 0;
	
	/**
	 * A Game may use different states to interpret different events, 
	 * e.g. user I/O during STATE_PAUSE may be limited and during STATE_INGAME enabled
	 * 
	 * @author soeren
	 *
	 */
	public static enum GameStates {
		STATE_START,
		STATE_STARTBOARD,
		STATE_CHOOSEMENU,
		STATE_POSITION,
		STATE_MENU,
		STATE_PAUSE,
		STATE_INGAME,
		STATE_FINISHED;
	
		
		private static final int size = GameStates.values().length;
	};
	public static enum PhaseStates {
		STATE_BEFORE,
		STATE_PLAYER,
		STATE_FIRE,
		STATE_END;		
		private static final int size = PhaseStates.values().length;
	};
	
	
	public GameEngine() {
		levelMap = new HashMap<Integer, Level>();
		resources = loadResources();
		currentLevel = 1;

		currentGameState=GameStates.STATE_START;
		currentPhaseState=PhaseStates.STATE_PLAYER;
		for(int i=0;i<6;i++)
		{		
			playerbase[i]=null;
			playerzonebase[i]=null;
			etbbase[i]=null;
			playervisualbase[i]=null;
			activemarkerfield[i]=null;
			canYouDriveWithMe[i]=false;
			wannaDriveWithMe[i]=false;
		}
		

		playercount=0;			//Anzahl aktiver Spieler
		buildingdamage=0;		
		person_marker=10;			//Anzahl Personen im Spiel insgesamt
		false_alarm_marker=5;		//Anzahl falscher Alarme insgesamt
		saved_danger=0;
		interest_onboard=0;		
		saved_person=0;
		dead_person=0;
		buildingdamage=0;	
		interestleft=person_marker+false_alarm_marker;
		ending=null;
		visorTouched=false;
		actionTouched=false;
		waiting=false;
		buttoncount=0;
		cargoal=null;
		carride=4;
		driveButtonField[0]=null;
		driveButtonField[1]=null;
		//initActions();
		init_measures();
		init_board();
		init_actionfield();
		init_blocks(json_path+"/block_start_json.json");
		//init_choosing mit menu und auswahl versehen

		
		init_statusoverview();
		
		init_choosing();
		

		rand = new Random();
		init_beginningfire();
		
		currentGameState=GameStates.STATE_POSITION;
		//init_Player so abaendern, dass die Startpositionen gewaehlt werden
		init_player();
		currentGameState=GameStates.STATE_INGAME;
		
		fillVisorfield();
		
		
		/*
		while(currentGameState!=GameStates.STATE_FINISHED)
		{
			//eigentlicher Spielablauf
		
			if(this.saved_person>6)
			{
				//
				currentGameState=GameStates.STATE_FINISHED;
				didwewin=true;
			}
			else if(this.dead_person>3||this.buildingdamage>this.maxbuildingdamage)
				currentGameState=GameStates.STATE_FINISHED;
		}
	
		
		if(didwewin)
		{
			//Textausgabe gewonnen --> Zone erstellen
		}
		else
		{
			//Textausgabe verloren --> Zone
		}
		*/
	
		
		
	}
	
	/**
	 * 
	 */
	private void init_actionfield() {
		// TODO Auto-generated method stub
		// Actiontype.values()
		
		Actionfield[0]= new Action(Actiontype.MOVE);
		Actionfield[1]= new Action(Actiontype.MOVE_TO_FIRE);
		Actionfield[2]= new Action(Actiontype.MOVE_CARRY_PERSON);
		Actionfield[3]= new Action(Actiontype.MOVE_WITH_HEALED_PERSON);	
		Actionfield[4]= new Action(Actiontype.MOVE_CARRY_AND_HEALED);
		Actionfield[5]= new Action(Actiontype.TRANSPORT_DANGER);
		Actionfield[6]= new Action(Actiontype.TRANSPORT_DANGER_AND_HEALED);
		Actionfield[7]= new Action(Actiontype.EXTINQUISH_FIRE);
		Actionfield[8]= new Action(Actiontype.EXTINQUISH_SMOKE);
		Actionfield[9]= new Action(Actiontype.EXTINQUISH_STEP);
		Actionfield[10]= new Action(Actiontype.MOVE_AMBULANCE);
		Actionfield[11]= new Action(Actiontype.MOVE_FIRETRUCK);
		Actionfield[12]= new Action(Actiontype.USE_FIRETRUCK);
		
		Actionfield[13]= new Action(Actiontype.HEAL_PERSON);
		Actionfield[14]= new Action(Actiontype.REMOVE_DANGER);
		Actionfield[15]= new Action(Actiontype.IDENTIFY);
		Actionfield[16]= new Action(Actiontype.CONTROL_FIREFIGHTER);	

		Actionfield[17]= new Action(Actiontype.OPEN_DOOR);	
		Actionfield[18]= new Action(Actiontype.CLOSE_DOOR);
		Actionfield[19]= new Action(Actiontype.DAMAGE_WALL);
		Actionfield[20]= new Action(Actiontype.CANCEL);
		
	}

	public void showPossibleActions(int type, Block start, Block ziel, Wallblock wall1)
	{
		boolean possible;
		int count=0;
		for(int i=0;i<actionnumber;i++) 
		{
			if(i>=0&&i<13)		//Blockaktionen von jedem Firefighter
			{
				if(type==1)
				{
				
					possible=Actionfield[i].action_possible(start, ziel);
					if(possible)
						possibleactions[i]=playerbase[this.activePlayer].enoughPoints(Actionfield[i].getType());
				}

				
			}
			
			else if(i>=17&&i<20)  //Wallaktionen
			{
				if(type==2||type==3)
				{
					possible=Actionfield[i].wallaction_possible(wall1);
					if(possible)
						possibleactions[i]=playerbase[this.activePlayer].enoughPoints(Actionfield[i].getType());
				}

			}
			else if(i==13)		//Heilen darf nur der Rettungssanitäter
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.RETTUNGSSANITAETER)
				{
					possible=Actionfield[i].action_possible(start, ziel);
					if(possible)
						possibleactions[i]=playerbase[this.activePlayer].enoughPoints(Actionfield[i].getType());
				}
				
			}
			else if(i==14)		//Gefahr entfernen darf nur der Gefahrstoffspezialist
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.GEFAHRSTOFFSPEZIALIST)
				{
					possible=Actionfield[i].action_possible(start, ziel);
					if(possible)
						possibleactions[i]=playerbase[this.activePlayer].enoughPoints(Actionfield[i].getType());
				}
				
			}
			else if(i==15)		//Identifizieren darf nur der Spezialist mit Wärmekamera
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
				{
					possible=Actionfield[i].action_possible(start, ziel);
					if(possible)
						possibleactions[i]=playerbase[this.activePlayer].enoughPoints(Actionfield[i].getType());
				}
				
			}
			else if(i==16)		//Kommandieren darf nur der Einsatzleiter
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.EINSATZLEITER)
				{
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);
				}
				
			}
			else //Abbruch Aktion
			{
				possibleactions[i]=true;
				/*
				if(type==1)
				{
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);
				}
				else 
				{
					possibleactions[i]=Actionfield[i].wallaction_possible(wall1);
				}
				*/
			}

			if(possibleactions[i])
				count++;
		}

		
	 

	double winkel=360/count*(Math.PI/180);
	int actionsize;
	if(count <5)
		actionsize=(int)(block_size*0.9);
	else if(count<9)
		actionsize=(int)(block_size*0.8);
	else
		actionsize=(int)(block_size*0.5);
	
	int factor=0;
	float midpointx=	(float)(x_offset+ziel.getYb()*this.block_size+0.5*block_size);
	float midpointy=	(float)(y_offset+ziel.getXb()*this.block_size+0.5*block_size);
	int xposition,yposition;
	for (int i=0;i<possibleactions.length;i++) {
		if (possibleactions[i]) {
			xposition=(int)((midpointx+block_size*Math.cos(winkel*factor-90*(Math.PI/180)))-0.5*block_size);
			yposition=(int)((midpointy+block_size*Math.sin(winkel*factor-90*(Math.PI/180)))-0.5*block_size);
			ActionButton ab;
			if(i>=7&&i<=9&&(playerbase[activePlayer].getSpecialist()==SpecialistType.RETTUNGSSANITAETER||playerbase[activePlayer].getSpecialist()==SpecialistType.RETTUNGSSPEZIALIST))
				ab= new ActionButton(xposition,yposition,actionsize,actionsize,Actionfield[i].getApcost()*2, Actionfield[i],start,ziel);
			else if(i==19&&(playerbase[activePlayer].getSpecialist()==SpecialistType.RETTUNGSSPEZIALIST))
				ab= new ActionButton(xposition,yposition,actionsize,actionsize,1, Actionfield[i],start,ziel);
			else if(i==12&&(playerbase[activePlayer].getSpecialist()==SpecialistType.FAHRZEUGMASCHINIST))
				ab= new ActionButton(xposition,yposition,actionsize,actionsize,2, Actionfield[i],start,ziel);		
			else
				ab= new ActionButton(xposition,yposition,actionsize,actionsize,Actionfield[i].getApcost(), Actionfield[i],start,ziel);
			factor++;
			
			actionButtons.add(ab);
			ab.addListener(this);
			AppInjector.zoneManager().add(ab);
		}
	}
		
		
	}
	
	public void removePossibleActions()
	{
		while(actionButtons.size()>0) 
		{
				ActionButton ab= actionButtons.remove(0);
				AppInjector.zoneManager().remove(ab);
			
		}
		for(int i=0; i<possibleactions.length;i++) 
		{
			possibleactions[i]=false;
		}
	}	
	
	
	
	@Override
	public void actionSelected(Action what,Block start, Block ziel) {
		if(playerbase[this.activePlayer].enoughPoints(what.getType()))
		{
			if(what.getType()==Actiontype.MOVE)
			{
				System.out.println("Move: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				if(ziel.isInterest())
					ziel.scanInterest();
				
				//ap kosten:
			}
			else if(what.getType()==Actiontype.MOVE_TO_FIRE)
			{
				System.out.println(" Firemove: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				
				//ap kosten: 
			}
			else if(what.getType()==Actiontype.MOVE_CARRY_PERSON)
			{
				System.out.println(" Carrymove: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				start.reducePeople();
				ziel.increasePeople();
				if(ziel.isInterest())
					ziel.scanInterest();
				//Test, ob Personen gerettet wurde
				if(!ziel.isInside_Block())
				{
					this.checkSavedPeople(ziel);
				}
				
			}
			else if(what.getType()==Actiontype.MOVE_WITH_HEALED_PERSON)
			{
				System.out.println(" Healmove: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				start.reduceHealedPeople();   
				ziel.increaseHealedPeople();
				if(ziel.isInterest())
					ziel.scanInterest();
				if(!ziel.isInside_Block())
				{
					this.checkSavedPeople(ziel);
				}
				 
			}	
			else if(what.getType()==Actiontype.MOVE_CARRY_AND_HEALED)
			{
				System.out.println(" CarryHealmove: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				start.reducePeople();
				ziel.increasePeople();
				start.reduceHealedPeople();   
				ziel.increaseHealedPeople();
				if(ziel.isInterest())
					ziel.scanInterest();
				if(!ziel.isInside_Block())
				{
					this.checkSavedPeople(ziel);
				}
			}
			else if(what.getType()==Actiontype.TRANSPORT_DANGER)
			{
				System.out.println(" Dangermove: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				start.reduceDanger();
				ziel.increaseDanger();
				if(ziel.isInterest())
					ziel.scanInterest();
				if(!ziel.isInside_Block())
				{
					this.checkSavedPeople(ziel);
				}
			}
			else if(what.getType()==Actiontype.TRANSPORT_DANGER_AND_HEALED)
			{
				System.out.println(" DangerHealmove: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				playerbase[this.activePlayer].setXb(ziel.getXb());
				playerbase[this.activePlayer].setYb(ziel.getYb());
				start.reduceDanger();
				ziel.increaseDanger();
				start.reduceHealedPeople();   
				ziel.increaseHealedPeople();
				if(ziel.isInterest())
					ziel.scanInterest();
				if(!ziel.isInside_Block())
				{
					this.checkSavedPeople(ziel);
				}
			}
			else if(what.getType()==Actiontype.EXTINQUISH_FIRE)
			{
				System.out.println(" ExtinquishFire: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				ziel.setFire(false);			
				//ap kosten: 
			}
			else if(what.getType()==Actiontype.EXTINQUISH_SMOKE)
			{
				System.out.println(" ExtinquishSmoke: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				ziel.setSmoke(false);
				//ap kosten: 
			}
			else if(what.getType()==Actiontype.EXTINQUISH_STEP)
			{
				System.out.println(" FireToSmoke: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				ziel.setFire(false);
				ziel.setSmoke(true);
				//ap kosten: 
			}
			//TODO: Fahrzeugaktionen
			else if(what.getType()==Actiontype.HEAL_PERSON)
			{
				System.out.println(" HealPerson: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				start.reducePeople();
				start.increaseHealedPeople();
				//ap kosten: 
			}
			else if(what.getType()==Actiontype.REMOVE_DANGER)
			{
				System.out.println(" RemoveDanger: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				start.reduceDanger();
				saved_danger++;
				//ap kosten: 
			}
			else if(what.getType()==Actiontype.IDENTIFY)
			{
				System.out.println(" Identify: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
				start.scanInterest();
				//ap kosten: 
			}
			//TODO: Einsatzleiter

			//TODO: Wagenaktionen, inkl checkSavedPeople(Block ziel) bei Ambulance Move
			//Wallaktionen
			else if(what.getType()==Actiontype.OPEN_DOOR||what.getType()==Actiontype.CLOSE_DOOR||what.getType()==Actiontype.DAMAGE_WALL)
			{
				Wallblock wall=null;
				if(start.getXb()<ziel.getXb())
				{
						wall=start.getSouth();					
				}
				else if(start.getXb()>ziel.getXb())
				{
						wall=start.getNorth();					
				}
				else if(start.getYb()<ziel.getYb())
				{
						wall=start.getEast();					
				}
				else if(start.getYb()>ziel.getYb())
				{
						wall=start.getWest();					
				}
				if(wall!=null)
				{
					if(what.getType()==Actiontype.OPEN_DOOR||what.getType()==Actiontype.CLOSE_DOOR)
					{
						wall.switch_Door();
						System.out.println(" DoorSwitch: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
					}
					else
					{
						this.buildingdamage+=wall.damage_Wall();
						System.out.println(" DamageWall: X:  "+ziel.getXb()+"  Y: "+ziel.getYb());
						
					}
				}
				
			}
			else if(what.getType()==Actiontype.CANCEL)
			{
	
				System.out.println("Abbruch");
			}
			playerbase[this.activePlayer].spendPoints(what.getType());
		}
		if(what.getType()!=Actiontype.MOVE_AMBULANCE&&what.getType()!=Actiontype.MOVE_FIRETRUCK)
		{
			this.visorTouched=false;
			this.actionTouched=false;
			removeVisorfield();
			fillVisorfield();
		}
		
		
		if(what.getType()==Actiontype.MOVE_AMBULANCE)
		{
			this.visorTouched=false;
			this.actionTouched=false;
			removeVisorfield();
			fillVisorfieldplace(what.getType(), start);
			//TODO: Abfrage, ob jmd mitfahren will
			
			System.out.println("Krankenwagen bewegen");
		}
		else if(what.getType()==Actiontype.MOVE_FIRETRUCK)
		{
			this.visorTouched=false;
			this.actionTouched=false;
			removeVisorfield();
			fillVisorfieldplace(what.getType(), start);
			//TODO: Abfrage, ob jmd mitfahren will
			System.out.println("Feuerwehrwagen bewegen");
		}
		
		
		
		
		
		
		

	}
	
	public void canYouRide(int car, Block start, Block ziel) //4= Ambulanz, 5=Feuerwehr
	{
		boolean forward=true;
		carride=car;
		int xb,yb;
		for(int i=0;i<6;i++)
		{
			//kann jemand mitfahren
			if(playerbase[i]!=null)
			{
				xb=playerbase[i].getXb();
				yb=playerbase[i].getYb();
				if(car==4&&board[xb][yb].isAmbulance())
				{
					canYouDriveWithMe[i]=true;	
					forward=false;  //kann nicht direkt weitergehen, erst muss abgefragt werden
				}
				if(car==5&&board[xb][yb].isFiretruck())
				{
					if(i!=activePlayer)//aktueller Spieler muss mitfahren
					{
						canYouDriveWithMe[i]=true;	
						forward=false;  //kann nicht direkt weitergehen, erst muss abgefragt werden
					}
					else
						wannaDriveWithMe[i]=true;
				}
			}
		}
		buttoncount=0;
		if(forward)
			placesomething(car, start, ziel);
		else
		{
			
			cargoal=ziel;
			waiting=true;
			int startindex=-1; //ersten Button merken
			//Anzahl der Abfragen in buttoncount festhalten
			for(int i=0;i<6;i++)
			{
				if(playerbase[i]!=null&&canYouDriveWithMe[i]==true)
				{
					buttoncount++;
					if(startindex==-1)
						startindex=i;
				}
			}
			nextRideButton(startindex);
		}		
		//canYouDriveWithMe[0]=true;		
//		wannaDriveWithMe[0]=true;		
	}

	
	public void nextRideButton(int i)
	{
		//2 neue Buttons anlegen zur Abfrage, ob der Spieler mitfahren will
		if(driveButtonField[0]!=null&&driveButtonField[1]!=null)
		{
		AppInjector.zoneManager().remove(driveButtonField[0]);
		AppInjector.zoneManager().remove(driveButtonField[1]);
		}
		String ride="mitfahren";
		String noride=" nicht\nmitfahren";
		if(i==0)
		{
		driveButtonField[0]=new DriveButton((int)(x_offset-2.5*this.block_size),(int)(y_offset+5.5*block_size),block_size, block_size/2,ride,i , playerbase[i].getPlayerColor(),this);
		driveButtonField[1]=new DriveButton((int)(x_offset-0.5*this.block_size),(int)(y_offset+5.5*block_size),block_size, block_size/2,noride,i , playerbase[i].getPlayerColor(),this);					
		AppInjector.zoneManager().add(driveButtonField[0]);
		driveButtonField[0].addButtonListener(this);
		AppInjector.zoneManager().add(driveButtonField[1]);
		driveButtonField[1].addButtonListener(this);
		}
		else if(i==1)
		{
		driveButtonField[0]=new DriveButton((int)(x_offset-0.5*block_size),(int)(y_offset+2.5*block_size),block_size, block_size/2,ride,i , playerbase[i].getPlayerColor(),this);
		driveButtonField[1]=new DriveButton((int)(x_offset-0.5*block_size),(int)(y_offset+4.5*block_size),block_size, block_size/2,noride,i , playerbase[i].getPlayerColor(),this);					
		AppInjector.zoneManager().add(driveButtonField[0]);
		driveButtonField[0].addButtonListener(this);
		AppInjector.zoneManager().add(driveButtonField[1]);
		driveButtonField[1].addButtonListener(this);
		}
		else if(i==2)
		{
		driveButtonField[0]=new DriveButton((int)(x_offset+0.5*this.block_size),(int)(y_offset+2.5*block_size),block_size, block_size/2,ride,i , playerbase[i].getPlayerColor(),this);
		driveButtonField[1]=new DriveButton((int)(x_offset-1.5*this.block_size),(int)(y_offset+2.5*block_size),block_size, block_size/2,noride,i , playerbase[i].getPlayerColor(),this);					
		AppInjector.zoneManager().add(driveButtonField[0]);
		driveButtonField[0].addButtonListener(this);
		AppInjector.zoneManager().add(driveButtonField[1]);
		driveButtonField[1].addButtonListener(this);
		}
		else if(i==3)
		{
		driveButtonField[0]=new DriveButton((int)(x_offset+12.5*this.block_size),(int)(y_offset+2.5*block_size),block_size, block_size/2,ride,i , playerbase[i].getPlayerColor(),this);
		driveButtonField[1]=new DriveButton((int)(x_offset+10.5*this.block_size),(int)(y_offset+2.5*block_size),block_size, block_size/2,noride,i , playerbase[i].getPlayerColor(),this);					
		AppInjector.zoneManager().add(driveButtonField[0]);
		driveButtonField[0].addButtonListener(this);
		AppInjector.zoneManager().add(driveButtonField[1]);
		driveButtonField[1].addButtonListener(this);
		}
		else if(i==4)
		{
		driveButtonField[0]=new DriveButton((int)(x_offset+10.5*block_size),(int)(y_offset+5.5*block_size),block_size, block_size/2,ride,i , playerbase[i].getPlayerColor(),this);
		driveButtonField[1]=new DriveButton((int)(x_offset+10.5*block_size),(int)(y_offset+3.5*block_size),block_size, block_size/2,noride,i , playerbase[i].getPlayerColor(),this);					
		AppInjector.zoneManager().add(driveButtonField[0]);
		driveButtonField[0].addButtonListener(this);
		AppInjector.zoneManager().add(driveButtonField[1]);
		driveButtonField[1].addButtonListener(this);
		}
		else if(i==5)
		{
		driveButtonField[0]=new DriveButton((int)(x_offset+9.5*this.block_size),(int)(y_offset+5.5*block_size),block_size, block_size/2,ride,i , playerbase[i].getPlayerColor(),this);
		driveButtonField[1]=new DriveButton((int)(x_offset+11.5*this.block_size),(int)(y_offset+5.5*block_size),block_size, block_size/2,noride,i , playerbase[i].getPlayerColor(),this);					
		AppInjector.zoneManager().add(driveButtonField[0]);
		driveButtonField[0].addButtonListener(this);
		AppInjector.zoneManager().add(driveButtonField[1]);
		driveButtonField[1].addButtonListener(this);
		}
	}
	
	public void nextPress(String command)
	{
		//wird ausgelöst, wenn jmd auf Mitfahren/Nicht mitfahren drückt
		AppInjector.zoneManager().remove(driveButtonField[0]);
		AppInjector.zoneManager().remove(driveButtonField[1]);
		int number=0;
		//commands abprüfen und entsprechend das Feldelement auf true oder false setzen, number auf die Spielernummer setzen
		if(command=="player0_ride")
		{
			wannaDriveWithMe[0]=true;
			number=0;
		}
		else if(command=="player0_noride")
		{
			wannaDriveWithMe[0]=false;
			number=0;
		}
		else if(command=="player1_ride")
		{
			wannaDriveWithMe[1]=true;
			number=1;
		}
		else if(command=="player1_noride")
		{
			wannaDriveWithMe[1]=false;
			number=1;
		}
		else if(command=="player2_ride")
		{
			wannaDriveWithMe[2]=true;
			number=2;
		}
		else if(command=="player2_noride")
		{
			wannaDriveWithMe[2]=false;
			number=2;
		}
		else if(command=="player3_ride")
		{
			wannaDriveWithMe[3]=true;
			number=3;
		}
		else if(command=="player3_noride")
		{
			wannaDriveWithMe[3]=false;
			number=3;
		}
		else if(command=="player4_ride")
		{
			wannaDriveWithMe[4]=true;
			number=4;
		}
		else if(command=="player4_noride")
		{
			wannaDriveWithMe[4]=false;
			number=4;
		}
		else if(command=="player5_ride")
		{
			wannaDriveWithMe[5]=true;
			number=5;
		}
		else if(command=="player5_noride")
		{
			wannaDriveWithMe[5]=false;
			number=5;
		}
		 
		number++;
		buttoncount--;
		if(buttoncount>0) //nächste Nummer ermitteln
		{
			while(number<6&&canYouDriveWithMe[number]==false)
			{
				number++;
			}
			
		}
		
		
		
		if(buttoncount==0)
		{
			letsRide();
		}
		else
			nextRideButton(number);
		
	}
	
	
	public void letsRide()  //Nach Fahrt abfragen, ob die Leute mitwollen
	{
		AppInjector.zoneManager().remove(driveButtonField[0]);
		AppInjector.zoneManager().remove(driveButtonField[1]);
		for(int i=0;i<6;i++)
		{
			if(wannaDriveWithMe[i])
			{
				playerbase[i].setXb(cargoal.getXb()); 
				playerbase[i].setYb(cargoal.getYb());
			}
			canYouDriveWithMe[i]=false;
			wannaDriveWithMe[i]=false;
		}
		placesomething(carride, cargoal, cargoal);
		this.waiting=false;
	}
	
	
	/**
	 * 
	 */
	public void placesomething(int type, Block start, Block ziel)
	{
		waiting=false;
		removeVisorfield();
		if(type==4) //Krankenwagen umsetzen
		{
			//alte Position entfernen:
			for(int i=1;i<this.vertical_blocks-1;i++)
			{
				board[i][0].setAmbulance(false);
				board[i][this.horizontal_blocks-1].setAmbulance(false);
			}
			for(int i=1;i<this.horizontal_blocks-1;i++)
			{
				board[0][i].setAmbulance(false);
				board[this.vertical_blocks-1][i].setAmbulance(false);
			}
			//neue Position setzen
			ziel.setAmbulance(true);
			//passendes Nachbarfeld finden
			int x=ziel.getXb();
			int y=ziel.getYb();
			int newx=0,newy=0; //Zielkoordinaten, zum abprüfen ob dort Menschen sind und zum Mitnehmen der Feuerwehrleute
			if(x==0)
			{
				board[0][y+1].setAmbulance(true);
				newx=0;
				newy=y+1;
			}
			else if(x==this.vertical_blocks-1)
			{
				board[this.vertical_blocks-1][y-1].setAmbulance(true);
				newx=this.vertical_blocks-1;
				newy=y-1;
			}
			else if(y==0)
			{
				board[x-1][0].setAmbulance(true);
				newx=x-1;
				newy=0;
			}
			else if(y==this.horizontal_blocks-1)
			{
				board[x+1][this.horizontal_blocks-1].setAmbulance(true);
				newx=x+1;
				newy=this.horizontal_blocks-1;
			}
			checkSavedPeople(ziel);
			checkSavedPeople(board[newx][newy]);
			
		}
		if(type==5) //Feuerwehrwagen umsetzen
		{
			//alte Position entfernen:
			for(int i=1;i<this.vertical_blocks-1;i++)
			{
				board[i][0].setFiretruck(false);
				board[i][this.horizontal_blocks-1].setFiretruck(false);
			}
			for(int i=1;i<this.horizontal_blocks-1;i++)
			{
				board[0][i].setFiretruck(false);
				board[this.vertical_blocks-1][i].setFiretruck(false);
			}
			//neue Position setzen
			ziel.setFiretruck(true);
			//passendes Nachbarfeld finden
			int x=ziel.getXb();
			int y=ziel.getYb();
			int newx=0,newy=0; //Zielkoordinaten, zum mitnehmen der Feuerwehrleute
			if(x==0)
			{
				board[0][y+1].setFiretruck(true);
				newx=0;
				newy=y+1;

			}
			else if(x==this.vertical_blocks-1)
			{
				board[this.vertical_blocks-1][y-1].setFiretruck(true);
				newx=this.vertical_blocks-1;
				newy=y-1;

			}
			else if(y==0)
			{
				board[x-1][0].setFiretruck(true);
				newx=x-1;
				newy=0;

			}
			else if(y==this.horizontal_blocks-1)
			{
				board[x+1][this.horizontal_blocks-1].setFiretruck(true);
				newx=x+1;
				newy=this.horizontal_blocks-1;

			}
			//Leute umsetzen
			
			
		}
		
		
		
		removeVisorfield();
		fillVisorfield();
	}
	
	
	public void removeVisorfield()
	{
		for(int i=0;i<visorFill;i++)
			AppInjector.zoneManager().remove(visorfield[i]);
		visorFill=0;
	}
	
	private void fillVisorfieldplace(Actiontype type, Block block)
	{
		if(type==Actiontype.MOVE_AMBULANCE) //Krankenwagen bewegen --> 2 neue Zielfelder (kann nur auf benachbarte AmbulanzFelder gezogen werden)
		{
			int x=block.getXb();
			int y=block.getYb();
			int longside=(int)(this.block_size*0.7);
			float factor1=(float)0.15;
			if(x==0||x==this.vertical_blocks-1) //Krankenwagen steht oben/unten --> links und rechts suchen
			{
				for(int i=1;i<this.vertical_blocks-1;i++)
				{
					//links
					if(board[i-1][0].isAmbulanceplace()&&board[i][0].isAmbulanceplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,4,block,board[i][0], (float)(x_offset+0*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
					//rechts
					if(board[i+1][this.horizontal_blocks-1].isAmbulanceplace()&&board[i][this.horizontal_blocks-1].isAmbulanceplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,4,block,board[i][this.horizontal_blocks-1], (float)(x_offset+(this.horizontal_blocks-1)*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
				}
			}
			else if(y==0||y==this.horizontal_blocks-1) //Krankenwagen steht links/rechts --> oben und unten suchen
			{
				for(int i=1;i<this.horizontal_blocks-1;i++)
				{
					if(board[0][i].isAmbulanceplace()&&board[0][i+1].isAmbulanceplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,4,block,board[0][i], (float)(x_offset+(i)*block_size+factor1*block_size), (float)(y_offset+(0)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
					if(board[this.vertical_blocks-1][i].isAmbulanceplace()&&board[this.vertical_blocks-1][i-1].isAmbulanceplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,4,block,board[this.vertical_blocks-1][i], (float)(x_offset+(i)*block_size+factor1*block_size), (float)(y_offset+(this.vertical_blocks-1)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
				}
			}
		}
		
		if(type==Actiontype.MOVE_FIRETRUCK) //Feuerwehrwagen bewegen --> 2 neue Zielfelder (kann nur auf benachbarte FeuerwehrFelder gezogen werden)
		{
			int x=block.getXb();
			int y=block.getYb();
			int longside=(int)(this.block_size*0.7);
			float factor1=(float)0.15;
			if(x==0||x==this.vertical_blocks-1) //Feuerwehrwagen steht oben/unten --> links und rechts suchen
			{
				for(int i=1;i<this.vertical_blocks-1;i++)
				{
					//links
					if(board[i-1][0].isFiretruckplace()&&board[i][0].isFiretruckplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,5,block,board[i][0], (float)(x_offset+0*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
					//rechts
					if(board[i+1][this.horizontal_blocks-1].isFiretruckplace()&&board[i][this.horizontal_blocks-1].isFiretruckplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,5,block,board[i][this.horizontal_blocks-1], (float)(x_offset+(this.horizontal_blocks-1)*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
				}
			}
			else if(y==0||y==this.horizontal_blocks-1) //Feuerwehrwagen steht links/rechts --> oben und unten suchen
			{
				for(int i=1;i<this.horizontal_blocks-1;i++)
				{
					if(board[0][i].isFiretruckplace()&&board[0][i+1].isFiretruckplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,5,block,board[0][i], (float)(x_offset+(i)*block_size+factor1*block_size), (float)(y_offset+(0)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
					if(board[this.vertical_blocks-1][i].isFiretruckplace()&&board[this.vertical_blocks-1][i-1].isFiretruckplace())
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,5,block,board[this.vertical_blocks-1][i], (float)(x_offset+(i)*block_size+factor1*block_size), (float)(y_offset+(this.vertical_blocks-1)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
				}
			}
		}
	}
	
	
	public void fillVisorfield() {

		if(playerbase[this.activePlayer].getAp()>0||playerbase[this.activePlayer].getSp()>0)
		{
			int x,y,longside,shortside;
			x=playerbase[activePlayer].getXb();
			y=playerbase[activePlayer].getYb();
			longside=(int)(this.block_size*0.7); //Laenge der Markierung
			shortside= (int)(this.block_size*0.35);
			float factor1=(float)0.15;
			float factor2=(float)0.2;
			visorFill=0;
			boolean ambulanceInRange=false;
			//Norden
			Wallblock test=board[x][y].getNorth();
			if(test==null||test.passage_Wall())
			{
			
				visorfield[visorFill]=new VisorBlock( this.pic_path,1,board[x][y],board[x-1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x-1)*block_size+factor1*block_size), longside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
				if(board[x-1][y].isAmbulance())
					ambulanceInRange=true;
			}
			if(test!=null&&test.getWall()!=Walltype.BOARDEND&&(test.getWall()==Walltype.DOOROPEN||!test.passage_Wall()))
			{
				
				visorfield[visorFill]=new VisorBlock( this.pic_path,2, board[x][y],board[x-1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+x*block_size-factor2*block_size), shortside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);		
				visorFill++;	
			}
			
			
			//Osten
			
			test=board[x][y].getEast();
			if(test==null||test.passage_Wall())
			{
			
				visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[x][y],board[x][y+1], (float)(x_offset+(y+1)*block_size+factor1*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
				if(board[x][y+1].isAmbulance())
					ambulanceInRange=true;
			}
			if(test!=null&&test.getWall()!=Walltype.BOARDEND&&(test.getWall()==Walltype.DOOROPEN||!test.passage_Wall()))
			{
				
				visorfield[visorFill]=new VisorBlock( this.pic_path,3, board[x][y],board[x][y+1], (float)(x_offset+(y+1)*block_size-factor2*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,shortside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
			}
			
			//Sueden
			test=board[x][y].getSouth();
			if(test==null||test.passage_Wall())
			{
			
				visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[x][y],board[x+1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x+1)*block_size+factor1*block_size), longside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
				if(board[x+1][y].isAmbulance())
					ambulanceInRange=true;
			}
			if(test!=null&&test.getWall()!=Walltype.BOARDEND&&(test.getWall()==Walltype.DOOROPEN||!test.passage_Wall()))
			{
				
				visorfield[visorFill]=new VisorBlock( this.pic_path,2, board[x][y],board[x+1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x+1)*block_size-factor2*block_size), shortside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
			}
			
			//Westen
			test=board[x][y].getWest();
			if(test==null||test.passage_Wall())
			{
			
				visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[x][y],board[x][y-1], (float)(x_offset+(y-1)*block_size+factor1*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);	
				visorFill++;
				if(board[x][y-1].isAmbulance())
					ambulanceInRange=true;
			}
			if(test!=null&&test.getWall()!=Walltype.BOARDEND&&(test.getWall()==Walltype.DOOROPEN||!test.passage_Wall()))
			{
				
				visorfield[visorFill]=new VisorBlock( this.pic_path,3, board[x][y],board[x][y-1], (float)(x_offset+y*block_size-factor2*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,shortside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
			}
			
			
			//eigenes Feld anwaehlbar
			if(board[x][y].isFiretruck()||board[x][y].isFire()||board[x][y].isSmoke()||(playerbase[activePlayer].getSpecialist()==SpecialistType.RETTUNGSSANITAETER&&board[x][y].getPeople()>0)||(playerbase[activePlayer].getSpecialist()==SpecialistType.GEFAHRSTOFFSPEZIALIST&&board[x][y].getDanger()>0))			
			{
				//Einsatzleiter fehlt
				
				visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[x][y],board[x][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x)*block_size+factor1*block_size), longside,longside,this);
				AppInjector.zoneManager().add(visorfield[visorFill]);
				visorFill++;
				if(board[x][y].isAmbulance())
					ambulanceInRange=true;
				
			}
			
			//Specialist mit Wärmekamera global suchen
			if(playerbase[activePlayer].getSpecialist()==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
			{
				for(int i=1; i<this.vertical_blocks-1;i++)
					for(int j=1; j<this.horizontal_blocks-1;j++)
					{
						if(board[i][j].isInterest())
						{
							visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[i][j],board[i][j], (float)(x_offset+j*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
							AppInjector.zoneManager().add(visorfield[visorFill]);
							visorFill++;
						}
					}	
			}
			
			if(!ambulanceInRange)
			{
				//Krankenwagen suchen
				for(int i=1; i<this.vertical_blocks-1;i++) 
				{
					if(board[i][0].isAmbulance()&&board[i-1][0].isAmbulance()) //links
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[i][0],board[i][0], (float)(x_offset+0*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
					if(board[i][this.horizontal_blocks-1].isAmbulance()&&board[i+1][this.horizontal_blocks-1].isAmbulance()) //rechts
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[i][this.horizontal_blocks-1],board[i][this.horizontal_blocks-1], (float)(x_offset+(this.horizontal_blocks-1)*block_size+factor1*block_size), (float)(y_offset+(i)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
				}
				for(int j=1; j<this.horizontal_blocks-1;j++)
				{
					if(board[0][j].isAmbulance()&&board[0][j+1].isAmbulance()) //oben
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[0][j],board[0][j], (float)(x_offset+j*block_size+factor1*block_size), (float)(y_offset+(0)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
					if(board[this.vertical_blocks-1][j].isAmbulance()&&board[this.vertical_blocks-1][j-1].isAmbulance()) //unten
					{
						visorfield[visorFill]=new VisorBlock( this.pic_path,1, board[this.vertical_blocks-1][j],board[this.vertical_blocks-1][j], (float)(x_offset+j*block_size+factor1*block_size), (float)(y_offset+(this.vertical_blocks-1)*block_size+factor1*block_size), longside,longside,this);
						AppInjector.zoneManager().add(visorfield[visorFill]);
						visorFill++;
					}
				}	
			}
		//TODO: weitere Aktionsmöglichkeiten abpruefen (aktuelles Feld, Einsatzleiter, Waermebildkamera)
		
		
		}
	}
	
	public void setNewInterest()
	{
		int randomvalue = rand.nextInt(8)+1;
		int randomvaluered = rand.nextInt(6)+1;
		if(this.difficulty==GameDifficulty.BEGINNER)
		{
			while(board[randomvaluered][randomvalue].isInterest())
			{
				randomvalue = rand.nextInt(8)+1;
				randomvaluered = rand.nextInt(6)+1;
			}
			board[randomvaluered][randomvalue].setFire(false);
			board[randomvaluered][randomvalue].setSmoke(false);
			board[randomvaluered][randomvalue].setInterest(true);

			
			//Test, ob ein Feuerwehrmann auf dem neuen Einsatzmarker steht
			boolean lucky=false;
			for(int i=0;i<6;i++)
			{
				if(playerbase[i]!=null&&(playerbase[i].getXb()==randomvaluered&&playerbase[i].getYb()==randomvalue))
				{
					System.out.println(playerbase[i].getPlayerColor()+ " steht auf neuem Interest");
					lucky=true;
				}
			}
			if(lucky)
			{
				board[randomvaluered][randomvalue].scanInterest();
			}
		}
		else //andere Schwierigkeitsgrade
		{
			boolean unlucky=false;
			do
			{
				unlucky=false;
				//solange neu wuerfeln, bis ein Feld ohne InterestMarker, ohne Feuer und ohne Rauch gefunden wurde
				while(board[randomvaluered][randomvalue].isInterest()||board[randomvaluered][randomvalue].isFire()||board[randomvaluered][randomvalue].isSmoke()||board[randomvaluered][randomvalue].getHealed_people()>0||board[randomvaluered][randomvalue].getPeople()>0)
				{
					randomvalue = rand.nextInt(8)+1;
					randomvaluered = rand.nextInt(6)+1;
				}
				
				//Marker darf nicht auf ein Feld mit Feuerwehrmann
				
				for(int i=0;i<6;i++)
				{
					if(playerbase[i]!=null&&(playerbase[i].getXb()==randomvaluered&&playerbase[i].getYb()==randomvalue))
					{
						unlucky=true;
					}
				}
			}while(unlucky);
			
			board[randomvaluered][randomvalue].setInterest(true);
		}
		interest_onboard++;
		interestleft--;
	}

	/**
	 * 
	 */
	private void init_statusoverview() {
		// TODO Auto-generated method stub
		stat= new Statusoverview(this);
		AppInjector.zoneManager().add(stat);
		
	}

	/**
	 * 
	 */
	private void init_choosing() {
	
		// TODO Auto-generated method stub
		//Spieler wählen Spielfiguren und Schwierigkeitsgrad
		//Testwerte:
		playercount=6;
		difficulty= GameDifficulty.HERO;
		/*   bis auf Spiel starten gedrueckt wird
		while(currentGameState==GameStates.STATE_START)
		{
			
		}
		*/

		//testwerte
		
		//Spieler, Spielerzonen,Runde Beenden Button, Aktivmarker anlegen
		
		//Spieler
		playerbase[0]=new Player(this);
		playerbase[0].setplayer(SpecialistType.RETTUNGSSANITAETER, PlayerColor.GREEN, 0, 0, 6);	
		AppInjector.zoneManager().add(playerbase[0]);
		//Spielervisualisierung
		playervisualbase[0]=new PlayerVisual(this, playerbase[0],pic_path);
		AppInjector.zoneManager().add(playervisualbase[0]);
		//Spielerzone
		playerzonebase[0]=new Playerzone(pic_path,playerbase[0], this, 0,  (x_offset/2)-block_size,(int)(y_offset/2+3.2*block_size) , 2*block_size, (int) (2.5*block_size));		
		AppInjector.zoneManager().add(playerzonebase[0]);	
		//Runde Beenden Button
		etbbase[0]=new EndTurnButton((x_offset),(int)(y_offset+8*block_size),block_size*2, block_size,0,playerbase[0].getPlayerColor(),this);
		AppInjector.zoneManager().add(etbbase[0]);
		etbbase[0].addButtonListener(this);
		//Aktivmarker
		activemarkerfield[0]= new ActiveMarker((x_offset-2*block_size),(int)(y_offset+7*block_size),block_size,block_size,0,pic_path,this);
		AppInjector.zoneManager().add(activemarkerfield[0]);

		
		
		playerbase[1]=new Player(this);
		playerbase[1].setplayer(SpecialistType.RETTUNGSSPEZIALIST, PlayerColor.WHITE, 0, 3, 0);
		AppInjector.zoneManager().add(playerbase[1]);
		playervisualbase[1]=new PlayerVisual(this, playerbase[1],pic_path);
		AppInjector.zoneManager().add(playervisualbase[1]);
		playerzonebase[1]=new Playerzone(pic_path,playerbase[1],this,  1, (int)  ((x_offset/2)-block_size*0.65),(int)(y_offset/2+1.5*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(playerzonebase[1]);
		etbbase[1]=new EndTurnButton((x_offset-3*block_size),(int)(y_offset+5*block_size),block_size*2, block_size,1,playerbase[1].getPlayerColor(),this);
		AppInjector.zoneManager().add(etbbase[1]);
		etbbase[1].addButtonListener(this);
		activemarkerfield[1]= new ActiveMarker((int)(x_offset-2*block_size),(int)(y_offset+3*block_size),block_size,block_size,1,pic_path,this);
		AppInjector.zoneManager().add(activemarkerfield[1]);
		
		
		playerbase[2]=new Player(this);
		playerbase[2].setplayer(SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA, PlayerColor.RED,0, 3, 0);
		AppInjector.zoneManager().add(playerbase[2]);
		playervisualbase[2]=new PlayerVisual(this, playerbase[2],pic_path);
		AppInjector.zoneManager().add(playervisualbase[2]);
		playerzonebase[2]=new Playerzone(pic_path,playerbase[2], this, 2,  (x_offset/2),(int)(y_offset/2+0.8*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(playerzonebase[2]);
		etbbase[2]=new EndTurnButton((x_offset+2*block_size),(int)(y_offset),block_size*2, block_size,2,playerbase[2].getPlayerColor(),this);
		AppInjector.zoneManager().add(etbbase[2]);
		etbbase[2].addButtonListener(this);
		activemarkerfield[2]= new ActiveMarker((x_offset),(int)(y_offset+block_size),block_size,block_size,2,pic_path,this);
		AppInjector.zoneManager().add(activemarkerfield[2]);
		
		playerbase[3]=new Player(this);
		playerbase[3].setplayer(SpecialistType.ALLESKOENNER, PlayerColor.YELLOW, 0, 7, 3);
		AppInjector.zoneManager().add(playerbase[3]);
		playervisualbase[3]=new PlayerVisual(this, playerbase[3],pic_path);
		AppInjector.zoneManager().add(playervisualbase[3]);
		playerzonebase[3]=new Playerzone(pic_path,playerbase[3],this,  3,  (x_offset/2)+6*block_size,(int)(y_offset/2+0.8*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(playerzonebase[3]);
		etbbase[3]=new EndTurnButton((x_offset+10*block_size),(int)(y_offset),block_size*2, block_size,3,playerbase[3].getPlayerColor(),this);
		AppInjector.zoneManager().add(etbbase[3]);
		etbbase[3].addButtonListener(this);
		activemarkerfield[3]= new ActiveMarker((x_offset+12*block_size),(int)(y_offset+block_size),block_size,block_size,3,pic_path,this);
		AppInjector.zoneManager().add(activemarkerfield[3]);
		
		playerbase[4]=new Player(this);
		playerbase[4].setplayer(SpecialistType.GEFAHRSTOFFSPEZIALIST, PlayerColor.BLUE, 0, 4, 9);
		AppInjector.zoneManager().add(playerbase[4]);
		playervisualbase[4]=new PlayerVisual(this, playerbase[4],pic_path);
		AppInjector.zoneManager().add(playervisualbase[4]);
		playerzonebase[4]=new Playerzone(pic_path,playerbase[4],this,  4, (int)  ((x_offset/2)+block_size*5.65),(int)(y_offset/2+2.5*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(playerzonebase[4]);
		etbbase[4]=new EndTurnButton((x_offset+13*block_size),(int)(y_offset+3*block_size),block_size*2, block_size,4,playerbase[4].getPlayerColor(),this);
		AppInjector.zoneManager().add(etbbase[4]);	
		etbbase[4].addButtonListener(this);
		activemarkerfield[4]= new ActiveMarker((int)(x_offset+12*block_size),(int)(y_offset+5*block_size),block_size,block_size,4,pic_path,this);
		AppInjector.zoneManager().add(activemarkerfield[4]);
		
		playerbase[5]=new Player(this);
		playerbase[5].setplayer(SpecialistType.LOESCHSCHAUMSPEZIALIST, PlayerColor.ORANGE,  0, 5, 9);
		AppInjector.zoneManager().add(playerbase[5]);
		playervisualbase[5]=new PlayerVisual(this, playerbase[5],pic_path);
		AppInjector.zoneManager().add(playervisualbase[5]);
		playerzonebase[5]=new Playerzone(pic_path,playerbase[5],this,  5,  (x_offset/2)+5*block_size,(int)(y_offset/2+3.2*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(playerzonebase[5]);		
		etbbase[5]=new EndTurnButton((x_offset+8*block_size),(int)(y_offset+8*block_size),block_size*2, block_size,5,playerbase[5].getPlayerColor(),this);
		AppInjector.zoneManager().add(etbbase[5]);
		etbbase[5].addButtonListener(this);
		activemarkerfield[5]= new ActiveMarker((x_offset+10*block_size),(int)(y_offset+7*block_size),block_size,block_size,5,pic_path,this);
		AppInjector.zoneManager().add(activemarkerfield[5]);
		
		
		
		//Startspieler ermitteln		
		activePlayer=0;
		while(playerbase[activePlayer]==null)
			activePlayer++;
		playerbase[activePlayer].start_turn();
		
		currentGameState=GameStates.STATE_STARTBOARD;
		
	}
	/* (non-Javadoc)
	 * @see vialab.SMT.event.ButtonZoneListener#buttonClicked(vialab.SMT.event.ButtonEvent)
	 */
	@Override
	public void buttonClicked(ButtonEvent e) {
		String command =e.getActionCommand();
		System.out.println(command);
		if(this.actionTouched||this.visorTouched)
			return;
		//TODO: Abfrage, ob wirklich beenden
		//TODO: Wenn Spieler auf Feuer steht, nicht beenden lassen
		if(command=="player0_turnend"&&activePlayer==0&&!waiting)
		{			
			nextPlayer();		
		}
		else if(command=="player1_turnend"&&activePlayer==1&&!waiting)
		{
			nextPlayer();		
		}
		else if(command=="player2_turnend"&&activePlayer==2&&!waiting)
		{			
			nextPlayer();		
		}
		else if(command=="player3_turnend"&&activePlayer==3&&!waiting)
		{
			nextPlayer();		
		}
		else if(command=="player4_turnend"&&activePlayer==4&&!waiting)
		{			
			nextPlayer();		
		}
		else if(command=="player5_turnend"&&activePlayer==5&&!waiting)
		{
			nextPlayer();		
		}
		else if(command=="player0_ride"||command=="player0_noride"||command=="player1_ride"||command=="player1_noride"||command=="player2_ride"||command=="player2_noride"||command=="player3_ride"||command=="player3_noride"||command=="player4_ride"||command=="player4_noride"||command=="player5_ride"||command=="player5_noride"	)
		{
			nextPress(command);
		}

		
		
	}
	public void checkWinLoseConditions()
	{
		if(this.saved_person>=this.saved_person_to_win)
		{
			//Win
			ending=new EndingScreen(0,0,AppInjector.application().displayWidth,AppInjector.application().displayHeight,1, this.pic_path);
			AppInjector.zoneManager().add(ending);
			currentGameState=GameStates.STATE_FINISHED;
			removeVisorfield();
			for(int i=0;i<6;i++)
			{
				if(playerbase[i]!=null)//alte Zonen löschen, bei Neustart nötig
				{
					AppInjector.zoneManager().remove(playerbase[i]);
					AppInjector.zoneManager().remove(playerzonebase[i]);
					AppInjector.zoneManager().remove( etbbase[i]);
					AppInjector.zoneManager().remove(activemarkerfield[i]);
					AppInjector.zoneManager().remove(playervisualbase[i]);
				}
				
			}

				

		}
		else if(this.dead_person>=this.dead_person_to_lose)
		{
			//Niederlage durch zuviele Leute gestorben
			ending=new EndingScreen(0,0,AppInjector.application().displayWidth,AppInjector.application().displayHeight,2, this.pic_path);
			AppInjector.zoneManager().add(ending);
			currentGameState=GameStates.STATE_FINISHED;
			removeVisorfield();
			for(int i=0;i<6;i++)
			{
				AppInjector.zoneManager().remove(playerbase[i]);
				AppInjector.zoneManager().remove(playerzonebase[i]);
				AppInjector.zoneManager().remove( etbbase[i]);
			}

		}
		else if(this.buildingdamage>=this.maxbuildingdamage)
		{
			//Niederlage durch eingestürtzes Gebäude
			ending=new EndingScreen(0,0,AppInjector.application().displayWidth,AppInjector.application().displayHeight,3, this.pic_path);
			AppInjector.zoneManager().add(ending);
			currentGameState=GameStates.STATE_FINISHED;
			removeVisorfield();
			for(int i=0;i<6;i++)
			{
				AppInjector.zoneManager().remove(playerbase[i]);
				AppInjector.zoneManager().remove(playerzonebase[i]);
				AppInjector.zoneManager().remove( etbbase[i]);
			}
			
			
			           
			                         
               
		}
		//TODO: Button für Spiel neustarten

	}
	
	public void nextPlayer()
	{
		playerbase[activePlayer].end_turn();

		this.currentPhaseState=PhaseStates.STATE_FIRE;

		
		extendFire();
		
		this.currentPhaseState=PhaseStates.STATE_END;
		checkAfterExtend();
		checkWinLoseConditions();
		if(currentGameState!=GameStates.STATE_FINISHED)
		{
		this.activePlayer++;
		while(playerbase[activePlayer%6]==null)
			this.activePlayer++;
		this.activePlayer%=6;
		this.currentPhaseState=PhaseStates.STATE_PLAYER;
		playerbase[activePlayer].start_turn();
		removeVisorfield();
		fillVisorfield();
		}
	}

	public void checkSavedPeople(Block ziel)
	{
		//Personen und Gefahrenstoffe abprüfen, ob gerettet
		
		if(this.difficulty==GameDifficulty.BEGINNER) //Keine Gefahrenstoffe, alle Personen ausserhalb des Gebäudes sind gerettet
		{
			if(!ziel.isInside_Block())
			{
				while(ziel.getHealed_people()>0)
				{
					this.saved_person++;
					this.interest_onboard--;
					ziel.reduceHealedPeople();
					person_marker--;
				}				
				while(ziel.getPeople()>0)
				{
					this.saved_person++;
					this.interest_onboard--;
					ziel.reducePeople();
					person_marker--;
				}
			}
		}
		else
		{
			//Gefahrenstoffe ausserhalb des Gebäudes sind sicher
			while(ziel.getDanger()>0)
			{
				this.saved_danger++;
				ziel.reduceDanger();
			}
			//Personen müssen zum Krankenwagen getragen werden
			if(ziel.isAmbulance())
			{
				while(ziel.getHealed_people()>0)
				{
					this.saved_person++;
					this.interest_onboard--;
					ziel.reduceHealedPeople();
					person_marker--;
				}				
				while(ziel.getPeople()>0)
				{
					this.saved_person++;
					this.interest_onboard--;
					ziel.reducePeople();
					person_marker--;
				}
			}

		}
	}
	public void fireFighterUnderFire(int number)
	{
		//TODO: Feuerwehrmann ausserhalb neu platzieren
		if(this.difficulty==GameDifficulty.BEGINNER)
		{
			int difference=100; //Maximale Distanz kann 80 sein
			int newdifference=0;
			int boarderx=0,boardery=0; //welcher Spot ist am naechsten
			//Krankenwagenspots suchen
			for(int i=1; i<this.horizontal_blocks-1;i++)
			{
				if(board[0][i].isAmbulanceplace()&&board[0][i+1].isAmbulanceplace()) //oben
				{
					newdifference= playerbase[number].getXb()+ Math.abs(playerbase[number].getYb()-i-1);
					//System.out.println("Differenz oben: " + newdifference);
					if(newdifference<difference)
					{
						difference=newdifference;
						boarderx=0;
						boardery=i+1;
					}
				}
				else if(board[vertical_blocks-1][i].isAmbulanceplace()&&board[vertical_blocks-1][i-1].isAmbulanceplace())//unten
				{
					newdifference= vertical_blocks-1-playerbase[number].getXb()+ Math.abs(playerbase[number].getYb()-i+1);
					//System.out.println("Differenz unten: " + newdifference);
					if(newdifference<difference)
					{
						difference=newdifference;
						boarderx=vertical_blocks-1;
						boardery=i-1;
					}
				}
			}
			for(int i=1; i<this.vertical_blocks-1;i++)
			{				
				if(board[i][0].isAmbulanceplace()&&board[i-1][0].isAmbulanceplace())//links
				{
					newdifference= playerbase[number].getYb()+  Math.abs(playerbase[number].getXb()-i+1);
					//System.out.println("Differenz links: " + newdifference);
					if(newdifference<difference)
					{
						difference=newdifference;
						boarderx=i-1;
						boardery=0;
					}
				}
				else if(board[i][horizontal_blocks-1].isAmbulanceplace()&&board[i+1][horizontal_blocks-1].isAmbulanceplace())//rechts
				{
					newdifference= horizontal_blocks-1-playerbase[number].getYb()+  Math.abs(playerbase[number].getXb()-i-1);
					//System.out.println("Differenz rechts: " + newdifference);
					if(newdifference<difference)
					{
						difference=newdifference;
						boarderx=i+1;
						boardery=horizontal_blocks-1;
					}
				}
			}
			//Feuerwehrmann neue Position zuweisen
			playerbase[number].setXb(boarderx);
			playerbase[number].setYb(boardery);
		}
		else
		{
			//Krankenwagen suchen
			for(int i=1; i<this.horizontal_blocks;i++)
			{
				if(board[0][i].isAmbulance()&&board[0][i+1].isAmbulance()) //oben
				{
					playerbase[number].setXb(0);
					playerbase[number].setYb(i+1);
					return;
				}
				else if(board[vertical_blocks-1][i].isAmbulance()&&board[vertical_blocks-1][i-1].isAmbulance())//unten
				{
					playerbase[number].setXb(vertical_blocks-1);
					playerbase[number].setYb(i-1);
					return;
				}
			}
			for(int i=1; i<this.vertical_blocks;i++)
			{
				if(board[i][0].isAmbulance()&&board[i-1][0].isAmbulance())//links
				{
					playerbase[number].setXb(i-1);
					playerbase[number].setYb(0);
					return;
				}
				else if(board[i][horizontal_blocks-1].isAmbulance()&&board[i+1][horizontal_blocks-1].isAmbulance())//rechts
				{
					playerbase[number].setXb(i+1);
					playerbase[number].setYb(horizontal_blocks-1);
					return;
				}
			}
		}
	}
	//TODO: nach Feuer ausbreiten Auswirkungen auf Personen-/Interestmarker und Feuerwehrleute testen
	public void checkAfterExtend()
	{
		//Testen, ob die Feuerwehrmänner im Feuer stehen
		
		for(int i=0;i<6;i++)
		{
			if(playerbase[i]!=null)
			{
				if(board[playerbase[i].getXb()][playerbase[i].getYb()].isFire())
					fireFighterUnderFire(i);
			}
		}
		
		
		for(int i=0;i<vertical_blocks;i++)
			for(int j=0;j<horizontal_blocks;j++)
			{
				if(board[i][j].isFire())
				{
					if(board[i][j].isInterest())
						board[i][j].scanInterest();
					while(board[i][j].getPeople()>0)   //Leute sterben
					{
						board[i][j].reducePeople();
						dead_person++;
						person_marker--;
						interest_onboard--;
					}
					while(board[i][j].getHealed_people()>0) //verarztete Leute sterben
					{
						board[i][j].reduceHealedPeople();
						dead_person++;
						interest_onboard--;
						person_marker--;
					}
					
				}
				
				
				
			}
		
		while (interest_onboard<3)
		{
			setNewInterest();
			interestleft--;
		}
		
		
		/*
		saved_person=0;			//Anzahl geretteter Personen
		private int dead_person=0;
		
		person_marker=10;			//Anzahl Personen im Spiel insgesamt
		private int false_alarm_marker=5;		//Anzahl falscher Alarme insgesamt
		private int saved_danger=0;
		private int interestleft;
		
		//Prüfen, ob Gebäude zusammenbricht
		buildingdamage=0;		
		private int maxbuildingdamage=24;
		*/
	}
	
	
	//Feuer ausbreiten
	public void extendFire()
	{
		boolean seatonspot=false;		//true, wenn auf aktuellem Feld ein Brandherd ist (muss gemerkt werden, da bei Gefahrenstoffexplosion ein neuer Brandherd entstehen kann)
		int randomvalue = rand.nextInt(8)+1;
		int randomvaluered = rand.nextInt(6)+1;
		System.out.println("Zielfeld: "+ randomvaluered + ": " + randomvalue );
		if(!board[randomvaluered][randomvalue].isFire()&&!board[randomvaluered][randomvalue].isSmoke())
		{
			board[randomvaluered][randomvalue].setSmoke(true);
			System.out.println("neuer Rauchmarker auf: "+ randomvaluered + ": " + randomvalue );
		}
		else if(board[randomvaluered][randomvalue].isSmoke())
		{
			board[randomvaluered][randomvalue].setSmoke(false);
			board[randomvaluered][randomvalue].setFire(true);
			System.out.println("neuer Feuermarker auf: "+ randomvaluered + ": " + randomvalue );
		}
		else if(board[randomvaluered][randomvalue].isFire())
		{
			explosion(randomvaluered,randomvalue);	
			System.out.println("Explosion auf: "+ randomvaluered + ": " + randomvalue );
		}
		if(board[randomvaluered][randomvalue].isSeat())
		{
			seatonspot=true;
		}

		
		boolean smokesearch=true,test;
		boolean dangersearch=true;
		//[vertical_blocks][horizontal_blocks]
		while(dangersearch)
		{
			while(smokesearch)
			{
				smokesearch=false;
				for(int i=0;i<vertical_blocks;i++)
					for(int j=0;j<horizontal_blocks;j++)
					{
						if(board[i][j].isSmoke())
						{
							test=board[i][j].checkneightbors_Fire(i,j);
							if(test)
							{
								smokesearch=true;
								System.out.println("Rauch auf Feuer gedreht beim Durchsuchen auf: "+ i + ": " + j );
							}
						}
					}
			}
			if(this.difficulty==GameDifficulty.BEGINNER)
				dangersearch=false;
			else
			{
				dangersearch=false;
				for(int i=0;i<vertical_blocks;i++)
					for(int j=0;j<horizontal_blocks;j++)
					{
						if(board[i][j].getDanger()>0&&board[i][j].isFire())   //Feuer und Gefahrstoff -> Explosion
						{
							dangersearch=true;
							while(board[i][j].getDanger()>0)
							{
								System.out.println("Gefahrexplosion an " + i +" "+ j);
								explosion(i,j);
								board[i][j].reduceDanger();
							}
							if(this.inactive_seats>0)
							{
								board[i][j].setSeat(true);
								this.inactive_seats--;
								System.out.println("neuer Brandherd auf  " + i +" "+ j);
								
							}
							
						}
					}
			}
				
		}
		//Feuer ausserhalb des Gebäudes entfernen
		for(int i=0;i<vertical_blocks;i++)
		{
			board[i][0].setFire(false);
			board[i][horizontal_blocks-1].setFire(false);
		}
			
		for(int j=0;j<horizontal_blocks;j++)
		{
			board[0][j].setFire(false);
			board[vertical_blocks-1][j].setFire(false);
		}

		if(seatonspot)
		{
			System.out.println("Brandherd bei "+ randomvaluered+" "+randomvalue+" erwischt" );
			hittedseat=true;
			this.extendFire();
		}
		else
		{
			if(hittedseat)		//wurde das aktuelle extend durch ein vorheriges Brandherd Auflodern ausgeloest?
			{
				hittedseat=false;
				if(this.inactive_seats>0)
				{
					board[randomvaluered][randomvalue].setSeat(true);
					inactive_seats--;
					System.out.println("neuer Brandherd nach Auflodern auf  " + randomvaluered +" "+ randomvalue);
				}
			}
		}
	}

	/**
	 * @param difficulty2
	 * @param playercount2
	 */
	private void init_beginningfire() 
	{
		// TODO Auto-generated method stub
		//Feuer verursachen bei Spielbeginn
		 
		if(difficulty == GameDifficulty.BEGINNER)
		{
			person_marker=10;
			false_alarm_marker=5;	
			
			
			/* explosionstest
			board[1][6].setFire(true);
			board[1][7].setFire(true);
			
			explosion(1,6);
			explosion(1,6);
			explosion(1,6);
			explosion(1,6);
			explosion(1,6);	
			explosion(2,4);
			explosion(5,1);
			explosion(2,4);		
			*/
			board[2][2].setFire(true);
			board[2][3].setFire(true);
			board[3][2].setFire(true);
			board[3][3].setFire(true);
			board[3][4].setFire(true);
			board[3][5].setFire(true);
			board[4][4].setFire(true);
			board[5][6].setFire(true);
			board[5][7].setFire(true);
			board[6][6].setFire(true);
			/*
			board[6][7].setSmoke(true);
			board[1][5].setSmoke(true);
			*/
			board[2][4].setInterest(true);
			board[5][1].setInterest(true);
			board[5][8].setInterest(true);
			interest_onboard=3;
			
			
		}
		else 
		{		
			//erste Explosion
			
			int randomvalue = rand.nextInt(8);
			if(randomvalue==0)
			{
				board[3][3].setFire(true);
				board[3][3].setSeat(true);
				explosion(3,3);
			}
			else if(randomvalue==1)
			{
				board[3][4].setFire(true);
				board[3][4].setSeat(true);
				explosion(3,4);
			}
			else if(randomvalue==2)
			{
				board[3][5].setFire(true);
				board[3][5].setSeat(true);
				explosion(3,5);
			}
			else if(randomvalue==3)
			{
				board[3][6].setFire(true);
				board[3][6].setSeat(true);
				explosion(3,6);
			}
			else if(randomvalue==4)
			{
				board[4][6].setFire(true);
				board[4][6].setSeat(true);
				explosion(4,6);
			}
			else if(randomvalue==5)
			{
				board[4][5].setFire(true);
				board[4][5].setSeat(true);
				explosion(4,5);
			}
			else if(randomvalue==6)
			{
				board[4][4].setFire(true);
				board[4][4].setSeat(true);
				explosion(4,4);
			}
			else if(randomvalue==7)
			{
				board[4][3].setFire(true);
				board[4][3].setSeat(true);
				explosion(4,3);
			}
			
			//Startexplosion 2
			randomvalue = rand.nextInt(8)+1;
			int randomvaluered = rand.nextInt(6)+1;
			while(board[randomvaluered][randomvalue].isFire())
			{
				randomvalue = rand.nextInt(8)+1;
				randomvaluered = rand.nextInt(6)+1;
			}
			board[randomvaluered][randomvalue].setFire(true);
			board[randomvaluered][randomvalue].setSeat(true);
			explosion(randomvaluered,randomvalue);

				
			//Startexplosion 3
			if(randomvalue==1)
				randomvalue=6;
			else if(randomvalue==2)
				randomvalue=5;
			else if(randomvalue==3)
				randomvalue=8;
			else if(randomvalue==4)
				randomvalue=7;
			else if(randomvalue==5)
				randomvalue=2;
			else if(randomvalue==6)
				randomvalue=1;
			else if(randomvalue==7)
				randomvalue=4;
			else if(randomvalue==8)
				randomvalue=3;
			
			randomvaluered=rand.nextInt(6)+1;
			while(board[randomvaluered][randomvalue].isFire())
			{
				randomvaluered = rand.nextInt(6)+1;
			}
			board[randomvaluered][randomvalue].setFire(true);
			board[randomvaluered][randomvalue].setSeat(true);
			explosion(randomvaluered,randomvalue);
			
			if(difficulty == GameDifficulty.HERO)
			{
				//vierte Explosion
				randomvalue = rand.nextInt(8)+1;
				randomvaluered = rand.nextInt(6)+1;
				while(board[randomvaluered][randomvalue].isFire())
				{
					randomvalue = rand.nextInt(8)+1;
					randomvaluered = rand.nextInt(6)+1;
				}
				board[randomvaluered][randomvalue].setFire(true);
				board[randomvaluered][randomvalue].setSeat(true);
				explosion(randomvaluered,randomvalue);
			}
			
			//Ermittlung Anzahl weiterer Brandherde und Anzahl Gefahrenstoffe
			if(playercount>3)
				active_seats=3;	
			else if(playercount<3)
				active_seats=0;
			else
				active_seats=2;
			inactive_seats=0;			
			int neededdanger=0;
			
			if(difficulty == GameDifficulty.RECRUT)
			{
			neededdanger=3;		
			}
			else if(difficulty == GameDifficulty.VETERAN)
			{
			neededdanger=4;	
			active_seats+=3;
			inactive_seats=6;
			}
			else if(difficulty == GameDifficulty.HERO)
			{
			neededdanger=5;	
			active_seats+=3;
			inactive_seats=12;
			}
			
			//Verteilung Gefahrenstoffe
			while(neededdanger>0)
			{
				randomvalue = rand.nextInt(8)+1;
				randomvaluered = rand.nextInt(6)+1;
				while(board[randomvaluered][randomvalue].isFire())
				{
					randomvalue = rand.nextInt(8)+1;
					randomvaluered = rand.nextInt(6)+1;
				}
				int tmp=board[randomvaluered][randomvalue].getDanger();
				board[randomvaluered][randomvalue].setDanger(tmp+1);
				neededdanger--;
			}
			
			//EInsatzmarker verteilen
			while (interest_onboard<3)
			{
				randomvalue = rand.nextInt(8)+1;
				randomvaluered = rand.nextInt(6)+1;
				while(board[randomvaluered][randomvalue].isFire()||board[randomvaluered][randomvalue].isInterest())
				{
					randomvalue = rand.nextInt(8)+1;
					randomvaluered = rand.nextInt(6)+1;
				}
				board[randomvaluered][randomvalue].setInterest(true);
				interest_onboard++;
				interestleft--;
			}
			
			//weitere Brandherde verteilen
			int seatplace=active_seats;
			while (seatplace >0)
			{
				randomvalue = rand.nextInt(8)+1;
				randomvaluered = rand.nextInt(6)+1;
				while(board[randomvaluered][randomvalue].isSeat())
				{
					randomvalue = rand.nextInt(8)+1;
					randomvaluered = rand.nextInt(6)+1;
				}
				board[randomvaluered][randomvalue].setSeat(true);;
				seatplace--;
			}
			
		}	
			
		//Feuer ausserhalb des Gebäudes entfernen
		for(int i=0;i<vertical_blocks;i++)
		{
			board[i][0].setFire(false);
			board[i][horizontal_blocks-1].setFire(false);
		}
			
		for(int j=0;j<horizontal_blocks;j++)
		{
			board[0][j].setFire(false);
			board[vertical_blocks-1][j].setFire(false);
		}
		//TODO: Feuerwerhmaenner/Fahrzeuge auf Spielbrett stellen und Startspieler bestimmen
			
		
		
		
		
	}
	
	private void explosion(int x, int y)
	{
		//in alle 4 Richtungen einzeln abprüfen
		//Norden
		boolean expention=true;
		int i=x, j=y;
		while (expention)
		{
			Wallblock wall=board[i][j].getNorth();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i-1][j].isFire())
				{
					board[i-1][j].setFire(true);
					expention=false;
				}
				else
					i--;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					expention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					expention=false;
				}
			}
		}
		
		
		//Sueden
		expention=true;
		i=x; j=y;
		while (expention)
		{
			Wallblock wall=board[i][j].getSouth();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i+1][j].isFire())
				{
					board[i+1][j].setFire(true);
					expention=false;
				}
				else
					i++;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					expention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					expention=false;
				}
			}
		}
		
		//Osten
		
		expention=true;
		i=x; j=y;
		while (expention)
		{
			Wallblock wall=board[i][j].getEast();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i][j+1].isFire())
				{
					board[i][j+1].setFire(true);
					expention=false;
				}
				else
					j++;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					expention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					expention=false;
				}
			}
		}
		//Westen
		
		expention=true;
		i=x; j=y;
		while (expention)
		{
			Wallblock wall=board[i][j].getWest();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i][j-1].isFire())
				{
					board[i][j-1].setFire(true);
					expention=false;
				}
				else
					j--;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					expention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					expention=false;
				}
			}
		}
		
		
	}
	
	/**
	 * 
	 */
	
	/*
	private void initActions() {
		Hashmap<Actiontype,Action> map = bla;
		for (Actiontype at:Actiontype.values()) {
			map.put(at,new Action(at));
		}
		
	}


	*/

	
	/**
	 * Loads all Resources from a file into the game engine. This method is only considered a placeholder.
	 * Either you would have to validate resources in here or create a method specifically for each type of resource.
	 * 
	 * ATTENTION: Levels are also considered resources (which contain additional resources).
	 * 
	 * @return The loaded resources in an ArrayList<>
	 */
	private ArrayList<Object> loadResources() {
		
		  Path path = Paths.get(Constants.RESOURCE_URI); // loads the images for a card
		  
		  ArrayList<Object> cards = new ArrayList<Object>();
		  try {
	            Stream<String> lines = Files.lines(path);
	            lines.forEach(s -> {
	            	if (!s.startsWith("#")) { //ignore comment
	            		String[] parts = s.split(",");
//	        			cards.add(new CardContent(parts[0],parts[1],parts[2]));
	            		resources.add(new Object());
	            	}
	            	});
	            lines.close();
	        } catch (IOException ex) {

	        }
		return cards;
	}
	

	
		
	public void init_measures()
	{
		block_size = AppInjector.application().displayHeight/(vertical_blocks+2);

		panel_width = 6*block_size;
		panel_height = block_size;
		board_width=10*block_size;
		board_height=8*block_size;		
		


		y_offset = (AppInjector.application().displayHeight - vertical_blocks*block_size)/2;
		x_offset = (AppInjector.application().displayWidth - horizontal_blocks*block_size)/2;
		
				
	}
	public void init_board()
	{
		background=new Background(backgroundpic,x_offset,y_offset,board_width,board_height );
		AppInjector.zoneManager().add(background);
		
		
		for (int i=0;i<vertical_blocks;i++) {
			  for (int j=0;j<horizontal_blocks;j++) 
			  {
			      board[i][j]= new Block(this); 
			      AppInjector.zoneManager().add(board[i][j]);
			      boardvisual[i][j]= new BlockVisual(this, pic_path,board[i][j]); 
			      AppInjector.zoneManager().add(boardvisual[i][j]);
			      }
			}
	}	

	//json
	

	
    public void fillmatrixFromJSON(JSONObject jsonObject) {
    	
    	JSONArray jsonboard = jsonObject.getJSONArray("Board");
    	
    	for (int i=0;i<jsonboard.size();i++) 
    	{
    	JSONObject boardobj = jsonboard.getJSONObject(i);
    	int x= boardobj.getInt("x");
    	int y=boardobj.getInt("y");
    	board[x][y].set_all(x,y,boardobj.getBoolean("inside_Block")  , boardobj.getBoolean("smoke"), boardobj.getBoolean("fire"), boardobj.getInt("danger"), boardobj.getBoolean("interest"), boardobj.getBoolean("seat"), boardobj.getInt("people"), boardobj.getInt("healed_people"), null, null, null, null);
    	}
    	
    	//Boardgrenzen anlegen
    	Wallblock wall;
    	for(int i=0; i<horizontal_blocks;i++)
    	{
    		wall=new Wallblock(0, Walltype.BOARDEND);
    		board[0][i].setNorth(wall);
    		board[vertical_blocks-1][i].setSouth(wall);
    	}
    	for(int i=0; i<vertical_blocks;i++)
    	{
    		wall=new Wallblock(1, Walltype.BOARDEND);
    		board[i][0].setWest(wall);
    		board[i][horizontal_blocks-1].setEast(wall);
    	}
    	
    	JSONArray jsonwalls = jsonObject.getJSONArray("Walls");
    	int x1,y1,x2,y2,direction,walltype;
    	for (int i=0;i<jsonwalls.size();i++) 
    	{    	
    		JSONObject wallobj = jsonwalls.getJSONObject(i);
    		x1=wallobj.getInt("x1");
    		x2=wallobj.getInt("x2");
    		y1=wallobj.getInt("y1");
    		y2=wallobj.getInt("y2");
    		walltype=wallobj.getInt("walltype");
    		
    		direction=wallobj.getInt("direction");
    		
    		if(direction ==0) //horizontal
    		{
    			wall=new Wallblock(direction, walltype );
    			board[x1][y1].setSouth(wall);
    			board[x2][y2].setNorth(wall);
    		}
    		else if(direction ==1) //vertikal
    		{
    			wall=new Wallblock(direction, walltype );
    			board[x2][y2].setWest(wall);
    			board[x1][y1].setEast(wall);
    			
    			
    		}    		
    		
    	}
    	JSONArray jsonambulancespots = jsonObject.getJSONArray("Ambulancespots");
    	
    	for (int i=0;i<jsonambulancespots.size();i++) 
    	{    	
    		JSONObject ambulancespot = jsonambulancespots.getJSONObject(i);
    		x1=ambulancespot.getInt("x1");
    		x2=ambulancespot.getInt("x2");
    		y1=ambulancespot.getInt("y1");
    		y2=ambulancespot.getInt("y2");
    		board[x1][y1].setAmbulanceplace();
    		board[x2][y2].setAmbulanceplace();
    		//System.out.println("Ambulancespot: " + x1 +" "+ y1 + "und"+ x2 +" "+ y2);
    	}
    	JSONArray jsonfiretruckspots = jsonObject.getJSONArray("Firetruckspots");
    	
    	for (int i=0;i<jsonfiretruckspots.size();i++) 
    	{    	
    		JSONObject firetruckspot = jsonfiretruckspots.getJSONObject(i);
    		x1=firetruckspot.getInt("x1");
    		x2=firetruckspot.getInt("x2");
    		y1=firetruckspot.getInt("y1");
    		y2=firetruckspot.getInt("y2");
    		board[x1][y1].setFiretruckplace();
    		board[x2][y2].setFiretruckplace();
    		//System.out.println("Firetruckspot: " + x1 +" "+ y1 + "und"+ x2 +" "+ y2);
    	}
    	
    }
	
	public void init_blocks(String jsonpath)
	{
		JSONObject obj= Utility.getJSONObjectFromPath(jsonpath);
		fillmatrixFromJSON(obj);

		return;
	}
	
	private void init_player() {
		
		//TODO: alle Spielfiguren nacheinander aufs Spielbrett setzen
		
		
		//Fahrzeuge aufs Spielbrett setzen, vorerst Dummy; später nur auf entsprechende Spots setzen ermöglichen
		/*
		board[0][5].setAmbulance(true);	//oben
		board[0][6].setAmbulance(true);
		
		board[3][9].setAmbulance(true); //rechts
		board[4][9].setAmbulance(true);
		board[7][3].setAmbulance(true); //unten
		board[7][4].setAmbulance(true);
		*/
		board[3][0].setAmbulance(true);	//links
		board[4][0].setAmbulance(true);
		
		
		board[0][7].setFiretruck(true); //oben
		board[0][8].setFiretruck(true);
		/*
		board[5][9].setFiretruck(true); //rechts
		board[6][9].setFiretruck(true);
		board[7][1].setFiretruck(true); //unten
		board[7][2].setFiretruck(true);
		board[1][0].setFiretruck(true); //links
		board[2][0].setFiretruck(true);
		*/
		
	}

	//Getter und Setter:
	
	public void increaseInterest_onboard()
	{
		interest_onboard++;
	}
	public void decreaseInterest_onboard()
	{
		interest_onboard--;
	}
	
	public GameStates getCurrentGameState() {
		return currentGameState;
	}
	
	public void setCurrentGameState(GameStates state) {
		this.currentGameState = state;
	}
	public Integer getVertical_blocks() {
		return vertical_blocks;
	}

	public void setVertical_blocks(Integer vertical_blocks) {
		this.vertical_blocks = vertical_blocks;
	}

	public Integer getBlock_size() {
		return block_size;
	}

	public void setBlock_size(Integer block_size) {
		this.block_size = block_size;
	}

	public Integer getHorizontal_blocks() {
		return horizontal_blocks;
	}

	public void setHorizontal_blocks(Integer horizontal_blocks) {
		this.horizontal_blocks = horizontal_blocks;
	}

	public Integer getX_offset() {
		return x_offset;
	}

	public void setX_offset(Integer x_offset) {
		this.x_offset = x_offset;
	}

	public Integer getY_offset() {
		return y_offset;
	}

	public void setY_offset(Integer y_offset) {
		this.y_offset = y_offset;
	}
	

	
	/**
	 * @return the panel_width
	 */
	public int getPanel_width() {
		return panel_width;
	}

	/**
	 * @return the panel_height
	 */
	public int getPanel_height() {
		return panel_height;
	}



	/**
	 * @return the playerbase
	 */
	public model.Player[] getPlayerbase() {
		return playerbase;
	}

	/**
	 * @return the playercount
	 */
	public int getPlayercount() {
		return playercount;
	}

	/**
	 * @return the saved_person
	 */
	public int getSaved_person() {
		return saved_person;
	}

	/**
	 * @return the dead_person
	 */
	public int getDead_person() {
		return dead_person;
	}

	/**
	 * @return the buildingdamage
	 */
	public int getBuildingdamage() {
		return buildingdamage;
	}

	/**
	 * @return the maxbuildingdamage
	 */
	public int getMaxbuildingdamage() {
		return maxbuildingdamage;
	}

	/**
	 * @return the person_marker
	 */
	public int getPerson_marker() {
		return person_marker;
	}

	/**
	 * @return the false_alarm_marker
	 */
	public int getFalse_alarm_marker() {
		return false_alarm_marker;
	}



	/**
	 * @return the inactive_seats
	 */
	public int getInactive_seats() {
		return inactive_seats;
	}

	/**
	 * @return the interest_onboard
	 */
	public int getInterest_onboard() {
		return interest_onboard;
	}
	public int getInterest_left() {
		return interestleft;
	}



	/**
	 * @return the activePlayer
	 */
	public int getActivePlayer() {
		return activePlayer;
	}

	/**
	 * @param activePlayer the activePlayer to set
	 */
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
	}

	/**
	 * @return the visorTouched
	 */
	public boolean isVisorTouched() {
		return visorTouched;
	}

	/**
	 * @param visorTouched the visorTouched to set
	 */
	public void setVisorTouched(boolean visorTouched) {
		this.visorTouched = visorTouched;
	}

	/**
	 * @return the actionTouched
	 */
	public boolean isActionTouched() {
		return actionTouched;
	}

	/**
	 * @param actionTouched the actionTouched to set
	 */
	public void setActionTouched(boolean actionTouched) {
		this.actionTouched = actionTouched;
	}

	/**
	 * @param person_marker the person_marker to set
	 */
	public void setPerson_marker(int person_marker) {
		this.person_marker = person_marker;
	}

	/**
	 * @param false_alarm_marker the false_alarm_marker to set
	 */
	public void setFalse_alarm_marker(int false_alarm_marker) {
		this.false_alarm_marker = false_alarm_marker;
	}

	/**
	 * @return the currentPhaseState
	 */
	public PhaseStates getCurrentPhaseState() {
		return currentPhaseState;
	}

	/**
	 * @return the saved_person_to_win
	 */
	public int getSaved_person_to_win() {
		return saved_person_to_win;
	}

	/**
	 * @param saved_person_to_win the saved_person_to_win to set
	 */
	public void setSaved_person_to_win(int saved_person_to_win) {
		this.saved_person_to_win = saved_person_to_win;
	}

	/**
	 * @return the dead_person_to_lose
	 */
	public int getDead_person_to_lose() {
		return dead_person_to_lose;
	}

	/**
	 * @param dead_person_to_lose the dead_person_to_lose to set
	 */
	public void setDead_person_to_lose(int dead_person_to_lose) {
		this.dead_person_to_lose = dead_person_to_lose;
	}

	/**
	 * @return the visorshown
	 */
	public boolean isVisorshown() {
		return visorshown;
	}

	/**
	 * @param visorshown the visorshown to set
	 */
	public void setVisorshown(boolean visorshown) {
		this.visorshown = visorshown;
	}



	/* (non-Javadoc)
	 * @see controller.listener.IActionListener#actionSelected()
	 */


}
