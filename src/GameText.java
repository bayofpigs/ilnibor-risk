public class GameText {
	public static void main(String[] args) throws InterruptedException {
		Country alpha = new Country("America");
		alpha.occupy(0);
		alpha.troops += 5;
		Country beta = new Country("Mexico");
		beta.occupy(1);
		beta.troops += 3;
		System.out.println(alpha + "\n" + beta + "\n");
		beta.nuke(alpha);
		System.out.println(alpha + "\n" + beta + "\n");
	}
}