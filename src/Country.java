import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Dimension;

/**
 * The Country class represents a country in the board-game RISK.
 * It is capabale of holding upto 100 troops from a single army but can also exist in an unoccupied state.
 * @author Akhil Velagapudi
 * @version 1.0
 * @see Engine
 */
public class Country extends JLabel{
	private static final long serialVersionUID = 1;
	public ArrayList<Country> neighbors;
	public String name;
	public int troops;
	public Army army;
	public Color color;
	public Dimension dim;
	/**
	 * Class Country constructor. 
	 * @param countryName The name of the country.
	 */
	public Country(String countryName, int leftBound, int topBound) {
		name = countryName;
		neighbors = new ArrayList<Country>();
		army = null;
		troops = 0;
		
		// Debug -- sets the hit box to opaque
		setOpaque(true);
		
		setPreferredSize(new Dimension(40, 40));
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setBounds(leftBound - 10, topBound - 10, 40, 40);
		updateLabel();
	}
	public void updateLabel(){
		if (army != null)
			setForeground(army.armyColor);
		setText("" + troops);
	}
	
	public void addNeighbors(ArrayList<Country> countryNeighbors){
		neighbors = countryNeighbors;
	}
	
	/**
	 * Updates the army that owns this territory.
	 * @param teamNumber The team constant color to occupy this territory
	 */
	public void occupy(Army anArmy) {
		army = anArmy;
		color = anArmy.armyColor;
		troops = 1;
	}
	
	public boolean isNeighbor(Country other){
		return neighbors.contains(other);
	}
	
	/**
	 * Attacker attacks the country once.
	 * Returns true if and only if attacker successfully conquers the country.
	 * @param attacker The country attacking this country
	 * @return true if invasion in successful, otherwise false
	 * @throws InterruptedException
	 */
	public boolean invade(Country attacker) throws InterruptedException {
		if (!isNeighbor(attacker) || army.equals(attacker.army) || attacker.troops == 1) return false;
		Random die = new Random();
		System.out.println(attacker.army.armyName + " attacks " + name + " from " + attacker.name + ".\n");
		Thread.sleep(1000);
		int attackDice = attacker.troops - 1, defendDice = troops;
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
			if (army.continents.size() == 0)
				attacker.army.riskCards += army.riskCards;
			army = attacker.army;
			color = army.armyColor;
			System.out.println(name + " has been conquered by " + attacker.army.armyName);
			reinforce(attacker, attacker.troops - 1);
			return true;
		}
		System.out.println(name + " defends the attack by " + attacker.army.armyName + ".\n");
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
			if (!isNeighbor(attacker) || army.equals(attacker.army) || attacker.troops == 1) return false;
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
		if (numTroops == 0 || !isNeighbor(donator) || !army.equals(donator.army))
			return false;
		donator.troops -= numTroops;
		troops += numTroops;
		System.out.print(donator.army.armyName + " reinforced " + name + " with " + numTroops + " troop");
		if (numTroops != 1)
			System.out.print("s");
		System.out.println(" from " + donator.name + "\n");
		updateLabel();
		return true;
	}
	
	/**
	 * Prints the status of the country
	 * @return name, army, troops
	 */
	public String toString() {
		String print = name + ": ";
		if (army == null) return print + "No Man's Land";
		print += army.armyName + " (" + troops;
		if (troops == 1) return print + " troop)";
		return print + " troops)";
	}
}