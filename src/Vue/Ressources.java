package Vue;


import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


public class Ressources {
	private Image[] images; //attribut tableau d'image
	private File dossier;
	
	/**
	 * CONSTRUCT: charge les images dans un tableau à partir des fichiers du dossier image.
	 */
	public Ressources() {
		dossier = new File("images/");
		Image image = null;
		images = new Image[dossier.listFiles().length];
		for(int i=0;i<images.length;i++) {
			try {
				
				image =ImageIO.read(getClass().getClassLoader().getResource(i+".png"));			
				images[i]=image;
	
			} catch (IOException e) {
				System.out.println("l'image"+i+"n'a pas pu etre lue");
			}
		}
	}
	
	/**fonction getImage(x) du tableau
	 * @param x 
	 * @return L'image désirée
	 */
	public Image getImage(int x) {
		return images[x];
	}

}
