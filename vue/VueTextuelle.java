package vue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import exceptions.CapacityReachedException;
import exceptions.InsufficientResourcesException;
import jeu.IA;
import jeu.Jeu;
import jeu.Joueur;
import jeu.Lanceur;
import plateau.AreteLocation;
import plateau.CarteDev;
import plateau.Location;

public class VueTextuelle {
	private ArrayList<Joueur> joueurs;
	private Jeu jeu;
	private ArrayList<String> tourCourant;
	private Scanner sc;
	public VueTextuelle() {
		 sc=new Scanner(System.in);
		 joueurs=new ArrayList<Joueur> ();
		 tourCourant= new ArrayList<String>();
	}
	
	public void Jouer() {
		
		iniTialisationDuJeu();
		
		//1 er tour
		for(int i=0;i<joueurs.size();i++) {
			placeColonieTour12(joueurs.get(i),false);
		}
		//2eme tour
		for(int i=joueurs.size()-1;i>=0;i--) {
			placeColonieTour12(joueurs.get(i),true);
		}
		
		afficherEtat();
		//Debut d'une tour normale
		while(!jeu.finPartie()) {
			for(int i=0;i<joueurs.size() && !jeu.finPartie();i++) {
	
				jeu.Affiche();//Affiche plateau
				
				System.out.println(joueurs.get(i).infoJoueur());//afficher les infos au joueur courant
				
				int des=jeu.lanceeDes();
				
				System.out.println("\n\t***** "+des+" au dés : *****");
				
				if(des==7) {
					AuDes7(joueurs.get(i));
					System.out.println(joueurs.get(i).infoJoueur());
				}
				
				tour(joueurs.get(i));

				if( joueurs.get(i).aGagner()) {
					jeu.setGagnant(joueurs.get(i));
				}
				
				//on vide les cartes qu'a acheter le joueur durant ce tour
				tourCourant.clear();
			}
		}
		afficherEtat();

		System.out.println("\n\t*********************ET LE GRAND GAGNANT EST ****************** \n est :\t"+
							jeu.getGagnant()+" avec un score de :"+jeu.getGagnant().getPointsVictoire()
							+" points de victoire!\b BRAVO champion!");

	
		sc.close();
	}


	
	
	/**
	 * Cette methode sert a initialiser notre jeu , demande a l'utilisateur s'il veut joueur
	 * ensuite créé les joueurs selon les entrées de l'utilisateur
	 * @param jeu
	 * @param joueurs
	 * @param sc
	 */
	public  void iniTialisationDuJeu() {
		boolean done=false;
		do {
			System.out.println("\nVoullez vous creer un joueur(yes/non)?"
					+ "\n Saisissez :"
					+ "\n\t yes: Pour creer un nouveau joueur"
					+ "\n\t no: pour lancer le jeu avec les joueurs deja créés");
			System.out.println("Vous avez creer " +joueurs.size()+
					" joueurs pour l'instant noubliez pas que c'est minimum 3"
					+ " et maximum 4 !");
			if(sc.hasNext()) {
				String s=sc.next();
				if(s.equals("yes")) {
					creerJoueur();
				}else if(s.equals("no")) {
					try {
						Lanceur.joueurs=joueurs;
						jeu=new Jeu (joueurs);
						done=true;
			
					}catch(IllegalArgumentException e) {
						System.out.println("\n !**ERREUR**! : "+e.getMessage());
						joueurs.clear(); //Vider ma liste
						System.out.println("\n\n\tRecommançant ");

					}
				}
			}
					
		}while(!done);
		

	
	}
	/**
	 *  Pour creer des joueur
	 * Ici pour cette version la couleur n'aura pas d'importance donc on met a null
	 * @param j la liste de nos joueurs qu'on auara recuperés
	 */
	public void creerJoueur() {
		
		String nom="";
		System.out.println("Quelle type de joueur souhaitez vous créer ?"
				+ "\n Saisissez :"
				+ "\n\t 1 : pour Humain"
				+ "\n\t 2 : pour IA");
		if(sc.hasNext()) {
			String choix=sc.next();
			if(choix.equals("1")) {
				System.out.println("\tSaisissez le nom du joueur");
				if(sc.hasNext()) {
					nom=sc.next();
					joueurs.add(new Joueur(nom));
				}
			}else if(choix.equals("2")){
				joueurs.add(new IA());
			}else {
				System.out.println("\nRespectez les choix proposez s'il vous plait !");
			}
		}
		

		
	}
	/**
	 * Cette methode sert a placer les colonies et routes le 1 er et second tour seulement 
	 * @param jeu le jeu afin de pouvoir mettre a jour notre plateau
	 * @param sc une scanner
	 * @param jr le joueur courant
	 */
	public void placeColonieTour12(Joueur j,boolean secondTour) {
		if(j instanceof IA) {
			placeColonieTour12IA((IA)j,secondTour);
		}else {
			placeColonieTour12H(j,secondTour);
		}
	}

	public void placeColonieTour12IA(IA j,boolean secondTour) {
		Boolean placeColonie=false;
		System.out.print("("+j+")");
		do {
			Location loc ;
				int x= (int)(1 + (Math.random() * (6 - 1)));
				int y= (int)(1 + (Math.random() * (6 - 1)));
				
				loc=new Location(x,y);
				if(loc!=null) {
					if(jeu.construireColonie12Tour(j,loc,secondTour)) {
						System.out.println("Colonie positionnée aux coordonnées :"
								+ "\n\t x ="+x
								+ "\n\t y ="+y);
						do {
							placeColonie=ConstruireRouteIA(j, false);
						}while(!placeColonie);
					}
				}
		}
		while(!placeColonie);
	}


