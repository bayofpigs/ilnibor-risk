import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

class Country {
	static final int OPEN = -1, YELLOW_ARMY = 0, GREEN_ARMY = 1, RED_ARMY = 2, BLUE_ARMY = 3, ORANGE_ARMY = 4;
	String name;
	int army, troops;
	Country(String countryName) {
		name = countryName;
		army = OPEN;
		troops = 0;
	}
	String getName(){
		return name;
	}
	int getTeam(){
		return army;
	}
	int getTroops(){
		return troops;
	}
	boolean unoccupied() {
		return (army == OPEN);
	}
	void occupy(int teamNumber) {
		army = teamNumber;
		troops = 1;
	}
	void reinforce(){
		troops ++;
	}
	void loseTroop(){
		troops --;
	}
	boolean invade(Country attacker) {
		Random die = new Random();
		int enemyTroops = attacker.getTroops();
		if (enemyTroops == 1)
			return false;
		int attackDice = enemyTroops - 1;
		int defendDice = troops;
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
