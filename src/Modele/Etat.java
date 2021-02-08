package Modele;


import java.awt.Point;


import Vue.Affichage;


public class Etat {

	private  Road road; //pour recuperer l'instance de la Road, neccesaire pour le testPerdu()

	private  int positionVoiture; //variable qui donne l'ordonnee du centre du cercle, initilalisee dans le constructeur

	private final  int DEPLACEMENT=55;	// constante definisant la taille d'un deplacement de la voiture

	private boolean flagTestPerdu = false;
	

	/** Constructeur */
	public Etat(Road roa) {
		this.road =roa;
		positionVoiture = Affichage.getAbsVoiture(); //je prefere initialiser positionVoiture l'ors de l'instanciation de la Classe.

	}

	/**************METHODES GET *******************/

	public int getPositionVoiture(){ 
		return positionVoiture;
	}
	
	public boolean getFlagTestPerdu(){ 
		return flagTestPerdu;
	}

	/**************AUTRE METHODES *******************/

	/**
	 * testPerdu() recupere 2 points, un a gauche et un a droite de l'abscisse du cercle.
	 * ensuite elle calcule le "coefficient de pente entre les 2 points grace a leurs Ordonnees
	 * puis elle trouve en operant un equation a une inconnue l'ordonnee du point la pente correspondant a l'abscisse du centre du cercle
	 * et ceci pour l'ordonnee du point de la ligneHaut et celui de la ligneBas
	 * Enfin cette methode teste si le haut du cercle depasse l'ordonnee du point de ligne haut ou le bas du cercle l'ordonnee du point de ligneBas
	 * si cest le cas, elle renvoie true, sinon false.
	 * @return boolean indiquant si la partie est perdue ou non (true=oui)
	 */
	public boolean testPerdu() {
		int indexP1= road.getPointProches();//on recupere l'index du premier point en dessous de la voiture
		int indexP2= indexP1+1; //on recupere l'index du premier point au dessus de la voiture
		Point p1 =road.getLigneGauche().get(indexP1); // on recupere le p1 de ligneGauche
		Point p2 =road.getLigneGauche().get(indexP2);// on recupere le p2 de ligneGauche
		//a noter que la seule difference avec les points de ligneDroite est le (+ LARGEUR_ROUTE) de leur ordonnee

		float pente = ((p1.x) - (p2.x) )/ ((float)(p1.y) - (float)(p2.y)); //calcul de la pente entre deux points, la meme pour la ligneGauche et ligneDroite 
		float pointxDeLaDroiteDeGauche = ( -pente*(p1.y-Affichage.getOrdVoiture())+p1.x );//calcul l'ordonnee du point de la pente de ligneGauche (avec p.y=cercle.y)
		float pointxDeLaDroiteDeDroite = ( -pente *(p1.y-Affichage.getOrdVoiture()) + (p1.x-Road.getLargeurRoute()) );//meme calcul pour ligneHaut

		if(pointxDeLaDroiteDeGauche <= (positionVoiture+Affichage.getTailleVoiture())){ // si le point de la ligne de gauche touche ou depasse la gauche de la voiture	
			return true;
		}
		else if(pointxDeLaDroiteDeDroite >= positionVoiture){// si le point de la ligne de droite  touche ou depasse la droite de la voiture
			return true;
		}
		else {	
			return false;
		}
	}

	public  void move(Controler.Direction d) {
			switch (d) {
			/*case up:				
				break;
			case down:				
				break;*/
			case right:
				this.positionVoiture=this.positionVoiture+this.DEPLACEMENT;
				break;		
			case left:
				this.positionVoiture=this.positionVoiture-this.DEPLACEMENT;
				break;
			default:
				break;
			}	
	}
	
	
}

