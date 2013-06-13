import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.SwingConstants;


/**
 * TODO: 
 * Find where the Country label is being created and its bounds set
 * Fix the bounds so that the label displays properly on the map, not just weird squares/rectangles
 * Align the images of the countries on the map by modifying the countries.txt file
 * Change Black Images to Country Images so that images are transparent again
 * 
 * The Country class represents a country in the board-game RISK.
 * It is capable of holding up to 100 troops from a single army but can also exist in an unoccupied state.
 * @author Akhil Velagapudi
 * @version 1.0
 * @see Engine
 */
public class Country extends JLabel{
	private static final long serialVersionUID = 1;
	public ArrayList<Country> neighbors;
	public String name;
	public String fileName;
	public int troops;
	public Army army;
	public Color color;
	public Dimension dim;
	public boolean attackPosition;
	/**
	 * Class Country constructor. 
	 * @param countryName The name of the country.
	 */
	public Country(String name, int leftBound, int topBound) {
		this.name = name;
		neighbors = new ArrayList<Country>();
		army = null;
		troops = 0;
		attackPosition = false;
		setPreferredSize(new Dimension(49, 49));
		setHorizontalAlignment(SwingConstants.CENTER);
		setVerticalAlignment(SwingConstants.CENTER);
		setBounds(leftBound - 12, topBound - 12, 49, 49);
		
		// Debug -- sets the hit box to opaque
		//setOpaque(true);
		updateLabel();
	}
	public void updateLabel(){
		if (army != null) setForeground(army.armyColor);
		setText("" + troops);
		if (attackPosition) setText("=" + troops + "=");
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
		anArmy.countries.add(this);
		color = anArmy.armyColor;
		troops = 1;
	}
	
	public void toggleSpecialOn() {
		attackPosition = true;
	}
	
	public void toggleSpecialOff(){
		attackPosition = false;
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
		if (attack.size() < defend.size()) defend.remove(1);
		while (defend.size() < attack.size())
			attack.remove(attack.size() - 1);
		for (int i = 0; i < attack.size(); i++)
			if (attack.get(i) > defend.get(i))
				troops--;
			else
				attacker.troops--;
		if (troops == 0) {
			army.countries.remove(this);
			if (army.countries.size() == 0)
				attacker.army.riskCards += army.riskCards;
			army = attacker.army;
			army.countries.add(this);
			color = army.armyColor;
			troops ++;
			attacker.troops --;
			System.out.println(name + " has been conquered by " + attacker.army.armyName);
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
			System.out.println("-------------------------------\n");
			System.out.println(attacker + "\n" + toString() + "\n");
		}
		return true;
	}
	
	public boolean canReinforce(Country donator) {
		return (isNeighbor(donator) && army.equals(donator.army) && !donator.equals(this));
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
