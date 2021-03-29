package Modele;


import java.awt.Point;


import Vue.Affichage;


public class Etat {

	private  Road road; //pour recuperer l'instance de la Road, neccesaire pour le testRalentissementRoad() et testRalentissementObstacles()
	private  Obstacles obstacles; //pour recuperer l'instance de la Road, neccesaire pour le testRalentissementRoad() et testRalentissementObstacles()
	
	private  int positionVehicule; //variable qui donne l'abscisse de la voiture, initilalisee dans le constructeur

	private static final int DEPLACEMENT_MAX=20;	// variable definisant la taille d'un deplacement de la voiture
	private  int deplacement= DEPLACEMENT_MAX;	// variable definisant la taille d'un deplacement de la voiture
	

	/** CONSTRUCTEUR */
	public Etat(Road roa, Obstacles obs) {
		this.road =roa;
		this.obstacles=obs;
		positionVehicule = Affichage.getAbsVehicule(); //je prefere initialiser positionVoiture l'ors de l'instanciation de la Classe.
		
	}

	/**************METHODES GET et SET *******************/
	/**
	 * donne la derniere abscisse connue de la voiture
	 */
	public int getPositionVehicule(){ 
		return this.positionVehicule;
	}
	
	public void setPositionVehicule(int x) {
		this.positionVehicule=x;
	}


	public static int getDeplacementMax() {
		return DEPLACEMENT_MAX;
	}
	
	public int getDeplacement() {
		return deplacement;		
}
	/**
	 * on ajoute x a la valeur de this.deplacement
	 */
	public void setDeplacement(double x) {
		if((int)x<=DEPLACEMENT_MAX)
		this.deplacement=(int)x;
	}
	

	
	
	/**************AUTRE METHODES *******************/
	
	/**
	 * setFin() nous indique si la variable deplacement est a 0 (ou inferieur) car alors la voiture ne se depalce plus et c'est la fin de la partie
	 * cette methode est appellee par la classe (thread) Avancer
	 * @return booleen
	 */
	public boolean setFin() {
		if (deplacement<=0) {
			return true;
		}
		return false;
	}
	/**
	 * la methode testRalentissement() recupere 2 points, un au dessous  et un au dessus de l'abscisse du vehicule (positionvoiture).
	 * ensuite elle calcule le "coefficient de pente entre les 2 points grace a leurs Abscisses
	 * puis elle trouve en operant un equation a une inconnue l'abcsisse du point la pente correspondant a l'ordonnee du centre du vehicule
	 * et ceci pour l'ordonnee du point de la ligneGauche et celui de la ligneDroite
	 * Enfin cette methode teste si la gauche du vehicule depasse l'abscisse du point de ligneGauche ou la droite du vehicule l'abscisse du point de ligneDroite
	 * si cest le cas, elle renvoie le nombre de pixels d'espacement entre le  vehicule et de  la route .
	 * @return Float indiquant le nombre de pixel d'eloignement entre le vehicule et de  la route.
	 */
	public boolean testRalentissementRoad() {
		
		int indexP1= road.getPointProches();//on recupere l'index du premier point en dessous de la voiture
		
		int indexP2= indexP1+1; //on recupere l'index du premier point au dessus de la voiture
		
		/**Pour Ligne Gauche*/		
		Point p1g =road.getLigneGauche().get(indexP1); // on recupere le p1 de ligneGauche
		Point p2g =road.getLigneGauche().get(indexP2);// on recupere le p2 de ligneGauche
		float penteg = (float)((p2g.x) - (p1g.x) )/ ((float)(p2g.y) - (float)(p1g.y)); //calcul de la pente entre deux points 		
		
		//prochaine ligne : calcul l'abscisse point ligneGauche (le -LargVehicule est pour montrer de la clemence envers le pied sur la ligne)	
		float pointxDeGauche =  (-penteg*(p2g.y-Affichage.getOrdVehicule())+p2g.x)-Affichage.getLargVehicule() ;
		
		/**Pour Ligne Droite*/	//meme chose que pour ligneGauche mais pour la droite (a quelques signes pres)
		Point p1d =road.getLigneDroite().get(indexP1); 
		Point p2d =road.getLigneDroite().get(indexP2);
		float pented = (float)((p2d.x) - (p1d.x) )/ ((float)(p2d.y) - (float)(p1d.y)); 
		float pointxDeDroite =  (-pented*(p2d.y-Affichage.getOrdVehicule())+p2d.x)+Affichage.getLargVehicule();
		
		if(pointxDeGauche >= positionVehicule){ // si le point de la ligne de gauche touche ou depasse la gauche de la voiture	
					
			return  true;
		}
		else if(pointxDeDroite <= positionVehicule+Affichage.getHautVehicule()){// si le point de la ligne de droite  touche ou depasse la droite de la voiture
			
			return true;
		}
		else {	
			
			return false;
		}
	}

	
		
