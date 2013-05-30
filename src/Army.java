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
	public boolean redeemRisk(){
		if (riskCards > 4){
			riskCards -= 3;
			return true;
		}
		return false;
	}
	public int reinforcements(int riskTroops){
		int territoryTroops = countries.size() / 3;
		if (territoryTroops < 3)
			territoryTroops = 3;
		int continentTroops = 0;
		for (Continent a: continents)
			continentTroops += a.continentValue;
		return riskTroops + territoryTroops + continentTroops;
	}
}
