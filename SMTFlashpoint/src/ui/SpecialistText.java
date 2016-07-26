/**
 * 
 */
package ui;

import model.SpecialistType;
import vialab.SMT.Zone;

/**
 * @author Relic
 *
 */
public class SpecialistText extends Zone {
	private int x,y,width,height,playerNumber, blocksize,textsize;
	//SpecialistType specialist;
	private String specitext;
	
	private static final String TEXT_NONE="Kein Spezialist gewählt.\nBitte einen Spezialisten wählen,\num mitzuspielen. ";
	private static final String TEXT_ALLESKOENNER="Alleskönner \n 5 AP pro Runde \n\nKeine besonderen Fähigkeiten \noder Aktionen ";
	private static final String TEXT_EINSATZLEITER="Einsatzleiter (Fähigkeit NICHT eingebunden) \n4 AP pro Runde + 2 freie Befehls –AP (SP)\nBEFEHLEN: Der Einsatzleiter kann seine AP dazu \nverwenden, andere Feuerwehrleute zu bewegen. \nDer Einsatzleiter muss dieselbe Anzahl an AP für diese \nAktionen bezahlen, die ein Feuerwehrmann normaler-\nweise dafür bezahlen muss.\nFreie Befehls-AP dürfen nicht aufgespart werden.";
	private static final String TEXT_FAHRZEUGMASCHINIST="Fahrzeugmaschinist \n(Fähigkeit NICHT eingebunden)\n 4 AP pro Runde\nLÖSCHMONITOR EINSETZEN: 2AP\nWenn der Fahrzeugmaschinist den Lösch-\nmonitor einsetzt, darf er mit einem oder \nmit beiden Würfeln erneut würfeln. \nDer zweite Wurf zählt.";
	private static final String TEXT_GEFAHRSTOFFSPEZIALIST="Gefahrstoffspezialist \n4 AP pro Runde \nENTSORGEN: 2 AP \nDer Gefahrspezialist kann einen \nGefahrstoff auf seinem Feld entfernen.";
	private static final String TEXT_LOESCHSCHAUMSPEZIALIST="Löschschaumspezialist \n 3 AP pro Runde + \n3 freie AP zum Feuerlöschen (SP)\nDie freien AP zum Feuerlöschen \nkönnen nicht aufgespart werden.";
	private static final String TEXT_RETTUNGSSANITAETER="Rettungssanitäter\n 4 AP pro Runde\nBEHANDELN: 1 AP\nEine behandelte Person kann von einem \nFeuerwehrmann mitgenommen werden, \nohne dass sie getragen werden muss. \n Der Rettungssanitäter muss die doppelte Anzahl an \nAP zahlen, um Feuer oder Rauch zu löschen!";
	private static final String TEXT_RETTUNGSSPEZIALIST="Rettungsspezialist\n 4 AP pro Runde + 3 freie Bewegungs-AP (SP)\nEinschlagen (Wand): 1 AP\nDer Rettungsspezialist muss die \ndoppelte Anzahl an AP zahlen, \num Feuer oder Rauch zu löschen.\nDie freien Bewegungsaktionspunkte \nkönnen nicht aufgespart werden.";
	private static final String TEXT_WAERMEBILDKAMERA="Spezialist mit Wärmebildkamera\n 4 AP pro Runde\nIDENTIFIZIEREN: 1 AP\nDer Spezialist mit Wärmebildkamera \ndarf einen beliebigen Einsatzmarker \nauf dem Spielplan umdrehen.";
	
	
	public SpecialistText(int x, int y, int width, int height, int playerNumber, SpecialistType specialist, int blocksize)
	{
		super(x,y,width,height);
		this.x=x;
		this.y=y;
		this.height=height;
		this.width=width;
		this.playerNumber=playerNumber;
		this.blocksize=blocksize;
		textsize=(int)(blocksize/90.0*14);
		//TODO: Texte der Specialisten einfügen
		specitext= TEXT_NONE;
		
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
	
	public void setSpecialist(SpecialistType specialist)
	{
		textsize=(int)(blocksize/90.0*14);
		specitext= TEXT_NONE;
		if(specialist==SpecialistType.ALLESKOENNER)
			specitext= TEXT_ALLESKOENNER;
		else if(specialist==SpecialistType.EINSATZLEITER)
		{
			textsize=(int)(blocksize/90.0*10);
			specitext=TEXT_EINSATZLEITER;
		}
		else if(specialist==SpecialistType.FAHRZEUGMASCHINIST)
		{
			textsize=(int)(blocksize/90.0*12);
			specitext=TEXT_FAHRZEUGMASCHINIST;
		}
		else if(specialist==SpecialistType.GEFAHRSTOFFSPEZIALIST)
		{
			textsize=(int)(blocksize/90.0*14);
			specitext=TEXT_GEFAHRSTOFFSPEZIALIST;
		}
		else if(specialist==SpecialistType.LOESCHSCHAUMSPEZIALIST)
		{
			textsize=(int)(blocksize/90.0*14);
			specitext=TEXT_LOESCHSCHAUMSPEZIALIST;
		}
		else if(specialist==SpecialistType.RETTUNGSSANITAETER)
		{
			textsize=(int)(blocksize/90.0*10);
			specitext=TEXT_RETTUNGSSANITAETER;
		}
		else if(specialist==SpecialistType.RETTUNGSSPEZIALIST)
		{
			textsize=(int)(blocksize/90.0*10);
			specitext=TEXT_RETTUNGSSPEZIALIST;
		}
		else if(specialist==SpecialistType.SPEZIALIST_MIT_WAERMEBILDKAMERA)
		{
			textsize=(int)(blocksize/90.0*14);
			specitext=TEXT_WAERMEBILDKAMERA;
		}
	}
	
	@Override
	public void draw()
	{
		fill(0);
		rect(0,0,width,height);
		textAlign(CENTER);
		fill(255);
		textSize(textsize);
		text(specitext,width/2,height/8);

		
	}
	
	@Override
	public void touch()
	{
		rst(false,false,false,false);
	}
}
