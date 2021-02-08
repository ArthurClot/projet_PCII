package Controler;

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



	
	/**
	 * ici se trouve les différentes touches utilisées dans le jeu
	 */
	public void keyPressed(KeyEvent e) {
		System.out.println("Got there.");
		switch (e.getKeyCode()) {
		/*case KeyEvent.VK_UP:
			etat.move(Direction.up); //aller en haut 
			break;
		case KeyEvent.VK_DOWN:
			etat.move(Direction.down);//aller en bas
			break;*/
		case KeyEvent.VK_LEFT:
			etat.move(Direction.left);//aller a' gauche
			System.out.println("allons bon..Gauche");
			affichage.revalidate();
			affichage.repaint();	
			break;
		case KeyEvent.VK_RIGHT:
			System.out.println("allons bon..Droite");
			etat.move(Direction.right);//aller a' droite
			affichage.revalidate();
			affichage.repaint();	
			break;
		}	
	
	}
	
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}





