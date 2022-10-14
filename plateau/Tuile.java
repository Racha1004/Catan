package plateau;
public class Tuile {
	private final String type;
	/*Les valeurs possibles :
	 * Foret Pré Champs Colline Montagne  
	 */
	private int ethiquette = 0;
	private boolean aVoleur = false;
	private Location location;
	public Tuile(String type, int x, int y , int ethiq ) {
		this.type=type;
		location = new Location(x,y);
		ethiquette=ethiq;
	}
	public Tuile(String type) {
		this.type=type;
	}
	public Tuile(String type, boolean b) {
		this.type=type;
		aVoleur = b;
	}
	public Location getLocation() {
		return location;
	}
	public void setCoords(int x, int y) {
		location = new Location(x, y);
	}
	public void setEthiquette(int ethiq) {
		ethiquette=ethiq;
	}
	public int getEthiquette() {
		return ethiquette;
	}

	public String getType() {
		return type;
	}
	
	public boolean aVoleur() {
		return aVoleur;
	}
	
	public void setVoleur(boolean b) {
		aVoleur = b;
	}
	public String toString() {
		return type+"( "+ethiquette+" )";
	}
	//cette methode retourn le nom de la ressource associée à une tuile
	public String nomRessouce() {
		String s="";
		switch(type) {
		case "Foret" : s="BOIS"; break;
		case "Pré" : s="LAINE"; break;
		case "Champs" : s="BLÉ"; break;
		case "Colline" : s="ARGILE"; break;
		case "Montagne" : s="MINERAI"; break;
		default: break;
		}
		return s;
	}
}