import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
/**
 * 
 * @author Michael Zhang
 * The game runner. Place all game testers and such here.
 *
 * ~~CLEANING UP CODE PROCESS~~ 
 * TODO:
 * Remove references to Engine in Game GUi
 * Remove reference to Engine in gameboardpanel
 * add references to gameboardpanel in game GUI
 * add references to gamegui in engine
 * ~~MIKE ZHANG~~
 * 
 */

public class Game {
	public static void main(String[] args) throws java.io.FileNotFoundException{
		Army north = new Army(Color.RED, "iLCroga");
		Army south = new Army(Color.BLUE, "iLNary");
		//Army east = new Army(Color.ORANGE, "iLFaryn");
		//Are we going to add the team selection process to the game?
		//I think it's fine if we just leave it in code.
		ArrayList<Army> teams = new ArrayList<Army>();
		teams.add(north);
		teams.add(south);
		//teams.add(east);
		GuiFrame gui = new GuiFrame();
		Engine engine = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"), teams, 
				gui);
		gui.setVisible(true);		
	}
}
