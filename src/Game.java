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
		Army north = new Army(Color.RED, "Akhil");
		Army south = new Army(Color.BLUE, "Sid");
		ArrayList<Army> teams = new ArrayList<Army>();
		teams.add(north);
		teams.add(south);
		
		Engine engine = new Engine(new File("resources/Countries.txt"), 
				   new File("resources/Neighbors.txt"), 
				   new File("resources/Continents.txt"), teams); 
		GuiFrame gui = new GuiFrame(engine);
		gui.setVisible(true);
		
		engine.countries.get(0).occupy(engine.armies.get(0));
	}
}
