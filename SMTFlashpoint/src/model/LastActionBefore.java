/**
 * 
 */
package model;

/**
 * @author Relic
 *
 */
public class LastActionBefore {
	private Actiontype type;
	private Player player;
	private Block start, ziel, side; //side : zweiter Block des Ambulanz Ziels
	private Wallblock wall;
	private int spendAP=0,spendSP=0;
	private int[] playerPositions=new int[12];
	private int savedhealpeople, savedpeople,sidesavedhealpeople,sidesavedpeople;

	public LastActionBefore(Actiontype type, Player player, Block start, Block ziel, Wallblock wall)
	{
		this(type, player, start, ziel, wall,0,0,0,0,0,0,0,0,0,0,0,0);
	}
	public LastActionBefore(Actiontype type, Player player, Block start, Block ziel, Wallblock wall,int p0x,int p0y,int p1x,int p1y,int p2x,int p2y,int p3x,int p3y,int p4x,int p4y,int p5x,int p5y)
	{
		this.type=type;
		this.player=player;
		this.start=start;
		this.ziel=ziel;
		this.wall=wall;
		this.playerPositions[0]=p0x;
		this.playerPositions[1]=p0y;
		this.playerPositions[2]=p1x;
		this.playerPositions[3]=p1y;
		this.playerPositions[4]=p2x;
		this.playerPositions[5]=p2y;
		this.playerPositions[6]=p3x;
		this.playerPositions[7]=p3y;
		this.playerPositions[8]=p4x;
		this.playerPositions[9]=p4y;
		this.playerPositions[10]=p5x;
		this.playerPositions[11]=p5y;
		
		side=null;
		savedhealpeople=0; 
		savedpeople=0;
		sidesavedhealpeople=0;
		sidesavedpeople=0;
	}
	
	public void setAP(int anz)
	{
		spendAP=anz;
	}
	public void setSP(int anz)
	{
		spendSP=anz;
	}
	/**
	 * @return the type
	 */
	public Actiontype getType() {
		return type;
	}
	/**
	 * @return the player
	 */
	public Player getPlayer() {
		return player;
	}
	/**
	 * @return the start
	 */
	public Block getStart() {
		return start;
	}
	/**
	 * @return the ziel
	 */
	public Block getZiel() {
		return ziel;
	}
	public Block getSide() {
		return side;
	}
	/**
	 * @return the wall
	 */
	public Wallblock getWall() {
		return wall;
	}
	/**
	 * @return the spendAP
	 */
	public int getSpendAP() {
		return spendAP;
	}
	/**
	 * @return the spendSP
	 */
	public int getSpendSP() {
		return spendSP;
	}
	/**
	 * @return the playerPositions
	 */
	public int[] getPlayerPositions() {
		return playerPositions;
	}
	/**
	 * @return the savedhealpeople
	 */
	public int getSavedhealpeople() {
		return savedhealpeople;
		
	}
	public int getSavedhealpeopleSide() {
		return sidesavedhealpeople;
	}
	/**
	 * @param savedhealpeople the savedhealpeople to set
	 */
	public void setSavedhealpeople(int savedhealpeople) {
		this.savedhealpeople = savedhealpeople;
	}
	public void setSavedhealpeopleSide(int savedhealpeople) {
		this.sidesavedhealpeople = savedhealpeople;
	}
	/**
	 * @return the savedpeople
	 */
	public int getSavedpeople() {
		return savedpeople;
	}
	public int getSavedpeopleSide() {
		return sidesavedpeople;
	}
	/**
	 * @param savedpeople the savedpeople to set
	 */
	public void setSavedpeople(int savedpeople) {
		this.savedpeople = savedpeople;
	}
	public void setSavedpeopleSide(int savedpeople) {
		this.sidesavedpeople = savedpeople;
	}
	/**
	 * @param ziel the ziel to set
	 */
	public void setZiel(Block ziel) {
		this.ziel = ziel;
	}
	public void setSide(Block side) {
		this.ziel = side;
	}
}