	public void placeColonieTour12H(Joueur j,boolean secondTour) {
		Boolean placeColonie=false;
		jeu.Affiche();
		do {
				System.out.println("("+j.getNom()+")Construction Colonie + Route : \n\tNoubliez pas"
									+ " de \n\tPour la colonie : "
									+ "\n\t * Respeter la distance de minimum 2 routes entre 2 colonies"
									+ "\n\t * S'assurer que le sommet ne possede pas deja une colonie "
									+ "\n\n\t Pour la route :"
									+ "\n\t * Costruire une route liée a cette colonie que vous venez de creer");
				Location loc =askForCoordonne(sc,false);
				
				if(loc!=null) {
					if(jeu.construireColonie12Tour(j,loc,secondTour)) {
						do {
						placeColonie=ConstruireRoute(j, false);
						}while(!placeColonie);
					}else {
						System.out.println("\n\tERREUR :("+j.getNom()+") *Mauvais emplacement !");
						System.out.println("\n\t("+j.getNom()+") *Verifiez que les 3 conditions sont bien satisfaites pour la construction de votre colonie!");
						System.out.println("\t("+j.getNom()+") *Reessayez!\n");
					}
				}
			
		}while(!placeColonie);
			
	}

	public void tour(Joueur j) {
		if(j instanceof IA) {
			tourIA((IA)j);
		}else {
			tourH(j);
		}
	}
	
	public void tourIA(IA j) {
		/*commerceIA(j);
		ConstruireIA(j);*/
		CarteDevIA(j);
		
	}
	public void tourH(Joueur j) {

		boolean done=false;
		do {
			System.out.println("\n "+"("+j.getNom()+")Que souhaitez vous faire ? : \n\t Tapez :");
			System.out.println("\n\t\" 0 \": pour consulter vos ressources");
			System.out.println("\n\t\" 1 \": pour commercer(Acheter une ressources)");
			System.out.println("\n\t\" 2 \": pour construire (Colonie/Ville/Route)");
			System.out.println("\n\t\" 3 \": pour Joueur/Acheter une carte de developpement(CHEVALIER/PROGRES)");
			System.out.println("\n\t\" -1 \": pour passer au joueur suivant");

			if(sc.hasNext()) {
				String action=sc.next();
				switch(action) {
				case "0":System.out.println(j.infoJoueur());break;
				case "1":commerce(j);break;
				case "2":Construire(j);break;
				case "3":CarteDev(j);break;
				case "-1":done=true;break;
				default:System.out.println("("+j.getNom()+")Le numero choisi n'existe pas!"); break;
				}
			}
		}while(!done);
		
	}
	
	public void commerce(Joueur j) {		
		if(j instanceof IA) {
			commerceIA((IA)j);
		}else {
			commerceH(j);
		}
	}
	public void commerceIA(IA j) {
				
			String action=String.valueOf((int)((1+Math.random() * (3-1))));
			switch(action) {
				case "1":commerceInterieurIA(j);break;
				case "2":commerceExterieurIA(j);break;
				default:;
			}

	}
	public void commerceH(Joueur j) {
		boolean done=false;
		do {
			System.out.println("\n"+"("+j.getNom()+")Quel type de commerce voulez vous faire : \n\t Tapez :");
			System.out.println("\n\t\" 0 \": pour consulter vos ressources");
			System.out.println("\n\t\" 1 \": commerce intérieur (commerce avec vos adversaires) ");
			System.out.println("\n\t\" 2 \": commerce Maritime (commerce avec la banque/port)");
			System.out.println("\n\t\" -1 \": pour revenir en arriere(sortir)");
			if(sc.hasNext()) {
				
				String action=sc.next();
				switch(action) {
				case "0":System.out.println(j.infoJoueur());break;
				case "1":commerceInterieurH(j);break;
				case "2":commerceExterieurH(j);break;
				case "-1":done=true;break;
				default: System.out.println("("+j.getNom()+")Le numero choisi n'existe pas!");
				}
			}
		}while(!done);

	}
	private ArrayList<Joueur> adversaire(Joueur j){
		ArrayList<Joueur> advrsr= new ArrayList<Joueur>();
		for(Joueur jr : joueurs) {
			if(jr!=j) {
				advrsr.add(jr);
			}
		}
		return advrsr;
	}
	
	public void commerceInterieurIA(IA j) {

			ArrayList<Joueur> adversaires=adversaire(j);
			
			int x= (int)(Math.random() * (adversaires.size()));
			if(adversaires.size()>0) {
				Joueur adversaire=adversaires.get(x);
					ArrayList<String> resAacheter=new ArrayList<String> ();
					ArrayList<String> resAsacrifier=new ArrayList<String> ();
	
					String typeressource=askForRessourcesIA();
					resAacheter.add(typeressource);
	
					
					if(resAacheter.size()!=0) {	 //si la liste de ressources a echanger n'est pas vide					
						typeressource=askForRessourcesIA();
						resAsacrifier.add(typeressource);
							
						
						if(resAsacrifier.size()!=0 && jeu.commerceInterne(j,adversaire,resAsacrifier,resAacheter)) {
							System.out.println("\n\t("+j+") a fait du commerce interieur avec son adversaire : ("+adversaire+")");
							System.out.println("\t("+j+") a donner à ("+adversaire+")");
							
							for(String s:resAsacrifier) {
								System.out.println("\t  "+s);
							}
							System.out.println("\n\tEt a reçu :");
							
							for(String s:resAacheter) {
								System.out.println("\t  "+s);
							}
						}
					}
			}
		
	}
	private String askForRessourcesIA() {
		String s="";
		String action=String.valueOf(1+(int)((Math.random() * (6-1))));
		
		switch(action) {
			case "1":s="ARGILE";break;
			case "2":s="LAINE";break;
			case "3":s="MINERAI";break;
			case "4":s="BLÉ";break;
			case "5":s="BOIS";break;
		default:; break;
		}

		return s;
	}

