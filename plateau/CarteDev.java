package plateau;

public class CarteDev {
	private final String type;
	/**
	 * Ici pour le type soit on aura:
	 * 	CHEVALIER (14)
	 * 	PROGRES (6)
	 * 	POINT DE VICTOIRE (5)
	 */
	private final String sousType;
	/**
	 * Ici pour le sousType:
	 * 	pour CHEVALIER: 
	 * 			on a pas de sous Type les insctuctions sont les memes pour toutes les cartes chevalier:
	 *				-Le joueur deplace le voleur
	 *				-Il designe ensuite un joueur parmi ceux presents sur la tuile ou est placé le voleur
	 *					pour lui piquer une carte(au hasard)
	 *				-Si un joueur atteint 3 cartes CHEVALIER  c celui ci recois la fiche armée plus puissante
	 *					qui lui permettra de gagner 2 points de victoire
	 * 	pour PROGRES:
	 * 			on dispose de 3 sous types:
	 * 				1-CONSTRUCTION DE ROUTES: le joueur peut construire gratuitement 2 routes (2)
	 * 				2-INVENTION:le joueur peut piocher 2 cartes matieres premieres de son choix et les utiliser
	 * 							directement pout construire avec (2)
	 * 				3-MONOPOLE: le joueur designe une matiere premiere et tous les joueurs la possédant, lui 
	 * 							passeront l'ensemble de leurs cartes(2)
	 * pour POINT DE VICTOIRE:(5)
	 * 			on a pas de sous type ici non plus , le joueur gagne avec celle ci un point de victoire 
	 * 			elle est revelée a la fin 
	 *
	 */
	
	
	/**
	 * Pour les carteDev qui n ont pas de sous type
	 * @param s : CHEVALIER/PROGRES/POINT DE VICTOIRE
	 */
	public CarteDev(String type) {
		this.type=type;
		sousType=null;
	}
	
	/**
	 * Pour les catre Progres pour designer si cest CONSTRUCTION DE ROUTE/INVENTION ou MONOPOLE
	 * @param type  PROGRES
	 * @param sousType CONSTRUCTION  DE ROUTES/INVENTON/MONOPOLE
	 */
	public CarteDev(String type,String sousType) {
		this.type=type;
		this.sousType=sousType;
	}
	
	/**
	 * Pour avoir le type de notre carte developpement
	 * @return
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Pour avor le sousType de notre carte PROGRES
	 * @return on aura soit ("CONSTRUCTION DE ROUTES","INVENTION"","MONOPOLE")
	 */
	public String getSousType() {
		return sousType;
	}
	
	public String toString() {
		String s="";
		s= type ;
		if(sousType!=null){
			s+=" ("+sousType+")";
		}
		return s;
	}
}
