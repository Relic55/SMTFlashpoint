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
import model.Background;
import model.Block;
import model.Color;
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
	private model.Player active_firefighter;
	private model.Action move= new model.Action(model.Actiontype.MOVE);
	private model.Action move_to_fire= new model.Action(model.Actiontype.MOVE_TO_FIRE);
	private model.Action move_carry_person= new model.Action(model.Actiontype.MOVE_CARRY_PERSON);
	private model.Action move_with_healed_person= new model.Action(model.Actiontype.MOVE_WITH_HEALED_PERSON);	
	private model.Action move_caryy_and_healed= new model.Action(model.Actiontype.MOVE_CARRY_AND_HEALED);
	private model.Action transport_danger= new model.Action(model.Actiontype.TRANSPORT_DANGER);
	private model.Action extinquish_fire= new model.Action(model.Actiontype.EXTINQUISH_FIRE);
	private model.Action extinquish_smoke= new model.Action(model.Actiontype.EXTINQUISH_SMOKE);	
	private model.Action heal_person= new model.Action(model.Actiontype.HEAL_PERSON);
	private model.Action remove_danger= new model.Action(model.Actiontype.REMOVE_DANGER);
	private model.Action identify= new model.Action(model.Actiontype.IDENTIFY);
	private model.Action control_firefighter= new model.Action(model.Actiontype.CONTROL_FIREFIGHTER);	
	private model.Action move_ambulance= new model.Action(model.Actiontype.MOVE_AMBULANCE);
	private model.Action move_firetruck= new model.Action(model.Actiontype.MOVE_FIRETRUCK);
	private model.Action use_firetruck= new model.Action(model.Actiontype.USE_FIRETRUCK);
	private model.Action open_door= new model.Action(model.Actiontype.OPEN_DOOR);	
	private model.Action close_door= new model.Action(model.Actiontype.CLOSE_DOOR);
	private model.Action damage_wall= new model.Action(model.Actiontype.DAMAGE_WALL);
	private model.Action cancel= new model.Action(model.Actiontype.CANCEL);	
	
	
	
	
	
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
		
		// Since we created the Level objects in the loadResources() method we can now add the Engine as a listener to each one.
		initLevelListeners();
		//initActions();
		init_measures();
		init_board();
		
		for (int i=0;i<vertical_blocks;i++) {
			  for (int j=0;j<horizontal_blocks;j++) 
			  {
			      board[i][j]= new Block(); 
			      }
			}
			
		init_blocks(json_path+"/block_start_json.json");
		init_player();

		
		
	}
	
	/**
	 * 
	 */


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
		


		y_offset = (AppInjector.application().displayHeight - vertical_blocks*block_size)/4;
		x_offset = (AppInjector.application().displayWidth - horizontal_blocks*block_size)/4;
		
				
	}
	public void init_board()
	{
		background=new Background(backgroundpic,x_offset,y_offset,board_width,board_height );
		SMT.add(background);
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
    	//System.out.println(boardobj.getInt("x")+ " " +boardobj.getInt("y")+ " " +boardobj.getBoolean("inside_Block")+ " " +boardobj.getBoolean("smoke")+ " " + boardobj.getBoolean("fire")+ " " +boardobj.getInt("danger")+ " " +boardobj.getBoolean("interest")+ " " +boardobj.getBoolean("seat")+ " " + boardobj.getInt("people")+ " " + boardobj.getInt("healed_people"));
    	//System.out.println(board[0][0].getHealed_people());
    	//board[boardobj.getInt("x")][boardobj.getInt("y")]=new Block(boardobj.getBoolean("inside_Block")  , boardobj.getBoolean("smoke"), boardobj.getBoolean("fire"), boardobj.getInt("danger"), boardobj.getBoolean("interest"), boardobj.getBoolean("seat"), boardobj.getInt("people"), boardobj.getInt("healed_people"), null, null, null, null);
    	//System.out.println(board[0][0].isInside_Block());
    	board[boardobj.getInt("x")][boardobj.getInt("y")].set_all(boardobj.getBoolean("inside_Block")  , boardobj.getBoolean("smoke"), boardobj.getBoolean("fire"), boardobj.getInt("danger"), boardobj.getBoolean("interest"), boardobj.getBoolean("seat"), boardobj.getInt("people"), boardobj.getInt("healed_people"), null, null, null, null);
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
    		else if(direction ==1) //horizontal
    		{
    			wall=new Wallblock(direction, walltype );
    			board[x1][y1].setEast(wall);
    			board[x2][y2].setWest(wall);
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
		ff1=new Player(this);
		ff1.setplayer(SpecialistType.DUMMY, Color.GREEN, 4, 0, 0, 0);
		SMT.add(ff1);
		
	}
	
}
