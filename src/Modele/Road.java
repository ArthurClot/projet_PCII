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
	private static final int ESPACE_MAX= 450; // l'espace min plus le max du random ,cest a dire 100
	
	private static final int DEPARTGAUCHE = Affichage.getAbsVehicule()-150;//le depart du parcoursBas sera a gauche du vehicule 
	private static final int DEPARTDROITE = Affichage.getAbsVehicule()+150;//le depart du parcoursHaut sera a droite du vehicule
	
	private static final Random rand = new Random(); //variable aleatoire pour pouvoir utiliser la bibliotheque java.util.Random.
	private  static final float ECARTEMENT=  (float) 0.6; //donne la taille de pixel dont s'ecarte les deux cotes de la route tout en se rapprochent du bas  	/**CONSTRUCTEUR*/
	
	
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
	 * en meme temps que de mettre a jour l'ordonnee des points de ligneGauche et ligneDroite
	 */
	public  void setPosition() {
		this.ligneGauche.forEach(p -> p.y += AVANCE); //modifie l'ordonnee des points dans l'arraylist ligneGauche
		this.ligneDroite.forEach(p -> p.y += AVANCE); //modifie l'ordonnee des points dans l'arraylist ligneDroite
		this.position = this.position+AVANCE;		
	}

	/**perspectiveEtViragesLignes() permet "d'agrandir" la largeur de la route quand elle se rapproche du bas depuis l'horizon
	 * cette methode permet aussi au deux points Gauche et droite des lignes de modifier leurs abscisses creant ainsi des "virages"
	 *cette methode est appelee par MaJLignes()
	 */
	private void perspectiveEtViragesLignes() {
		for (int i=0;i<ligneGauche.size();i++) {		
			//on ecarte les lignes gauches et droites progressivement (tout les 4 pixels) a partir du moment ou elles depassent l'horizon
			if (ligneGauche.get(i).y>Affichage.getHorizon()) {
				if(ligneGauche.get(i).y%3==0) {
					ligneGauche.get(i).x-=ECARTEMENT;
					ligneDroite.get(i).x+=ECARTEMENT*2;
				}
				//on modifie l'abscisse des lignes gauches et droites a partir du moment ou elles depassent l'horizon pour faire les virages			
				//si le virage est a gauche
				if (((ligneGauche.get(i).x+ligneDroite.get(i).x)/2)<abscissesRoute.get(i)) {
					ligneGauche.get(i).x+=1;
					ligneDroite.get(i).x+=1;
				}
				//si le virage est a droite
				else if (((ligneGauche.get(i).x+ligneDroite.get(i).x)/2)>abscissesRoute.get(i)) {
					ligneGauche.get(i).x-=1;
					ligneDroite.get(i).x-=1;
				}		
			}
		}
	}
	
	
	/**
	 * MaJLignes() permet sous  certaines conditions de supprimer les points qui sortent de la fenetre et d'appeller a en rajouter en bout de liste
	 * de plus elle modifie l'abscisse des points des lignes en appelant perspectiveEtViragesLignes()
	 * cette methode est  appellee a chaque utilisation du thread de la Classe Avancer
	 */
	public void MaJLignes() {
		perspectiveEtViragesLignes();	
		int ByeByeY =ligneGauche.get(1).y;//on recup l'ordonnee du deuxieme point de ligneGauche (c'est la meme ordonnee que pour le point de ligneDroite)
		if(ByeByeY>=Affichage.getHauteurFenetre()+ESPACE_MAX) { //si l'ordonnee ByeByeY (du deuxieme point de la ligne) est sorti de la fenetre (en bas) depuis quelque temps (100),				
			this.ligneGauche.remove(0); // on supprime (le premier point) celui en dessous de ByeByeY pour ligneGauche et ligneDroite
			this.ligneDroite.remove(0); 
			this.abscissesRoute.remove(0);
			ajouteFindeListesRandomP();//et on rajoute un point au bout des 2 listes			
		}
				
				
	}

	/**
	 * ajouteFindeListesRandomP() agit exactement de la meme maniere qu'initLignes pour creer des points aleatoires aux ligneDroite et ligneGauche
	 * a ceci pret que cette methode potentiellement appellee depuis la methode MaJLignes() a chaque utilisation du thread de la Classe Avancer
	 */
	private void ajouteFindeListesRandomP() {
		int y= ((ligneGauche.get(ligneGauche.size()-1).y)-rand.nextInt(100))-ESPACE_MIN;//abscisse du dernier point de ligneBas + une random val(+ESPACE_MIN)
		//ligne suivante: l'abscisse  du point commence au centre de la fenetre 
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
	public ArrayList<Integer> initZigzagTab() {
		if(!abscissesRoute.isEmpty()) {
			abscissesRoute.removeAll(abscissesRoute);
		}
		for(int i=0; i < ligneGauche.size(); i++){
		    abscissesRoute.add(rand.nextInt(BORNE_MAX)+BORNE_MIN) ;
		}
		return abscissesRoute;
	}
	/** initLigne rempli l'Arraylist<point> lignegauche et ligneDroite de points ayant:
	 *  une ordonnee croisssante(mais qui "avance" de valeur aleatoire en valeur aleatoire avec un ESPACE_MIN (minimum)
	 *  et une abscisse qui commence au centre de la fenetre
	 *  //pour les 2 premiers points de chaques lignes, ont choisit nous meme les coordonnees pour donner un effet d'entree de circuit.
	 */
	public void initLignes() {
		if(!ligneGauche.isEmpty()) {
			ligneGauche.removeAll(ligneGauche);
			ligneDroite.removeAll(ligneDroite);
		}
		Point startG = new Point(DEPARTGAUCHE,Affichage.getOrdVehicule()+10); //on cree le premier point de ligneGauche (et ligneDroite) pour qu'il se situe a gauche (a droite) du vehicule
		Point startD = new Point(DEPARTDROITE,Affichage.getOrdVehicule()+10);
		ligneGauche.add(startG); //on ajoute les points de depart aux Arraylist ligneGauche et ligneDroite
		ligneDroite.add(startD);		
		int y=Affichage.getHauteurFenetre()-ESPACE_MIN; //le premier y Random va commencer apres cet ord
		while(y>(-Affichage.getHauteurFenetre()*2)) { //on veut creer des point jusqu'a deux fois la hauteur de la fenetre
			y=(y-rand.nextInt(100))-ESPACE_MIN;//creation d'une ord random commun aux deux lignes avec un espace minimum entre deux ord
			//ligne suivante: l'absc aleatoires de depart des points sont au centre de la fenetre.
			int x=Affichage.getLargeurFenetre()/2; 
			Point pG = new Point(x,y);
			Point pD = new Point(x,y); //on dÃ©cale l'absc pour correspondre a un point a droite (pour ligneDroite)		
			ligneGauche.add(pG); // on ajoute les point aux coordonees "aleatoires" a l'arraylist ligneGauche et l'arraylist ligneDroite
			ligneDroite.add(pD); 
		}
	}

	

}
