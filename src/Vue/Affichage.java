package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;


import javax.swing.JOptionPane;
import javax.swing.JPanel;

import Controller.Avancer;
import Controller.Controls;
//import Controler.Avancer;
//import Controler.Controls;
//import Controler.Controls;
import Modele.Etat;
import Modele.Road;



//import java.awt.Point;


@SuppressWarnings("serial")   // parce que je n'ai pas compris dans notre exercice l'interet d'avoir un serial ID number .
public class Affichage extends JPanel {

	/** pour les interactions avec les autres classes */
	private  Etat etat;
	private Road road;
	private Controls fleches; 
	private Ressources ress;
	private int direction =0 ; //indique la direction du vehicule (0->straight,1->gauche,2->droite) cette variable et modifiee par la classe Controls
	private int score=0;//s'incremente a chaques fois qu'une paire de points dépassent l'ordonnée du vehicule

	/* Constantes */

	
	/**dimmensions de la voiture*/
	private static final int LARG_VEHICULE = 37;
	public static final int HAUT_VEHICULE = 50; 
	
	/**dimmensions intitales d'une bouee*/
	private  int larg_bouee = 1; 
	
	
	/** dimensions de la fenetre */
	private static final int LARG_FENETRE = 1000;
	private static final int HAUT_FENETRE = 800;

	
	/**coordonnÃ©es de la voiture*/
	private static final int ABS_VEHICULE = LARG_FENETRE/2; //l abcsisse initiale de la voiture. C'est etat.getPositionVehicule() que l'on utilisera en pratique.
	private static final int ORD_VEHICULE = (HAUT_FENETRE-(HAUT_VEHICULE*2))-200; //la hauteur (ordonnnee) initiale de la voiture. 
	
	private static final int HORIZON = (HAUT_FENETRE/2)-(HAUT_FENETRE/3); // donne l'ordonnee ou se positionne l'horizon
	
	/** CONSTRUCTEUR */
	public Affichage(Etat eta, Road roa, Ressources r) {
		this.ress=r;
		this.etat = eta;
		this.road = roa;
		this.setPreferredSize(new Dimension(LARG_FENETRE, HAUT_FENETRE));
		Color e = Color.getHSBColor(0.53F,0.93F , 0.95F);//la couleur de la mer
		this.setBackground(e);
		this.fleches = new Controls(this,etat);
		
	}
	
	/**************METHODES GET *******************/
	
	public static int getHauteurFenetre(){
		return HAUT_FENETRE;
	}

	public static int getLargeurFenetre(){
		return LARG_FENETRE;
	}
	
	public static int getHorizon(){
		return HORIZON;
	}

	public static int getOrdVehicule(){
		return ORD_VEHICULE;
	}

	public static int getAbsVehicule(){
		return ABS_VEHICULE;
	}

	public static int getHautVehicule() {
		return HAUT_VEHICULE;
	}
	
	public static int getLargVehicule() {
		return LARG_VEHICULE;
	}

	public void setDirection(int x) {
		 direction=x;
	}
	/**************METHODES DE DESSIN *******************/
		
	/**
	 * methode qui dessine le vehicule
	 * @param g
	 */
	private void dessineVehicule(Graphics g,int d) {
			switch (d) {
			/*case up:				
				break;
			case down:				
				break;*/
			case 0: //va tout droit
				g.drawImage(ress.getImage(2), this.etat.getPositionVehicule(), ORD_VEHICULE, LARG_VEHICULE, HAUT_VEHICULE, this);
				break;
			case 1: //va a gauche
				g.drawImage(ress.getImage(3), this.etat.getPositionVehicule(), ORD_VEHICULE, LARG_VEHICULE, HAUT_VEHICULE, this);
				break;		
			case 2: //va a droite
				g.drawImage(ress.getImage(4), this.etat.getPositionVehicule(), ORD_VEHICULE, LARG_VEHICULE, HAUT_VEHICULE, this);
				break;
			
			}	
	
			
	}
		
	
	


