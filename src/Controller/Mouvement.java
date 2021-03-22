package Controller;


import Modele.Etat;
import Vue.Affichage;

public class Mouvement implements Runnable{
	

		private Affichage affichage;
		private Etat etat;
		//private Controls controls;
		private  final int TIME=10; //c'est le temps que l'on veut entre chaque mise a jour de la fenetre quand le parcours avance que l'on initialise au TIMEMAX-1);
		
		
		private static boolean flagDeFin=false; //condition d'activation du Thread

		/** CONSTRUCTEUR */
		public Mouvement(Controls cont, Affichage aff,Etat eta) {

			//this.controls=cont;
			this.affichage = aff;
			this.etat= eta;
		}


		/** Methodes pour GET et SET la variable time*/

		public double getTime() {
			return this.TIME;
		}

		


		public static boolean getFlagDeFin() {
			return flagDeFin;
		}

		public static void setFlagDeFin() {
			flagDeFin=true;
		}



	





		/**
		 * methode de l'interface de Thread permettant d'executer un thread
		 */
		@Override
		public void run() {



			while(flagDeFin==false) {
				if(etat.setFin())
					flagDeFin=true;
			//	this.controls.update();
				affichage.revalidate();
				affichage.repaint();        //on reactualise l'image depuis l'instance affichage
				try { Thread.sleep(TIME); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
				catch (Exception e) { e.printStackTrace(); }
			}
		}


	}


