package Controller;

import Modele.Etat;


public class Horloge implements Runnable{

	
	
	private Etat etat;
	
	private  double time=250; //1/4 seconde, car 4*time cest une seconde pour le timer et 1/4 ca sert aux autres affichages "clignotants"
	private static int clignote=0; //variable qui donne un entier entre 0 et nbClignote pour realiser un clignotement
	
	/** CONSTRUCTEUR */
	public Horloge(Etat eta) {
	
		this.etat=eta;
		
	}


	public static int getClignote() {
		return clignote;
	}
	/**
	 * methode de l'interface de Thread permettant d'executer un thread
	 */	
	public void run() {

		//durant ce while la seconde qui s'écoule entre chaque executions et divisée en 4, pour faire "clignoter" tout les 1/4 secondes
		
		while(true) { 		
			
			if(!Avancer.getFlagDeFin()) 
				etat.decrementeMinuteur();
			clignote++;
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
			clignote++;
			try { Thread.sleep((int)this.time); }
			catch (Exception e) { e.printStackTrace(); }
			clignote++;
			
			try { Thread.sleep((int)this.time); } 
			catch (Exception e) { e.printStackTrace(); }
			clignote=0;	
			
			try { Thread.sleep((int)this.time); } 
			catch (Exception e) { e.printStackTrace(); }
		}

		


	}


}
