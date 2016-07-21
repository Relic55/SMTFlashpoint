/**
 * 
 */
package ui;

import controller.GameEngine;
import model.Block;
import processing.core.PImage;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class BlockVisual extends Zone{
	private Block block;
	private GameEngine g;
	private int size, x_offset, y_offset;
	private String pic_path;
	private PImage fireimage;
	private PImage smokeimage;
	private PImage interestimage;
	private PImage dangerimage;
	private PImage seatimage;
	private PImage closeddoorimage;
	private PImage opendoorimage;
	private PImage catimage;
	private PImage healoverlayimage;
	private PImage firetruckimage;
	private PImage ambulanceimage;
	
	public BlockVisual(GameEngine g,String pic_path, Block block)
	{
		this.g=g;
		this.pic_path=pic_path;
		this.block=block;
	}

}
