package Controller;

import Modele.Etat;


public class Timer implements Runnable{

	
	
	private Etat etat;
	
	private  double time=1000; //une seconde, c'est le temps que l'on veut entre chaque mise a jour de la fenetre quand le parcours avance que l'on initialise au TIMEMAX-1);
	
	/** CONSTRUCTEUR */
	public Timer(Etat eta) {
	
		this.etat=eta;
		
	}


	/**
	 * methode de l'interface de Thread permettant d'executer un thread
	 */	
	public void run() {


		while(true) {
			
			if(!Avancer.getFlagDeFin()) 
				etat.decrementeMinuteur();
				
			//System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwtf");
			try { Thread.sleep((int)this.time); } //on utilise Thread.sleep pour qu'il se passe un temps entre chaque Road.setPosition().
			catch (Exception e) { e.printStackTrace(); }
		}

		


	}


}
