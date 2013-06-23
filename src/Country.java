import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import javax.swing.JLabel;

/**
 * The Country class represents a country in the board-game RISK.
 * It is capable of holding up to 100 troops from a single army but can also exist in an unoccupied state.
 * @author Akhil Velagapudi
 * @version 1.0
 * @see Engine
 */
public class Country extends JLabel{
	private static final long serialVersionUID = 1;
	private final Font DEFAULT_FONT = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
	private final Font SPECIAL_FONT = new Font(getFont().getName(), Font.BOLD, getFont().getSize() + 1);
	public MessageLog log;
	public ArrayList<Country> neighbors;
	public String name;
	public int troops;
	public Army army;
	public boolean special;
	/**
	 * Class Country constructor. 
	 * @param countryName The name of the country.
	 */
	public Country(String countryName, int leftBound, int topBound, MessageLog messages) {
		name = countryName;
		log = messages;
		neighbors = new ArrayList<Country>();
		army = null;
		troops = 0;
		special = false;
		setBounds(leftBound + 6, topBound, 25, 25);
		updateLabel();
	}
	/**
	 * TODO: Create a better way to show a "highlighted" country
	 */
	public void updateLabel(){
		setFont(DEFAULT_FONT);
		if (army != null) setForeground(army.armyColor);
		if (troops > 0) setText("" + troops);
		if (special) setFont(SPECIAL_FONT);
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
		//log.write("Country 1");
		log.write(attacker.army.armyName + " attacks " + name + " from " + attacker.name + ".", attacker.army.armyColor);
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
		String attackDieString = "" + attack.get(0);
		for (int i = 1; i < attack.size(); i++)
			if (i == attack.size() - 1)
				attackDieString = attackDieString + " and " + attack.get(i);
			else
				attackDieString = attackDieString + ", " + attack.get(i);
		String defendDieString = "" + defend.get(0);
		for (int i = 1; i < defend.size(); i++)
			if (i == defend.size() - 1)
				defendDieString = defendDieString + " and " + defend.get(i);
			else
				defendDieString = defendDieString + ", " + defend.get(i);
		//log.write("Country 2");
		log.write("The attacker, " + attacker.name + ", rolls a " + attackDieString + ".", attacker.army.armyColor);
		//log.write("Country 3");
		log.write("The defender, " + name + ", rolls a " + defendDieString + ".", army.armyColor);
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
			troops ++;
			attacker.troops --;
			//log.write("Country 4");
			log.write(name + " has been conquered by " + attacker.army.armyName + ".", attacker.army.armyColor);
			return true;
		}
		//log.write("Country 5");
		log.write(name + " defends the attack from " + attacker.army.armyName + ".", army.armyColor);
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
			log.write("Country 6");
			log.write(attacker + "\n" + toString(), attacker.army.armyColor);
			log.write("-------------------------------", attacker.army.armyColor);
			log.write(attacker + "\n" + toString(), attacker.army.armyColor);
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
