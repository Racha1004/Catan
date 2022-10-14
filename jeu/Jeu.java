package jeu;
import java.util.ArrayList;
import java.awt.Color;
import java.util.Collections;
import java.util.Scanner;

import exceptions.CapacityReachedException;
import exceptions.InsufficientResourcesException;
import plateau.AreteLocation;
import plateau.CarteDev;
import plateau.Location;
import plateau.Pioche;
import plateau.Plateau;
import plateau.Tuile;

public class Jeu {
	private ArrayList<Joueur> joueurs;
	private Plateau plateau;
	private Pioche pioche;
	private Joueur gagnant;
	public Jeu(ArrayList<Joueur> joueurs) {
		if (joueurs.size()< 3 || joueurs.size() > 4) {
			throw new IllegalArgumentException("Ce jeu ne peut etre joué qu'a 3 ou 4!");
		}
		ArrayList<String> noms = new ArrayList<String>();
		for (Joueur j : joueurs) {
			noms.add(j.getNom());
		}
		for (String s : noms) {
			if (Collections.frequency(noms, s) > 1){
				throw new IllegalArgumentException("Les joueurs doivent avoir des noms differents!");
			}
		}

		ArrayList<Color> couleurs = new ArrayList<Color>();
		couleurs.add(Color.RED);
		couleurs.add(Color.GREEN);
		couleurs.add(Color.BLUE);
		couleurs.add(Color.YELLOW);
		
		for(int i=0 ; i<joueurs.size() ; i++) {
			joueurs.get(i).setCouleur(couleurs.get(i));
		}
		Collections.shuffle(joueurs);

		this.joueurs = joueurs;
		plateau = new Plateau();
		pioche = new Pioche();

		Lanceur.setPremierJoueur();
		
	}
	
	public ArrayList<Joueur> getJoueur(){
		return joueurs;
	}
	
	public Joueur getJoueur(String nom) {
		for(Joueur j:joueurs) {
			if(j.getNom().equals(nom)) {
				return j;
			}
		}
		return null;
	}
	public Plateau getPlateau() {
		return plateau;
	}
	
	public void repartitionRessources(int ethiq) {
		plateau.repartitionRessources(ethiq);
	}
	
	/////////////////////LANCÉ DÉS///////////////////////////////////////
	/**
	 * Lancé des dés
	 * @return le nombre obtenu au lancé
	 */
	public int lanceeDes() {
		int rd= (int)( Math.random()*6+1)+(int)( Math.random()*6+1);
		
		if(rd==7) {
			return rd;
		}else {
			repartitionRessources(rd);
			return rd;
		}
	}
	
