package plateau;

public class Colonie extends Construction{
	public Colonie(int x, int y ) {
		setLocation(new Location(x, y));
		setType(0);
	}
	public void donnerRessources(String resType) {
		getJoueur().RecevoirRessources(resType,1);
	}
}