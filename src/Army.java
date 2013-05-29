import java.awt.Color;
import java.util.ArrayList;

public class Army {
	public ArrayList<Country> countries;
	public ArrayList<Continent> continents;
	public Color armyColor;
	public String armyName;
	public int riskCards;
	public Army(Color color, String name){
		armyColor = color;
		armyName = name;
	}
	public int reinforcements(int riskTroops){
		int territoryTroops = countries.size() / 3;
		if (territoryTroops < 3)
			territoryTroops = 3;
		int continentTroops = 0;
		return riskTroops + territoryTroops + continentTroops;
	}
}
