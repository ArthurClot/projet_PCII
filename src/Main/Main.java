package Main;




import javax.swing.JFrame;

import Controler.Avancer;
import Controler.Controls;
//import Controler.Voler;
import Modele.Etat;
import Modele.Road;
import Vue.Affichage;
import Vue.Ressources;

    
public class Main {
	
	public static void main(String [] args) {
	
		/**Creation d'instances de Classes*/
		JFrame fenetre = new JFrame("Ring racer");//instance de fenetre
		Road road = new Road();//instance de
		Etat etat = new Etat(road);//instance de
		Ressources ress = new Ressources();//instance de
		Affichage affichage = new Affichage( etat,road,ress);//instance de
	
		/**activation de la fenetre et ajout de contenu (affichage)*/
	    fenetre.add(affichage);
		fenetre.pack(); 
		fenetre.setVisible(true);
		fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
		fenetre.addKeyListener(new Controls(affichage,etat));//pour que la fenetre ecoute les pressedKeys
		/** creation des Threads*/
		//new Thread(new Voler(etat,affichage)).start();
		new Thread(new Avancer(road,affichage)).start();			
	  }		

}
