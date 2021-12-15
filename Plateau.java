import java.util.*;
public class Plateau {
	private Tuile[][] tuiles;
	private Construction[][] constructions;
	private Route[][][] routes;
	private Location voleur;
	public Plateau() {
		tuiles = new Tuile[6][6];
		constructions= new Construction[6][6];
		routes = new Route[6][6][2];
		Tuile desert = new Tuile("Desert",true);
		//on crée une ArrayList avec toutes les tuiles que le plateau peut contenir
		ArrayList<Tuile> listeTuiles = new ArrayList<Tuile>();
		listeTuiles.add(new Tuile("Foret")); listeTuiles.add(new Tuile("Foret"));  listeTuiles.add(new Tuile("Foret"));
		listeTuiles.add(new Tuile("Pre")); listeTuiles.add(new Tuile("Pre"));  listeTuiles.add(new Tuile("Pre"));
		listeTuiles.add(new Tuile("Champs")); listeTuiles.add(new Tuile("Champs")); listeTuiles.add(new Tuile("Champs"));
		listeTuiles.add(new Tuile("Colline")); listeTuiles.add(new Tuile("Colline")); listeTuiles.add(new Tuile("Colline"));
		listeTuiles.add(new Tuile("Montagne")); listeTuiles.add(new Tuile("Montagne")); listeTuiles.add(new Tuile("Montagne"));
		listeTuiles.add(desert);
		//on fait un random pour la liste des tuiles créé
		Collections.shuffle(listeTuiles);
		int index=0;
		for(int lig = 0 ; lig<6 ; lig++ ) {
			for(int col = 0 ; col<6 ; col++ ) {
				if(lig==0 || lig==5 || col==0 || col==5) 
					tuiles[lig][col] =new Tuile(null,lig,col,0);
				else {

					tuiles[lig][col] = listeTuiles.get(index);
					tuiles[lig][col].setCoords(lig, col);
					index++;
				}
			}
		}
		//on intialise la position du voleur
		voleur = desert.getLocation();
		//on crée un tableau qui contient toutes les éthiquettes du plateau
		int[] ethiquettes = {6,10,11,8,4,9,5,12,3,10,6,9,8,5,2};
		index=0;
		for(int lig = 1 ; lig<5 ; lig++ ) {
			for(int col = 1 ; col<5 ; col++ ) {
				if(!tuiles[lig][col].getType().equals("Desert")) {
					tuiles[lig][col].setEthiquette(ethiquettes[index]);
					index++;
				}
			}
		}
		//on place toutes les construction dans le plateau
		for(int lig = 0 ; lig<6 ; lig++ ) {
			for(int col = 0 ; col<6 ; col++ ) {
				constructions[lig][col] = new Colonie(lig,col);
			}
		}
		//on place toutes les routes dans le plateau
		for(int lig = 0 ; lig<6 ; lig++ ) {
			for(int col = 0 ; col<6 ; col++ ) {
				for(int o=0 ; o<routes[0][0].length ; o++) {
					routes[lig][col][o] = new Route(lig,col,o);
				}
			}
		}
	}
	public Construction getConstructions(Location loc) {
		return constructions[loc.getX()][loc.getY()];
	}
	public void setConstructions(Location loc , Construction c) {
		constructions[loc.getX()][loc.getY()]=c;
	}
	public Route getRoute(AreteLocation loc) {
		return routes[loc.getX()][loc.getY()][loc.getArete()];
	}
	//Cette methode retourne une liste de toutes les tuiles avec le nombre ethiq
	private ArrayList<Tuile> getTuileAvecEthiquette(int ethiq) {
		ArrayList<Tuile> tuilesAthiq = new ArrayList<Tuile>();

		for (int i = 1; i < tuiles.length-1; i++) {
			for (int j = 1; j < tuiles[0].length-1; j++) {
				if (tuiles[i][j].getEthiquette() == ethiq)
					tuilesAthiq.add(tuiles[i][j]);
			}
		}
		return tuilesAthiq;
	}
	//Cette methode permet de distribuer à tous les joueurs qui ont des construction aux sommets de la tuile avec l'ethiquette (ethiq) les ressources de cette tuiles
	public void repartitionRessources(int ethiq) {
		ArrayList<Tuile> listeTuiles = getTuileAvecEthiquette(ethiq);
		for (Tuile t : listeTuiles) {
			if (!t.aVoleur() && !t.getType().equals("Desert")) {
				ArrayList<Construction> listeConstructions= new ArrayList<Construction>();
				Location loc = t.getLocation();
				listeConstructions.add(constructions[loc.getX()][loc.getY()]);
				listeConstructions.add(constructions[loc.getX()][loc.getY()+1]);
				listeConstructions.add(constructions[loc.getX()+1][loc.getY()]);
				listeConstructions.add(constructions[loc.getX()+1][loc.getY()+1]);
				for(Construction c : listeConstructions) {
					if(c.getJoueur()!=null) {
						c.donnerRessources(t.nomRessouce());
					}
				}
			}
		}
	}
	/*cette methode check si une location donnée est un port est retourne :
	 * 0 si c'est port général 
	 * 1 si c'est un port de mouton
	 * 2 si c'est un port de bois
	 * 3 si c'est un port d'argile
	 * 4 si c'est un port de minerai
	 * 5 si c'est un port de blé
	 * -1 SINON  
	 */
	private int checkPort(Location loc) {
		int x= loc.getX();
		int y= loc.getY();
		if((x==1 && y==2)|| (x==1 && y==3))
			return 1;
		else if((x==2 && y==5)|| (x==3 && y==5))
			return 2;
		else if((x==5 && y==4)|| (x==5 && y==3))
			return 3;
		else if((x==5 && y==1)|| (x==5 && y==2))
			return 4;
		else if((x==5 && y==1)|| (x==5 && y==2))
			return 4;
		else if((x==3 && y==1)|| (x==4 && y==1))
			return 5;
		else if((x==1 && y==1)|| (x==2 && y==1) || (x==1 && y==4)|| (x==1 && y==5) || (x==4 && y==5)|| (x==5 && y==5))
			return 4;
		else return -1;
	}
	/*cette methode permet d'ajouter une colonie à un plateau (elle est utilisé dans le premier et le second tours */
	public boolean ajouterConstructionSansRoute(Location loc , Joueur j) {
		if(constructions[loc.getX()][loc.getY()].getJoueur()!=null) {
			return false;
		}
		if(constructions[loc.getX()-1][loc.getY()].getJoueur()==null && 
		   constructions[loc.getX()][loc.getY()-1].getJoueur()==null && 
		   (loc.getY()+1>=6 || constructions[loc.getX()][loc.getY()+1].getJoueur()==null) &&
		   (loc.getX()+1>=6 || constructions[loc.getX()+1][loc.getY()].getJoueur()==null)) {
				if(this.checkPort(loc)!=-1) {
					//joueur.addPort(checkPort(loc));
				}
				constructions[loc.getX()][loc.getY()].setJoueur(j);
				return true;
		}
	
		return false;
	}
	/*cette methode permet d'ajouter une colonie à un plateau*/
	public boolean ajouterConstruction(Location loc , Joueur j) {
		if(constructions[loc.getX()][loc.getY()].getJoueur()!=null) {
			return false;
		}
		if(constructions[loc.getX()-1][loc.getY()].getJoueur()==null && 
		   constructions[loc.getX()][loc.getY()-1].getJoueur()==null && 
		   (loc.getY()+1>=6 || constructions[loc.getX()][loc.getY()+1].getJoueur()==null) &&
		   (loc.getX()+1>=6 || constructions[loc.getX()+1][loc.getY()].getJoueur()==null)) {
				if(j.equals(routes[loc.getX()][loc.getY()][0].getJoueur())||
				   j.equals(routes[loc.getX()][loc.getY()][1].getJoueur())||
				   j.equals(routes[loc.getX()-1][loc.getY()][0].getJoueur())||
				   j.equals(routes[loc.getX()][loc.getY()-1][1].getJoueur())) {
					if(this.checkPort(loc)!=-1) {
						//joueur.addPort(checkPort(loc));
					}
					constructions[loc.getX()][loc.getY()].setJoueur(j);
					return true;
				}
		}
		return false;
	}
	/**cette methode permet d'ajouter une ville au plateau*/
	public boolean ajouterVille(Location loc ,Joueur j) {
		if(j.equals(constructions[loc.getX()][loc.getY()].getJoueur()) && constructions[loc.getX()][loc.getY()].getType()==0) {
			constructions[loc.getX()][loc.getY()].setType(1);
			return true;
		}
		return false;
	}
	public boolean ajouterRoute(AreteLocation loc , Joueur j) {
		if(routes[loc.getX()][loc.getY()][loc.getArete()].getJoueur()!=null){
			return false;
		}
		if(loc.getArete()==0) {
			if(j.equals(constructions[loc.getX()][loc.getY()].getJoueur()) || j.equals(constructions[loc.getX()+1][loc.getY()].getJoueur())) { 
				routes[loc.getX()][loc.getY()][loc.getArete()].setJoueur(j);
				return true;
			}
			else {if(j.equals(routes[loc.getX()+1][loc.getY()][0].getJoueur()) ||
					j.equals(routes[loc.getX()+1][loc.getY()][1].getJoueur()) ||
					j.equals(routes[loc.getX()+1][loc.getY()-1][1].getJoueur()) ||
					j.equals(routes[loc.getX()][loc.getY()][1].getJoueur()) ||
					j.equals(routes[loc.getX()-1][loc.getY()][0].getJoueur()) ||
					j.equals(routes[loc.getX()][loc.getY()-1][1].getJoueur()) ) {
						routes[loc.getX()][loc.getY()][loc.getArete()].setJoueur(j);
						return true;
					}
			}
		}
		else {
			if(loc.getArete()==1) {
				if(j.equals(constructions[loc.getX()][loc.getY()].getJoueur()) || j.equals(constructions[loc.getX()][loc.getY()+1].getJoueur())) {
					routes[loc.getX()][loc.getY()][loc.getArete()].setJoueur(j);
					return true;
				}
				else {if(j.equals(routes[loc.getX()][loc.getY()+1][0].getJoueur()) ||
						j.equals(routes[loc.getX()][loc.getY()+1][1].getJoueur()) ||
						j.equals(routes[loc.getX()-1][loc.getY()+1][0].getJoueur()) ||
						j.equals(routes[loc.getX()][loc.getY()][0].getJoueur()) ||
						j.equals(routes[loc.getX()+1][loc.getY()][0].getJoueur()) ||
						j.equals(routes[loc.getX()][loc.getY()-1][1].getJoueur()) ) {
							routes[loc.getX()][loc.getY()][loc.getArete()].setJoueur(j);
							return true;
						}
				}
		}
		}
		
		return false;
	}
	public boolean deplacerVoleur(Location loc) {
		Location courante = voleur;
		if(loc.getX()!=courante.getX() || loc.getY()!=courante.getY()){
			tuiles[loc.getX()][loc.getY()].setVoleur(true);
			tuiles[courante.getX()][courante.getY()].setVoleur(false);
			voleur.setX(loc.getX());
			voleur.setY(loc.getY());
			return true;
		}
		return false;
	}
	public ArrayList<Tuile> getAdjacenteTuiles(Location loc){
		ArrayList<Tuile> listeTuile = new ArrayList<Tuile>();
		Tuile a = tuiles[loc.getX()][loc.getY()];
		if (a.getType() != null)
			listeTuile.add(a);
		Tuile b = tuiles[loc.getX()-1][loc.getY()];
		if (b.getType() != null)
			listeTuile.add(b);
		Tuile c = tuiles[loc.getX()][loc.getY()-1];
		if (c.getType() != null)
			listeTuile.add(c);
		Tuile d = tuiles[loc.getX()-1][loc.getY()-1];
		if (d.getType() != null)
			listeTuile.add(d);
		return listeTuile;
	}
	public void afficher() {
		for(int i=1 ; i<5 ; i++) {
			for(int j=1 ; j<5 ; j++) {
				System.out.print(tuiles[i][j]+" | ");
			}
			System.out.print("\n");
		}
	}
}
