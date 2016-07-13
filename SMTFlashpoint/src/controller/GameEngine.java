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

import java.util.Random; //Zufallszahlen

import controller.listener.ILevelListener;
import model.Actiontype;
import model.Action;
import model.Background;
import model.Block;
import model.GameDifficulty;
import model.PlayerColor;
import model.Playerzone;
import model.Level;
import model.Player;
import model.SpecialistType;
import model.Statusoverview;
import model.VisorBlock;
import model.Wallblock;
import model.Walltype;
import processing.core.PImage;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
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
public class GameEngine implements ILevelListener {
	
	//Beginn Flashpoint Umsetzung
	
	
	private int vertical_blocks = 8;
	private int block_size;
	private int horizontal_blocks = 10;
	private int x_offset;
	private int y_offset;
	
	//private Properties prop;
	public String pic_path="./pictures";
	public String json_path="./jsonfiles";
	private Integer mapnumber;
	public Block[][] board =new Block[vertical_blocks][horizontal_blocks];
	private Player ff0=null,ff1=null,ff2=null,ff3=null,ff4=null,ff5=null;
	private Playerzone ffz0=null,ffz1=null,ffz2=null,ffz3=null,ffz4=null,ffz5=null;
	private Statusoverview stat;
	
	PImage backgroundpic= Utility.getImage(pic_path+ "/Spielplan_groß.gif");
	
	private Background background;
	//background.draw();
	
	private int panel_width;
	private int panel_height;
	private int board_width;
	private int board_height;
	
	private int activePlayer;
	public Player[] playerbase=new Player[6];
	private int playercount;			//Anzahl aktiver Spieler
	private int saved_person=0;			//Anzahl geretteter Personen
	private int dead_person=0;			//Anzahl verstorbener Personen
	private int buildingdamage=0;		
	private int maxbuildingdamage=24;
	private int person_marker=10;			//Anzahl Personen im Spiel insgesamt
	private int false_alarm_marker=5;		//Anzahl falscher Alarme insgesamt
	private int interestleft;
	private int active_seats;			//aktive Brandherde
	private int inactive_seats;			//weitere Brandherde
	private int interest_onboard=0;		//Anzahl Einsatzmarker auf dem Spielfeld
	private Random rand;				//Objekt fuer Zufallszahlen
	
	private GameDifficulty difficulty;
	public boolean didwewin=false;
	public boolean GameActive=true;
	
	
	public VisorBlock[] visorfield=new VisorBlock[15];
	//0=N, 1=O, 2=S, 3=W, 4=eigenes Feld
	
	
	private Player active_firefighter;
	
	private int actionnumber=21;
	private Action[] Actionfield=new Action[actionnumber];
	private boolean[] possibleactions=new boolean[actionnumber];
	//private Action[] possibleactions=new Action[actionnumber];

	
	
	private GameStates currentGameState;
	
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

	
	
