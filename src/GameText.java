import java.awt.Color;

public class GameText {
	public static void main(String[] args) throws InterruptedException {
		Country america = new Country("America");
		Army north = new Army(Color.RED, "Akhil");
		america.occupy(north);
		america.troops += 5;
		Country mexico = new Country("Mexico");
		Army south = new Army(Color.BLUE, "Sid");
		mexico.occupy(south);
		mexico.troops += 3;
		System.out.println(america + "\n" + mexico + "\n");
		mexico.nuke(america);
		System.out.println(america + "\n" + mexico + "\n");
	}
}