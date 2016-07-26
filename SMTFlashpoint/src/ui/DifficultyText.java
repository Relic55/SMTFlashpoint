/**
 * 
 */
package ui;

import model.GameDifficulty;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class DifficultyText extends Zone{
	private int x_offset,y_offset,width,height, blocksize,textsize;
	private String difficultyText;
	
	private static final String DIFFICULTY_TEXT_BEGINNER="Keine Spezialisten, Keine Fahrzeuge, keine Gefahrenstoffe\n keine Brandherde, Feuer zu Beginn an festen Positionen\n (bitte einen beliebigen Spezialisten wählen, \num zu zeigen, dass man mitspielen möchte)";
	private static final String DIFFICULTY_TEXT_RECRUT="Rekrutentext";
	private static final String DIFFICULTY_TEXT_VETERAN="Veterantext";
	private static final String DIFFICULTY_TEXT_HERO="Heldtext";
	
	public DifficultyText(int x_offset, int y_offset, int blocksize)
	{
		this.x_offset=x_offset;
		this.y_offset=y_offset;
		this.height=blocksize;
		this.width=blocksize*4;
		this.blocksize=blocksize;
		textsize=(int)(blocksize/90.0*12);
		difficultyText=DIFFICULTY_TEXT_BEGINNER;
		
	}
	
	
	
	
	public void changeDifficultyText(GameDifficulty gameDifficulty)
	{
		if(gameDifficulty==GameDifficulty.BEGINNER)
			difficultyText=DIFFICULTY_TEXT_BEGINNER;
		else if(gameDifficulty==GameDifficulty.RECRUT)
			difficultyText=DIFFICULTY_TEXT_RECRUT;
		else if(gameDifficulty==GameDifficulty.VETERAN)
			difficultyText=DIFFICULTY_TEXT_VETERAN;
		if(gameDifficulty==GameDifficulty.HERO)
			difficultyText=DIFFICULTY_TEXT_HERO;
		
	}
	
	
	@Override
	public void draw()
	{
		pushMatrix();
			//Anzeige unten
			translate(x_offset+blocksize*3,y_offset+blocksize*5);
			fill(150,150,150);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficultyText,width/2,height/7);
		popMatrix();
		pushMatrix();
			//Anzeige links
			translate(x_offset+blocksize*3,y_offset+blocksize*2);
			rotate(PI/2);
			fill(150,150,150);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficultyText,width/2,height/7);
		popMatrix();
		pushMatrix();
			//Anzeige oben
			translate(x_offset+blocksize*7,y_offset+blocksize*3);
			rotate(PI);
			fill(150,150,150);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficultyText,width/2,height/7);
		popMatrix();
		pushMatrix();
			//Anzeige rechts
			translate(x_offset+blocksize*7,y_offset+blocksize*6);
			rotate(PI*3/2);
			fill(150,150,150);
			rect(0,0,this.width,this.height);
			textAlign(CENTER);
			fill(0);
	        textSize(textsize);
	        text(difficultyText,width/2,height/7);
		popMatrix();
		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
}
