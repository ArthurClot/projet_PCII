package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import Modele.Etat;
import Vue.Affichage;




public class Controls implements KeyListener {

	private Affichage affichage ;
	private Etat etat;
	
	//CONSTRUCTEUR:
	public Controls(Affichage aff,Etat eta){
		this.affichage=aff;
		this.etat=eta;
	}

	

	public void keyMove(KeyEvent e) {
		affichage.setDirection(0); //code pour STRAIGHT (par defaut)
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:
			affichage.setFlagDeDebut(true);
			if(affichage.getFlagDeDebut()) {
				affichage.DebutDePartie(); //redemmarer la partie
				affichage.revalidate();	
			}
			break;
		/*case KeyEvent.VK_DOWN:
			etat.move(Direction.down);//aller en bas
			break;*/
		case KeyEvent.VK_LEFT:
			affichage.setDirection(1);//code pour left	
			etat.move(Direction.left);//aller a' gauche
			affichage.revalidate();				
			break;
		case KeyEvent.VK_RIGHT:
			affichage.setDirection(2);//code pour right			
			etat.move(Direction.right);//aller a' droite
			affichage.revalidate();				
			break;
		}	
	
	}
	
	
	
	/**
	 * ici se trouve les différentes touches utilisées dans le jeu
	 */

	@Override
	public void keyTyped(KeyEvent e) {
		//this.keyMove( e);
	}
	
	
	public void keyPressed(KeyEvent e) {
		
		this.keyMove (e);
		
	
	}
	
	
	





@Override
	public void keyReleased(KeyEvent arg0) {
	affichage.setDirection(0); //code pour STRAIGHT (par defaut)
		
	}

}