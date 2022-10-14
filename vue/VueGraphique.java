package vue;

import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class VueGraphique {
	public final static int INTERVAL = 20;
	private CatanePlateau plateau;
	public JoueurBar joueurBar;
	private PartieBar partieBar;
	public VueGraphique() {
		plateau = new CatanePlateau();
		joueurBar = new JoueurBar();
		partieBar = new PartieBar(this);
		createAndShowGUI();
		Timer timer = new Timer(INTERVAL,
				new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
							plateau.repaint();
					}
				});

		timer.start();
	}
	private void createAndShowGUI() {
		
		JFrame frame = new JFrame("Catan");
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setSize(1500, 1000);
		Container content = frame.getContentPane();
		content.setLayout(new GridLayout(1,2));
		content.add(plateau);
		JPanel panel = new JPanel(new GridLayout(2,1));
		panel.add(joueurBar);
		panel.add(partieBar);
		content.add(panel);
		
		
		
		frame.setVisible(true);
		plateau.repaint();
	}
	public CatanePlateau getPlateau() {
		return plateau;
	}
}
