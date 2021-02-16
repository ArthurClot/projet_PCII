package Controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


import Modele.Etat;
import Vue.Affichage;




public class Controls implements KeyListener {

	Affichage affichage ;
	Etat etat;

	public Controls(Affichage aff,Etat eta){
		this.affichage=aff;
		this.etat=eta;
	}



	public void keyMove(KeyEvent e) {
		switch (e.getKeyCode()) {
		/*case KeyEvent.VK_UP:
			etat.move(Direction.up); //aller en haut 
			break;
		case KeyEvent.VK_DOWN:
			etat.move(Direction.down);//aller en bas
			break;*/
		case KeyEvent.VK_LEFT:
			affichage.repaint();	
			etat.move(Direction.left);//aller a' gauche
			affichage.revalidate();
			//affichage.repaint();	
			break;
		case KeyEvent.VK_RIGHT:
			affichage.repaint();
			etat.move(Direction.right);//aller a' droite
			affichage.revalidate();
			//affichage.repaint();	
			break;
		}	
	
	}
	
	
	
	/**
	 * ici se trouve les différentes touches utilisées dans le jeu
	 */

	@Override
	public void keyTyped(KeyEvent e) {
		//this.keyMove( arg0);
	}
	
	
	public void keyPressed(KeyEvent e) {
		this.keyMove (e);
	
	}
	
	
	





@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
