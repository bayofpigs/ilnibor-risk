import java.util.ArrayList;

public class Continent {
	public ArrayList<Country> territories;
	public String continentName;
	public int continentValue;
	public Continent(String name, int troopValue, ArrayList<Country> countries){
		continentName = name;
		continentValue = troopValue;
		territories = countries;
	}
	public boolean completeControl(Army anArmy){
		for (Country a: territories)
			if (!anArmy.continents.contains(a))
				return false;
		return true;
	}
	public int continentTroops(Army anArmy){
		if (completeControl(anArmy))
			return continentValue;
		return 0;
	}
}
