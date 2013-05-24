import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Country {
	/*
	 * Updates:
	 * Commented the methods of this class
	 * See Comments on the invade method. Message me responses.
	 * - Mike
	 */
	
	// Constants indicating the nation (color) occupying the territory
	static final int OPEN = -1, YELLOW_ARMY = 0, GREEN_ARMY = 1, RED_ARMY = 2, 
			BLUE_ARMY = 3, ORANGE_ARMY = 4;
	
	// Name of the country
	private String name;
	
	// integer representation of the army in question: based 
	//   on the army constant
	private int army;
	
	// The amount of troops of the occupying nation on the specified country
	private int troops;
	
	/**
	 * Class Country constructor.
	 * @param countryName The name of the country.
	 */
	Country(String countryName) {
		name = countryName;
		army = OPEN;
		troops = 0;
	}
	
	/**
	 * 
	 * @return The name of the country.
	 */
	String getName(){
		return name;
	}
	
	/**
	 * 
	 * @return The army occupying the territory
	 */
	int getTeam(){
		return army;
	}
	
	/**
	 * 
	 * @return The number of units on the specified territory
	 */
	int getTroops(){
		return troops;
	}
	
	/**
	 * 
	 * @return Whether or not the nation in question is unoccupied.
	 */
	boolean unoccupied() {
		return (army == OPEN);
	}
	
	/**
	 * Updates the army that owns this territory.
	 * @param teamNumber The team constant color to occupy this territory
	 */
	void occupy(int teamNumber) {
		army = teamNumber;
		troops = 1;
	}
	
	/**
	 * Reinforce the territory
	 */
	void reinforce(){
		troops ++;
	}
	
	/**
	 * Lose a soldier in this territory (typically the result of battle)
	 */
	void loseTroop(){
		troops --;
	}
	
	
	boolean invade(Country attacker) {
		/* Comments:
		 * Can possibly be moved to the engine class
		 * (separate Objects from game methods) 
		 * In what situation does this function return true?
		 */
		Random die = new Random();
		int attackDice = attacker.getTroops() - 1, defendDice = troops;
		if (attackDice == 0)
			return false;
		if (attackDice > 3)
			attackDice = 3;
		if (defendDice > 2)
			defendDice = 2;
		ArrayList<Integer> defend = new ArrayList<Integer>();
		ArrayList<Integer> attack = new ArrayList<Integer>();
		for (int a = 0; a < defendDice; a ++)
			defend.add(die.nextInt(6) + 1);
		for (int b = 0; b < attackDice; b ++)
			attack.add(die.nextInt(6) + 1);
		Collections.sort(defend);
		Collections.sort(attack);
		Collections.reverse(defend);
		Collections.reverse(attack);
		if (attack.size() < defend.size())
			defend.remove(1);
		while (defend.size() < attack.size())
			attack.remove(attack.size() - 1);
		for (int i = 0; i < attack.size(); i ++)
			if (attack.get(i) > defend.get(i))
				troops --;
			else
				attacker.loseTroop();
		
		return false;
	}
	
	
	public String toString() {
		String print = name + ": ";
		if (unoccupied()) return print + "No Man's Land";
		switch (army) {
			case 0: print += "Yellow Army";
					break;
			case 1: print += "Green Army";
					break;
			case 2: print += "Red Army";
					break;
			case 3: print += "Blue Army";
					break;
			case 4: print += "Orange Army";
					break;
		}
		print += " (" + troops;
		if (troops == 1)
			return print + " troop)";
		return print + " troops)";
	}
	
	
	public static void main(String[] args) {
		Country alpha = new Country("America");
		alpha.occupy(0);
		alpha.reinforce();
		alpha.reinforce();
		System.out.println(alpha);
		Country beta = new Country("Mexico");
		beta.occupy(1);
		beta.reinforce();
		System.out.println(beta);
		beta.invade(alpha);
	}
}
