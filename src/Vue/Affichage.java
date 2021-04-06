package Vue;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import Controller.Avancer;
import Controller.Controls;
import Controller.Horloge;
import Modele.Etat;
import Modele.Obstacles;
import Modele.Road;





@SuppressWarnings("serial")   // parce que je n'ai pas compris dans notre exercice l'interet d'avoir un serial ID number .
public class Affichage extends JPanel {

	/** pour les interactions avec les autres classes */
	private  Etat etat;
	private Road road;
	private Obstacles obstacles;
	private Controls fleches; 
	private Ressources ress;
	private int direction =0 ; //indique la direction du vehicule (0->straight,1->gauche,2->droite) cette variable et modifiee par la classe Controls
	
	private static boolean flagDeDebut=false;
	/* Constantes */


	/**dimmensions de la voiture*/
	private static final int LARG_VEHICULE = 37;
	public static final int HAUT_VEHICULE = 50; 



	/** dimensions de la fenetre */
	private static final int LARG_FENETRE = 1000;
	private static final int HAUT_FENETRE = 600;


	/**coordonnÃ©es de la voiture*/
	private static final int ABS_VEHICULE = LARG_FENETRE/2; //l abcsisse initiale de la voiture. C'est etat.getPositionVehicule() que l'on utilisera en pratique.
	private static final int ORD_VEHICULE = (HAUT_FENETRE-(HAUT_VEHICULE*2))-100; //la hauteur (ordonnnee) initiale de la voiture. 

	private static final int HORIZON = (HAUT_FENETRE/2)-(HAUT_FENETRE/3); // donne l'ordonnee ou se positionne l'horizon

