import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The game engine for the Risk game
 * @author Akhil Velegapudi
 * @version 1.0
 * @see Game
 * @Code_Reviewer Mike Zhang
 *
 *TODO
 *Separate the gameArmies build variable thing from the constructor or at least add a separate setArmies
 *once debugging is over. It would be better to have the user input the number of players and
 *the army names through the GUI.
 *
 */

public class Engine {
	public ArrayList<Country> countries; // The array of countries; to be read from Countries.txt
	public ArrayList<Continent> continents; // The array of continents, to be read from Continents.txt
	public ArrayList<Army> armies; // The array of Armies to be read from input
	public int gameState;
	private Army turn;
	public static final int PRE_GAME = 0, REINFORCE = 1, RECRUIT = 2, ATTACK = 3, OCCUPY = 4, FORTIFY = 5, END_GAME = 6;
	private ArrayList<Integer> riskValues = new ArrayList<Integer>();
	/*
	 * Text versions:
	 * PRE_GAME, OCCUPY = "OCCUPY"
	 * REINFORCE_A, RECRUIT = "REINFORCE"
	 * ATTACK = "ATTACK"
	 * FORTIFY = "FORTIFY"
	 * END_GAME = "GAME OVER"
	 */
	public Engine(File mapCountries, File mapNeighbors, File mapContinents, ArrayList<Army> gameArmies) throws FileNotFoundException{
		// Initialize the array variables
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		armies = gameArmies;
		for (int i = 2; i < 40; i += 2)
			riskValues.add(i);
		for (Army a: armies){
			a.addReinforcements(armies.size());
			a.addRiskValues(riskValues);
		}
		// Fill the countries array with the contents of Countries.txt file
		Scanner a = new Scanner(mapCountries);
		buildCountries(a);
		
		// Add neighbors to each country
		a = new Scanner(mapNeighbors);
		buildNeighbors(a);
		
		// Fill the continents array with the contents of the Continents.txt file
		a = new Scanner(mapContinents);
		buildContinents(a);
		turn = armies.get(0);
		gameState = 0;
	}
	
	/**
	 * Builds the country array
	 * @param in: The scanner containing the file with the country data
	 */
	public void buildCountries(Scanner in){
		int a = Integer.parseInt(in.nextLine());
		for (int i = 0; i < a; i ++)
			countries.add(new Country(in.nextLine(), Integer.parseInt(in.nextLine()), Integer.parseInt(in.nextLine())));
	}
	
	/**
	 * Sets the neighbors of each country
	 * @param in: The scanner containing the file with the neighbor data
	 */
	public void buildNeighbors(Scanner in){
		int a = in.nextInt();
		for (int i = 0; i < a; i ++){
			ArrayList<Country> temp = new ArrayList<Country>();
			int b = in.nextInt();
			for (int j = 0; j < b; j ++)
				temp.add(countries.get(in.nextInt()));
			countries.get(i).addNeighbors(temp);
		}
	}
	
	/**
	 * Builds the continents array
	 * @param in: The scanner containing the file with Continent Data
	 */
	public void buildContinents(Scanner in){
		int a = Integer.parseInt(in.nextLine());
		for (int i = 0; i < a; i ++){
			String continentName = in.nextLine();
			int troopValue = Integer.parseInt(in.nextLine());
			ArrayList<Country> temp = new ArrayList<Country>();
			int b = Integer.parseInt(in.nextLine());
			int c = Integer.parseInt(in.nextLine());
			for (int j = b; j <= c; j ++)
				temp.add(countries.get(j));
			continents.add(new Continent(continentName, troopValue, temp));
		}
	}
	
	/**
	 * 
	 * @return A Boolean indicated whether or not any territories remained
	 * unoccupied
	 */
	public boolean unoccupiedTerritory(){
		for (Country a: countries)
			if (a.army == null) return true;
		return false;
	}
	
	public boolean checkGame(){
		for (Army a: armies){
			if (a.countries.size() == 0)
				armies.remove(a);
			for (Continent b: continents)
				if (b.completeControl(a)) a.continents.add(b);
		}
		return (armies.size() > 1);
	}
	
	public void readClick(Country c) {
		if (gameState == 0) preGame(c);
		else if (gameState == 1) reinforce(c);
		System.out.println(c);
		c.updateLabel();
	}
	
	public void preGame(Country c){
		if (c.troops != 0)
			return;
		c.occupy(turn);
		turn.reinforcements --;
		rotate();
		if (!unoccupiedTerritory()) gameState = 1;
	}
	
	public void reinforce(Country c){
		if (!c.army.equals(turn) || turn.reinforcements < 1)
			return;
		c.troops ++;
		turn.reinforcements --;
		rotate();
		gameState = 2;
		for (Army a: armies)
			if (a.reinforcements != 0) gameState = 1;
		if (gameState == 2)
			for (Army a: armies)
				a.reinforcements();
	}
	
	public void rotate(){
		if (armies.indexOf(turn) == armies.size() - 1) turn = armies.get(0);
		else turn = armies.get(armies.indexOf(turn) + 1);
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Engine alpha = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"), null);
	}
}
