
public abstract class Construction {
	private Joueur joueur=null;
	private Location location;
	private int type; //type=0 si c'est une colonie, type=1 sinon
	public abstract void donnerRessources(String resType);
	public void setJoueur(Joueur j) {
		if (joueur==null)
			joueur=j;
	}
	public Joueur getJoueur() {
		return joueur;
	}
	public void setLocation(Location loc) {
		location = loc;
	}
	public Location getLocation() {
		return location;
	}
	public int getType() {
		return type;
	}
	public void setType(int t) {
		type = t;
	}
	
}