	/**cette methode dessine le parcours ( deux lignes brisees (gauche et droite) :
	 * pour donner l'effet de cet ligne brisee, on veut dessiner plusieurs lignes de differentes directions reliees les unes aux autres
	 *  pour cela on recupere  une liste de Points cree et mise a jour dans la classe parcours .
	 * on va aussi dans un second temps afficher des bouee a chaques points de la ligne 
	 * et faire un début d'essai de perspective en faisant grossir les images quand elle vont depuis le haut vers le bas
	 * @param g
	 */
	private void dessineRoad(Graphics g) {
		int taille = this.road.getLigneDroite().size();
		
		
		for(int i=0;i<taille-1;i++) {//entre chaques paire de points ( d'arraylist ligne on remplie les tableaux)
			
			Point pG1=this.road.getLigneGauche().get(i);   //les points pour les lignes (de-commenter aussi l'import pour les Points)
			Point pG2=this.road.getLigneGauche().get(i+1);
			Point pD1=this.road.getLigneDroite().get(i);
			Point pD2=this.road.getLigneDroite().get(i+1);			
			g.setColor(Color.BLUE);	//on choisit la couleur de la route 
			
			if(pG2.y<HORIZON) {
				g.drawLine(pG1.x,pG1.y,pG2.x,HORIZON);
				g.drawLine(pD1.x,pD1.y,pD2.x,HORIZON);
			}
			else {
				g.drawLine(pG1.x,pG1.y,pG2.x,pG2.y);
				g.drawLine(pD1.x,pD1.y,pD2.x,pD2.y);				
			}			
			// faire grossir les images qui se rapporchent (de maniere simpliste)
			if(pG1.y>=HORIZON) { 
				int tailleBouee=larg_bouee+((pG1.y*pG1.y)/5000);//!\peut etre trouver un calcul d'incrementation de la taille plus "logique"		
				g.drawImage(ress.getImage(1),(pG1.x-tailleBouee/2),(pG1.y-tailleBouee/2), tailleBouee,  tailleBouee, this);
				g.drawImage(ress.getImage(1),(pD1.x-tailleBouee/2),(pD1.y-tailleBouee/2), tailleBouee, tailleBouee, this);	
			}				
			
		}
		
	}


	/**
	 * methode qui dessine/affiche le score au centre en haut de la fenetre
	 * @param g
	 */
	private void dessineScore(Graphics g) {
		//on icremente le score quand le vehicule "depasse une paire de points en passant entre les deux
		for(int i = 0; i<this.road.getLigneGauche().size();i++) {
		if(this.road.getLigneGauche().get(i).y==ORD_VEHICULE //la paire de point depasse le vehicule
		   && this.road.getLigneGauche().get(i).x<this.etat.getPositionVehicule() //vehicule droite du point gauche
		   && this.road.getLigneDroite().get(i).x>this.etat.getPositionVehicule() ) // vehicule gauche du point droit
				score++;	
		}
		g.setColor(Color.RED);//choix de la couleur des lettres et chiffres du score
		g.setFont(new Font( "Cambria" ,Font.BOLD,30));// choix de la police et de la taille des lettres et chiffres du score
		g.drawString("Score :"+score, LARG_FENETRE-200, HAUT_FENETRE/20);//on place et dessine un score qui s'incremente a chaque depassement de points
	}
	/**
	 * methode qui dessine/affiche un message de fin avec le score final et termine les thread par la meme occasion
	 * @param g
	 */
	private void dessineFin(Graphics g) {		
		//ligne suivante : affiche un message indiquant que la partie est finie est indique un score final en se basant sur la position (voir dessineScore())
		JOptionPane.showMessageDialog(this, "Fin de partie !  Score : "+this.score);
	}
	
	
	/**
	 * Override de la methode paint de l'interface de JComponent qui:
	 *  dessine le vehicule, la "mer" et le score dans la fenetre.
	 *  et verifie si la partie est finie auquel cas arrete un keyListener et affiche le message de fin
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g); //permet de raffraichir la fenetre en appelant repaint
		
		 if (Avancer.getFlagDeFin()) {//si la partie est finie
			removeKeyListener(fleches);//enleve le MouseListener cest a dire l'action de saut produite par un clique
			dessineFin(g);	
		}		
		
		dessineRoad(g);
		dessineVehicule(g,this.direction);
		g.drawImage(ress.getImage(0), 0, 0, LARG_FENETRE, HORIZON, this);// image "sky background" d'apres  l'horizon
		dessineScore(g);
	}

}
