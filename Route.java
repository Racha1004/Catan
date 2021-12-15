
public class Route {
	private Joueur joueur=null;
	private AreteLocation location;
	private boolean visited; 
	public Route(int x , int y , int a) {
		location = new AreteLocation(x,y,a);
	}
	public void setJoueur(Joueur j) {
		if (joueur==null)
			joueur=j;
	}
	public Joueur getJoueur() {
		return joueur;
	}
	public void setLocation(AreteLocation loc) {
		location = loc;
	}
	public AreteLocation getLocation() {
		return location;
	}
	public boolean isVisited() {
		return visited;
	}
	public void visit() {
		visited = true;
	}
	public void resetVisited() {
		visited = false;
	}
}