	public GameEngine() {
		levelMap = new HashMap<Integer, Level>();
		resources = loadResources();
		currentLevel = 1;

		currentGameState=GameStates.STATE_START;
		saved_person=0;
		dead_person=0;
		buildingdamage=0;	
		interestleft=person_marker+false_alarm_marker;
		// Since we created the Level objects in the loadResources() method we can now add the Engine as a listener to each one.
		initLevelListeners();
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
		
		
		Actionfield[0]= new Action(model.Actiontype.MOVE);
		Actionfield[1]= new Action(model.Actiontype.MOVE_TO_FIRE);
		Actionfield[2]= new Action(model.Actiontype.MOVE_CARRY_PERSON);
		Actionfield[3]= new Action(model.Actiontype.MOVE_WITH_HEALED_PERSON);	
		Actionfield[4]= new Action(model.Actiontype.MOVE_CARRY_AND_HEALED);
		Actionfield[5]= new Action(model.Actiontype.TRANSPORT_DANGER);
		Actionfield[6]= new Action(model.Actiontype.TRANSPORT_DANGER_AND_HEALED);
		Actionfield[7]= new Action(model.Actiontype.EXTINQUISH_FIRE);
		Actionfield[8]= new Action(model.Actiontype.EXTINQUISH_SMOKE);
		Actionfield[9]= new Action(model.Actiontype.EXTINQUISH_STEP);
		Actionfield[10]= new Action(model.Actiontype.MOVE_AMBULANCE);
		Actionfield[11]= new Action(model.Actiontype.MOVE_FIRETRUCK);
		Actionfield[12]= new Action(model.Actiontype.USE_FIRETRUCK);
		
		Actionfield[13]= new Action(model.Actiontype.HEAL_PERSON);
		Actionfield[14]= new Action(model.Actiontype.REMOVE_DANGER);
		Actionfield[15]= new Action(model.Actiontype.IDENTIFY);
		Actionfield[16]= new Action(model.Actiontype.CONTROL_FIREFIGHTER);	

		Actionfield[17]= new Action(model.Actiontype.OPEN_DOOR);	
		Actionfield[18]= new Action(model.Actiontype.CLOSE_DOOR);
		Actionfield[19]= new Action(model.Actiontype.DAMAGE_WALL);
		Actionfield[20]= new Action(model.Actiontype.CANCEL);
		
	}

