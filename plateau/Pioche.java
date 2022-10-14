package plateau;

import java.util.ArrayList;
import java.util.Collections;

public class Pioche {

	private ArrayList<CarteDev> cartes;
	
	public Pioche() {
		cartes=new ArrayList<CarteDev>(25);

		for(int i=0;i<14;i++) {
			cartes.add(new CarteDev("CHEVALIER"));
		}
		
		cartes.add(new CarteDev("PROGRES","CONSTRUCTION DE ROUTES"));
		cartes.add(new CarteDev("PROGRES","CONSTRUCTION DE ROUTES"));
		cartes.add(new CarteDev("PROGRES","INVENTION"));
		cartes.add(new CarteDev("PROGRES","INVENTION"));
		cartes.add(new CarteDev("PROGRES","MONOPOLE"));
		cartes.add(new CarteDev("PROGRES","MONOPOLE"));

		cartes.add(new CarteDev("REPLACER ROUTE"));
		cartes.add(new CarteDev("REPLACER ROUTE"));
		cartes.add(new CarteDev("REPLACER ROUTE"));

		for(int i=0;i<5;i++) {
			cartes.add(new CarteDev("POINT DE VICTOIRE"));
		}	
		
		Collections.shuffle(cartes);
	}
	
	public CarteDev getPioche(){
		return cartes.get(cartes.size()-1);
	}
	
	public boolean Vide() {
		return cartes.size()==0;
	}
	
	public void piocher() {
		cartes.remove(cartes.size()-1);
	}
	
	public String toString() {
		String s="Pioche:";
		for(CarteDev cd:cartes) {
			s+="\n\t"+cd;
		}
		return s;
	}
}
