package vue;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.*;
import java.util.*;

import jeu.*;
import plateau.*;

public class JoueurBar extends JPanel {
	private ArrayList<JPanel> joueurPanel = new ArrayList<JPanel>();
	public final static int INTERVAL = 50;
	private Timer timer;
	public JoueurBar() {
	}
	public void setJoueurBar() {
		setBackground(Color.LIGHT_GRAY);
		setLayout(new GridLayout(1,Lanceur.getNombreJoueurs()));
		for(int i=0 ; i<Lanceur.getNombreJoueurs() ; i++) {
			JPanel p= new JPanel(new GridLayout(12,1));
			JLabel nom = new JLabel(Lanceur.getJoueur(i).getNom(), SwingConstants.CENTER);
			nom.setForeground(Lanceur.getJoueur(i).getCouleur());
			p.add(nom);
			for(int j=0 ; j<11 ; j++) {
				p.add(new JLabel("", SwingConstants.CENTER));
			}
			((JLabel)p.getComponent(7)).setText("Carte de développement");
			String nomFont = ((JLabel)p.getComponent(7)).getFont().getName();
			((JLabel)p.getComponent(7)).setFont(new Font(nomFont, 3, 11));
			
			joueurPanel.add(p);
			add(p);
		}
		timer = new Timer(INTERVAL,
				new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						for (int i = 0; i < joueurPanel.size(); i++) {
							mettreAJour(joueurPanel.get(i), Lanceur.getJoueur(i));
						}
					}
				});
		timer.start();
	}
	public void mettreAJour(JPanel panel , Joueur joueur) {
		((JLabel)panel.getComponent(1)).setText("Bois : "+joueur.getNbrRessource("BOIS"));
		((JLabel)panel.getComponent(2)).setText("Blé : "+joueur.getNbrRessource("BLÉ"));
		((JLabel)panel.getComponent(3)).setText("Laine : "+joueur.getNbrRessource("LAINE"));
		((JLabel)panel.getComponent(4)).setText("Argile : "+joueur.getNbrRessource("ARGILE"));
		((JLabel)panel.getComponent(5)).setText("Minerai : "+joueur.getNbrRessource("MINERAI"));
		((JLabel)panel.getComponent(6)).setText("Points de victoire : "+joueur.getPointsVictoire());
		((JLabel)panel.getComponent(8)).setText("Chevalier : "+joueur.getTypeCarteDev("CHEVALIER"));
		((JLabel)panel.getComponent(9)).setText("Invention : "+joueur.getTypeCarteDev("INVENTION"));
		((JLabel)panel.getComponent(10)).setText("Monopol : "+joueur.getTypeCarteDev("MONOPOLE"));
		((JLabel)panel.getComponent(11)).setText("Construction de routes : "+joueur.getTypeCarteDev("CONSTRUCTION DE ROUTES"));
		String nomFont = ((JLabel)panel.getComponent(10)).getFont().getName();
		((JLabel)panel.getComponent(11)).setFont(new Font(nomFont, 1, 10));
		
	}
}