		//on r�cup differents points (un carr�) pour la hit box des obstacles
		//
		public boolean testRalentissementObstacles() {
			int tailleHitbox=10;
			//on sait qu'il aura tjrs 1 point et 1 seul en dessous de celui succeptible de toucher le vehicule. d'ou le get(1) ci dessous (get(0+1) )
			Point boueeDroite =road.getLigneDroite().get(1); 
			Point boueeGauche =road.getLigneGauche().get(1); 		
			//le premier if marche pour boueeGauche et Bouee droite car elles ont la meme ordonnee.
			if(((boueeGauche.y+tailleHitbox)>=Affichage.getOrdVehicule() ) && ((boueeGauche.y+tailleHitbox)<=(Affichage.getOrdVehicule()+Affichage.getHautVehicule()))) {
				if(((boueeGauche.x+tailleHitbox) >= positionVehicule  && (boueeGauche.x+tailleHitbox) <= (positionVehicule+Affichage.getLargVehicule()))
				   ||
				   ((boueeGauche.x-tailleHitbox) >= positionVehicule && (boueeGauche.x-tailleHitbox) <= (positionVehicule+Affichage.getLargVehicule()))) {
					    return true;
				}
				if(((boueeDroite.x+tailleHitbox) >= positionVehicule  && (boueeDroite.x+tailleHitbox) <= (positionVehicule+Affichage.getLargVehicule()))
				   ||
				   ((boueeDroite.x-tailleHitbox) >= positionVehicule && (boueeDroite.x-tailleHitbox) <= (positionVehicule+Affichage.getLargVehicule()))) {
						return true;
			    }
			}
			//on sait qu'il aura tjrs 1 point et 1 seul en dessous de celui succeptible de toucher le vehicule. d'ou le get(1) ci dessous (get(0+1) )
			Point obstacle = obstacles.getObstacleList().get(1);
			if(((obstacle.y+tailleHitbox)>=Affichage.getOrdVehicule() ) && ((obstacle.y+tailleHitbox)<=(Affichage.getOrdVehicule()+Affichage.getHautVehicule()))) {
				if(((obstacle.x+tailleHitbox) >= positionVehicule  && (obstacle.x+tailleHitbox) <= (positionVehicule+Affichage.getLargVehicule()))
				   ||
				   ((obstacle.x-tailleHitbox) >= positionVehicule && (obstacle.x-tailleHitbox) <= (positionVehicule+Affichage.getLargVehicule()))) {

					return true;
				}
			}	
			return false;
		}
		
		
		/**
		 * Methode appelle par la Classe Controls, deplace le vehicule vers la gauche ou la droite d'un nombre de pixel egal a DEPLACEMENT
		 */
	public  void move(Controller.Direction d) {
			switch (d) {
			/*case up:				
				break;
			case down:				
				break;*/
			case right:
				this.positionVehicule=this.positionVehicule+this.deplacement;
				break;		
			case left:
				this.positionVehicule=this.positionVehicule-this.deplacement;
				break;
			default:
				break;
			}	
	}
	
	
}

