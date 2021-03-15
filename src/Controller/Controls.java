package Controller;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import Modele.Etat;
import Vue.Affichage;




public class Controls implements KeyListener {

	private Affichage affichage ;
	private Etat etat;
	
	/**on cree un array keys qui donne un booleen pour les int correspondant au touches du clavier.
	** A savoir que KeyEvent.VK_LEFT = 37 et KeyEvent.VK_RIGHT = 39 (d'ou la taille du tableau)
	*/
	private Boolean[] keys =new Boolean[40];
	
	
	//CONSTRUCTEUR:
	public Controls(Affichage aff,Etat eta){
		this.affichage=aff;
		this.etat=eta;
		
		//on initalise les valeurs a false (en particulier KeyEvent.VK_LEFT et KeyEvent.VK_RIGHT)
		for(@SuppressWarnings("unused") Boolean e : keys) {
			e = false;
		}	
	}


	public void keyTyped(KeyEvent e) {
	}
		
	public void keyPressed(KeyEvent e) {		
	    keys[e.getKeyCode()] = true;
	}

	public void keyReleased(KeyEvent e) {
	    keys[e.getKeyCode()] = false;
	 
	}

	


	
	/**
	 * ici se trouve la methode update utilisee dans le thread Mouvement qui 
	 * modifie la position et l'image du vehicule si la touche correspondante est pressee
	 */
	public void update() {

	    if( keys[KeyEvent.VK_LEFT]){
	    	affichage.setDirection(1);//code pour image left	
			etat.move(Direction.left);//aller a' gauche
	    }

	    if( keys[KeyEvent.VK_RIGHT]){
	    	affichage.setDirection(2);//code pour image right			
			etat.move(Direction.right);//aller a' droite
	    }
	    else
	    	affichage.setDirection(0); //code pour image STRAIGHT (par defaut)
	}

	
	
}