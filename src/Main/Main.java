package Main;


import javax.swing.JFrame;

import Controller.Avancer;
import Controller.Controls;
import Controller.Horloge;
//import Controller.Mouvement;
import Modele.Etat;
import Modele.Obstacles;
import Modele.Road;
import Vue.Affichage;
import Vue.Ressources;



    
public class Main {
	
	
	public static void main(String [] args) {
		
		JFrame fenetre = new JFrame("Water race");
		Road road = new Road();
		Obstacles obstacles = new Obstacles();
		
		Etat etat = new Etat(road,obstacles);
		Ressources ress = new Ressources();
		Affichage affichage = new Affichage( etat,road,ress,obstacles);
		Controls controls = new Controls(affichage,etat);
		
		/**activation de la fenetre et ajout de contenu (affichage)*/
	    fenetre.add(affichage);
		fenetre.pack(); 
		fenetre.setVisible(true);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		fenetre.addKeyListener(controls);//pour que la fenetre ecoute les pressedKeys
		
		
		
		/** creation des Threads*/
		Avancer avancer=new Avancer(road,affichage,etat,obstacles);
		Horloge timer=new Horloge(etat);
		
		new Thread(avancer).start();	// déroulement de la route
		new Thread(timer).start();      // minuteur
	  }		

}
