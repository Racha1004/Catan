package jeu;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import exceptions.CapacityReachedException;
import exceptions.InsufficientResourcesException;
import plateau.CarteDev;
import plateau.Route;

public class Joueur {

	private final String nom;
	private Color couleur;
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
						// 3 = MINERAI
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
	
	public Joueur(String n) {
		nom=n;
		routes= new ArrayList<Route>();

		ressources=new HashMap<String,Integer>();
		ressources.put("ARGILE", 0);
		ressources.put("LAINE", 0);
		ressources.put("MINERAI", 0);
		ressources.put("BLÉ", 0);
		ressources.put("BOIS", 0);

		main=new ArrayList<CarteDev>();
	}
	
	public String infoJoueur() {
		String s="**"+nom+"**\n Ressources : ";
		for(Map.Entry x : ressources.entrySet()) {
			s+="\n\t"+x.getKey()+" : "+x.getValue();
		}
		s+="\n Carte de developpement ("+main.size()+") : ";
		for(CarteDev  x : main) {
			s+=" \n\t"+x;
		}
		s+="\n\n Nombre de chevalier : "+nbrChevalier
				+"\n Nombre de Colonies : "+nbrColonies
				+"\n Nombre de Villes : "+nbrVilles
				+"\n Nombre de Routes : "+nbrRoutes
				+"\n\n Points de Victoire : "+pointsVictoire;
		s+="\n\n La plus grande armée :"+aLaPlusGrandeArmee;
		s+="\n\n Ports:";
		int i=0;
		for(int j=0;j<ports.length;j++) {
			if(ports[j]) {
				switch(j){
					case 0:  s+="\n\tport general";i++;break;
					case 1:  s+="\n\tPort d'argile";i++;break;
					case 2:  s+="\n\tPort de laine";i++;break;
					case 3:  s+="\n\tPort de minerai";i++;break;
					case 4:  s+="\n\tPort de blé";i++;break;
					case 5:  s+="\n\tPort de bois";i++;break;
					default:break;
				}
			}
		}
		if(i==0)s+="\n\tPas de ports";
		return s;
	}
	public String toString() {
		return nom;
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
		if (str == null || ressources.get(str)==null)
			return -1;
		else {
			return ressources.get(str);
		}
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
	public void addCarteDev(CarteDev cdv) {
		main.add(cdv);
	}
	
	
	public ArrayList<CarteDev> getMain(){
		return main;
	}
	
	
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
	 * Recuperation de la liste des ressources du joueur courant
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
	
	public int getNbrChevalier() {
		return nbrChevalier;
	}
	public void PossedePLusGrandeARmee(boolean b) {
		aLaPlusGrandeArmee=true;
	}
	
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
	//Surcharge
	public boolean hasRessources(String res,int n) {
		return ressources.get(res)>=n;
	}
	/**
	 * verifier si l utilisateur a dans sa main la carte dev(str) passée en parametre
	 * @param str
	 * @return
	 */
	
	public boolean aCarteDev(String str) {
		for(CarteDev c:main) {
			if(c.getType().equals(str) || c.getSousType()!=null && c.getSousType().equals(str)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean aCarteDev(CarteDev cd) {
		for(CarteDev c:main) {
			if(c.equals(cd)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * supprimer la carte dev de nom str
	 * @param str 	par exemple: CHEVALIER/MONOPOLE/NVENTION
	 */
	public void removeCard(String str){
		for(CarteDev c:main) {
			if(c.getType().equals(str) || c.getSousType()!=null && c.getSousType().equals(str)) {
				main.remove(c);
				return;
			}
		}
	}
	
	 
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
		if(portTag<ports.length) {
			ports[portTag]=true;
		}
	}
	
	public boolean[] getPorts() {
		return ports;
	}

	/*public String toString() {
		return nom;
	}*/
	
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
	public void addPointDeVictoire() {
		pointsVictoire++;
	}
	public void EnleverRessources(ArrayList<String> res) {
		for(String s:res) {
			setNbrRessource(s,getNbrRessource(s)-1);
		}
	}
	
	//Overloading
	public void EnleverRessources(String res,int n) {
		setNbrRessource(res,getNbrRessource(res)-n);
	}
	
	public void RecevoirRessources(ArrayList<String> res) {
		for(String s:res) {
			setNbrRessource(s, getNbrRessource(s)+1);
		}
	}
	
	public void RecevoirRessources(String res,int n) {
		if(getNbrRessource(res)!=-1)
		setNbrRessource(res, getNbrRessource(res)+n);
	}
	
	public int NombreTotatlDeRessources() {
		return (getNbrRessource("ARGILE")+
				getNbrRessource("LAINE")+
				getNbrRessource("BOIS")+
				getNbrRessource("MINERAI")+
				getNbrRessource("BLÉ"));
	}
	
	public int getTypeCarteDev(String str) {
		int nbr=0;
		for(CarteDev cd:main) {
			if(cd.getType().equals(str) || cd.getSousType()!=null && cd.getSousType().equals(str)) {
				nbr++;
			}
		}
		return nbr;
	}
	

	public boolean PeutAcheterRoute() throws  InsufficientResourcesException,CapacityReachedException{
		if(nbrRoutes<15){//On peut au plus construire 15 routes
			ArrayList<String> res=new ArrayList<String>();
			res.add("ARGILE");
			res.add("BOIS");
			if(hasRessources(res)){
				return true;
			}
			throw new InsufficientResourcesException();
		}
		throw new CapacityReachedException();

	}
	public void AcheterRoute(){
			ArrayList<String> res=new ArrayList<String>();
			res.add("ARGILE");
			res.add("BOIS");
				
			EnleverRessources(res);
			nbrRoutes++;
		
	}

	public boolean PeutAcheterColonie() throws  InsufficientResourcesException,CapacityReachedException{
		if(nbrColonies<5){//On peut au plus construire 5 colonies
			ArrayList<String> res=new ArrayList<String>();
			res.add("ARGILE");
			res.add("BOIS");
			res.add("LAINE");
			res.add("BLÉ");
			if(hasRessources(res)){
				return true;
			}
			throw new InsufficientResourcesException();
		}
		throw new CapacityReachedException();

	}
	public void AcheterColonie(){
		ArrayList<String> res=new ArrayList<String>();
		res.add("ARGILE");
		res.add("BOIS");
		res.add("LAINE");
		res.add("BLÉ");
		
		EnleverRessources(res);
		nbrColonies++;
		pointsVictoire++;
			
	}
	
	public void AcheterVille(){
			ArrayList<String> res=new ArrayList<String>();
			res.add("MINERAI");
			res.add("MINERAI");
			res.add("MINERAI");
			res.add("BLÉ");
			res.add("BLÉ");
			
			EnleverRessources(res);
			nbrVilles++;
			nbrColonies--;
			pointsVictoire-=1;
			pointsVictoire+=2;
			
	}
	public boolean PeutAcheterVille() throws  InsufficientResourcesException,CapacityReachedException {
		if(nbrVilles<4){//On peut au plus construire 4 villes
			ArrayList<String> res=new ArrayList<String>();
			res.add("MINERAI");
			res.add("MINERAI");
			res.add("MINERAI");
			res.add("BLÉ");
			res.add("BLÉ");
			if(hasRessources(res)){
				return true;
			}
			throw new InsufficientResourcesException();
		}
		throw new CapacityReachedException();

	}
	
	public boolean AcheterCartesDev(CarteDev cdev)  throws  InsufficientResourcesException{
		ArrayList<String> res=new ArrayList<String>();
		res.add("MINERAI");
		res.add("LAINE");
		res.add("BLÉ");
		if(hasRessources(res)){
			EnleverRessources(res);
			main.add(cdev);
			if(cdev.getType().equals("POINT DE VICTOIRE")) {
				pointsVictoire++;
			}
			return true;
		}
		throw new InsufficientResourcesException();

	}
	
	public void annulerAcheterVille(){
		ArrayList<String> res=new ArrayList<String>();
		res.add("MINERAI");
		res.add("MINERAI");
		res.add("MINERAI");
		res.add("BLÉ");
		res.add("BLÉ");
	
		RecevoirRessources(res);
		nbrVilles--;
		nbrColonies++;
		pointsVictoire+=1;
		pointsVictoire-=2;
	
		}
	public void annulerAcheterColonie(){
		ArrayList<String> res=new ArrayList<String>();
		res.add("ARGILE");
		res.add("BOIS");
		res.add("LAINE");
		res.add("BLÉ");

		RecevoirRessources(res);
		nbrColonies--;
		pointsVictoire--;

	}
	public void annulerAcheterRoute(){
		ArrayList<String> res=new ArrayList<String>();
		res.add("ARGILE");
		res.add("BOIS");

		RecevoirRessources(res);
		nbrRoutes--;
	}
	public boolean aGagner() {
		return pointsVictoire>=10;

	}
	public void setCouleur (Color c) {
		couleur=c;
	}
	
	
	
	
	
	
	
	
}