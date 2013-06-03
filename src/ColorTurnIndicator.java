import java.awt.Color;
import java.awt.Dimension;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JLabel;

public class ColorTurnIndicator extends JPanel {
	private Dimension displayDimension;
	private JLabel textLabel;
	
	public ColorTurnIndicator() {
		displayDimension = new Dimension(200, 70);
		textLabel = new JLabel();
		textLabel.setAlignmentX(SwingConstants.CENTER);
		textLabel.setAlignmentY(SwingConstants.CENTER);
		
		this.setMinimumSize(displayDimension);
		this.setMaximumSize(displayDimension);
		this.setPreferredSize(displayDimension);
		this.setLayout(new BorderLayout());
		this.add(textLabel, BorderLayout.CENTER);
		
		this.setOpaque(true);
		this.setBackground(Color.gray);
	}
	
	public void changeColor(Color c) {
		this.setBackground(c);
	}
	
	public Dimension getDim() {
		return displayDimension;
	}
	
	public void setText(int state) {
		switch(state) {
		case Engine.PRE_GAME:
			textLabel.setText("OCCUPY");
			break;
		case Engine.REINFORCE:
			textLabel.setText("REINFORCE");
			break;
		case Engine.RECRUIT:
			textLabel.setText("REINFORCE");
			break;
		case Engine.ATTACK_A:
			textLabel.setText("ATTACK");
			break;
		case Engine.FORTIFY:
			textLabel.setText("FORTIFY");
			break;
		case Engine.END_GAME:
			textLabel.setText("GAME OVER");
			break;
		}
	}
}
