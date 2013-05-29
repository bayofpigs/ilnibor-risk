import javax.swing.JLabel;
import java.awt.Color;

public class TroopLabel extends JLabel {
	private int numTroops;
	private Color color;
	
	public TroopLabel() {
		numTroops = 0;
		this.setOpaque(true);
		this.setNumber();
	}
	
	public void setNumber() {
		this.setText("" + numTroops);
	}
	
	public void decTroops() {
		numTroops--;
		setNumber();
	}
	
	public void incTroops() {
		numTroops++;
		setNumber();
	}
	
	public void setTroops(int troops) {
		numTroops = troops;
		setNumber();
	}
}
