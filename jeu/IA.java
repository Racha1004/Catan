package jeu;

import java.awt.Color;

public class IA extends Joueur {
	private static int nbrAi=1;
	public IA(Color c) {
		super("Ai"+nbrAi,c);
	}
	public IA() {
		super("Ai"+nbrAi);
		nbrAi++;
	}

}
