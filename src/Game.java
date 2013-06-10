import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
/**
 * 
 * @author Michael Zhang
 * The game runner. Place all game testers and such here.
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
		Engine engine = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"), teams);
		GuiFrame gui = new GuiFrame(engine);
		gui.setVisible(true);		
	}
}