	/** CONSTRUCTEUR */
	public Affichage(Etat eta, Road roa, Ressources r, Obstacles obs) {
		this.ress=r;
		this.etat = eta;
		this.road = roa;
		this.obstacles = obs;
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

	public  boolean getFlagDeDebut() {
		return flagDeDebut;
	}

	public  void setFlagDeDebut(boolean bool) {
		flagDeDebut=bool;
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





	/**cette methode dessine le parcours :
	 *  deux lignes brisees (gauche et droite) representées par des ovales jaunes,
	 *  ainsi qu'une bouee a chaques extremintees des lignes brisees.
	 * il y a deux sortes de lignes , celles qui rejoint la ligne de fuite sur l'horizon, et celle qui a une "bouee" a chaques bouts.
	 * de plus 1 bouee sur 5 est speciale.
	 * et pour finir, pour rajouter en perspective, les images et ovales grossissent quand ils vont depuis le haut vers le bas.
	 * @param g
	 */
	
	private void dessineRoad(Graphics g) {

		for(int i=0;i<this.road.getLigneDroite().size()-1;i++) {//entre chaques paire de points ( d'arraylist ligne on remplie les tableaux)
			
			Point pG1=this.road.getLigneGauche().get(i);   //les points pour les lignes 
			Point pG2=this.road.getLigneGauche().get(i+1);
			Point pD1=this.road.getLigneDroite().get(i);
			Point pD2=this.road.getLigneDroite().get(i+1);			
			g.setColor(Color.yellow);	//on choisit la couleur de la route 

			if(pG2.y<HORIZON) {
	
				/** on dessine la "route" constituee d'ovales jaunes formant une ligne entre deux bouees */
				int ecart=((pG1.y-pG2.y)/10);
				
				float penteg = (float)((LARG_FENETRE/2) - (pG1.x) )/ ((float)(HORIZON) - (float)(pG1.y)); //calcul de la pente entre deux points 		
				float pented = (float)((LARG_FENETRE/2) - (pD1.x) )/ ((float)(HORIZON) - (float)(pD1.y)); //calcul de la pente entre deux points 

				for(int j=(pG1.y-ecart);j>=HORIZON;j-=ecart) {//on prend 9 points entre 2 boueesGauches (meme chose pour boueesDroites)	separes par l'"ecart"		
					int tailleOval=j/25;					
					float absOvalG =  (-penteg*(HORIZON-j)+(LARG_FENETRE/2)) ;
					float absOvalD =  (-pented*(HORIZON-j)+(LARG_FENETRE/2)) ;					
					g.fillOval((int)absOvalD, j, tailleOval, tailleOval);
					g.fillOval((int)absOvalG, j, tailleOval, tailleOval);			
				}
			
				/** on dessine selon le if la bouee normale ou bien la speciale */
				int tailleBouee=1+((pG1.y*pG1.y)/5000);// faire grossir les images qui se rapporchent (de maniere simpliste)
				if((etat.getScore()%5==4 && i==road.getPointProches()+1)
					||
					(etat.getScore()%5==0 && i==road.getPointProches())) {//conditions pour la bouee speciale
						g.drawImage(ress.getImage(9),(pG1.x-tailleBouee/2),(pG1.y-tailleBouee/2), tailleBouee,  tailleBouee, this);
						g.drawImage(ress.getImage(9),(pD1.x-tailleBouee/2),(pD1.y-tailleBouee/2), tailleBouee, tailleBouee, this);
				}
				else {//bouee "ordinaire"					
				g.drawImage(ress.getImage(1),(pG1.x-tailleBouee/2),(pG1.y-tailleBouee/2), tailleBouee,  tailleBouee, this);
				g.drawImage(ress.getImage(1),(pD1.x-tailleBouee/2),(pD1.y-tailleBouee/2), tailleBouee, tailleBouee, this);	
				}
			
			}
			else {
							
				/** on dessine la "route" constituee d'ovales jaunes formant une ligne entre deux bouees */
				int ecart1=((pG1.y-pG2.y)/10);
				float penteg1 = (float)((pG2.x) - (pG1.x) )/ ((float)(pG2.y) - (float)(pG1.y)); //calcul de la pente entre deux points 				
				float pented1 = (float)((pD2.x) - (pD1.x) )/ ((float)(pD2.y) - (float)(pD1.y)); //calcul de la pente entre deux points 				
				
				for(int j=pG2.y+ecart1;j<=(pG1.y-ecart1);j+=ecart1) {//on prend 9 points entre 2 boueesGauches (meme chose pour boueesDroites) separes par l'"ecart"
					int tailleOval=j/25;	
					float absOvalG =  (-penteg1*(pG1.y-j)+pG1.x) ;
					float absOvalD =  (-pented1*(pD1.y-j)+pD1.x) ;				
					g.fillOval((int)absOvalG, j, tailleOval, tailleOval);
					g.fillOval((int)absOvalD, j, tailleOval, tailleOval);
				}
				
				/** on dessine selon le if la bouee normale en arriere plan ou bien la speciale en premier plan   */
				int tailleBouee=1+((pG2.y*pG2.y)/5000);
				if((etat.getScore()%5==0 && i==road.getPointProches())) {//conditions pour la bouee speciale
					int tailleBoueeSP=1+((pG1.y*pG1.y)/5000);
					g.drawImage(ress.getImage(9),(pG1.x-tailleBoueeSP/2),(pG1.y-tailleBoueeSP/2), tailleBoueeSP,  tailleBoueeSP, this);
					g.drawImage(ress.getImage(9),(pD1.x-tailleBoueeSP/2),(pD1.y-tailleBoueeSP/2), tailleBoueeSP, tailleBoueeSP, this);
				}
				else {//bouee "ordinaire"
				g.drawImage(ress.getImage(1),(pG2.x-tailleBouee/2),(pG2.y-tailleBouee/2), tailleBouee,  tailleBouee, this);
				g.drawImage(ress.getImage(1),(pD2.x-tailleBouee/2),(pD2.y-tailleBouee/2), tailleBouee, tailleBouee, this);
				}
			}
		}
	}
	
	

	/**cette methode dessine les obstacles  :
	 *  on  afficher des requins a chaques points de la list 
	 * et faire un essai de perspective en faisant grossir les images quand elle vont depuis le haut vers le bas
	 * @param g
	 */
	private void dessineObstacles(Graphics g) {

		for(int i=1;i<this.obstacles.getObstacleList().size();i++) { //on n'affiche pas l'obstacle  en position 0 du tableau (pour aider la hitbox dans Etat)

			Point pObs=this.obstacles.getObstacleList().get(i);

			// dessiner et faire grossir les images(obstacles) qui se rapporchent (de maniere simpliste)

			int tailleObst=1+((pObs.y*pObs.y)/5000);// fait grossir limage en fonction de son ordonnee
			if(this.obstacles.getObstacleListBool().get(i))
				g.drawImage(ress.getImage(8),(pObs.x-tailleObst/2),(pObs.y-tailleObst/2), tailleObst,  tailleObst, this);
			else
				g.drawImage(ress.getImage(5),(pObs.x-tailleObst/2),(pObs.y-tailleObst/2), tailleObst,  tailleObst, this);


		}

	}
	
	
	/**
	 * methode qui dessine/affiche le score au centre en haut de la fenetre
	 * @param g
	 */
	private void dessineMinuteur(Graphics g) {
		
		g.setColor(Color.RED);//choix de la couleur des lettres et chiffres du score
		g.setFont(new Font( "Cambria" ,Font.BOLD,30));// choix de la police et de la taille des lettres et chiffres du score
		g.drawString("Time : "+etat.getMinuteur(), 100, HAUT_FENETRE/12);//on place et dessine un score qui s'incremente a chaque depassement de points
	}

	private void dessineTempsFinal(Graphics g) {
		g.setColor(Color.black);//choix de la couleur des lettres et chiffres du score
		g.setFont(new Font( "Cambria" ,Font.BOLD,20));// choix de la police et de la taille des lettres du score
		g.drawImage(ress.getImage(7),(LARG_FENETRE/2)-72, 240,200,35, this);//on place le "titre" du score final dans le menu principal
		g.setFont(new Font( "Serif" ,Font.BOLD,30));// choix de la police et de la taille des lettres et chiffres du score
		//int tempsfinal =((etat.getTemps_initial()-etat.getMinuteur())+(etat.getTemps_initial()*(etat.getScore()/5)));
		g.drawString("     "+etat.getTempsTotal(), (LARG_FENETRE/2)-30, 260+30);//on place et dessine un score (le nombre) dans le menu principal
	}


	/**
	 * 
	 */
	/**
	 * methode qui dessine/affiche un message de fin avec le score final et termine les thread par la meme occasion
	 * @param g
	 */
	private void MenuFin(Graphics g) {
		removeKeyListener(fleches);//enleve le MouseListener cest a dire l'action de saut produite par un clique
		g.clearRect(0, 0 ,LARG_FENETRE,HAUT_FENETRE);
		g.drawImage(ress.getImage(6), 0, 0,LARG_FENETRE, HAUT_FENETRE,this);
		if(etat.getScore()>-1)//le score n'est egal a -1 qu'au premier lancement du jeu.
			dessineTempsFinal(g);
		
		//POur faire "clignoter l'indication "start"
		if(Horloge.getClignote()==0)
			g.setColor(Color.blue);
		if(Horloge.getClignote()==1)
			g.setColor(Color.red);
		if(Horloge.getClignote()==2)
			g.setColor(Color.magenta);
		if(Horloge.getClignote()==3)
			g.setColor(Color.black);
		
		g.setFont(new Font( "Cambria" ,Font.ITALIC,32));// choix de la police et de la taille des lettres du score
		g.drawString("------------------>  press SPACE to play  <------------------",  (LARG_FENETRE/2)-305, HAUT_FENETRE-100);//on place et dessine le "titre" du score final dans le menu principal

	}
	/**
	 * methode utilisee par le controler pour (re)demarrer la partie.
	 */
	public void DebutDePartie() {
		
		addKeyListener(fleches);
		etat.setScoreAZero();
		etat.initMinuteur();
		etat.setPositionVehicule(ABS_VEHICULE);
		etat.setDeplacement(Etat.getDeplacementMax());
		road.initLignes();
		road.initZigzagTab();
		obstacles.initList();
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
			MenuFin(g);	
		}
		else {
			dessineRoad(g);
			dessineObstacles(g);
			dessineVehicule(g,this.direction);
			g.drawImage(ress.getImage(0), 0, 0, LARG_FENETRE, HORIZON, this);//image "skybackground" d'apres l'horizon
			dessineMinuteur(g);
		}
		
	}

}
