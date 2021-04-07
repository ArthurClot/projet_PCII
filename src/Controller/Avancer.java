package Controller;

import Modele.Etat;
import Modele.Road;
import Modele.Obstacles;
import Vue.Affichage;

public class Avancer implements Runnable {

	private Road road;
	private Affichage affichage;
	private Etat etat;
	private Obstacles obstacles;
	private static final int TIMEMIN=4; //c'est la valeur minimale du temps que l'on veut entre chaque mise a jour du thread de défilment de la route (décide la vitesse).
	private static final int TIMEMAX=60;//c'est la valeur maximale du temps que l'on peut rajouter entre chaque mise a jour du thread de défilment de la route (décide la vitesse).

	private  double time; //c'est le temps que l'on veut entre chaque mise a jour de la fenetre quand le parcours avance que l'on initialise au TIMEMAX-1);
	
	/**Pour la variation de la vitesse*/
	private final int SLOW_OBSTACLES=15; //represente le cas ou l'on touche un obstacle (la valeur du ralentissement)
	
	//private int 
	private boolean flagObstacle = false; //condition de ralentissement du a l'obstacle
	private static boolean flagDeFin=true; //condition d'activation du Thread

	/** CONSTRUCTEUR */
	public Avancer(Road roa,Affichage aff,Etat eta,Obstacles obs) {

		this.road = roa;
		this.affichage = aff;
		this.etat= eta;
		this.obstacles=obs;
	}


	/** Methodes pour GET et SET la variable time*/

	public double getTime() {
		return this.time;
	}

	public static int getTimeMax() {
		return TIMEMAX;
	}


	public static boolean getFlagDeFin() {
		return flagDeFin;
	}

	public static void setFlagDeFin() {
		flagDeFin=true;
	}

	public void setFlagDeDebut() {
		flagDeFin=false;
	}

	/**modifie la vitesse de defilement en fonction d'un coefficient */
	private void variationSpeed() {
		
		/**ralenti la vitesse si l'on touche un obstacle*/
		if(etat.testRalentissementObstacles()) {		
			if(!flagObstacle) {
				flagObstacle=true;	//on ne ralenti la vitesse une seule fois quand on touche un obstacle		
				if(this.time+SLOW_OBSTACLES<TIMEMAX)
					this.time+=SLOW_OBSTACLES; 
				else
					flagDeFin=true;	
			}
		}
		else 
			flagObstacle = false;		
		
		/** On modifie la vitesse selon que l'on soit sur ou en dehors de la route */
		if (etat.testRalentissementRoad()==false) { //on va plus vite
			if(this.time>TIMEMIN)
				this.time-=((this.time/6)/TIMEMAX); //le "/6" cest pour temporiser l'acceleration)
		}
		else {//on va plus lentement :
			if(this.time<TIMEMAX)
				this.time+=(this.time/TIMEMAX)/3;//le "/3" cest pour temporiser un peu la decceleration)
		}
		etat.setDeplacement((TIMEMAX/2)-(this.time/2));	
		
		/**on fait varier la vitesse de deplacement horizontale des obstacles (requins) et le nombre de changements de sens*/
		obstacles.setDelayHorizonMove(((TIMEMAX/10)*2)-((this.time/10)*3));//nous donne une valeur entre 3 et 6 (on comptant les bornes de setDelayHorizon
		obstacles.setChangeSens((TIMEMAX*4)-(this.time*2));//donne une valeur entre 120 et 230
	}





	/**
	 * methode de l'interface de Thread permettant d'executer un thread
	 */
	@Override
	public void run() {

		this.time=TIMEMAX/2;//on initialise le temps entre chaques thread a la moitiée du temps max entre chaques thread.


		while(flagDeFin==false) {
			if(etat.setFin() ) {
				flagDeFin=true;			
			}
			
			road.setPosition() ;    //la position referente du parcours augmente et ont modifie du coup l'ordonnee des points des lignes.
			obstacles.setPosition(); // meme chose pour les ostacles (requins)
			road.MaJLignes();       //on regarde si il faut supprimer des points et en creer dans les arraylist Lignes (si oui on le fait)
			obstacles.MaJListO();// meme chose pour les ostacles (requins)
			variationSpeed(); //on varie le time du thread en fonction des criteres
			etat.incrementScoreEtMinuteur();//on verifie si on a depasse une bouee et qu'il faut incrementer le minuteur.
			affichage.revalidate();
			affichage.repaint();        //on reactualise l'image depuis l'instance affichage
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
		}

		this.time=250; // rien ne sert de verifier toutes les milisecondes , on verifie donc toute quart de secondes. (cela coincide aussi avec le temps du thread Horloge)

		while(flagDeFin==true) {// si la partie est finie
			if(affichage.getFlagDeDebut()) { //on attend le signal de l'affichage l'ors de l'appuye sur espace modifiant la valeur depuis la classe Controls 
				flagDeFin=false;
				this.time=TIMEMAX/2;//on se remet a avoir un temps plus rapide
				affichage.setFlagDeDebut(false);//on dit message recu
				run(); // on relance le run() ... est ce vraiment la meilleure maniere de faire ? surement pas...mais ca marche bien.
			}
			affichage.revalidate();//on revalidate et repaint uniquement pour permettre de faire clignoter l'indication "start"
			affichage.repaint(); 
			try { Thread.sleep((int)this.time); } 
			catch (Exception e) { e.printStackTrace(); }
		}

	}


}
