package Controller;

import Modele.Etat;


public class Horloge implements Runnable{

	
	
	private Etat etat;
	
	private  double time=250; //1 quart de seconde, car 4*time cest une seconde pour le timer et 1/4 ca sert aux autres affichages "clignotants"
	private static int clignote=0;
	
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


		while(true) {
			
			if(!Avancer.getFlagDeFin()) 
				etat.decrementeMinuteur();
			clignote++;
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
			clignote++;
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
			clignote++;
			
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
			clignote=0;	
			
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
		}

		


	}


}
