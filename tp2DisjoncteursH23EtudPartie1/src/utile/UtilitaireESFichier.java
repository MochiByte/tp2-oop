package utile;

import javax.swing.JOptionPane;
import disjoncteurs.Boite;

/*
 * Sert essentiellement à ouvrir les dialogues et à appeler la bonne méthode
 * selon l'option choisie qui doit être valide.
 * 
 * @author Pierre Bélisle
 * @version Copyright H2023
 */
public class UtilitaireESFichier {


	// Extension choisie arbitrairement pour les noms de fichier contenant
	// une boîte.
	public static final String EXTENSION_BOITE = "bte";

	// Sert pour le filtre des extension de fichier de JFileChooser.
	public static final String DESC_EXTENSION =
			"*."+EXTENSION_BOITE;

	/**
	 * Sert à l'interaction avec l'utilisateur pour obtenir le nom du fichier
	 * de sauvegarde et sa validation.
	 *
	 * @return La boite récupérer ou null.
	 */
	public static Boite recupererBoite() {

		/*
		 * Stratégie : On utilise l'utilitaireFichier pour obtenir un nom 
		 * de fichier valide.  Si le nom est null on avise avec JOptionPane.  
		 * Même chose pour la boite si la référence reçue est nulle.
		 */
		Boite boite = null;

		String nomFic = UtilitaireFichier.obtenirNomFic(DESC_EXTENSION,
				EXTENSION_BOITE,
				UtilitaireFichier.OUVRE);

		if(nomFic != null){
			boite = UtilitaireFichier.recupererBoite(nomFic);
		}

		if(boite == null){
			JOptionPane.showMessageDialog(null,
					"Problème de récupération, réessayer svp.");
		}

		return boite;
	}

	/**
	 * Sert à l'interaction avec l'utilisateur pour obtenir le nom du fichier
	 * de sauvegarde et sa validation.
	 *
	 * @param La boîte à sauvegarder.
	 */
	public static void sauvegarderBoite(Boite boite) {

		/*
		 * Stratégie.  On utilise obtenirFic de l'utilitaire fichier pour
		 * obtenir un nom valide et on sauvegarde aussi à l'aide de l'utilitaire.
		 * 
		 * S'il y a un problème, on avise avec JOptionPane.
		 */
		String nomFic = UtilitaireFichier.obtenirNomFic(DESC_EXTENSION,
				EXTENSION_BOITE,
				UtilitaireFichier.SAUVE);

		if(nomFic == null){
			JOptionPane.showMessageDialog(null,
					"Problème de sauvegarde, réessayer svp.");
		}

		else{
			UtilitaireFichier.sauvegarderBoite(boite, nomFic);
		}
	}
}