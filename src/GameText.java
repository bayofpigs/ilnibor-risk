public class GameText {
	public static void main(String[] args) throws InterruptedException {
		Country america = new Country("America");
		america.occupy(0);
		america.troops += 5;
		Country mexico = new Country("Mexico");
		mexico.occupy(1);
		mexico.troops += 3;
		System.out.println(america + "\n" + mexico + "\n");
		mexico.nuke(america);
		System.out.println(america + "\n" + mexico + "\n");
	}
}