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
	public Army turn;
	private Country attacker, donor;
	public static final int PRE_GAME = 0, REINFORCE = 1, RECRUIT = 2, ATTACK_A = 3, ATTACK_B = 4, FORTIFY_A = 5, FORTIFY_B = 6, END_GAME = 7;
	private ArrayList<Integer> riskValues = new ArrayList<Integer>();
	
	/*
	 * Text versions:
	 * PRE_GAME, OCCUPY = "OCCUPY"
	 * REINFORCE_A, RECRUIT = "REINFORCE"
	 * ATTACK_A, ATTACK_B = "ATTACK"
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
		attacker = null;
		donor = null;
		gameState = PRE_GAME;
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
	
	public void readClick(Country c) throws InterruptedException {
		if (gameState == PRE_GAME) preGame(c);
		else if (gameState == REINFORCE) reinforce(c);
		else if (gameState == RECRUIT) recruit(c);
		else if (gameState == ATTACK_A) attackA(c);
		else if (gameState == ATTACK_B) attackB(c);
		else if (gameState == FORTIFY_A) fortifyA(c);
		else if (gameState == FORTIFY_B) fortifyB(c);
		else if (gameState == END_GAME) System.out.println("GAME OVER");
		//System.out.println(c);
		for (Country a: countries)
			a.updateLabel();
	}
	
	public void endClick(){
		if (gameState == ATTACK_A || gameState == ATTACK_B) gameState = FORTIFY_A;
		else if (gameState == FORTIFY_A || gameState == FORTIFY_B){
			rotate();
			gameState = RECRUIT;
		}
	}
	
	public void preGame(Country c){
		if (c.troops != 0) return;
		c.occupy(turn);
		turn.reinforcements --;
		rotate();
		if (!unoccupiedTerritory()) gameState = REINFORCE;
	}
	
	public void reinforce(Country c){
		if (!c.army.equals(turn)) return;
		c.troops ++;
		turn.reinforcements --;
		rotate();
		gameState = RECRUIT;
		for (Army a: armies)
			if (a.reinforcements > 0) gameState = REINFORCE;
		if (gameState == RECRUIT)
			for (Army a: armies)
				a.reinforcements();
	}
	
	public void recruit(Country c){
		if (!c.army.equals(turn)) return;
		c.troops ++;
		turn.reinforcements --;
		if (turn.reinforcements <= 0)
			gameState = ATTACK_A;
	}
	
	public void attackA(Country c){
		if (!c.army.equals(turn)) return;
		
		// Mike's edit: Toggle which country is selected to attack with
		c.toggleAttackPos();
		c.updateLabel();
		// </end> Mike's edit
		
		attacker = c;
		gameState = ATTACK_B;
	}
	
	public void attackB(Country c) throws InterruptedException{
		if (c.invade(attacker))
			if (!checkGame()) gameState = END_GAME;
			else occupy(c);
		else {
			gameState = ATTACK_A;
		}
		
		// Mike's edit:
		attacker.toggleAttackPos();
		attacker.updateLabel();
		// End edit
	}
	
	public void occupy(Country c){
		//attacker sends troops to c
		//implement sliding bar
		int minTroops = 1;
		int maxTroops = attacker.troops - 1;
		c.reinforce(attacker, maxTroops);
		gameState = ATTACK_A;
	}
	
	public void fortifyA(Country c){
		if (!c.army.equals(turn)) return;
		donor = c;
		gameState = FORTIFY_B;
	}
	
	public void fortifyB(Country c){
		//donor sends troops to c
		//implement sliding bar
		int minTroops = 1;
		int maxTroops = donor.troops - 1;
		int numTroops = 0;
		if (!c.reinforce(donor, numTroops)) return;
		rotate();
		turn.reinforcements();
		gameState = RECRUIT;
	}
	
	public void rotate(){
		if (armies.indexOf(turn) == armies.size() - 1) turn = armies.get(0);
		else turn = armies.get(armies.indexOf(turn) + 1);
	}
}