	public void commerceExterieurIA(IA j) {			
			String resAacheter=askForRessourcesIA();
			String resAsacrifier=askForRessourcesIA();
			if(jeu.commerceExterne(j,resAsacrifier,resAacheter)!=1) {
				System.out.println("\n\t("+j+") a fait du commerce exterieur ");
			}
			
			
	}
	public void commerceInterieurH(Joueur j) {

		System.out.println("\n( "+j.getNom()+" )  un de vos adversaire pour negocier avec lui "
							+ "en saisissant son nom :");
		boolean done=false;
		do {
			ArrayList<Joueur> adversaires=adversaire(j);

			Joueur adversaire=askForAdversaire(adversaires,sc);
				if(adversaire != null) {// quand c'est null donc il a tapé -1 pour revenir en 
					
					System.out.println("\n\t Voila toutes les infos concerant l'adversaire selectionné ");
					System.out.println(adversaire.infoJoueur());
					ArrayList<String> resAacheter=new ArrayList<String> ();
					ArrayList<String> resAsacrifier=new ArrayList<String> ();

					System.out.println("\n"+"("+j.getNom()+")Quelles sont les ressources que vous voulez acheter : \n\t Tapez :");
					String typeressource=askForRessources(sc);
					while(!typeressource.equals("Fini")) {
						resAacheter.add(typeressource);
						typeressource=askForRessources(sc);
					}
					
					if(resAacheter.size()!=0) { //si la liste de ressources a echanger n'est pas vide
						System.out.println("\n"+"("+j.getNom()+")Quelles sont les ressources que vous voulez sacrifier : \n\t Tapez :");
						
						typeressource=askForRessources(sc);
						while(!typeressource.equals("Fini")) {
							resAsacrifier.add(typeressource);
							typeressource=askForRessources(sc);
						}
						
						if(resAsacrifier.size()!=0 
								&& jeu.commerceInterne(j,adversaire,resAsacrifier,resAacheter)) {
							done=true;
							System.out.println("("+j.getNom()+")Tout s'est bien passé");
						}else {

							System.out.println("("+j.getNom()+")ECHEC! vous ou l'adversaire selecionné n'avez pas"
									+ " les ressources mentionnées");//exception
							System.out.println("("+j.getNom()+")On recommence tout ");//exception

						}
					}else {//si la liste est vide donc il a tapé -1 pour revenir en arriere
						done=true;
					}
					
				}else {//le -1 pour revenir en arriere
					done=true;
				}
		}while(!done);
		
	}
	public static Joueur askForAdversaire(ArrayList<Joueur> joueurs,Scanner sc) {
		boolean done=false;
	
		do {
			System.out.println("Voici la liste des noms de tous les joueurs");

			for(Joueur jr:joueurs) {
				System.out.println("\t"+jr.getNom());
			}
		
			System.out.println("\n Nom de l 'adversaire :");
			if(sc.hasNext()) {
				
				String advrsr=sc.next();
				Joueur adversaire=null;
				for(Joueur jr:joueurs) {
					if(jr.getNom().equals(advrsr)){
						adversaire=jr;
						break;
					}
				}
				if(adversaire != null) {
					return adversaire;
				}
				if(advrsr.equals("-1")) {
					done=true;
				}
			}
		}while(!done);
		return null;
		
	}

	private static String askForRessources(Scanner sc){
		
		boolean correct=false;
		String s="";
		System.out.println("\n\nSelctionnez un numero: ");

		do {

		System.out.println("\n\t\" 1 \": pour Argile");
		System.out.println("\n\t\" 2 \": pour Laine");
		System.out.println("\n\t\" 3 \": pour Minerai");
		System.out.println("\n\t\" 4 \": pour Blé");
		System.out.println("\n\t\" 5 \": pour Bois");		
		System.out.println("\n\t\" -1 \": si vous avez terminé");		

				
			if(sc.hasNext()) {
				String ressource=sc.next();
				switch(ressource) {
				case "1":s="ARGILE";break;
				case "2":s="LAINE";break;
				case "3":s="MINERAI";break;
				case "4":s="BLÉ";break;
				case "5":s="BOIS";break;
				case "-1":s="Fini";break;
				default: System.out.println("Le numero choisi n'existe pas!"); break;
				}
				if(!s.equals("")) {
					correct=true;
				}else {
					System.out.println("Reessayez le chiffre n'existe pas!");
				}
			}
		}while(!correct);
		
		return s;
	}

	public void commerceExterieurH(Joueur j) {
		boolean correct=false;
		do {
			
			System.out.println("("+j.getNom()+")Quel type de ressource souhaitez vous acheter?");
			
			String resAacheter=askForRessources(sc);
			if(!resAacheter.equals("Fini")) {
				
				System.out.println("("+j.getNom()+")Quel type de ressource souhaitez vous sacrifier?");
	
				String resAsacrifier=askForRessources(sc);
				if(resAsacrifier.equals("Fini")) {
					correct =true;
				}else if( jeu.commerceExterne(j,resAsacrifier,resAacheter)==1){
					System.out.println("("+j.getNom()+")ECHEC ! Ressouces insufssantes ! Reesayez");
				}else {
					correct=true;
					System.out.println("("+j.getNom()+")L'operation a  bien reussi !!");
				}
			}else {
				correct=true;
			}

		}while(!correct);
	}

