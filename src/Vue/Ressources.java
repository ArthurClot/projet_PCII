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
		dossier = new File("src/images/");
		System.out.println("Nombre de fichiers dans le dossier images :"+Integer.toString(dossier.listFiles().length));
		Image image = null;
		images = new Image[dossier.listFiles().length];
		for(int i=0;i<images.length;i++) {
			try {
				File fichier = new File("src/images/"+i+".png");
				image = ImageIO.read(fichier);
				images[i]=image;
				System.out.println(fichier.toString()+"   lu");
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
