import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;

public class GameText {
	public static void main(String[] args) throws InterruptedException {
		
		
		Country america = new Country("America", 50, 50, "resources/Country Images/Eastern United States");
		Army north = new Army(Color.RED, "Akhil");
		america.occupy(north);
		america.troops += 5;
		Country mexico = new Country("Mexico", 100, 100, "resources/Country Images/Central America");
		Army south = new Army(Color.BLUE, "Sid");
		mexico.occupy(south);
		mexico.troops += 3;
		america.addNeighbors(new ArrayList<Country>(Arrays.asList(mexico)));
		mexico.addNeighbors(new ArrayList<Country>(Arrays.asList(america)));
		System.out.println(america + "\n" + mexico + "\n");
		mexico.nuke(america);
		System.out.println(america + "\n" + mexico + "\n");
	}
}