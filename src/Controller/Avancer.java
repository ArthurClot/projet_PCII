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
	private static final int TIMEMIN=6; //c'est la valeur minimale du temps que l'on veut entre chaque mise a jour du thread de d�filment de la route (d�cide la vitesse).
	private static final int TIMEMAX=60;//c'est la valeur maximale du temps que l'on peut rajouter entre chaque mise a jour du thread de d�filment de la route (d�cide la vitesse).

	private  double time; //c'est le temps que l'on veut entre chaque mise a jour de la fenetre quand le parcours avance que l'on initialise au TIMEMAX-1);
	
	private boolean flagObstacle = false; //condition de ralentissement du a l'obstacle
	private static boolean flagDeFin=false; //condition d'activation du Thread

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
				
		if(etat.testRalentissementObstacles()) {
						
			if(!flagObstacle) {
				flagObstacle=true;			
				if(this.time+20<TIMEMAX)
					this.time+=25; //le 25 est arbitraire apres des tests.
				else
					flagDeFin=true;	
			}
		}
		else {
			flagObstacle = false;		
		}
		
		if (etat.testRalentissementRoad()==false) { //on va plus vite
			
			if(this.time>TIMEMIN)
			this.time-=((this.time/6)/TIMEMAX); //le "/6" cest pour temporiser l'acceleration)
		
		}
			
		else {//on va plus lentement :
			
			if(this.time<TIMEMAX)
			this.time+=(this.time/TIMEMAX)/2;//le "/2" cest pour temporiser un peu la decceleration)
			
		}
			
		etat.setDeplacement((TIMEMAX/2)-(this.time/2));	
		
	}





	/**
	 * methode de l'interface de Thread permettant d'executer un thread
	 */
	@Override
	public void run() {
		
		this.time=TIMEMAX/2;//on initialise le temps entre chaques thread a la moiti�e du temps max entre chaques thread.


		while(flagDeFin==false) {
			if(etat.setFin()) {
				flagDeFin=true;
			}
			variationSpeed();
			road.setPosition() ;    //la position referente du parcours augmente et ont modifie du coup l'ordonnee des points des lignes.
			road.MaJLignes();       //on regarde si il faut supprimer des points et en creer dans les arraylist Lignes (si oui on le fait)
			//meme chose pour les obstacles
			obstacles.setPosition();
			obstacles.MaJListO();
			affichage.revalidate();
			affichage.repaint();        //on reactualise l'image depuis l'instance affichage
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
		}
		
		this.time=1000; // rien ne sert de verifier toutes les milisecondes , on verifie donc toute les secondes.
		
		while(flagDeFin==true) {
			
			if(affichage.getFlagDeDebut()) {
				flagDeFin=false;
				this.time=TIMEMAX/2;
				affichage.setFlagDeDebut(false);
				run();
			}
			try { Thread.sleep((int)this.time); } 
			catch (Exception e) { e.printStackTrace(); }
		}
	
	}


}
