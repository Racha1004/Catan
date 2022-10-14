package plateau;

public class Ville extends Construction{
	public Ville(int x, int y ) {
		setLocation(new Location(x, y));
		setType(1);
	}
	public void donnerRessources(String resType) {
		getJoueur().RecevoirRessources(resType,2);
	}
}