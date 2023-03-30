package utile;
/**
 * Classe utilitaire qui permet de sauvegarder dans un fichier binaire ou texte.
 * Elle petmer aussi de de r�cup�rer une boite.
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import disjoncteurs.Boite;
import disjoncteurs.Disjoncteur;
import programmePrincipal.Constantes;

public class UtilitaireFichier {

	private static final String TAB = "\t";

	// Mode d'ouverture possible du fileChooser.  
	// A un effet sur le titre de la bo�te.
	public static final int OUVRE = 0;
	public static final int SAUVE = 1;

	// Nom pr�d�fini du fichier texte de sauvegarde
	private static final String FIC_TXT_BOITE = "boite.xls";

	/*
	 * Fonction qui retourne le pour �viter la r�p�tition de code
	 * 
	 * @param nomFiltre Les fichiers montr�s par le fileChooser.
	 * @param extension L'extension de fichiers que montre le fileChooser.
	 * @param mode Le mode pour affiche un titre significatif � fileChooser.
	 * 
	 * @return Le fichier ouvert ou  null si le nom n'est pas valide 
	 * ou que l'utilisateur a annul�. 
	 */
	public static String obtenirNomFic(String nomFiltre, 
			String extension,
			int mode){

		
		/*
		 * Strat�gie : On utilise le fileChooser de Java pour obtenir un fichier
		 * valide avec l'extension fourni (ou pas).  Si le fichier est invalide,
		 * on retourne null.
		 * 
		 */
		// Cr�ation du s�lectionneur de fichier.
		JFileChooser fc = new JFileChooser(".");

		// Pour obtenir le fichier et son nom.
		File fic = null;
		String name = null;

		int etatOuverture;

		// On filtre seulement les fichiers avec l'extension fourni�.
		FileNameExtensionFilter filter = 
				new FileNameExtensionFilter(nomFiltre, extension);

		fc.addChoosableFileFilter(filter);
		fc.setFileFilter(filter);


		// On obtient le nom du fichier ouvrir.
		if(mode ==OUVRE){
			etatOuverture = fc.showOpenDialog(null);
		}
		else{
			etatOuverture = fc.showSaveDialog(null);
		}

		// Seulement si le fichier est choisi.
		if(etatOuverture == JFileChooser.APPROVE_OPTION){
			fic = fc.getSelectedFile();
			name = fic.getName();
			
			// �vite pls appels � l'accesseur.
			int taille = name.length();
			
			// On ajoute le . pour la comparaison avec l'extension re�ue.
			if(taille <= extension.length() + 1 && 
				name.substring(taille-Constantes.TAILLE_EXTENSION, 
						taille -1) != "." + extension){
				
				name = null;
			}
		}

		return name;
	}

	/**
	 * Sauvegarde la bo�te dans un fichier texte au nom de FIC_TXT_BOITE.
	 * 
	 */
	public static void sauvegarderDsFichierTexte(Boite boite){

		File fic = new File(FIC_TXT_BOITE);

		if(fic != null){

			try {

				// On parcourt le fichier texte en lisant une string (ligne ) � la fois.
				PrintWriter fichier = new PrintWriter(fic);

				ecrireEnteteBoite(fichier, boite);
				ecrireEnteteTable(fichier, boite);

				for(int colonne = 0; colonne < Boite.NB_COLONNES; colonne++){

					for(int i = 0; i < Boite.NB_LIGNES_MAX; i++){
						ecrireDisjoncteur(fichier, boite,colonne, i);
					}
				}
				fichier.close();


			} catch (FileNotFoundException e) {				
				e.printStackTrace();

			}						
		}			
	}

	/*
	 * Proc�dure locale pour �crire l'en-t�te des infos de la bo�te.
	 */
	private static void ecrireEnteteBoite(PrintWriter fichier, Boite boite) {

		fichier.println("Amp�rage de la bo�te" + 
		                        TAB + 
								boite.getMaxAmperes());
		
		fichier.println("Nombre de disjoncteurs" + 
		                         TAB + 
		                         boite.getNbDisjoncteurs());
		
		fichier.println("Qt�e de disjoncteurs phase" + 
		                         TAB + 
		                         boite.getNbDisjoncteursPhase());
		
		fichier.println("Qt�e de disjoncteurs entr�e" + 
		                        TAB + 
		                        boite.getNbDisjoncteursEntree());

		fichier.println();

		
	}

	/*
	 * Proc�dure locale pour �crire les donn�es du disjoncteur de la bo�te �
	 * la position (colonne-ligne0.
	 */
	private static void ecrireDisjoncteur(PrintWriter fichier, 
			Boite boite, 
			int colonne, int ligne) {

		Disjoncteur d = boite.getDisjoncteur(colonne, ligne);

		if(d != null){
			
			fichier.print("(" + colonne + "-" + ligne +")" + TAB);
			fichier.print(d.getTension() + TAB);
			fichier.print(d.getAmpere() + TAB);
			fichier.println(d.getPuissanceEnAmpere());
		}
	}

	/*
	 * Proc�dure locale pour �crire l'en-t�te des infos des disjoncteurs de la bo�te.
	 */
	private static void ecrireEnteteTable(PrintWriter fichier,  Boite boite) {

		fichier.print("Position (colonne-ligne)" + TAB);
		fichier.print("Tension" + TAB);
		fichier.print("Amp�rage utilis� (W)" + TAB);
		fichier.println("Ratio d'utilisation" + TAB);
		fichier.println();
	}

	/**
	 * Sauvegarde la bo�te dans le fichier fichier binaire avec le nom re�u.
	 * 
	 * On pr�sume le nom de fichier valide.
	 * 
	 * @param nomFic o� sauvegarder la bo�te.
	 * @param boite La bo�te � sauvegarder.
	 */
	public static void sauvegarderBoite(Boite boite, String nomFic){

		/*
		 * Strat�gie : On utilise FileOutputStream qui �crit en binaire des objets
		 * S�rializable.
		 */

		//  Tampon de ficher.
		FileOutputStream fs;

		try {
			//On ouvre le fichier d'�criture
			fs = new FileOutputStream(nomFic);

			// Tampon d'objet pour la s�rialisation.
			ObjectOutputStream o = new ObjectOutputStream(fs); 

			// On sauve le tout.
			o.writeObject(boite);

			// Fermeture de la connection.
			o.close();    

		} catch (IOException e) {		
			e.printStackTrace();
		}

	}
	
	/**
	 * Ouvre le fichier dont le nom correspond � celui re�u.
	 * 
	 * Exception : Le fichier doit contenir une bo�te sauvegarder par
	 * la m�thode sauvegarderBoite.
	 * 
	 * @param nomFic Le nom du fichier � ouvrir
	 * @return La bo�te contenu dans le fichier.
	 */
	public static Boite recupererBoite(String nomFic){

		/*
		 * Strat�gie : On obtient le nom de fichier � l'aide d'obtenirFic et
		 * on y lit le contenu de la bo�te si l'utilisateur n'a pas annul�.
		 */

		Boite boite = null;

		// Le tampon de fichier � ouvrir en lecture.
		FileInputStream fs;

		try {

			// On tente d'ouvrir le fichier.
			fs = new FileInputStream(nomFic);

			// Tampon d'objet pour d�s�rialiser le tout.
			ObjectInputStream o = new ObjectInputStream(fs); 

			// R�cup�ration de la saison via le tampon d'objet.
			boite  = ((Boite) o.readObject());

			// Fermeture de la connection.
			o.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return boite;
	}
}