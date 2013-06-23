import java.io.File;
import java.io.IOException;
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
	public static void main(String[] args) throws IOException, InterruptedException{
		GuiFrame gui = new GuiFrame();
		Engine engine = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"), gui);
		engine.start();
		gui.setVisible(true);
		gui.messages.setVisible(true);
	}
}
