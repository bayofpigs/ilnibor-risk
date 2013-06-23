import java.awt.Color;
import java.util.ArrayList;

public class Army {
	public ArrayList<Country> countries;
	public ArrayList<Continent> continents;
	public ArrayList<Integer> risk;
	public Color armyColor;
	public String armyName;
	public int riskCards, reinforcements;
	public MessageLog log;
	public Army(Color color, String name, MessageLog messages, int numArmies, ArrayList<Integer> riskValues){
		log = messages;
		countries = new ArrayList<Country>();
		continents = new ArrayList<Continent>();
		risk = new ArrayList<Integer>();
		armyColor = color;
		armyName = name;
		risk = riskValues;
		addReinforcements(numArmies);
	}
	public void addReinforcements(int armies){
		if (armies == 2) reinforcements += 40;
		else if (armies == 3) reinforcements += 35;
		else if (armies == 4) reinforcements += 30;
		else if (armies == 5) reinforcements += 25;
	}
	public void reinforcements(){
		if (riskCards > 4){
			riskCards -= 3;
			reinforcements += risk.remove(0);
		}
		int territoryTroops = countries.size() / 3;
		if (territoryTroops < 3) territoryTroops = 3;
		reinforcements += territoryTroops;
		//log.write("Army 2");
		//log.write(armyName + "'s Reinforcements: " + reinforcements, armyColor);
		//log.write("Army 3");
		//log.write(continents.toString(), armyColor);
		for (Continent a: continents)
			reinforcements += a.continentValue;
		//log.write("Army 4");
		log.write(armyName + " has " + reinforcements + " reinforcements.", armyColor);
	}
	
	public String toString(){
		return armyName;
	}
}