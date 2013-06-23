import java.io.File;
import java.io.IOException;
/**
 * 
 * @author Michael Zhang
 * The game runner. Place all game testers and such here.
 * 
 */

public class Game {
	public static void main(String[] args) throws IOException, InterruptedException{
		Engine engine = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"));
		engine.start();
	}
}
