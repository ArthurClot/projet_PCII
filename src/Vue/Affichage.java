package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;


import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controler.Avancer;
//import Controler.Controls;
import Controler.Controls;
import Modele.Etat;
import Modele.Road;



//import java.awt.Point;


@SuppressWarnings("serial")   // parce que je n'ai pas compris dans notre exercice l'interet d'avoir un serial ID number .
public class Affichage extends JPanel {

	/** pour les interactions avec la classe Etat*/
	private  Etat etat;
	//private Controls fleches; 
	private Road road;
	private Ressources ress;
	/* Constantes */

	/** taille de la fenetre */
	private static final int LARG = 1000;
	private static final int HAUT = 800;

	/**coordonnées de la voiture*/
	private static final int ABS_VOITURE = LARG/2;
	private static final int ORD_VOITURE = 600; //la hauteur initiale. C'est Etat.hauteur qui se base sur cet 'y' que l'on utilisera en pratique.

	/**dimmensions de la voiture*/
	private static final int WIDTH = 50; //largeur voiture
	public static final int HEIGHT = 50; //hauteur voiture

	private static final int HORIZON = (HAUT/2)-(HAUT/4);
	
	/** CONSTRUCTEUR */
	public Affichage(Etat eta, Road roa, Ressources r) {
		this.ress=r;
		this.etat = eta;
		this.road = roa;
		setPreferredSize(new Dimension(LARG, HAUT));
		//this.fleches = new Controls(this,etat);
		//this.addKeyListener(new Controls(this,etat));
	}
	
	/**************METHODES GET *******************/
	
	public static int getHauteurFenetre(){
		return HAUT;
	}

	public static int getLargeurFenetre(){
		return LARG;
	}
	
	public static int getHorizon(){
		return HORIZON;
	}

	public static int getOrdVoiture(){
		return ORD_VOITURE;
	}

	public static int getAbsVoiture(){
		return ABS_VOITURE;
	}

	public static int getTailleVoiture() {
		return HEIGHT;
	}

	/**************METHODES DE DESSIN *******************/
		
	/**
	 * methode qui dessine l'ovale/cercle
	 * @param g
	 */
	private void dessineOval(Graphics g) {
		g.setColor(Color.BLACK);//couleur du cercle/oval
		//g.drawOval((ABS_OVAL-WIDTH/2), this.etat.getHauteur(), WIDTH, HEIGHT);//dessine le contours du cercle/oval (ligne de code remplacee par la suivante)
		g.fillOval( this.etat.getPositionVoiture(),ORD_VOITURE, WIDTH, HEIGHT);//dessine le contours et l'interieur du cercle/oval
	}


