package utile;
/**
 * Classe utilitaire qui permet de sauvegarder dans un fichier binaire ou texte.
 * Elle petmer aussi de de récupérer une boite.
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
	// A un effet sur le titre de la boîte.
	public static final int OUVRE = 0;
	public static final int SAUVE = 1;

	// Nom prédéfini du fichier texte de sauvegarde
	private static final String FIC_TXT_BOITE = "boite.xls";

	/*
	 * Fonction qui retourne le pour éviter la répétition de code
	 * 
	 * @param nomFiltre Les fichiers montrés par le fileChooser.
	 * @param extension L'extension de fichiers que montre le fileChooser.
	 * @param mode Le mode pour affiche un titre significatif à fileChooser.
	 * 
	 * @return Le fichier ouvert ou  null si le nom n'est pas valide 
	 * ou que l'utilisateur a annulé. 
	 */
	public static String obtenirNomFic(String nomFiltre, 
			String extension,
			int mode){

		
		/*
		 * Stratégie : On utilise le fileChooser de Java pour obtenir un fichier
		 * valide avec l'extension fourni (ou pas).  Si le fichier est invalide,
		 * on retourne null.
		 * 
		 */
		// Création du sélectionneur de fichier.
		JFileChooser fc = new JFileChooser(".");

		// Pour obtenir le fichier et son nom.
		File fic = null;
		String name = null;

		int etatOuverture;

		// On filtre seulement les fichiers avec l'extension fournié.
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
			
			// Évite pls appels à l'accesseur.
			int taille = name.length();
			
			// On ajoute le . pour la comparaison avec l'extension reçue.
			if(taille <= extension.length() + 1 && 
				name.substring(taille-Constantes.TAILLE_EXTENSION, 
						taille -1) != "." + extension){
				
				name = null;
			}
		}

		return name;
	}

	/**
	 * Sauvegarde la boîte dans un fichier texte au nom de FIC_TXT_BOITE.
	 * 
	 */
	public static void sauvegarderDsFichierTexte(Boite boite){

		File fic = new File(FIC_TXT_BOITE);

		if(fic != null){

			try {

				// On parcourt le fichier texte en lisant une string (ligne ) à la fois.
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
	 * Procédure locale pour écrire l'en-tête des infos de la boîte.
	 */
	private static void ecrireEnteteBoite(PrintWriter fichier, Boite boite) {

		fichier.println("Ampérage de la boîte" + 
		                        TAB + 
								boite.getMaxAmperes());
		
		fichier.println("Nombre de disjoncteurs" + 
		                         TAB + 
		                         boite.getNbDisjoncteurs());
		
		fichier.println("Qtée de disjoncteurs phase" + 
		                         TAB + 
		                         boite.getNbDisjoncteursPhase());
		
		fichier.println("Qtée de disjoncteurs entrée" + 
		                        TAB + 
		                        boite.getNbDisjoncteursEntree());

		fichier.println();

		
	}

	/*
	 * Procédure locale pour écrire les données du disjoncteur de la boîte à
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
	 * Procédure locale pour écrire l'en-tête des infos des disjoncteurs de la boîte.
	 */
	private static void ecrireEnteteTable(PrintWriter fichier,  Boite boite) {

		fichier.print("Position (colonne-ligne)" + TAB);
		fichier.print("Tension" + TAB);
		fichier.print("Ampérage utilisé (W)" + TAB);
		fichier.println("Ratio d'utilisation" + TAB);
		fichier.println();
	}

	/**
	 * Sauvegarde la boîte dans le fichier fichier binaire avec le nom reçu.
	 * 
	 * On présume le nom de fichier valide.
	 * 
	 * @param nomFic où sauvegarder la boîte.
	 * @param boite La boîte à sauvegarder.
	 */
	public static void sauvegarderBoite(Boite boite, String nomFic){

		/*
		 * Stratégie : On utilise FileOutputStream qui écrit en binaire des objets
		 * Sérializable.
		 */

		//  Tampon de ficher.
		FileOutputStream fs;

		try {
			//On ouvre le fichier d'écriture
			fs = new FileOutputStream(nomFic);

			// Tampon d'objet pour la sérialisation.
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
	 * Ouvre le fichier dont le nom correspond à celui reçu.
	 * 
	 * Exception : Le fichier doit contenir une boîte sauvegarder par
	 * la méthode sauvegarderBoite.
	 * 
	 * @param nomFic Le nom du fichier à ouvrir
	 * @return La boîte contenu dans le fichier.
	 */
	public static Boite recupererBoite(String nomFic){

		/*
		 * Stratégie : On obtient le nom de fichier à l'aide d'obtenirFic et
		 * on y lit le contenu de la boîte si l'utilisateur n'a pas annulé.
		 */

		Boite boite = null;

		// Le tampon de fichier à ouvrir en lecture.
		FileInputStream fs;

		try {

			// On tente d'ouvrir le fichier.
			fs = new FileInputStream(nomFic);

			// Tampon d'objet pour désérialiser le tout.
			ObjectInputStream o = new ObjectInputStream(fs); 

			// Récupération de la saison via le tampon d'objet.
			boite  = ((Boite) o.readObject());

			// Fermeture de la connection.
			o.close();

		} catch (Exception e) {

			e.printStackTrace();
		}
		return boite;
	}
}