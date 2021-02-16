package Main;




import java.awt.Color;

import javax.swing.JFrame;

import Controller.Avancer;
import Controller.Controls;
//import Controler.Voler;
import Modele.Etat;
import Modele.Road;
import Vue.Affichage;
import Vue.Ressources;



    
public class Main {
	
	public static void main(String [] args) {
	
		/**Creation d'instances de Classes*/
		JFrame fenetre = new JFrame("Ring racer");
		Road road = new Road();
		
		Etat etat = new Etat(road);
		Ressources ress = new Ressources();
		Affichage affichage = new Affichage( etat,road,ress);
		
		
	
		/**activation de la fenetre et ajout de contenu (affichage)*/
	    fenetre.add(affichage);
		fenetre.pack(); 
		fenetre.setBackground(Color.BLUE);
		fenetre.setVisible(true);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		
		
		fenetre.addKeyListener(new Controls(affichage,etat));//pour que la fenetre ecoute les pressedKeys
		/** creation des Threads*/
		new Thread(new Avancer(road,affichage,etat)).start();	// déroulement de la route
		
	  }		

}