	public void showPossibleActions(int type, Block start, Block ziel, Wallblock wall1)
	{
		int count=0;
		for(int i=0;i<actionnumber;i++) 
		{
			if(i>=0&&i<13)		//Blockaktionen von jedem Firefighter
			{
				if(type==1)
				
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);


				
			}
			
			else if(i>=17&&i<20)  //Wallaktionen
			{
				if(type==2||type==3)
					possibleactions[i]=Actionfield[i].wallaction_possible(wall1);

			}
			else if(i==13)		//Heilen darf nur der Rettungssanitäter
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.RETTUNGSSANITAETER)
				{
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);
				}
				
			}
			else if(i==14)		//Gefahr entfernen darf nur der Gefahrstoffspezialist
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.GEFAHRSTOFFSPEZIALIST)
				{
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);
				}
				
			}
			else if(i==15)		//Identifizieren darf nur der Spezialist mit Wärmekamera
			{
				if(type==1&&playerbase[activePlayer].getSpecialist()==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
				{
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);
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
				if(type==1)
				{
					possibleactions[i]=Actionfield[i].action_possible(start, ziel);
				}
				else 
				{
					possibleactions[i]=Actionfield[i].wallaction_possible(wall1);
				}
			}

			if(possibleactions[i])
				count++;
		}
		System.out.println("count: "+count);
		
		
		//Feld mit möglichen AKtionen erzeugen, in denen jeweils die Touchzones erzeugt werden
		Action shownActions[]=new Action[count];
		shownActions[0]=new Action(Actionfield[20].getType(), 200,200,200,200);
		AppInjector.zoneManager().add(shownActions[0]);
		
		
		
	}
	
	
	
	/**
	 * 
	 */
	private void fillVisorfield() {
		// TODO Auto-generated method stub
		int x,y,longside,shortside;
		x=playerbase[activePlayer].getXb();
		y=playerbase[activePlayer].getYb();
		longside=(int)(this.block_size*0.7); //Laenge der Markierung
		shortside= (int)(this.block_size*0.35);
		float factor1=(float)0.15;
		float factor2=(float)0.2;
		
		//Norden
		Wallblock test=board[x][y].getNorth();
		if(test==null||test.passage_Wall())
		{
		
			visorfield[0]=new VisorBlock(this, this.pic_path,1,board[x][y],board[x-1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x-1)*block_size+factor1*block_size), longside,longside);
			AppInjector.zoneManager().add(visorfield[0]);			
		}
		else if(test.getWall()!=Walltype.BOARDEND)
		{
			
			visorfield[0]=new VisorBlock(this, this.pic_path,2, board[x][y],board[x-1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+x*block_size-factor2*block_size), shortside,longside);
			AppInjector.zoneManager().add(visorfield[0]);			
		}
		
		//Osten
		
		test=board[x][y].getEast();
		if(test==null||test.passage_Wall())
		{
		
			visorfield[1]=new VisorBlock(this, this.pic_path,1, board[x][y],board[x][y+1], (float)(x_offset+(y+1)*block_size+factor1*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,longside);
			AppInjector.zoneManager().add(visorfield[1]);			
		}
		else if(test.getWall()!=Walltype.BOARDEND)
		{
			
			visorfield[1]=new VisorBlock(this, this.pic_path,3, board[x][y],board[x][y+1], (float)(x_offset+(y+1)*block_size-factor2*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,shortside);
			AppInjector.zoneManager().add(visorfield[1]);			
		}
		
		//Sueden
		test=board[x][y].getSouth();
		if(test==null||test.passage_Wall())
		{
		
			visorfield[2]=new VisorBlock(this, this.pic_path,1, board[x][y],board[x+1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x+1)*block_size+factor1*block_size), longside,longside);
			AppInjector.zoneManager().add(visorfield[2]);			
		}
		else if(test.getWall()!=Walltype.BOARDEND)
		{
			
			visorfield[2]=new VisorBlock(this, this.pic_path,2, board[x][y],board[x+1][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x+1)*block_size-factor2*block_size), shortside,longside);
			AppInjector.zoneManager().add(visorfield[2]);			
		}
		
		//Westen
		test=board[x][y].getWest();
		if(test==null||test.passage_Wall())
		{
		
			visorfield[3]=new VisorBlock(this, this.pic_path,1, board[x][y],board[x][y-1], (float)(x_offset+(y-1)*block_size+factor1*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,longside);
			AppInjector.zoneManager().add(visorfield[3]);			
		}
		else if(test.getWall()!=Walltype.BOARDEND)
		{
			
			visorfield[3]=new VisorBlock(this, this.pic_path,3, board[x][y],board[x][y-1], (float)(x_offset+y*block_size-factor2*block_size), (float)(y_offset+x*block_size+factor1*block_size), longside,shortside);
			AppInjector.zoneManager().add(visorfield[3]);			
		}
		
		//eigenes Feld loeschbar
		if(board[x][y].isFire()||board[x][y].isSmoke())			
		{
			visorfield[4]=new VisorBlock(this, this.pic_path,1, board[x][y],board[x][y], (float)(x_offset+y*block_size+factor1*block_size), (float)(y_offset+(x)*block_size+factor1*block_size), longside,longside);
			AppInjector.zoneManager().add(visorfield[4]);
			
		}
		//TODO: weitere Aktionsmöglichkeiten abpruefen (aktuelles Feld, Einsatzleiter, Waermebildkamera)
		
		
		
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
		ff0=new Player(this);
		ff0.setplayer(SpecialistType.RETTUNGSSANITAETER, PlayerColor.GREEN, 4, 0, 1, 6);
		
		AppInjector.zoneManager().add(ff0);
		ff1=new Player(this);
		ff1.setplayer(SpecialistType.RETTUNGSSPEZIALIST, PlayerColor.WHITE, 4, 0, 4, 0);
		AppInjector.zoneManager().add(ff1);
		ff2=new Player(this);
		ff2.setplayer(SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA, PlayerColor.RED, 4, 0, 0, 3);
		AppInjector.zoneManager().add(ff2);
		ff3=new Player(this);
		ff3.setplayer(SpecialistType.FAHRZEUGMASCHINIST, PlayerColor.YELLOW, 4, 0, 7, 0);
		AppInjector.zoneManager().add(ff3);
		ff4=new Player(this);
		ff4.setplayer(SpecialistType.GEFAHRSTOFFSPEZIALIST, PlayerColor.BLUE, 4, 0, 7, 7);
		AppInjector.zoneManager().add(ff4);
		ff5=new Player(this);
		ff5.setplayer(SpecialistType.LOESCHSCHAUMSPEZIALIST, PlayerColor.ORANGE, 4, 0, 5, 9);;
		AppInjector.zoneManager().add(ff5);
		
		//Playerzones
		//ffz0=new Playerzone(pic_path,ff0, this, 0,  x_offset-2*block_size, (int)(y_offset+6.5*block_size), 2*block_size, (int) 2.5*block_size);
		ffz0=new Playerzone(pic_path,ff0, this, 0,  (x_offset/2)-block_size,(int)(y_offset/2+3.2*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(ffz0);	
		ffz1=new Playerzone(pic_path,ff1, this, 1, (int)  ((x_offset/2)-block_size*0.65),(int)(y_offset/2+1.5*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(ffz1);
		ffz2=new Playerzone(pic_path,ff2, this, 2,  (x_offset/2),(int)(y_offset/2+0.8*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(ffz2);
		ffz3=new Playerzone(pic_path,ff3, this, 3,  (x_offset/2)+6*block_size,(int)(y_offset/2+0.8*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(ffz3);
		ffz4=new Playerzone(pic_path,ff4, this, 4, (int)  ((x_offset/2)+block_size*5.65),(int)(y_offset/2+2.5*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(ffz4);
		ffz5=new Playerzone(pic_path,ff5, this, 5,  (x_offset/2)+5*block_size,(int)(y_offset/2+3.2*block_size) , 2*block_size, (int) (2.5*block_size));
		AppInjector.zoneManager().add(ffz5);
		
		activePlayer=0;
		playerbase[0]=ff0;
		playerbase[1]=ff1;
		playerbase[2]=ff2;
		playerbase[3]=ff3;
		playerbase[4]=ff4;
		playerbase[5]=ff5;
		
		
		
		
		currentGameState=GameStates.STATE_STARTBOARD;
		
	}

	/**
	 * 
	 */


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
	
	private void initLevelListeners() {
		for(Level level : levelMap.values()) {
			level.addListener(this);
		}
	}
	
	public void loadLevel() {
		loadLevel(currentLevel);
	}
	
	/**
	 * This instructs the app to load a certain level.
	 * @param levelNr
	 */
	public void loadLevel(int levelNr) {
		
		// Instructions for loading a level and its objects go here
		// Consider creating helper classes for loading object information from serialized sources,
		// e.g. XML, CSV, INI, databases, etc
		
		// The Engine should instruct the Controller to load (and prepare) the necessary resources (mainly Zone objects)
		
	}
	
	/**
	 * Implemented via ILevelListener interface. Gets called if the current level is completed.
	 */
	@Override
	public void levelCompleted() {
		this.currentLevel++;
		this.loadLevel(currentLevel);
	}
	
	public int getCurrentLevelNumber() {
		return currentLevel;
	}
	
	public Level getCurrentLevel() {
		return levelMap.get(currentLevel);
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
		background=new Background(backgroundpic,x_offset/2,y_offset/2,board_width,board_height );
		AppInjector.zoneManager().add(background);
		
		
		for (int i=0;i<vertical_blocks;i++) {
			  for (int j=0;j<horizontal_blocks;j++) 
			  {
			      board[i][j]= new Block(this, pic_path); 
			      AppInjector.zoneManager().add(board[i][j]);
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
    	
    }
	
	public void init_blocks(String jsonpath)
	{
		JSONObject obj= Utility.getJSONObjectFromPath(jsonpath);
		fillmatrixFromJSON(obj);

		return;
	}
	
	private void init_player() {
		
		//alle Spielfiguren nacheinander aufs Spielbrett setzen
		
		
	}

	//Getter und Setter:
	
	
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
	 * @return the ff0
	 */
	public Player getFf0() {
		return ff0;
	}

	/**
	 * @return the ff1
	 */
	public Player getFf1() {
		return ff1;
	}

	/**
	 * @return the ff2
	 */
	public Player getFf2() {
		return ff2;
	}

	/**
	 * @return the ff3
	 */
	public Player getFf3() {
		return ff3;
	}

	/**
	 * @return the ff4
	 */
	public Player getFf4() {
		return ff4;
	}

	/**
	 * @return the ff5
	 */
	public Player getFf5() {
		return ff5;
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

}