	public void Construire(Joueur j) {
		boolean done=false;
		do {
			System.out.println("("+j.getNom()+")\nQue souhaitez vous construire ? : \n\t Tapez :");
			System.out.println("\n\t\" 0 \": pour consulter vos ressources");
			System.out.println("\n\t\" 1 \":pour construire  une colonie");
			System.out.println("\n\t\" 2 \": pour construire une ville");
			System.out.println("\n\t\" 3 \": pour construire une route");
			System.out.println("\n\t\" 4 \": pour voir le plateau");
			System.out.println("\n\t\" -1 \": pour revenir en arriere");

			if(sc.hasNext()) {
				String action=sc.next();
				switch(action) {
				case "0":System.out.println(j.infoJoueur());break;
				case "1":if(ConstruireColonie(j)) {done=true;};break;
				case "2":if(ConstruireVille(j)){done=true;};break;
				case "3":if(ConstruireRoute(j,true)){done=true;};break;
				case "4":jeu.Affiche();break;
				case "-1":done=true;break;
				default:System.out.println("("+j.getNom()+")Le numero choisi n'existe pas!"); break;
				}
			}
		}while(!done);
	}
	public void ConstruireIA(IA j) {
		String action=String.valueOf((int)((1+Math.random() * (4-1))));
		
		switch(action) {
		case "1":ConstruireColonieIA(j);break;
		case "2":ConstruireVilleIA(j);break;
		case "3":ConstruireRouteIA(j,true);break;
		default:System.out.println("("+j.getNom()+")N'a pas construit!"); break;
		}
			
	}
	private Boolean ConstruireRouteIA(IA j, boolean enAchetant) {
		
		int x= (int)(1 + (Math.random() * (6 - 1)));
		int y=(int)(1 + (Math.random() * (6 - 1)));
		int a=(int)(Math.random() * (2));
		
		AreteLocation loc =new AreteLocation(x,y,a);
		
		try {
			if(jeu.construireRoute(j,loc,enAchetant)) {
				System.out.println("("+j+")Nouvelle Route Placée aux coordonnées :"
						+ "\n\t x ="+x
						+ "\n\t y ="+y
						+ "\n\t a= "+a);
				return true;
			}
		}catch(InsufficientResourcesException e) {}
		catch(CapacityReachedException e) {}
		
		return false;
		
	}
	private boolean ConstruireVilleIA(Joueur j) {
		int x= (int)(1 + (Math.random() * (6 - 1)));
		int y=(int)(1 + (Math.random() * (6 - 1)));
		
		Location loc =new Location(x,y);
		
		if(loc!=null) {
			try {
				if(jeu.construireVille(j, loc)) {
					System.out.println("("+j+")Nouvelle ville Placée aux coordonnées :"
							+ "\n\t x ="+x
							+ "\n\t y ="+y);
					return true;
				}	
			}catch(InsufficientResourcesException e) {}
			catch(CapacityReachedException e) {}
		}
		return false;
		
	}
	private boolean ConstruireColonieIA(Joueur j) {
		int x= (int)(1 + (Math.random() * (6 - 1)));
		int y=(int)(1 + (Math.random() * (6 - 1)));
		
		Location loc =new Location(x,y);
		
		if(loc!=null) {
			try {
				if(jeu.construireColonie(j, loc)) {
					System.out.println("("+j+")Nouvelle ville Placée aux coordonnées :"
							+ "\n\t x ="+x
							+ "\n\t y ="+y);
					return true;
				}
			}catch(InsufficientResourcesException e) {}
			catch(CapacityReachedException e) {}
		}
		return false;
		
	}

	
	private static boolean isNumeric(String strNum) {
	    if (strNum == null) {
	        return false;
	    }
	    try {
	        int d =Integer.parseInt(strNum);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;
	}
	public static Location askForCoordonne(Scanner sc,boolean route) {
		Boolean placeColonie=false;
		do {
			int x=0;
			int y=0;
			do {
				System.out.println("\n\nQuelle est la coordonée verticale x "
						+ "de la tuille correspondant au sommet choisi (0<x<6)\n x=");
				
				if(sc.hasNext()) {
					 String s=sc.next();
					 if(isNumeric(s)) {
						 x=Integer.valueOf(s);
					 }
				}
				if(x==-1)return null;
				else if(x<=0 || x>=6) {
					System.out.println("Reessayez !!");
				}
			}while(x<=0 || x>=6);
			
			do {
				System.out.println("\n\nQuelle est la coordonée horizontale y "
						+ "de la tuille correspondant au sommet choisi (0<y<6)\n y=");
				
				if(sc.hasNext()) {
					 String s=sc.next();
					 if(isNumeric(s)) {
						 y=Integer.valueOf(s);
					 }
				}
				if(y==-1)return null;
				else if(y<=0 || y>=6) {
					System.out.println("Reessayez !!");
				}
			}while(y<=0 || y>=6);
			

			if(route) {
				int a=-2;
				do {
					System.out.println("\n\nQuelle est l'arrete sur laquelle vous voulez"
							+ "positionner votre route \n\t 0: arete gauche \n\t 1: arete en haute \n a=");
					
					if(sc.hasNext()) {
						 String s=sc.next();
						 if(isNumeric(s)) {
							 a=Integer.valueOf(s);
						 }
					}
					if(a==-1)return null;
					else if(a!=0 && a!=1) {
						System.out.println("Reessayez !!");
					}
				}while(a!=0 && a!=1);
					return new AreteLocation(x,y,a);	
			}
			return new Location (x,y);
		}while(!placeColonie);
	}
	
	private boolean ConstruireRoute( Joueur j,boolean enAchetant) {
		System.out.println("("+j.getNom()+")Conctruction Route : \n\tNoubliez pas"
				+ " de  \n\t* Construire une route liée a l'une vos ancienne routes precedement"
				+ "crée ou a l'une de vos colonies!");
		if(enAchetant) {
			System.out.println("\n\t* Verifiez que vous avez bien les ressources requises\n\t\t*Argile\n\t\t*Bois");

		}
		
		Location loc =askForCoordonne(sc,true);
		if(loc!=null) {
			if(loc instanceof AreteLocation) {
				AreteLocation arloc=(AreteLocation)loc;
				try {
					if(jeu.construireRoute(j, arloc,enAchetant)) {
						System.out.println("Super!");
						return true;
					}else {
						System.out.println("("+j.getNom()+") ERREUR:Mauvais emplacement !");
					}
				}catch(InsufficientResourcesException e) {
					System.out.println("\n("+j.getNom()+") ERREUR:Pas assez de ressouces pour construire une route!");
				
				}catch(CapacityReachedException e) {
					System.out.println("\n("+j.getNom()+") ERREUR:Vous avez atteint le nombre de routes maximal"
							+ "que nous pouvez creer!");
				}
			}
		}
		
		return false;
		
	}


	private boolean ConstruireVille(Joueur j) {
		System.out.println("("+j.getNom()+")Conctruction Ville : \n\tNoubliez pas"
				+ " de  \n\tCostruire une ville uniquement sur un sommet ou figure l'une de vos colonies!"
				+ "\n\tVerifiez que vous avez bien les ressources requises\n\t 2 * Blé"
				+ "\n\t 3 * Argile");
		Location loc =askForCoordonne(sc,false);
		
		if(loc!=null) {
			try {
				if(jeu.construireVille(j, loc)) {
					System.out.println("Super!");
					return true;
				}else {
					System.out.println("("+j.getNom()+") ERREUR:Mauvais emplacement vous ne possedez pas de "
							+ "colonie a cet emplacement la a transfomer en ville!");
				}
			}catch(InsufficientResourcesException e) {
				System.out.println("("+j.getNom()+") ERREUR:Pas assez de ressouces pour construire une ville!");
			
			}catch(CapacityReachedException e) {
				System.out.println("("+j.getNom()+") ERREUR:Vous avez atteint le nombre maximal de villes"
						+ "que nous pouvez creer!");
			}
		}
		return false;
		
	}


	private boolean ConstruireColonie(Joueur j) {
		 
			System.out.println("("+j.getNom()+")Conctruction Colonie : \n\tNoubliez pas"
								+ " de  \n\tRespeter la distance de minimum 2 routes entre 2 colonies"
								+ "\n\tS'assurer que le sommet ne possede pas deja une colonie"
								+"\n\tVerifier que vous avez bien les ressources requises\n\tArgile"
								+ "\n\tBois\n\tLaine\n\tBlé");
			Location loc =askForCoordonne(sc,false);
			
			if(loc!=null) {
				try {
					if(jeu.construireColonie(j, loc)) {
						System.out.println("Super!");
						return true;
					}else {
						System.out.println("("+j.getNom()+") ERREUR:Mauvais emplacement !");
					}
				}catch(InsufficientResourcesException e) {
					System.out.println("("+j.getNom()+") ERREUR:Pas assez de ressouces pour construire une colonie!");
				
				}catch(CapacityReachedException e) {
					System.out.println("("+j.getNom()+") ERREUR:Vous avez atteint le nombre maximal de colonies"
							+ "que nous pouvez creer!");
				}
			}
			return false;
			
	}

	/**
	 * Choisir si on veut joueur ou plutot acheter une carte developpemnt
	 * @param j
	 */
	public void CarteDev(Joueur j) {
		boolean done=false;
		do {
			System.out.println("\n("+j.getNom()+")Que souhaitez vous faire : \n\t Tapez :");
			System.out.println("\n\t\" 0 \": pour consulter vos ressources");
			System.out.println("\n\t\" 1 \": Acheter une carte de developpement");
			System.out.println("\n\t\" 2 \": Joueur une carte de developpement");
			System.out.println("\n\t\" -1 \": pour revenir en arriere(sortir)");
			if(sc.hasNext()) {
				
				String action=sc.next();
				switch(action) {
				case "0":System.out.println(j.infoJoueur());break;
				case "1":AcheterCarteDev(j);break;
				case "2":JouerCarteDev(j);break;
				case "-1":done=true;break;
				default: System.out.println("\t("+j.getNom()+")Le numero choisi n'existe pas!");break;
				}
			}
		}while(!done);
	}
	public void CarteDevIA(IA j) {
		String action=String.valueOf((int)((1+Math.random() * (3-1))));

		switch(action) {
			case "1":AcheterCarteDev(j);break;
			case "2":JouerCarteDevIA(j);break;
		default: System.out.println("\n\t("+j+")N'a pas acheter/Joueur de carte developpement !");break;
		}
	}
	
	private void JouerCarteDevIA( IA j) {
			String s=askForCarteDevIA();
			if(!s.equals("")) {
				if(j.aCarteDev(s)) {//s'il a la carte developpemnt selectionnée
					if(tourCourant.contains(s) && j.getTypeCarteDev(s)==1) {//si la carte a été achetée lors du tour courant
						return;
						
					}else {
						switch(s) {
							case "REPLACER ROUTE":CarteDevReplacerRouteIA(j);break; 
							case "CONSTRUCTION DE ROUTES":CarteDev2RoutesGruitesIA(j);break;
							case "INVENTION":CartesDevInventionIA(j);break;
							case "MONOPOLE":CarteDevMonopoleIA(j);break;
							case "CHEVALIER":CarteDevChevalierIA(j);break;
							default:System.out.println("\n\tL("+j+") a essayer de joueur une carte developpemnt mais n'a pas pu !"); break;
						}
					}
				}
				
			}
	
	}

	/**
	 * Methode qui permet au joueur de jouer une carte dev
	 * @param j
	 */

	private void JouerCarteDev( Joueur j) {
		boolean done=false;
		do {
			String s=askForCarteDev(sc);
			if(!s.equals("Fini")) {//s'il n'a pas tapé -1 pour revenir en arriere
				if(j.aCarteDev(s)) {//s'il a la carte developpemnt selectionnée
					if(tourCourant.contains(s) && j.getTypeCarteDev(s)==1) {//si la carte a été achetée lors du tour courant
						System.out.println("Vous ne pouvez pas utiliser cette carte de developpement"
								+ "il faut attendre qu'au moins un tour passe apres l'avoir achetée");
						
					}else {
						switch(s) {
							case "REPLACER ROUTE":CarteDevReplacerRoute(j);done=true;break; 
							case "CONSTRUCTION DE ROUTES":CarteDev2RoutesGruites(j);done=true;break;
							case "INVENTION":CartesDevInvention(j);done=true;break;
							case "MONOPOLE":CarteDevMonopole(j);done=true;break;
							case "CHEVALIER":CarteDevChevalier(j);done=true;break;
							default:System.out.println("Le numero choisi n'existe pas!"); break;
						}
					}
				}else {
					System.out.println(" ERREUR:Vous ne poccedez pas la carte selectionnée !");
				}
				
			}else {
				done=true;
			}
		}while(!done);
		
	
	}

	private void CarteDevReplacerRouteIA(IA j) {
		int x= (int)(1 + (Math.random() * (6 - 1)));
		int y=(int)(1 + (Math.random() * (6 - 1)));
		
		int a=(int)((Math.random() * (2)));

		AreteLocation loc =new AreteLocation(x,y,a);
		
		if(loc!=null) {
			if(j.equals(jeu.getPlateau().getRoute(loc).getJoueur())){

				if(ConstruireRouteIA(j, false)) {
					jeu.getPlateau().getRoute(loc).setJoueur(null);
					j.removeCard("REPLACER ROUTE");					
					System.out.println("\n\t("+j+")a bien deplacer sa route !");

				}
			}
		}
	}
	
	private void CarteDevReplacerRoute(Joueur j) {
		System.out.println("\n\t("+j+")Quelles sont les coordonnées de la route que vous souhaitez deplacer ?");

		AreteLocation loc =(AreteLocation)askForCoordonne(sc, true);
		
		if(loc!=null) {
			if(j.equals(jeu.getPlateau().getRoute(loc).getJoueur())){
				System.out.println("\n\t("+j+")Quelles sont les nouvelles coordonnées de la route que vous souhaitez deplacer ?");

				if(ConstruireRoute(j, false)) {
					jeu.getPlateau().getRoute(loc).setJoueur(null);
					j.removeCard("REPLACER ROUTE");
					System.out.println("\n\t("+j+")Vous avez bien deplacer votre route !");
				}
			}else {
				System.out.println("\n\t("+j+")il est necessaire pour cette carte developpemnt de selectionner une route que vous possedez deja !");
			}
		}
	}
	/**
	 * Joueur la carte dev chevalier
	 * @param j
	 */
	private void CarteDevChevalier(Joueur j) {
		if(Voleur(j)) {
			j.removeCard("CHEVALIER");
			j.plusdeChevalier();
			if(j.getNbrChevalier()>=3) {
			boolean PaspremierAavoirPlusGrandArmee=false;
				for(Joueur jr:joueurs) {
					if(jr.getPLusGrandeArmee()) {
						PaspremierAavoirPlusGrandArmee=true;
					}
					if(jr.getPLusGrandeArmee() && j.getNbrChevalier()>jr.getNbrChevalier()) {
						jr.setPointVictoire(jr.getPointsVictoire()-2);
						jr.PossedePLusGrandeARmee(false);
						
						j.setPointVictoire(j.getPointsVictoire()+2);
						j.PossedePLusGrandeARmee(true);
						break;
					}
				}
				
				if(!PaspremierAavoirPlusGrandArmee) {//si il est le premier a atteindre les 3 chvaliers
					j.setPointVictoire(j.getPointsVictoire()+2);
					j.PossedePLusGrandeARmee(true);
				}
			}
		}else {
			System.out.println(" ERREUR:La carte chevalier n'a pas été utilisée"
					+ "reessayez si vous voulez!");
		}
		
	}
	private void CarteDevChevalierIA(IA j) {
		if(VoleurIA(j)) {
			System.out.println("\n\t("+j+")A bien joué sa carte CHEVALIER!");
			j.removeCard("CHEVALIER");
			j.plusdeChevalier();
			if(j.getNbrChevalier()>=3) {
			boolean PaspremierAavoirPlusGrandArmee=false;
				for(Joueur jr:joueurs) {
					if(jr.getPLusGrandeArmee()) {
						PaspremierAavoirPlusGrandArmee=true;
					}
					if(jr.getPLusGrandeArmee() && j.getNbrChevalier()>jr.getNbrChevalier()) {
						jr.setPointVictoire(jr.getPointsVictoire()-2);
						jr.PossedePLusGrandeARmee(false);
						
						j.setPointVictoire(j.getPointsVictoire()+2);
						j.PossedePLusGrandeARmee(true);
						break;
					}
				}
				
				if(!PaspremierAavoirPlusGrandArmee) {//si il est le premier a atteindre les 3 chvaliers
					j.setPointVictoire(j.getPointsVictoire()+2);
					j.PossedePLusGrandeARmee(true);
				}
		
			}
		}
		
	}
	/**
	 * La methode sert a permettre au joueur courant de voler un adversaire
	 * @param j joueur courant
	 * @return
	 */
	private boolean Voleur(Joueur j ) {
		boolean done=false;
		System.out.println("("+j.getNom()+") Etape 01 : Delpacer le voleur : \n\t"
				+ "pour cela saisissaiez les coordonée de la nouvelle tuile sur "
				+ "laquelle vous souhaitez positionner le voleur : ");
		do {
			Location loc=askForCoordonne(sc,false);
			if(loc!=null) {
				if(DeplacerVoleur(loc)) {
					ArrayList<Joueur> tousLesJoueurAutourDeLoc=jeu.TousLesJoueursAutourDeTuile(loc);
					if(tousLesJoueurAutourDeLoc.contains(j)) {//au cas ou le joueur courant aussi a une 
															//colonie au tour de la nouvelle tuile ou y a le voleur
						tousLesJoueurAutourDeLoc.remove(j);
					}
					if(tousLesJoueurAutourDeLoc.size()==0) {
						System.out.println("\n\t("+j+")Vous n'avez aucune personne autour de cette tuile a la voler!");
					}else {
						Joueur adversaire=askForAdversaire(tousLesJoueurAutourDeLoc,sc);
						if(adversaire!=null) {
							jeu.voler(j, adversaire);
							System.out.println("\n\t("+j+")Bien joué chipeur");
						}
					}
					
					done=true;
				}else {
					System.out.println("\n\t("+j+")ERREUR: Mauvais emplacement !\n\tReessayez de replacer le voleur !!");
				}
			}
		}while(!done);
		
		return true;
	}
	
	private boolean VoleurIA(IA j ) {
		boolean done=false;
		do {
			int x= (int)(1 + (Math.random() * (6 - 1)));
			int y=(int)(1 + (Math.random() * (6 - 1)));
			
			Location loc =new Location(x,y);
			
			if(loc!=null) {
				if(DeplacerVoleur(loc)) {
					ArrayList<Joueur> tousLesJoueurAutourDeLoc=jeu.TousLesJoueursAutourDeTuile(loc);
					if(tousLesJoueurAutourDeLoc.contains(j)) {//au cas ou le joueur courant aussi a une 
															//colonie au tour de la nouvelle tuile ou y a le voleur
						tousLesJoueurAutourDeLoc.remove(j);
					}
					if(tousLesJoueurAutourDeLoc.size()!=0) {
						int r=(int)((Math.random() * (tousLesJoueurAutourDeLoc.size())));
						Joueur adversaire=tousLesJoueurAutourDeLoc.get(r);
						if(adversaire!=null) {
							jeu.voler(j, adversaire);
							System.out.println("\n\t("+j+")Bien joué chipeur , vous avez volé : "+adversaire);
						}
					}
					
					done= true;
				}
			}
		}while(!done);
		
		return true;
	}
	
	public boolean DeplacerVoleur(Location loc) {
		return jeu.deplacerVoleur(loc);
	}
	/**
	 * Methode qui s'occupe de la mise a jour du jeu lorsqu'un joueur joue une carte dev MONOPOLE
	 * @param j
	 */
	private void CarteDevMonopole( Joueur j) {
		String s=askForRessources(sc);
		jeu.PrendreToutMonopole(j, s);
		j.removeCard("MONOPOLE");
		System.out.println("\n\t("+j+")bien joué pour votre carte developpement de type monopole !");

	}
	private void CarteDevMonopoleIA( IA j) {
		
		String s=askForRessourcesIA();
		jeu.PrendreToutMonopole(j, s);
		j.removeCard("MONOPOLE");
		System.out.println("\n\t("+j+")a bien joué sa carte MONOPOLE !");
	}

	/**
	 * le joueur choisi deux cartes ressources et les pioches
	 * @param j
	 */

	private void CartesDevInvention( Joueur j) {
		ArrayList<String>res=new  ArrayList<String>();
		while(res.size()<2) {
			String s=askForRessources(sc);
			if(!s.equals("Fini")) {
				res.add(s);
			}
		}
		j.RecevoirRessources(res);
		j.removeCard("INVENTION");
	}

	private void CartesDevInventionIA( IA j) {
		ArrayList<String>res=new  ArrayList<String>();
		while(res.size()<2) {
			String s=askForRessourcesIA();
				res.add(s);
		}
		j.RecevoirRessources(res);
		j.removeCard("INVENTION");
		System.out.println("\n\t("+j+")A bien joué sa carte INVENTION");
	}

	/**
	 * carte dev qui offre deux construction de route gratuites
	 * @param joueur courant
	 */
	private void CarteDev2RoutesGruites(Joueur j) {
		if(ConstruireRoute(j, false) && ConstruireRoute(j, false) ) {
			j.removeCard("CONSTRUCTION DE ROUTES");
		}else if(ConstruireRoute(j, false) || ConstruireRoute(j, false)){
			j.removeCard("CONSTRUCTION DE ROUTES");
		}else {
			System.out.println("\n\t("+j.getNom()+") ERREUR:Ressayez de rejouer votre carte , la construction"
					+ " de vos 2 routes n'a pas reussi !");
		}
	}
	/**
	 * Permet a l IA de creer ses deux routes offertes
	 * @param j IA
	 */
	private void CarteDev2RoutesGruitesIA(IA j) {
		if(ConstruireRouteIA(j, false) && ConstruireRouteIA(j, false) ) {
			j.removeCard("CONSTRUCTION DE ROUTES");
			System.out.println("\n\t("+j+") A bien joué sa carte CONSTRUCTION DE ROUTE!");
		}else if(ConstruireRouteIA(j, false) || ConstruireRouteIA(j, false)){
			j.removeCard("CONSTRUCTION DE ROUTES");
			System.out.println("\n\t("+j+") A bien joué sa carte CONSTRUCTION DE ROUTE!");

		}
		

	}

	/**
	 * Cette methode permet au joueur d acheter une carte s'il a bien les ressources requises
	 * @param j le joueur
	 */
	private void AcheterCarteDev(Joueur j) {
		try {
			if(jeu.AcheterCarteDev(j)) {
				CarteDev cd=j.getMain().get(j.getMain().size()-1);//Recupéréer la carte achetée
				String s=cd.getType(); //on aura besoin que de son type /CHEVALIER/MONOPOLE/POINT DE VICTOIRE..
				if(cd.getSousType()!=null) {//si la carte a un sous type  j'enregistre celui la dans ma liste(PROGRES--> MONOPOLE  par exemple)
					s=cd.getSousType();
				}
				
				if(!s.equals("POINT DE VICTOIRE")) {//Pour sauvegarder les cartes dev qui ont été achetées lors du tour courant
													// mis apart les point de victoire car ce ne sont pas des cartes a jouer
													//on les reveles a la fin (on cumule directement les points de victoire lors de l'achat)
					tourCourant.add(s);
				}
				System.out.println("Super");
			}
		}catch(InsufficientResourcesException e) {
			if(!(j instanceof IA))System.out.println("\n\tERREUR : Verifiez que vous avez bien les ressources requises!");
		} catch (CapacityReachedException e) {
			if(!(j instanceof IA))System.out.println("\n\tERREUR : Vous ne pouvez plus piocher !La pioche est vide!");
		}
		
	}
	


	/**
	 * 	Cette methode nous permet de recuperer la cartes dev selecionnée par le joueur 
	 * @param sc
	 * @return
	 */
	private static String askForCarteDev(Scanner sc){

		boolean correct=false;
		String s="";
		System.out.println("\n\nSelctionnez la carte developpement en tapant sur : ");
		do {

		System.out.println("\n\t\" 1 \": pour jouer une carte PROGRES de type CONSTRUCTION DE ROUTES ");
		System.out.println("\n\t\" 2 \": pour jouer une carte PROGRES de type INVETION");
		System.out.println("\n\t\" 3 \": pour jouer une carte PROGRES de type MONOPOLE ");
		System.out.println("\n\t\" 4 \": pour jouer une carte CHEVALIER");
		System.out.println("\n\t\" 5 \": pour jouer une carte REPLACER ROUTE");

		

	
		System.out.println("\n\t\" -1 \": si vous avez terminé");		

				
			if(sc.hasNext()) {
				String ressource=sc.next();
				
				switch(ressource) {
				
					case "1":s="CONSTRUCTION DE ROUTES";break;
					case "2":s="INVENTION";break;
					case "3":s="MONOPOLE";break;
					case "4":s="CHEVALIER";break;
					case "5":s="REPLACER ROUTE";break;
					case "-1":s="Fini";correct=true;break;
					default: break;
				}
				
				if(!s.equals("")) {
					correct=true;
				}else {
					System.out.println("\n\tERREUR : Reessayez le chiffre n'existe pas!");
				}
			}
		}while(!correct);
		
		return s;
	}
	/**
	 * Recuperer une carte developpemnt choisie
	 * @return
	 */
	private static String askForCarteDevIA(){

		String choix=String.valueOf((int)((1+Math.random() * (6-1))));
		String s="";
	
		switch(choix) {
		
			case "1":s="CONSTRUCTION DE ROUTES";break;
			case "2":s="INVENTION";break;
			case "3":s="MONOPOLE";break;
			case "4":s="CHEVALIER";break;
			case "5":s="REPLACER ROUTE";break;
			default: break;
		}
		
		return s;
	}

	/**
	 * Afficher toutes les ressources des joueurs ainsi que toutes les information les concernat
	 * @param joueurs
	 */
	public void afficherEtat() {
		for(Joueur j:joueurs) {
			System.out.println(j.infoJoueur());
		}
	}
	
	/************    7 AU DÉS   ************/
	/**
	 * Methode qui s'occupe de faire perdre a tous les joueurs ayant plus de 
	 * 7 carte lors d'un 7 au Des
	 */
	public void defausserMoitierCartes() {
		for(Joueur j: jeu.getJoueur()) {
			int nbrCourantCartes=j.NombreTotatlDeRessources();
			if(nbrCourantCartes>7) {
				int nbrCartesaDefausser=nbrCourantCartes/2;
				while(nbrCartesaDefausser>0) {
					
					if(!(j instanceof IA)) {
						System.out.println("\n\tERREUR : ("+j.getNom()+")Voila tout ce que vous poccedez : ");
						System.out.println(j.infoJoueur());
						System.out.println("\n\tERREUR :("+j.getNom()+")Veuillez selectionner "+nbrCartesaDefausser+" Cartes a defausser s'il vous plait !");
					}
					
					HashMap<String, Integer> res2=new HashMap<String, Integer>();
					res2.put("ARGILE", 0);
					res2.put("LAINE", 0);
					res2.put("MINERAI", 0);
					res2.put("BLÉ", 0);
					res2.put("BOIS", 0);
					
					int somme=0;
					do {
						for(Map.Entry<String, Integer> entry : res2.entrySet()) {
							boolean has=false;
							int v=0;
							do {
								if(!(j instanceof IA)) {
									System.out.println("Pour "+entry.getKey());
									v=askForBeaucoupRessources(sc);
								}else {
									int max=j.getNbrRessource(entry.getKey());
									v=(int)(0+ (Math.random() * (max+1)));

								}
								if(j.hasRessources(entry.getKey(), v)) {
									res2.put(entry.getKey(),v);
									somme+=v;
									if(!( j instanceof IA))System.out.println("\n\t("+j+")Super!\n");

									has=true;
								}else {
									if(!(j instanceof IA)) {
										System.out.println("\n\tERREUR : Vous n'avez pas autant de "+entry.getKey()+
												"\n\t Reessayez !");
									}
								}
							}while(!has);
							
						}
						if(somme!=nbrCartesaDefausser) {
							if(!(j instanceof IA)) {
								System.out.println("\n\t ERREUR : Veuillez selectionner le nombre exacte de ressources s'il vous plait!!"
										+ " \n\t Reessayez !");
							}
							somme=0;

						}
					}while(somme!=nbrCartesaDefausser);

					if(somme<=nbrCartesaDefausser) {
						for(Map.Entry<String, Integer> entry : res2.entrySet() ) {
							j.EnleverRessources(entry.getKey(),entry.getValue());
							nbrCartesaDefausser-=somme;
						}
					}else {
						if(!(j instanceof IA)) {
							System.out.println("\n\tERREUR : Veuillez selectionner le nombre exactedes ressources que vous pocedez s'il vous plait!");
						}
					}
				}
			}
		}
	}
	/**
	 * Demander le nombre de ressources 
	 * @param sc
	 * @return nombre de ressources pour chaque type
	 */
	private static int askForBeaucoupRessources(Scanner sc) {
		boolean done=false;
		String s="";
		do {
			if(sc.hasNext()) {
				s=sc.next();
				if(isNumeric(s)) {
					if(Integer.valueOf(s)>=0) {
						done=true;
					}else {
						System.out.println("\n\tERREUR : Reessayez!");
					}
				}else {
					System.out.println("\n\tERREUR : Veuillez saisir un bon numero s'il vous plait !");
				}
				
			}
		}while(!done);
		return Integer.valueOf(s);
	}
	
	/**
	 * Methode qui gere tout lors d'un 7 au des
	 * @param jr le joueur courant
	 */

	public void AuDes7( Joueur jr) {
		boolean done=false;
		defausserMoitierCartes();
		do {
			if(jr instanceof IA) {
				done=VoleurIA((IA)jr);
			}else {
				done=Voleur(jr);

			}
			
		}while(!done);
	}

	

}