package Modele;

import java.util.Random;

import Vue.Affichage;

import java.awt.Point;
import java.util.ArrayList;

public class Obstacles {



	/** position et AVANCE sont les variables qui permettent de faire "avancer" la route*/
	private  int position =0; // position s'incremente tant que la partie continue
	private final static int AVANCE = 1;//valeur d'incrementation de la position

	/** Creation des Arraylist pour contenir les Points des Ligne Gauche et Droite ainsi qu'un tableau pour les abscisses*/
	private  ArrayList<Point> obstacleList= new ArrayList<Point>();//la liste de points avec 1 pont = 1 obstacle

	/** Constantes pour borner et aider a definir les abscisses et ordonnees des points de lignes generes aleatoirement */
	private static final int BORNE_MIN =(100); //absc la + a gauche de la fenetre possible 
	private static final int BORNE_MAX =(800);//absc la + a droite de la fenetre 
	private static final int ESPACE_MIN = 200; //on veut que chaques ordonnees des points soit au minimum espaces de 200.
	private static final int ESPACE_MAX = 300; //cest l'ESPACE_MIN + le max du nombre random (100)

	//private static final int DEPARTGAUCHE = Affichage.getAbsVehicule()-150;//le depart du parcoursBas sera a gauche du vehicule 

	private static final Random rand = new Random(); //variable aleatoire pour pouvoir utiliser la bibliotheque java.util.Random.
	private  static final float MOUVEMENT=  (float) 3; //donne la taille de pixel dont se déplace l'obstacle sur le cote

	/**CONSTRUCTEUR*/
	public Obstacles(){
		initList(); //on initialise les lignes a la creation d'une l'instance de Road

	}

	/***************METHODES GET ******************/



	public ArrayList<Point> getObstacleList() {

		return this.obstacleList;
	}



	public int getPosition() {
		return this.position;
	}

	public static int getAvance() {
		return AVANCE;
	}

	/**
	 * Cette methode get est un peu speciale car elle permet de recuperer l'index du point le plus proche en dessous du vehicule 
	 * @return l'index du premier point dont l'abscisse est en dessous du vehicule
	 */
	public int getPointProches(){
		int i = 0;

		while(this.obstacleList.get(i).y >= Affichage.getOrdVehicule() ) { //marcherai aussi avec ligneDroite puis qu'elles ont la meme ordonnee
			i++;
		}	

		return i-1;// -1 car en sortant du while ont a l'index du premier point a au dessus du vehicule, et ont veut celui en dessous.	
	}

	/***************AUTRES METHODES******************/

	/**
	 * setPosition() permet de mettre a jour la position (la faire "avancer")
	 * en meme temps que de mettre a jour l'ordonnee des points de l'obstacleList
	 */
	public  void setPosition() {
		this.obstacleList.forEach(p -> p.y += AVANCE); //modifie l'abscisse des points dans l'obstacleList
		this.position = this.position+AVANCE;		
	}

	/**
	 * cette methode permet  aux points d'obstacleList de modifier leurs abscisses creant ainsi un "mouvement horizontal"
	 */
	private void mouvementHorizontalObstacles() {
		for (int i=0;i<obstacleList.size();i++) {	
			
			//on deplace les points d'obstacleList progressivement (tout les 2 pixels) a partir du moment ou ils depassent l'horizon
			if (obstacleList.get(i).y>Affichage.getHorizon()) {
				if(obstacleList.get(i).y%3==0) {
				if(i%2==0)
					obstacleList.get(i).x-=MOUVEMENT;
				else
					obstacleList.get(i).x+=MOUVEMENT;
				}
			}	

		}
	}


	/**
	 * MaJListO() permet sous  certaines conditions de supprimer les points qui sortent de la fenetre et d'appeller a en rajouter en bout de liste
	 *de plus MaJListO() appelle la methode mouvementHorizontalObstacles() pour donner du mouvement aux obstacles
	 * cette methode est  appellee a chaque utilisation du thread de la Classe Avancer
	 */
	public void MaJListO() {		
		int ByeByeY =obstacleList.get(0).y;//on recup l'ordonnee du premier point de obstacleList
		if(ByeByeY>=Affichage.getHauteurFenetre()+ESPACE_MAX) { //si l'ordonnee ByeByeY (du premier point de la list) est sorti de la fenetre (en bas),				
			this.obstacleList.remove(0); // on supprime ce premier point pour obstacleList
			ajouteFindeListeRandomP();//et on rajoute un point au bout de l' ObstacleList 		
		}
		mouvementHorizontalObstacles();			

	}

	/**
	 * ajouteFindeListesRandomP() agit exactement de la meme maniere qu'initLignes pour creer des points aleatoires aux ligneDroite et ligneGauche
	 * a ceci pret que cette methode potentiellement appellee depuis la methode MaJLignes() a chaque utilisation du thread de la Classe Avancer
	 */
	private void ajouteFindeListeRandomP() {
		int y= ((obstacleList.get(obstacleList.size()-1).y)-rand.nextInt(100))-ESPACE_MIN;//abscisse du dernier point de obstacleList + une random val(+ESPACE_MIN)
		//ligne suivante: l'ordonnee aleatoire du point est bornee en fonction de la taille de la fenetre .
		rand.setSeed(5);
		int x=rand.nextInt(BORNE_MAX)+BORNE_MIN; 
		Point ob = new Point(x,y);

		obstacleList.add(obstacleList.size(),ob); // on ajoute le point aux coordonees "aleatoires" a la fin de l'arraylist ligne

	}


	/** initLigne rempli l'Arraylist<point> obstacleListe de points ayant:
	 *  une ordonnee croisssante(mais qui "avance" de valeur aleatoire en valeur aleatoire avec un ESPACE_MIN (minimum)
	 *  et une abscisse aleatoire bornee
	 *  //pour le premier points de la liste, ont choisit nous meme les coordonnees pour laisser un temps de repit au joueur
	 */
	public void initList() {
		if(!obstacleList.isEmpty()) {
			obstacleList.removeAll(obstacleList);
		}
		Point startObstacle = new Point(Affichage.getLargeurFenetre()/2,Affichage.getHorizon()-10); //on cree le premier point de ligneGauche (et ligneDroite) pour qu'il se situe a gauche (a droite) du vehicule
		obstacleList.add(startObstacle); //on ajoute le startpoint a l'Arraylist obstacleList
		int y=Affichage.getHorizon(); //le premier y Random va commencer apres cet ord
		while(y>-Affichage.getHauteurFenetre()) { //on veut creer des point jusqu'une fois la fenetre au dessus de la fenetre
			y=(y-rand.nextInt(100))-ESPACE_MIN;//creation d'une ord random  avec un espace minimum entre deux ord
			//ligne suivante: l'absc aleatoire du points est bornee en fonction de la largeur de la fenetre .
			int x=rand.nextInt(BORNE_MAX)+BORNE_MIN;
			Point pObs = new Point(x,y); //on dÃ©cale l'absc pour correspondre a un point a droite (pour ligneDroite)		
			obstacleList.add(pObs); // on ajoute le point aux coordonees "aleatoires" a l'arraylist obstacleList
		}
	}

}
