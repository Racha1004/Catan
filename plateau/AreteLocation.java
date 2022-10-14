package plateau;

public class AreteLocation extends Location{
	private int arete; // si elle est égale à 0 donc c"est l'arete gauche , si elle est égale à 1 c"est l'arete haute
	public AreteLocation(int x, int y, int a) {
		super(x, y);
		arete = a;
	}
	public int getArete() {
		return arete;
	}
}