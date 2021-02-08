package Modele;

import java.util.Random;

import Vue.Affichage;

import java.awt.Point;
import java.util.ArrayList;

public class Road {


	/** position et AVANCE sont les variables qui permettent de faire "avancer" le parcours*/
	private  int position =0; // position s'incremente tant que la partie continue
	private final static int AVANCE = 2;//valeur d'incrementation de la position

	/** Creation des Arraylist pour contenir les Points des Ligne haut et Bas */
	private  ArrayList<Point> ligneGauche= new ArrayList<Point>();//la liste de points que l'on relira pour faire le parcours de gauche.
	private  ArrayList<Point> ligneDroite= new ArrayList<Point>();//la liste de points que l'on relira pour faire le parcours de droite.

	/** Constantes pour borner et aider a definir les abscisses et ordonnees des points de lignes generes aleatoirement */
	private static final int BORNE_MIN =((Affichage.getLargeurFenetre()/2)-150); //absc la + a gauche de la fenetre possible (pour ligneGauche)
	private static final int BORNE_MAX =(200);//absc la + a droite de la fenetre pour ligneGauche
	private static final int ESPACE_MIN = 30; //on veut que chaques abscisses des points soit au minimum espaces de 100.
	private static final int LARGEUR_ROUTE = 100;
	
	private static final int DEPARTGAUCHE = Affichage.getAbsVoiture()-200;//le depart du parcoursBas sera en dessous de l'ovale
	private static final int DEPARTDROITE = Affichage.getAbsVoiture()-100+LARGEUR_ROUTE;//le depart du parcoursHaut sera au dessus de l'ovale
	
	private static final Random rand = new Random(); //variable aleatoire pour pouvoir utiliser la bibliothèque java.util.Random.

	/**Constructeur*/
	public Road(){
		initLignes(); //on initialise les lignes a la creation d'une l'instance de Parcours
	}

	/***************METHODES GET ******************/

	public static int getLargeurRoute(){
		return LARGEUR_ROUTE;
	}

	public ArrayList<Point> getLigneGauche() {

		return this.ligneGauche;
	}

	public ArrayList<Point> getLigneDroite() {

		return this.ligneDroite;
	}

	public int getPosition() {
		return this.position;
	}

	public static int getAvance() {
		return AVANCE;
	}

	/**
	 * Cette methode get est un peu speciale car elle permet de recuperer l'index du point le plus proche a gauche du cercle 
	 * (marche pour les points de ligneHaut et ligneBas)
	 * @return l'index du premier point dont l'abscisse est a gauche du cercle
	 */
	public int getPointProches(){
		int i = 0;
		while(this.ligneGauche.get(i).y <= Affichage.getOrdVoiture()) { //marcherai aussi avec ligneHaut puis qu'elles ont le meme abscisse
			i++;
		}
		return i-1;// -1 car pour sortir du while ont a pris le premier point a droite du cercle, et ont veut celui de gauche.
	}

	/***************AUTRES METHODES******************/

	/**
	 * setPosition() permet de mettre a jour la position (la faire "avancer")
	 * en meme temps que de mettre a jour l'abscisse des points de ligneHaut et ligneBas
	 */
	public  void setPosition() {
		this.ligneGauche.forEach(p -> p.y += AVANCE); //modifie l'abscisse des points dans l'arraylist ligneGauche
		this.ligneDroite.forEach(p -> p.y += AVANCE); //modifie l'abscisse des points dans l'arraylist ligneDroite
		this.position = this.position+AVANCE;		
	}

	/**
	 * MaJLignes() permet sous un certaine condition de supprimer les points qui sortent de la fenetre et d'appeller a en rajouter en bout de liste
	 * cette methode est  appellee a chaque utilisation du thread de la Classe Avancer
	 */
	public void MaJLignes() {		
		int ByeByeY =ligneGauche.get(1).y;//on recup l'ordonnee du deuxieme point de ligneGauche (c'est la meme ordonnee que pour le point de ligneDroite)				
		if(ByeByeY>=Affichage.getHauteurFenetre()) { //si l'ordonnee ByeByeY (du deuxieme point de la ligne) est sorti de la fenetre (en bas),		
			this.ligneGauche.remove(0); // on supprime (le premier point) celui en dessous de ByeByeY pour ligneGauche et ligneDroite
			this.ligneDroite.remove(0); 
			ajouteFindeListesRandomP();//et on rajoute un point au bout des 2 listes
		}		
	}

	/**
	 * ajouteFindeListesRandomP() agit exactement de la meme maniere qu'initLignes pour creer des points aleatoires aux ligneDroite et ligneGauche
	 * a ceci pret que cette methode potentiellement appellee depuis la methode MaJLignes() a chaque utilisation du thread de la Classe Avancer
	 */
	private void ajouteFindeListesRandomP() {
		int y= ((ligneGauche.get(ligneGauche.size()-1).y)-rand.nextInt(100))-ESPACE_MIN;//abscisse du dernier point de ligneBas + une random val(+ESPACE_MIN)
		//ligne suivante: l'ordonnee aleatoire du point est bornee en fonction de la taille de la fenetre et de l'ovale.(/!\ borne 2fois utilisee)
		int x=rand.nextInt(BORNE_MAX)+BORNE_MIN; 
		Point pG = new Point(x,y);
		Point pD = new Point((x+LARGEUR_ROUTE),y);
		ligneGauche.add(ligneGauche.size(),pG); // on ajoute le point aux coordonees "aleatoires" a la fin de l'arraylist ligne
		ligneDroite.add(ligneDroite.size(),pD); // on ajoute le point aux coordonees "aleatoires" a la fin de l'arraylist ligne
	}


	/** initLigne rempli l'Arraylist<point> ligneBas et ligneHaut de points ayant:
	 *  une absisse croisssante(mais qui "avance" de valeur aleatoire en valeur aleatoire avec un ESPACE_MIN au minimum)
	 *  et une ordonnee aleatoire bornee
	 *  pour ligne haut on "ajoute" LARGEUR_CAVERNE a l'ordonnee
	 *  //pour les 2 premiers points de chaques lignes, ont choisit nous meme les coordonnees pour donner un effet d'entree de caverne.
	 */
	private void initLignes() {
		Point startG = new Point(DEPARTGAUCHE,Affichage.getHauteurFenetre()); //on cree le premier point de ligneBas (et ligneHaut) pour qu'il se situe au bas (en haut) du cercle
		Point startD = new Point(DEPARTDROITE,Affichage.getHauteurFenetre());
		ligneGauche.add(startG); //on ajoute les points aux Arraylist ligneBas et ligneHaut
		ligneDroite.add(startD);		
		int y=Affichage.getHauteurFenetre(); //le premier y Random va commencer apres cet ord
		while(y>(-Affichage.getHauteurFenetre())) { //on veut creer des point jusqu'au 3/4 de la hauteur de la fenetre
			y=(y-rand.nextInt(100))-ESPACE_MIN;//creation d'une ord random commun aux deux lignes avec un espace minimum entre deux ord
			//ligne suivante: les absc aleatoires des points sont bornees en fonction de la largeur de la fenetre et de la voiture.
			int x=rand.nextInt(BORNE_MAX)+BORNE_MIN; 
			Point pG = new Point(x,y);
			Point pD = new Point(x+LARGEUR_ROUTE,y); //on décale l'absc pour correspondre a un point a droite (pour ligneDroite)		
			ligneGauche.add(pG); // on ajoute les point aux coordonees "aleatoires" a l'arraylist ligneGauche et l'arraylist ligneDroite
			ligneDroite.add(pD); 
		}
	}

	

}
