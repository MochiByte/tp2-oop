package utile;
import javax.swing.JOptionPane;

import disjoncteurs.Boite;
import disjoncteurs.Disjoncteur;

/**
 * Classe qui contient les SP pour gérer les boutons d'options
 * de menu.
 * 
 * S'il y a ajout de bouton, il faut modifier cette classe et y ajouter
 * le comportement désiré.
 * 
 * @author Pierre Bélisle
 * @version H2023
 *
 */
public class UtilitaireGestionMenu {

	// Extension choisie arbitrairement pour les noms de fichier contenant
	// une boîte.
	public static final String EXTENSION_BOITE = "bte";
	
	public static final String DESC_EXTENSION =
			"*."+EXTENSION_BOITE;

	/**
	 * L'utilisateur a quitté, on lui demande si c'est bien ce qu'il veut et 
	 * s'il veut sauvegarder avant de quitter.
	 * 
	 * return Si l'utilisateur poursuit dans sa démarche de quitter.
	 */
	public static boolean veutSortir(Boite boite){

		// Sera mis à vrai si l'utilisateur quitte pour de bon
		boolean sortie = false;
		
		/*
		 * La sollicitation est réalisée avec JOptionPane et la
		 * sauvegarde par la procédure locale.
		 */
		String reponse = 
				JOptionPane.showInputDialog(
						"Désirez-vous réellement quitter (o/n? )");

		if(reponsePositive(reponse)){

			reponse = 
					JOptionPane.showInputDialog(
							"Désirez-vous sauvegarder avant de  quitter (o/n? )");

			if(reponsePositive(reponse)){
				sauvegarderBoite(boite);
			}

			sortie = true;
		}		

		return sortie;
	}

	/**
	 * Ajoute un disjoncteur à la boîte.
	 * 	
	 * @param boite
	 */
	public static void ajouterDisjoncteur(Boite boite){

		// Sert à obtenir une coordonnée disponible dans la boîte.
		Coord c;

		// Sert à obtenir les infos du disjoncteur aajouter.

		// Selement s'il y en a de disponible
		if(boite.emplacementEncoreDisponible()){

			int tension;
			int ampere =  UtilitaireEntreeSortie.ampereValide();

			if(ampere != 0){

				tension = UtilitaireEntreeSortie.tensionValide();

				if(tension != 0){

					// Doit être récupéré et validé.
					Disjoncteur d = new Disjoncteur(ampere, tension);

					c  = boite.getEmplacementDisponible();

					boite.ajouterDisjoncteur(c.getColonne(), c.getLigne(), d);	
				}

				else{

					JOptionPane.showMessageDialog(null,
							"Il n'y a plus de place dans la boîte.  désolé!");
				}
			}
		}					
	}

	/**
	 * Ajoute une demande à un disjoncteur.  Si la demande est trop grande, 
	 * le disjoncteur est éteint.
	 * 
	 * @param boite  La boite à considérer.
	 */
	public static void ajouterDemande(Boite boite){
		
		// On commence par saisir la position du disjoncteur et ensuite
		// l'ampérage du disjoncteur.  L'utilisateur peut annuler en tout temps.
		double demande;
		int ligne;
		int colonne = 
				UtilitaireEntreeSortie.entierValide("Entrez un numéro de colonne", 
						1, 
						Boite.NB_COLONNES);
		
		// Si l'utilisateur n'a pas annulé, on saisit le numéro de ligne.
		if(colonne != 0){
			
			ligne = UtilitaireEntreeSortie.entierValide("Entrez un numéro de ligne", 
					1, 
					Boite.NB_LIGNES_MAX);
			
			// Si l'utilisateur n'a pas annulé, on saisit l'ampérage.
			if(ligne != 0){

				demande = 
						UtilitaireEntreeSortie.reelValide("Entrez l'ampérage à ajouter au disjoncteur");
				
				//Si l'utilisateur n'a pas annulé, on ajoute la demande en A au disjoncteur.
				if(demande != Double.NaN){
					
					
					// On ne fait rien si la demande est de 0.
					if(demande > 0){
						
						boite.ajouterDemande(colonne - 1,  ligne - 1, demande);
					}
					else if(demande < 0){
						
						boite.retirerPuissance(colonne - 1,  ligne - 1, demande);
					}
				}

				
			}
		}		
	}

	// Évite la répétition de cette évaluation booléenne.
	private static boolean reponsePositive(String reponse){
		
		boolean sortie = false;

		if(reponse != null){
			
			sortie = reponse.equals("o") || reponse.equals("O") ||
					reponse.toUpperCase().equals("OUI");
		}


		return sortie;
	}

	/**
	 * Sert à l'interaction avec l'utilisateur pour obtenir le nom du fichier 
	 * de sauvegarde et sa validation.
	 * 
	 * @return La boite récupérer ou null.
	 */
	public static Boite recupererBoite() {

		/*
		 * Stratégie : On utilise l'utilitaireFichierpour obtenir un nom de fichier
		 * valide.  Si le nom est null on avise avec JOptionPane.  Même chose 
		 * pour la boite si la référence reçue est nulle.
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
		 * Stratégie.  On utilise obtenirFic de l'utilitaire fichier pour obtenir un 
		 * nom valide et on le sauvegarde aussi à l'aide de l'utilitaire.  
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
			UtilitaireFichier.sauvegarderDsFichierTexte(boite);
		}		
	}
}
