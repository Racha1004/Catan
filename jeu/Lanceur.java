package jeu;




import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import javax.swing.SwingUtilities;

import plateau.*;
import vue.*;

public class Lanceur {
	private static Joueur joueurCourant;
	private static int nombreJoueurs=0;
	private static int index = 0;
	public static ArrayList<Joueur> joueurs = new ArrayList<Joueur>();
	private static Jeu jeu;
	private static Joueur gagnant;

	public static void main(String[] args) {
		Scanner sc =new Scanner(System.in);
		boolean done=false;
		System.out.println("********************************Bienvenue***********************************");
		System.out.println("\nSouhaitez vous jouer a Catan ? (La réponse est déja claire ;))");

		
		do {
			System.out.println("\n Tapez :\n\t 1 : pour choisir avec la version textuelle "
					+ "\n\t 2 : pour la version graphique"
					+"\n\t-1 : pour Arreter le programme ");
			if(sc.hasNext())
			{
				String s=sc.next();
				
				switch(s){ 
				case "1":
					done=true;
					VueTextuelle vt=new VueTextuelle();
					vt.Jouer();
					break;
				case "2":
					done=true;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							VueGraphique view = new VueGraphique();
							jeu = view.getPlateau().getJeu();
						}
					});
					break;
				case "-1":
					done=true;
					break;
				default :System.out.println("Veuillez respecter les choix proposés s'il vous plait !");break;
				}	
					
			}	
		}while(!done);
		
		sc.close();
	}
	public static void ajouterJoueur(Joueur j) {
		joueurs.add(j);
	}
	public static Joueur getJoueurCourant() {
		return joueurCourant;
	}
	public static void joueurSuivant() {
		index = (index+1)%nombreJoueurs;
		joueurCourant = joueurs.get(index);
	}public static void joueurPrecedant() {
		index = (index-1)%nombreJoueurs;
		joueurCourant = joueurs.get(index);
	}
	public static void setPremierJoueur() {
		joueurCourant = joueurs.get(0);
	}
	public static void setGagnant(Joueur j) {
		gagnant=j;
	}
	public static Joueur getGagnant() {
		return gagnant;
	}
	public static int getNombreJoueurs() {
		return nombreJoueurs;
	}
	public static void setNombreJoueurs(int n) {
		nombreJoueurs=n;
	}
	public static Joueur getJoueur(int i) {
		return joueurs.get(i);
	}
	
}