	/////////////////////////CONSTRUCTION////////////////////////////////
	/**
	 * Cette fonction s'occupe de la construction des routes
	 * la procedure est unpeu differente quand il s'aggit du premier tour
	 * d'ou l utilisation du boolean (notament par rapport a l'achat 
	 * @param j
	 * @param loc
	 * @param premierEtSecondTour
	 * @return
	 */
	public boolean construireRoute(Joueur j, AreteLocation loc,boolean enAchetant)  throws  InsufficientResourcesException,CapacityReachedException {
		if((enAchetant && j.PeutAcheterRoute()) || !enAchetant) {
			if(loc.getX()>0 && loc.getX()<7 && loc.getY()>0 && loc.getY()<7 && (loc.getArete()==0 || loc.getArete()==1)) {
				if(plateau.ajouterRoute(loc, j)) {
					if(enAchetant) {
						j.AcheterRoute();
					}
					return true;
				}
			}
		}
		return false;
	}
	/**
	 * cette methode est utlisée apres le 1 et 2 eme tour pour construire ses colonies
	 * @param j
	 * @param loc
	 * @return
	 */
	public boolean construireColonie(Joueur j, Location loc )  throws  InsufficientResourcesException,CapacityReachedException{
		if(j.PeutAcheterColonie()) {
			if(loc.getX()>0 && loc.getX()<7 && loc.getY()>0 && loc.getY()<7) {
				if(plateau.ajouterConstruction(loc, j)) {
					j.AcheterColonie();
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * cette methode permet de placer les colonies durant le 1 er et second tour
	 * le boolean second indiquera si on sera au second tour afin de pouvoir distribuer 
	 * les ressources de depart
	 */
	public boolean construireColonie12Tour(Joueur j, Location loc,boolean second ) {
		if(loc.getX()>0 && loc.getX()<7 && loc.getY()>0 && loc.getY()<7) {
			if(plateau.ajouterConstructionSansRoute(loc, j)) {
				if(second) {
					ArrayList<Tuile> tuilesAdjac=plateau.getAdjacenteTuiles(loc);
					for(Tuile t:tuilesAdjac) {
						if(!t.nomRessouce().equals("")) {
							j.RecevoirRessources(t.nomRessouce(),1);
						}
					}
				}
				return true;
			}
		}
		return false;
	}
	
	public boolean construireVille(Joueur j,Location loc)  throws  InsufficientResourcesException,CapacityReachedException{
		if(j.PeutAcheterVille()) {
			if(loc.getX()>0 && loc.getX()<7 && loc.getY()>0 && loc.getY()<7) {
				if(plateau.ajouterVille(loc, j)) {
					j.AcheterVille();
						return true;
					}
				}
		}
		return false;
	}
	///////////////////////////ACHATS//////////////////////////////////////
	/*	Verifier d'abord que la pioche n'est pas vide
	 * Ensuite suprimer la carte achtée de la pioche 
	 */
	
	public boolean AcheterCarteDev(Joueur j)throws InsufficientResourcesException, CapacityReachedException{
		if(j!=null) {
			if(!pioche.Vide()) {
				if( j.AcheterCartesDev(pioche.getPioche())){
					pioche.piocher();
					return true;
				}
			}
			else {
				throw new  CapacityReachedException();
			}
	
		}
		return false;
	}

	public boolean AcheterColonie(Joueur j)throws  InsufficientResourcesException,CapacityReachedException {
		if(j.PeutAcheterColonie()) {
			j.AcheterColonie();
			return true;
		}
		return false;	}

	public boolean AcheterVille(Joueur j)throws  InsufficientResourcesException,CapacityReachedException {
		if(j.PeutAcheterVille()) {
			j.AcheterVille();
			return true;
		}
		return false;
	}
	public boolean AcheterRoute(Joueur j)throws InsufficientResourcesException,CapacityReachedException {
		if(j.PeutAcheterRoute()) {
			j.AcheterRoute();
			return true;
		}
		return false;
	}
	/////////////////////////VOLEUR////////////////////////////////////////////
	/* 07 AU DÉS*/
	
	/**
	 * Cette methode sert a deplacer le voleur
	 * @param loc nouvelle localisation du voleur
	 * @return true si le voleur a bien été deplacer,false sinon
	 */
	public boolean deplacerVoleur(Location loc) {
		return plateau.deplacerVoleur(loc);
	}
	
	/**
	 * Cette methode permet au joueur j1 de voler une carte au hasard au joueur j2
	 * @param j1 joueur courant
	 * @param j2 joueur choisi par le j1
	 */
	public void voler(Joueur j1, Joueur j2) {

		ArrayList<String> ressources = new ArrayList<String>();
		for (int i = 0; i < j2.getNbrRessource("ARGILE"); i++) {
			ressources.add("ARGILE");
		}
		for (int i = 0; i < j2.getNbrRessource("LAINE"); i++) {
			ressources.add("LAINE");
		}
		for (int i = 0; i < j2.getNbrRessource("MINERAI"); i++) {
			ressources.add("MINERAI");
		}
		for (int i = 0; i <j2.getNbrRessource("BLÉ"); i++) {
			ressources.add("BLÉ");
		}
		for (int i = 0; i < j2.getNbrRessource("BOIS"); i++) {
			ressources.add("BOIS");
		}
		
		Collections.shuffle(ressources);

		if (ressources.size() <= 0) {
			return;
		}
		String result = ressources.get(0);

		j2.EnleverRessources(result, 1);
		j1.RecevoirRessources(result, 1);
	}
	
	public ArrayList<Joueur> TousLesJoueursAutourDeTuile(Location loc){
		return plateau.TousLesJoueursAutourDeTuile(loc);
	}
	/**
	 * Cette methode permet de faire perdre a tous les joueur ayant plus de 7 cartes la moitier
	 * le leurs cartes ressources
	 */
	
	public void defausserMoitierCartes() {
		for(Joueur j: joueurs) {
			int nbrCourantCartes=j.NombreTotatlDeRessources();
			if(nbrCourantCartes>7) {
				int nbrCartesaDefausser=nbrCourantCartes/2;
				Scanner sc=new Scanner(System.in);
				while(nbrCartesaDefausser>0) {
					System.out.println("Veuillez choisir une carte de ressource a defausser s'il vous plait : \n");
					System.out.println("\n\t1 : ARGILE "+"("+j.getNbrRessource("ARGILE")+")"
							+ "\n\t2 : LAINE "+"("+j.getNbrRessource("ARGILE")+")"
							+ "\n\t3 : MINERAI "+"("+j.getNbrRessource("MINERAI")+")"
							+ "\n\t4 : BLÉ "+"("+j.getNbrRessource("BLÉ")+")"
							+ "\n\t5 : BOIS	"+"("+j.getNbrRessource("BOIS")+")"
							);
					if(sc.hasNext()) {
						int n=Integer.valueOf(sc.next());
						String res="";
						switch(n) {
							case 1: res="ARGILE";break;
							case 2: res="LAINE";break;
							case 3: res="MINERAI";break;
							case 4: res="BLÉ";break;
							case 5: res="BOIS";break;
							default:break;
						}
						if(!res.equals("")) {
							j.EnleverRessources(res,1);
							nbrCartesaDefausser--;
						}
					}
				}
				sc.close();
			}
		}
	}
	/////////////////////COMMERCES///////////////////////////////////////////
	//TODO
	/**
	 * Cette fonction permet aux joueurs de negocier et de s'echanger des cartes
	 * @param joueurA
	 * @param joueurB
	 * @param fromA les ressources que va donner le joueur A au joueur B
	 * @param fromB les ressources que va donner le joueur B au joueur A
	 * @return true si l'echanger a bien était realisé
	 */
	public boolean commerceInterne(Joueur joueurA ,Joueur joueurB , ArrayList<String> fromA, ArrayList<String> fromB ) {
		if(joueurB.hasRessources(fromB) && joueurA.hasRessources(fromA)) {
			joueurB.EnleverRessources(fromB);
			joueurB.RecevoirRessources(fromA);
			
			joueurA.EnleverRessources(fromA);
			joueurA.RecevoirRessources(fromB);
			
			return true;
		}
		return false;
	}
	
	
	/**
	 *  portTag
	 * 					// 0 = general
						// 1 = ARGILE
						// 2 = LAINE
						// 3 = MINERAI
						// 4 = BLÉ
						// 5 = BOIS	
	 * @param j le joueur voulant effectuer l achat
	 * @param ressourceASacrifier la resources a vendre
	 * @param ressourceAchetee la ressource que veut le joueur acheter
	 * @return 0 si l'achat s'est bien passé 1 si ses ressources ne lui permettent pzs d'effectuer un achat
	 */
	public int commerceExterne (Joueur j,String ressourceASacrifier,String ressourceAchetee) {
		int port=-1;
		switch (ressourceASacrifier) {
			case "ARGILE": port=1;break;
			case "LAINE": port=2;break;
			case "MINERAI": port=3;break;
			case "BLÉ": port=4;break;
			case "BOIS": port=5;break;
			default:break;
		}
		boolean []ports=j.getPorts();
		if(port!=-1 && ports[port]) {
			if(j.hasRessources(ressourceASacrifier, 2)) {
				j.EnleverRessources(ressourceASacrifier, 2);
				j.RecevoirRessources(ressourceAchetee, 1);
			}else {
				return 1;//ressources insuffisantes
			}
		}else if(ports[0]) {
			if(j.hasRessources(ressourceASacrifier, 3)) {
				j.EnleverRessources(ressourceASacrifier, 3);
				j.RecevoirRessources(ressourceAchetee, 1);
			}else {
				return 1;//ressources insuffisantes
			}
		}else {
			if(j.hasRessources(ressourceASacrifier, 4)) {
				j.EnleverRessources(ressourceASacrifier, 4);
				j.RecevoirRessources(ressourceAchetee, 1);
			}else {
				return 1;//ressources insuffisantes
			}
		}
		return 0;
	}
	

	
	/**
	 * cette methode est appellée quand un joueur joue sa carte progres MONOPOLE
	 * celle ci lui permet de prendre toutes les carte de type res de ses adversaires
	 * @param res la ressource choisie par joueur
	 */
	public void PrendreToutMonopole(Joueur joueur,String res) {
		for(Joueur j:joueurs) {
			if(joueur!=j) {
				int  n=j.getNbrRessource(res);
				if(n>0) {
					joueur.RecevoirRessources(res,n);
					j.EnleverRessources(res,n);
				}
			}	
		}
	}
	
	public boolean finPartie() {
		return gagnant!=null;
	}
	
	public void setGagnant(Joueur j) {
		gagnant=j;
	}
	
	public Joueur getGagnant() {
		return gagnant;
	}
	public Joueur Gagnant() {
		for(Joueur j: joueurs) {
			if(j.getPointsVictoire()>10) {
				return j;
			}	
		}
		return null;
	}
	
	public void Affiche() {
		plateau.afficher();
		System.out.print("Pioche :");
		if(pioche.Vide()) {
			System.out.println(" plus aucune carte à piocher");
		}else {
			System.out.println(" vous pouvez toujours piocher il reste encore de la pioche ");

		}
		System.out.print("\n");

	}

	
}