	/**cette methode dessine le parcours ( deux lignes brisees (haute et basse) remplies par des polygones (au dessus et en dessous)):
	 * pour donner l'effet de cet ligne brisee, on veut dessiner plusieurs lignes de differentes directions reliees les unes aux autres
	 *  pour cela on recupere  une liste de Points cree et mise a jour dans la classe parcours .
	 *  puisque l'on cree finalement des polygones, cette liste de points sera sous la forme de 2 tableaux,
	 *   contenant respectivement les abscisses et les ordonnees des points
	 *   puisque l'on cree finalement des polygones on laisse en commentaire les partie referant a la creation des deux lignes brisees
	 * @param g
	 */
	private void dessineRoad(Graphics g) {
		int taille = this.road.getLigneDroite().size();
		int[] tabXD= new int[taille+3];//tableaux X et Y pour fillPolygon Droite
		int[] tabYD= new int[taille+3];	
		int[] tabXG= new int[taille+3];//tableaux X et Y pour fillPolygon Gauche
		int[] tabYG= new int[taille+3];

		//on cree le dernier point des polygones en bas a droite (pour celui dde droite) et en bas a gauche (pour celui de gauche)
				tabYD[0]=HAUT;//Prend un abscisse et un ordonne pour l'angle en bas a droite de la fenetre
				tabXD[0]=LARG;
				tabYG[0]=HAUT;//Prend un abscisse et un ordonne pour l'angle en bas a gauche de la fenetre
				tabXG[0]=0;
		
		
		for(int i=0;i<taille-1;i++) {//entre chaques paire de points ( d'arraylist ligne on remplie les tableaux)
			
			Point pB1=this.road.getLigneGauche().get(i);   //les points pour les lignes (de-commenter aussi l'import pour les Points)
			Point pB2=this.road.getLigneGauche().get(i+1);
			Point pH1=this.road.getLigneDroite().get(i);
			Point pH2=this.road.getLigneDroite().get(i+1);
			 
			tabXD[i+1]=this.road.getLigneDroite().get(i).x;//recupere l'abscisse puis l'ordonné des points du parcours pour fillPolygon haut
			tabYD[i+1]=this.road.getLigneDroite().get(i).y;
			tabXG[i+1]=this.road.getLigneGauche().get(i).x;//recupere l'abscisse puis l'ordonné des points du parcours pour fillPolygon bas
			tabYG[i+1]=this.road.getLigneGauche().get(i).y;
			
			g.setColor(Color.BLACK);	//on choisit la couleur de la caverne (et avant des lignes  : voir commentaires ci-dessous)
			g.drawLine(pB1.x,pB1.y,pB2.x,pB2.y);// dessine la ligne du bas entre deux points de de l'arraylist<Point> ligneBas
			g.drawLine(pH1.x,pH1.y,pH2.x,pH2.y);// dessine la ligne du bas entre deux points de de l'arraylist<Point> ligneBas
		}
		
		
		tabYD[taille]=HORIZON;//Prend un abscisse et un ordonne pour l'angle en haut(horizon) a droite de la fenetre
		tabXD[taille]=LARG;
		
		tabYG[taille]=HORIZON;//Prend un abscisse et un ordonne pour l'angle en haut(horizon) a gauche de la fenetre
		tabXG[taille]=0;
		g.setColor(Color.GREEN);	//on choisit la couleur de la caverne (et avant des lignes  : voir commentaires ci-dessous)

		g.fillPolygon(tabXD, tabYD, (taille+1)); ////pour colorier a droite de la ligne de droite
		g.fillPolygon(tabXG, tabYG, (taille+1)); ////pour colorier a gauche de la ligne de gauche
	}


	/**
	 * methode qui dessine/affiche le score au centre en haut de la fenetre
	 * @param g
	 */
	private void dessineScore(Graphics g) {
		g.setColor(Color.RED);//choix de la couleur des lettres et chiffres du score
		g.setFont(new Font( "Cambria" ,Font.BOLD,30));// choix de la police et de la taille des lettres et chiffres du score
		g.drawString("Score :"+(this.road.getPosition()/Road.getAvance())/10, LARG-200, HAUT/20);//on place et dessine un score se basant sur la position
	}
	/**
	 * methode qui dessine/affiche un message de fin avec le score final et termine les thread par la meme occasion
	 * @param g
	 */
	private void dessineFin(Graphics g) {
		//Avancer.setfin(); //termine le thread de la Classe Avancer
		//Voler.setfin();  //termine le thread de la Classe Voler
		//ligne suivante : affiche un message indiquant que la partie est finie est indique un score final en se basant sur la position (voir dessineScore())
		JOptionPane.showMessageDialog(this, "Fin de partie !  Score : "+(this.road.getPosition()/Road.getAvance())/10);
	}
	
	/**
	 * Override de la methode paint de l'interface de JComponent qui:
	 *  dessine le cercle, la "caverne et le score dans la fenetre.
	 *  et verifie si la partie est finie auquel cas arrete un MouseListener et affiche le message de fin
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g); //permet de raffraichir la fenetre en appelant repaint
		/*if (this.etat.getFlagTestPerdu()) {//si la partie est finie
			//removeMouseListener(fleches);//enleve le MouseListener cest a dire l'action de saut produite par un clique
			dessineFin(g);	
			//g.clearRect(0, 0, LARG, HAUT);// pour plutot mettre un image genre de batman a la fin.. (pas eu le temps)  	  

		}	*/	
		g.clearRect(0, 0, LARG, HAUT); //pour aider a redessiner la fenetre, efface le precedent affichage sur celle ci		
		
		
		dessineRoad(g);
		dessineOval(g);
		g.drawImage(ress.getImage(0), 0, 0, LARG, HORIZON, this);// image d'apres  l'horizon
		dessineScore(g);
	}

}
