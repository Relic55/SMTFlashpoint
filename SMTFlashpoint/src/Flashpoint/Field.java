package Flashpoint;


import processing.core.*;
import vialab.SMT.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

//import main.java.Bomberman.UserArea;

//import main.java.Bomberman.Blocks;
//import main.java.Bomberman.Bomberman;

public class Field extends PApplet {

	private Integer vertical_blocks = 8;
	private Integer block_size = null;
	private Integer horizontal_blocks = 10;
	private Integer x_offset = null;
	private Integer y_offset = null;
	private Properties prop;
	public String pic_path="/pictures";
	//private Blocks blocks;
	
	//public Firefighter f1, f2, f3, f4, f5, f6;
	
	public void setup() {

		size(displayWidth, displayHeight, SMT.RENDERER);
		SMT.init( this, TouchSource.AUTOMATIC);

		block_size = displayHeight/(vertical_blocks+6);

		int panel_width = 6*block_size;
		int panel_height = 3*block_size;



		y_offset = (height - vertical_blocks*block_size)/2;
		x_offset = (width - horizontal_blocks*block_size)/2;

		Zone board = new Zone("Myboard");
		//board.translate(x_offset,y_offset);
		
		SMT.add(board);
	
	}
	void drawMyboard(Zone board){
		  rect(x_offset, y_offset,horizontal_blocks*block_size, vertical_blocks*block_size);
		}
	
	
	
	
	
}

