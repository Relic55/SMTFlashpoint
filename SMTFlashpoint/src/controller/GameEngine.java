/**
 * This file is part of the BMBF funded project <B>Nebeneinander wird Miteinander</B>
 * at Technische Universit√§t Chemnitz.
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

import controller.listener.ILevelListener;
import model.Actiontype;
import model.Action;
import model.Background;
import model.Block;
import model.Gamedifficulty;
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
	private Player ff0,ff1,ff2,ff3,ff4,ff5;
	
	PImage backgroundpic= Utility.getImage(pic_path+ "/Spielplan_groﬂ.gif");
	
	private Background background;
	//background.draw();
	
	private int panel_width;
	private int panel_height;
	private int board_width;
	private int board_height;
	
	public model.Player[] playerbase=new model.Player[6];
	private int playercount;
	private int saved_person;
	private int dead_person;
	private int buildingdamage=0;
	private int maxbuildingdamage=24;
	private int person_marker;
	private int false_alarm_marker;
	
	private Gamedifficulty difficulty;
	
	private Player active_firefighter;
	private Action move= new Action(model.Actiontype.MOVE);
	private Action move_to_fire= new Action(model.Actiontype.MOVE_TO_FIRE);
	private Action move_carry_person= new Action(model.Actiontype.MOVE_CARRY_PERSON);
	private Action move_with_healed_person= new Action(model.Actiontype.MOVE_WITH_HEALED_PERSON);	
	private Action move_caryy_and_healed= new Action(model.Actiontype.MOVE_CARRY_AND_HEALED);
	private Action transport_danger= new Action(model.Actiontype.TRANSPORT_DANGER);
	private Action extinquish_fire= new Action(model.Actiontype.EXTINQUISH_FIRE);
	private Action extinquish_smoke= new Action(model.Actiontype.EXTINQUISH_SMOKE);	
	private Action heal_person= new Action(model.Actiontype.HEAL_PERSON);
	private Action remove_danger= new Action(model.Actiontype.REMOVE_DANGER);
	private Action identify= new Action(model.Actiontype.IDENTIFY);
	private Action control_firefighter= new Action(model.Actiontype.CONTROL_FIREFIGHTER);	
	private Action move_ambulance= new Action(model.Actiontype.MOVE_AMBULANCE);
	private Action move_firetruck= new Action(model.Actiontype.MOVE_FIRETRUCK);
	private Action use_firetruck= new Action(model.Actiontype.USE_FIRETRUCK);
	private Action open_door= new Action(model.Actiontype.OPEN_DOOR);	
	private Action close_door= new Action(model.Actiontype.CLOSE_DOOR);
	private Action damage_wall= new Action(model.Actiontype.DAMAGE_WALL);
	private Action cancel= new Action(model.Actiontype.CANCEL);	
	
	
	
	
	
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
		saved_person=0;
		dead_person=0;
		buildingdamage=0;	
		// Since we created the Level objects in the loadResources() method we can now add the Engine as a listener to each one.
		initLevelListeners();
		//initActions();
		init_measures();
		init_board();
		
		for (int i=0;i<vertical_blocks;i++) {
			  for (int j=0;j<horizontal_blocks;j++) 
			  {
			      board[i][j]= new Block(this, pic_path); 
			      AppInjector.zoneManager().add(board[i][j]);
			      }
			}
			
		init_blocks(json_path+"/block_start_json.json");
		init_choosing();
		init_beginningfire();
		init_player();

		
		
	}
	
	/**
	 * 
	 */
	private void init_choosing() {
		// TODO Auto-generated method stub
		//Spieler w‰hlen Spielfiguren und Schwierigkeitsgrad
		//Testwerte:
		playercount=6;
		difficulty= Gamedifficulty.BEGINNER;
		
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
		 
		if(difficulty == Gamedifficulty.BEGINNER)
		{
			person_marker=10;
			false_alarm_marker=5;	
			board[1][6].setFire(true);
			board[1][7].setFire(true);
			
			explosion(1,6);
			explosion(1,6);
			explosion(1,6);
			explosion(1,6);
			explosion(1,6);
			
			/*
			 * board[2][2].setFire(true);
			board[2][3].setFire(true);
			board[3][2].setFire(true);
			board[3][3].setFire(true);
			board[3][4].setFire(true);
			board[3][5].setFire(true);
			board[4][4].setFire(true);
			board[5][6].setFire(true);
			board[5][7].setFire(true);
			board[6][6].setFire(true);
			
			board[6][7].setSmoke(true);
			
			board[2][4].setInterest(true);
			board[5][1].setInterest(true);
			board[5][8].setInterest(true);
			*/
		}
		if(difficulty == Gamedifficulty.RECRUT)
		{
			int explosion_count= 3;
		}
		
		
		
	}
	
	private void explosion(int x, int y)
	{
		//in alle 4 Richtungen einzeln abpr¸fen
		//Norden
		boolean extention=true;
		int i=x, j=y;
		while (extention)
		{
			Wallblock wall=board[i][j].getNorth();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i-1][j].isFire())
				{
					board[i-1][j].setFire(true);
					extention=false;
				}
				else
					i--;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					extention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					extention=false;
				}
			}
		}
		
		
		//Sueden
		extention=true;
		i=x; j=y;
		while (extention)
		{
			Wallblock wall=board[i][j].getSouth();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i+1][j].isFire())
				{
					board[i+1][j].setFire(true);
					extention=false;
				}
				else
					i++;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					extention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					extention=false;
				}
			}
		}
		
		//Osten
		
		extention=true;
		i=x; j=y;
		while (extention)
		{
			Wallblock wall=board[i][j].getEast();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i][j+1].isFire())
				{
					board[i][j+1].setFire(true);
					extention=false;
				}
				else
					j++;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					extention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					extention=false;
				}
			}
		}
		//Westen
		
		extention=true;
		i=x; j=y;
		while (extention)
		{
			Wallblock wall=board[i][j].getWest();
			if(wall==null||wall.passage_Wall()) //keine Behinderung in diese Richtung
			{
				if(!board[i][j-1].isFire())
				{
					board[i][j-1].setFire(true);
					extention=false;
				}
				else
					j--;
			}
			else //Wand beschaedigen
			{
				if(wall.getWall()==Walltype.BOARDEND)
					extention=false;
				else
				{
					int dmg=wall.damage_Wall();
					buildingdamage+=dmg;
					extention=false;
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
	public GameStates getCurrentGameState() {
		return currentGameState;
	}
	
	public void setCurrentGameState(GameStates state) {
		this.currentGameState = state;
	}
	
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
		//test mit ff1 als gruen
		ff0=new Player(this);
		ff0.setplayer(SpecialistType.DUMMY, PlayerColor.GREEN, 4, 0, 0, 0);
		AppInjector.zoneManager().add(ff0);
		ff1=new Player(this);
		ff1.setplayer(SpecialistType.DUMMY, PlayerColor.WHITE, 4, 0, 4, 0);
		AppInjector.zoneManager().add(ff1);
		ff2=new Player(this);
		ff2.setplayer(SpecialistType.DUMMY, PlayerColor.RED, 4, 0, 0, 3);
		AppInjector.zoneManager().add(ff2);
		ff3=new Player(this);
		ff3.setplayer(SpecialistType.DUMMY, PlayerColor.YELLOW, 4, 0, 7, 5);
		AppInjector.zoneManager().add(ff3);
		ff4=new Player(this);
		ff4.setplayer(SpecialistType.DUMMY, PlayerColor.BLUE, 4, 0, 3, 7);
		AppInjector.zoneManager().add(ff4);
		ff5=new Player(this);
		ff5.setplayer(SpecialistType.DUMMY, PlayerColor.ORANGE, 4, 0, 5, 7);;
		AppInjector.zoneManager().add(ff5);
		
	}

}
