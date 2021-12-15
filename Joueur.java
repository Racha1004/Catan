import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Joueur {

	private final String nom;
	private final Color couleur;
	private HashMap<String,Integer> ressources;
	private ArrayList<CarteDev> main;
	private ArrayList<Route> routes;
	private int nbrChevalier=0;
	private int nbrColonies=2;
	private int nbrRoutes=2;
	private int nbrVilles=0;
	private int pointsVictoire=2;
	private boolean aLaPlusGrandeArmee;
	private boolean [] ports= {false, false, false, false, false, false};
						// 0 = general
						// 1 = ARGILE
						// 2 = LAINE
						// 3 = MINERA
						// 4 = BLÉ
						// 5 = BOIS	
	
	/**
	 * @param n nom du joueur
	 * @param c coueur du joueur
	 */
	public Joueur(String n,Color c) {
		
		nom=n;
		couleur=c;
		routes= new ArrayList<Route>();
		
		ressources=new HashMap<String,Integer>();
		ressources.put("ARGILE", 0);
		ressources.put("LAINE", 0);
		ressources.put("MINERAI", 0);
		ressources.put("BLÉ", 0);
		ressources.put("BOIS", 0);
		
		main=new ArrayList<CarteDev>();
		
	}
	/**
	 * 
	 * @param n nom du joueur
	 * @param c sa couleur
	 * @param argile nombre de ressources en argile
	 * @param laine nombre de ressources en laine
	 * @param minerai nombre de ressources en minerai
	 * @param ble nombre de ressources en blé
	 * @param bois nombre de ressources en bois
	 * @param PV points de victoire
	 */
	public Joueur(String n,Color c,int argile,int laine,int minerai,int ble,int bois,int PV) {
		
		this(n,c);
		
		setNbrRessource("ARGILE",argile);
		setNbrRessource("LAINE",laine);
		setNbrRessource("MINERAI",minerai);
		setNbrRessource("BLÉ",ble);
		setNbrRessource("BOIS",bois);

		setPointVictoire(PV);
		
		
	}
	
	/**
	 * 
	 * @return le nom du joueur
	 */
	public String getNom() {
		return nom;
	}
	
	/**
	 * 
	 * @return la couleur du joueur
	 */
	public Color getCouleur() {
		return couleur;
	}
	
	/**
	 * 
	 * @return les points de victoire (le score du joueur)
	 */
	public int getPointsVictoire() {
		return pointsVictoire;
	}
	
	/**
	 * Actualiser les points de victoire
	 * @param n le nouveau nombre de points de victoire 
	 */
	public void setPointVictoire(int n) {
		pointsVictoire=n;
	}
	
	public int getNbrRessource(String str) {
		if (str == null)
			return 0;
		return ressources.get(str);
	}

	/**
	 * ACtualiser nos ressources uen par une selon le string passé en parametre
	 * @param s le nom de la ressource
	 * @param n le nouveau nombre de cartes de ressources
	 */
	public void setNbrRessource(String s,int n) {
		ressources.put(s,Integer.valueOf(n));
	}
	
	//TODO
	public void addCarteDev(CarteDev cdv) {}
	
	//TODO
	/*
	public ArrayList<CarteDev> getMain(){
		return main;
	}
	*/
	
	/**
	 * Pour ajouter une nouvelle route a notre liste de routes
	 * @param r la nouvelle route
	 */
	public void addRoute(Route r) {
		routes.add(r);
	}
	
	/**
	 * Recuperation de la liste de nos routes
	 * @return
	 */
	public ArrayList<Route> getRoutes(){
		return routes;
	}
	
	/**
	 * Recuperation de la liste de nos ressources
	 * @return
	 */
	public ArrayList<String> getRessources(){
		ArrayList<String> res=new ArrayList<String>();
		for(Entry<String, Integer> s:ressources.entrySet()) {
			if(s.getValue()>0) {
				res.add(s.getKey());
			}
		}
		return res;
	}
	
	/**
	 * On incremente le noombre de chevalier
	 */
	public void plusdeChevalier() {
		nbrChevalier++;
	}
	/*
	public void PossedePLusGrandeARmee(boolean b) {
		aLaPlusGrandeArmee=true;
	}
	*/
	public boolean getPLusGrandeArmee() {
		return aLaPlusGrandeArmee;
	}
	
	public boolean hasRessources(ArrayList<String> res) {
		int argile=0,
				laine=0,
				minerai=0,
				ble=0,
				bois=0;
		for(String s:res) {
			switch(s) {
				case "ARGILE": argile++;break;
				case "LAINE": laine++;break;
				case "MINERAI": minerai++;break;
				case "BLÉ": ble++;break;
				case "BOIS": bois++;break;
				
				default:break;
			}
		}
		return !(argile>ressources.get("ARGILE") ||
				laine>ressources.get("LAINE") ||
				minerai>ressources.get("MINERAI") ||
				ble>ressources.get("BLÉ") ||
				bois>ressources.get("BOIS"));
	}
	
	/**
	 * verifier si l utilisateur a dans sa main la carte dev(str) passsée en parametre
	 * @param str
	 * @return
	 */
	/*
	public boolean aCarteDev(String str) {
		return true;
	}*/
	
	/**
	 * supprimer la carte dev de nom str
	 */
	/*
	public void removeCard(String str){
		
	}
	*/
	 
	/**
	 * Ajouter un port a notre liste de ports
	 * @param portTag
	 * 					// 0 = general
						// 1 = ARGILE
						// 2 = LAINE
						// 3 = MINERA
						// 4 = BLÉ
						// 5 = BOIS	
	
	 */
	public void addPort(int portTag) {
		ports[portTag]=true;
	}
	

	public String toString() {
		return nom;
	}
	
	/**
	 * Getter for numbSettlements
	 * @return int number of settlements
	 */
	public int getNbrColonnies() {
		return nbrColonies;
	}

	/**
	 * Getter for numbCities
	 * @return int number of cities
	 */
	public int getNbrVilles() {
		return nbrVilles;
	}

	/**
	 * Getter for numbRoads
	 * @return int number of roads
	 */
	public int getNbrRoutes() {
		return nbrRoutes;
	}
	
	public void addColonie() {
		nbrColonies++;
	}
	
	public void addVille() {
		nbrVilles++;
		nbrColonies--;
	}
	
	public void addRoute() {
		nbrRoutes++;
	}
	
	public void recevoirUneRessource(String res) {
		if(res==null ) {
			return;
		}
		ressources.put(res, ressources.get(res)+1);
	}
	
	public void EnleverRessources(ArrayList<String> res) {
		for(String s:res) {
			setNbrRessource(s,getNbrRessource(s)-1);
		}
	}
	
	public void RecevoirRessources(ArrayList<String> res) {
		for(String s:res) {
			setNbrRessource(s, getNbrRessource(s)+1);
		}
	}
	
	public int NombreTotatlDeRessources() {
		return (getNbrRessource("ARGILE")+
				getNbrRessource("LAINE")+
				getNbrRessource("BOIS")+
				getNbrRessource("MINERAI")+
				getNbrRessource("BLÉ"));
	}
	
	public int getTypeCarteDev(String str) {
		return 0;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
