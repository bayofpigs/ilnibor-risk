import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * The Country class represents a country in the board-game RISK.
 * It is capabale of holding upto 100 troops from a single army but can also exist in an unoccupied state.
 * @author Akhil Velagapudi
 * @version 1.0
 * @see Engine
 */
public class Country {
	static final int OPEN = -1, YELLOW_ARMY = 0, GREEN_ARMY = 1, RED_ARMY = 2, BLUE_ARMY = 3, ORANGE_ARMY = 4;
	public String name;
	public int army, troops;
	/**
	 * Class Country constructor. 
	 * @param countryName The name of the country.
	 */
	public Country(String countryName) {
		name = countryName;
		army = OPEN;
		troops = 0;
	}
	/**
	 * Updates the army that owns this territory.
	 * @param teamNumber The team constant color to occupy this territory
	 */
	public void occupy(int teamNumber) {
		army = teamNumber;
		troops = 1;
	}
	/**
	 * Attacker attacks the country once.
	 * Returns true if and only if attacker successfully conquers the country.
	 * @param attacker The country attacking this country
	 * @return true if invasion in successful, otherwise false
	 * @throws InterruptedException
	 */
	public boolean invade(Country attacker) throws InterruptedException {
		Random die = new Random();
		System.out.println("The " + getColor(attacker.army) + " attacks " + name + " from " + attacker.name + ".\n");
		Thread.sleep(1000);
		int attackDice = attacker.troops - 1, defendDice = troops;
		if (attackDice == 0) return false;
		if (attackDice > 3) attackDice = 3;
		if (defendDice > 2) defendDice = 2;
		ArrayList<Integer> defend = new ArrayList<Integer>(), attack = new ArrayList<Integer>();
		for (int a = 0; a < defendDice; a++)
			defend.add(die.nextInt(6) + 1);
		for (int b = 0; b < attackDice; b++)
			attack.add(die.nextInt(6) + 1);
		Collections.sort(defend);
		Collections.sort(attack);
		Collections.reverse(defend);
		Collections.reverse(attack);
		System.out.println("Attacker: " + attack + " (" + attacker.name + ")");
		System.out.println("Defender: " + defend + " (" + name + ")\n");
		Thread.sleep(2000);
		if (attack.size() < defend.size()) defend.remove(1);
		while (defend.size() < attack.size())
			attack.remove(attack.size() - 1);
		for (int i = 0; i < attack.size(); i++)
			if (attack.get(i) > defend.get(i))
				troops--;
			else
				attacker.troops--;
		if (troops == 0) {
			army = attacker.army;
			System.out.println(name + " has been conquered by the " + getColor(attacker.army));
			reinforce(attacker, attacker.troops - 1);
			return true;
		}
		System.out.println(name + " defends the attack by the " + getColor(attacker.army) + ".\n");
		return false;
	}
	/**
	 * Attacker keeps attacking the country until only one troop remains.
	 * Returns true if and only if attacker successfully conquers the country.
	 * @param attacker The country attacking this country
	 * @return true if invasion in successful, otherwise false
	 * @throws InterruptedException
	 */
	public boolean nuke(Country attacker) throws InterruptedException {
		while (!invade(attacker)){
			if (attacker.troops == 1)
				return false;
			System.out.println(attacker + "\n" + toString() + "\n");
			Thread.sleep(4000);
			System.out.println("-------------------------------\n");
			System.out.println(attacker + "\n" + toString() + "\n");
		}
		return true;
	}
	/**
	 * Used after a successful attack or during the reinforce phase.
	 * Enables the transfer of troops between two countries controlled by the same army.
	 * @param donator The country donating the troops for reinforcement
	 * @param numTroops The number of troops the country is donating for reinforcement
	 * @return true if troops are transferred between the countries, otherwise false
	 */
	public boolean reinforce(Country donator, int numTroops){
		if (numTroops == 0)
			return false;
		donator.troops -= numTroops;
		troops += numTroops;
		System.out.print("The " + getColor(donator.army) + " reinforced " + name + " with " + numTroops + " troop");
		if (numTroops != 1)
			System.out.print("s");
		System.out.println(" from " + donator.name + "\n");
		return true;
	}
	/**
	 * Converts integer representation of army to a string
	 * @param teamNumber Integer representation of the army in question: based on the army constants
	 * @return String representation of the army in question: based on the color of the army
	 */
	public String getColor(int teamNumber) {
		String armyColor = "No Man's Land";
		switch (teamNumber) {
			case 0 : armyColor = "Yellow Army";	break;
			case 1 : armyColor = "Green Army";	break;
			case 2 : armyColor = "Red Army";	break;
			case 3 : armyColor = "Blue Army";	break;
			case 4 : armyColor = "Orange Army";	break;
		}
		return armyColor;
	}
	/**
	 * Prints the status of the country
	 * @return name, army, troops
	 */
	public String toString() {
		String print = name + ": " + getColor(army);
		if (army == OPEN) return print;
		print += " (" + troops;
		if (troops == 1) return print + " troop)";
		return print + " troops)";
	}
}