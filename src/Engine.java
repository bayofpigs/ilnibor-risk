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
	private static final int PRE_GAME = 0, REINFORCE_A = 1, RECRUIT = 2, ATTACK = 3, OCCUPY = 4, REINFORCE_B = 5;
	public Engine(File mapCountries, File mapNeighbors, File mapContinents, ArrayList<Army> gameArmies) throws FileNotFoundException{
		// Initialize the array variables
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		armies = gameArmies;
		
		// Fill the countries array with the contents of Countries.txt file
		Scanner a = new Scanner(mapCountries);
		buildCountries(a);
		
		// Add neighbors to each country
		a = new Scanner(mapNeighbors);
		buildNeighbors(a);
		
		// Fill the continents array with the contents of the Continents.txt file
		a = new Scanner(mapContinents);
		buildContinents(a);
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
	
	
	public void preGame(){
		for (int i = 0; unoccupiedTerritory(); i ++){
			if (i > armies.size()) i = 0;
			occupyCountry().occupy(armies.get(i));
		}
	}
	
	public void game(){
		for (int i = 0; checkGame(); i ++){
			if (i > armies.size()) i = 0;
			
		}
	}
	
	public void reinforce(){
		
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
		System.out.println("Country " + c);
	}
	
	public Country occupyCountry(){
		//returns a country that is unoccupied (country.army = null) by reading mouse click
		Country clickedOn = new Country("This is the country they clicked on", 5, 5);
		if (clickedOn.army == null)
			return clickedOn;
		return occupyCountry();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Engine alpha = new Engine(new File("resources/Countries.txt"), new File("resources/Neighbors.txt"), new File("resources/Continents.txt"), null);
	}
}
