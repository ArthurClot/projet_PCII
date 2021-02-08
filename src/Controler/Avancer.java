package Controler;

import Modele.Road;
import Vue.Affichage;

public class Avancer implements Runnable {

	private Road road;
	private Affichage affichage;

	private final int TIME=50; //c'est le temps que l'on veut entre chaque mise a jour de la fenetre quand le parcours avance.

	//private static boolean flagDeFin=false; //condition d'activation du Thread

	/** Constructeur */
	public Avancer(Road roa,Affichage aff) {

		this.road = roa;
		this.affichage = aff;
	}

	/**
	 * methode publique qu'utilise l'Affichage pour "arreter" le Thread
	 */
	/*public static void setfin() { 
		flagDeFin=true;
	}*/


	/**
	 * methode de l'interface de Thread permettant d'executer un thread
	 */
	@Override
	public void run() {
		//while(flagDeFin==false) {
		while(true) {
			road.setPosition() ;    //la position referente du parcours augmente et ont modifie du coup l'abscisse des points des lignes.
			road.MaJLignes();       //on regarde si il faut supprimer des points et en creer dans les arraylist Lignes (si oui on le fait)
			affichage.revalidate();
			affichage.repaint();        //on reactualise l'image depuis l'instance affichage
			try { Thread.sleep(TIME); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Parcours.setPosition().
			catch (Exception e) { e.printStackTrace(); }
		}
	}

	
}
