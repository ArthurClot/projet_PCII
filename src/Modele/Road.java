package Modele;

import java.util.Random;

import Vue.Affichage;

import java.awt.Point;
import java.util.ArrayList;

public class Road {


	/** position et AVANCE sont les variables qui permettent de faire "avancer" la route*/
	private  int position =0; // position s'incremente tant que la partie continue
	private final static int AVANCE = 1;//valeur d'incrementation de la position

	/** Creation des Arraylist pour contenir les Points des Ligne Gauche et Droite ainsi qu'un tableau pour les abscisses*/
	private  ArrayList<Point> ligneGauche= new ArrayList<Point>();//la liste de points que l'on relira pour faire le parcours de gauche.
	private  ArrayList<Point> ligneDroite= new ArrayList<Point>();//la liste de points que l'on relira pour faire le parcours de droite.
	private ArrayList<Integer> abscissesRoute = new ArrayList<Integer>();; //la liste des différentes abscisses des portions de routes initialisees dans la methode initZigzagTab.
	
	/** Constantes pour borner et aider a definir les abscisses et ordonnees des points de lignes generes aleatoirement */
	private static final int BORNE_MIN =(50); //absc la + a gauche de la fenetre possible 
	private static final int BORNE_MAX =(850);//absc la + a droite de la fenetre 
	private static final int ESPACE_MIN = 350; //on veut que chaques ordonnees des points soit au minimum espaces de 350.
	
	
	private static final int DEPARTGAUCHE = Affichage.getAbsVehicule()-150;//le depart du parcoursBas sera a gauche du vehicule 
	private static final int DEPARTDROITE = Affichage.getAbsVehicule()+150;//le depart du parcoursHaut sera a droite du vehicule
	
	private static final Random rand = new Random(); //variable aleatoire pour pouvoir utiliser la bibliotheque java.util.Random.
	private  static final float ECARTEMENT=  (float) 0.5; //donne la taille de pixel dont s'ecarte les deux cotes de la route tout en se rapprochent du bas  
	
	/**CONSTRUCTEUR*/
	public Road(){
		initLignes(); //on initialise les lignes a la creation d'une l'instance de Road
		initZigzagTab(); ////on initialise les abscissesRoute a la creation d'une l'instance de Road
	}

	/***************METHODES GET ******************/

	

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
	 * Cette methode get est un peu speciale car elle permet de recuperer l'index du point le plus proche en dessous du vehicule 
	 * (marche pour les points de ligneDroite et ligneGauche)
	 * @return l'index du premier point dont l'abscisse est en dessous du vehicule
	 */
	public int getPointProches(){
		int i = 0;
		
		while(this.ligneGauche.get(i).y >= Affichage.getOrdVehicule() ) { //marcherai aussi avec ligneDroite puis qu'elles ont la meme ordonnee
			i++;
		}	
		
			return i-1;// -1 car en sortant du while ont a l'index du premier point a au dessus du vehicule, et ont veut celui en dessous.	
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
			this.abscissesRoute.remove(0);
			ajouteFindeListesRandomP();//et on rajoute un point au bout des 2 listes			
		}
		//on ecarte les lignes gauches et droites progressivement (tout les 4 pixels) a partir du moment ou elles depassent l'horizon
		for (int i=0;i<ligneGauche.size();i++) {				
			if (ligneGauche.get(i).y>Affichage.getHorizon()) {
				if(ligneGauche.get(i).y%3==0) {
					ligneGauche.get(i).x-=ECARTEMENT;
					ligneDroite.get(i).x+=ECARTEMENT*2;
				}
				if (((ligneGauche.get(i).x+ligneDroite.get(i).x)/2)<abscissesRoute.get(i)-1) {
					ligneGauche.get(i).x+=1;
					ligneDroite.get(i).x+=1;
				}
				else if (((ligneGauche.get(i).x+ligneDroite.get(i).x)/2)>abscissesRoute.get(i)+1) {
					ligneGauche.get(i).x-=1;
					ligneDroite.get(i).x-=1;
				}
			}
			
				
		}		
	}

	/**
	 * ajouteFindeListesRandomP() agit exactement de la meme maniere qu'initLignes pour creer des points aleatoires aux ligneDroite et ligneGauche
	 * a ceci pret que cette methode potentiellement appellee depuis la methode MaJLignes() a chaque utilisation du thread de la Classe Avancer
	 */
	private void ajouteFindeListesRandomP() {
		int y= ((ligneGauche.get(ligneGauche.size()-1).y)-rand.nextInt(100))-ESPACE_MIN;//abscisse du dernier point de ligneBas + une random val(+ESPACE_MIN)
		//ligne suivante: l'ordonnee aleatoire du point est bornee en fonction de la taille de la fenetre et de l'ovale.(/!\ borne 2fois utilisee)
		int x=Affichage.getLargeurFenetre()/2; 
		Point pG = new Point(x,y);
		Point pD = new Point(x,y);
		ligneGauche.add(ligneGauche.size(),pG); // on ajoute le point aux coordonees "aleatoires" a la fin de l'arraylist ligne
		ligneDroite.add(ligneDroite.size(),pD); // on ajoute le point aux coordonees "aleatoires" a la fin de l'arraylist ligne
		abscissesRoute.add(rand.nextInt(BORNE_MAX)+BORNE_MIN);
	}
	
	/**initZigzagTab permet de creer les virages de la route en retournant un tableau rempli de valeur aleatoires 
	 *  etant l'abscisse entre deux points gauche et droite de la route.
	 */
	private ArrayList<Integer> initZigzagTab() {
		for(int i=0; i < ligneGauche.size(); i++){
		    abscissesRoute.add(rand.nextInt(BORNE_MAX)+BORNE_MIN) ;
		}
		return abscissesRoute;
	}
	/** initLigne rempli l'Arraylist<point> lignegauche et ligneDroite de points ayant:
	 *  une ordonnee croisssante(mais qui "avance" de valeur aleatoire en valeur aleatoire avec un ESPACE_MIN minimum)
	 *  et une abscisse aleatoire bornee
	 *  //pour les 2 premiers points de chaques lignes, ont choisit nous meme les coordonnees pour donner un effet d'entree de circuit.
	 */
	private void initLignes() {
		Point startG = new Point(DEPARTGAUCHE,Affichage.getOrdVehicule()+10); //on cree le premier point de ligneGauche (et ligneDroite) pour qu'il se situe a gauche (a droite) du vehicule
		Point startD = new Point(DEPARTDROITE,Affichage.getOrdVehicule()+10);
		ligneGauche.add(startG); //on ajoute les points aux Arraylist ligneBas et ligneHaut
		ligneDroite.add(startD);		
		int y=Affichage.getHauteurFenetre()-ESPACE_MIN; //le premier y Random va commencer apres cet ord
		while(y>(-Affichage.getHauteurFenetre())) { //on veut creer des point jusqu'au 5/4 de la hauteur de la fenetre
			y=(y-rand.nextInt(100))-ESPACE_MIN;//creation d'une ord random commun aux deux lignes avec un espace minimum entre deux ord
			//ligne suivante: les absc aleatoires des points sont bornees en fonction de la largeur de la fenetre et de la voiture.
			int x=Affichage.getLargeurFenetre()/2; 
			Point pG = new Point(x,y);
			Point pD = new Point(x,y); //on dÃ©cale l'absc pour correspondre a un point a droite (pour ligneDroite)		
			ligneGauche.add(pG); // on ajoute les point aux coordonees "aleatoires" a l'arraylist ligneGauche et l'arraylist ligneDroite
			ligneDroite.add(pD); 
		}
	}

	

}
