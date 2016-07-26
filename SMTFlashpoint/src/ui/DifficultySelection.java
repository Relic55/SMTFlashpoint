/**
 * 
 */
package ui;

import model.GameDifficulty;
import model.SpecialistType;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class DifficultySelection extends Zone {
	private int x_offset,y_offset,width,height, blocksize,textsize;
	private String difficulty;
	private GameDifficulty gameDifficulty;
	
	private static final String DIFFICULTY_BEGINNER="Tutorial";
	private static final String DIFFICULTY_RECRUT="Rekrut";
	private static final String DIFFICULTY_VETERAN="Veteran";
	private static final String DIFFICULTY_HERO="Held";
	
	public DifficultySelection(int x_offset, int y_offset, int blocksize)
	{
		this.x_offset=x_offset;
		this.y_offset=y_offset;
		this.height=blocksize/2;
		this.width=blocksize;
		this.blocksize=blocksize;
		gameDifficulty=GameDifficulty.BEGINNER;
		textsize=(int)(blocksize/90.0*20);
		difficulty=DIFFICULTY_BEGINNER;
		
	}
	public void changeDifficulty(GameDifficulty gameDifficulty)
	{
		this.gameDifficulty=gameDifficulty;
		if(gameDifficulty==GameDifficulty.BEGINNER)
			difficulty=DIFFICULTY_BEGINNER;
		else if(gameDifficulty==GameDifficulty.RECRUT)
			difficulty=DIFFICULTY_RECRUT;
		else if(gameDifficulty==GameDifficulty.VETERAN)
			difficulty=DIFFICULTY_VETERAN;
		if(gameDifficulty==GameDifficulty.HERO)
			difficulty=DIFFICULTY_HERO;
		
	}
	
	@Override
	public void draw()
	{
		pushMatrix();
			//Anzeige unten
			translate(x_offset+blocksize*9/2,y_offset+blocksize*9/2);
			fill(89,89,89);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficulty,width/2,height*5/8);
		popMatrix();
		pushMatrix();
			//Anzeige links
			translate(x_offset+blocksize*4,y_offset+blocksize*7/2);
			rotate(PI/2);
			fill(89,89,89);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficulty,width/2,height*5/8);
		popMatrix();
		pushMatrix();
			//Anzeige oben
			translate(x_offset+blocksize*11/2,y_offset+blocksize*7/2);
			rotate(PI);
			fill(89,89,89);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficulty,width/2,height*5/8);
		popMatrix();
		pushMatrix();
			//Anzeige rechts
			translate(x_offset+blocksize*6,y_offset+blocksize*9/2);
			rotate(PI*3/2);
			fill(89,89,89);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficulty,width/2,height*5/8);
		popMatrix();
		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
	/**
	 * @return the gameDifficulty
	 */
	public GameDifficulty getGameDifficulty() {
		return gameDifficulty;
	}
}
