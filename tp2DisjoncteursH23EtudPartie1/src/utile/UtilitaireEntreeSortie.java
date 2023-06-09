package utile;
import javax.swing.JOptionPane;

import disjoncteurs.Disjoncteur;
/*
* Classe qui regroupe tout qui concerne les Entr�es/Sorties.
*
* On peut y sasir une tension et un amp�rage valide.
* 
*  @author Pierre B�lisle
*  @Copyright H2023
*/
public class UtilitaireEntreeSortie {

	/*
	 * Fonction  pour saisir et valider la tension d'un disjoncteur. 
	 */
	public static int tensionValide() {
		
		/*
		 * Strat�gie : On utilise JOptionPane pour la saisie et les constantes 
		 * pr�vues.
		 */
		
		// JOptionPane retourne un String.
		String tension;

		// Utilis� pour la validation avec les String.
		String tensionEntree =  String.valueOf(Disjoncteur.TENSION_ENTREE) ;
		String tensionPhase =  String.valueOf(Disjoncteur.TENSION_PHASE) ;

		do{
			tension = 
					JOptionPane.showInputDialog("Entrez une tension valide " + 
							Disjoncteur.CHAINE_TENSION_PERMISE);

		}while(tension != null && 
				!tension.equals(tensionEntree) &&
				!tension.equals(tensionPhase));

		return (tension== null)?0:Integer.parseInt(tension) ;
	}

	/*
	 * 
	 */


	/*
	 * Fonction locale pour saisir et valider l'amp�rage d'un disjoncteur. 
	 */
	public static int ampereValide() {

		/*
		 * Strat�gie : On utilise JOptionPane pour la saisie et les constantes 
		 * pr�vues.
		 */
		
		// JOptionPane retourne un String.
		String ampere;

		do{
			ampere = 
					JOptionPane.showInputDialog("Entrez un amp�rage valide " +
							Disjoncteur.CHAINE_AMPERAGE_PERMIS);

		}while(ampere != null && !ampereDsTableau(ampere));

		// Si c'est null, on retourne 0.
		return (ampere== null)?0:Integer.parseInt(ampere) ;
	}

	/*
	 * Fonction locale pour v�rifier si l'amp�re re�u est dans le tableau des 
	 * amp�rage permis
	 */
	private static boolean ampereDsTableau(String ampere) {
		
		/*
		 * Strat�gie : On utilise JOptionPane pour la saisie et Integer.PaserInt pour
		 * la conversion.  S'il  y a lev�e d'une exception, c'est invalide.
		 */
		
		boolean permis = false;

		int i = 0;

		// Si le bool�en devient true, c'est termin�, on a trouv�.
		while(i < Disjoncteur.AMPERAGES_PERMIS.length && !permis){

			try{

				// Tentative de conversion.
				permis =  Integer.parseInt(ampere) == 
						Disjoncteur.AMPERAGES_PERMIS[i];
			}

			catch(Exception e){
				// On ne fait rien s'il y a une exeption, c'est invalide.
			}
			i++;
		}

		return permis;
	}

	/**
	 * Saisit et valide un entier entre min et max.  La fonction retourne min - 1
	 * si l'utilisateur annule.
	 * 
	 * 
	 * @param msgSollic Le message affich�.
	 * @param min La plus petite valeur permise.
	 * @param max La plus grande valeur permise.
	 * 
	 * @return L'entier saisit ou min-1 si l'utilisateur annule.
	 */
	public static int entierValide(String msgSollic, int min, int max) {

		String entier = null;

		// Version String des valeurs re�ues.
		String minString =  String.valueOf(min) ;
		String maxString =  String.valueOf(max) ;

		do{
			entier = 
					JOptionPane.showInputDialog(msgSollic + 
							" entre " + minString + " et " + maxString);

			// V�rifier si c'est convertissable en entier.
			try{
				
				if(entier != null){
					int x = Integer.parseInt(entier);
				}
			}
			catch(Exception e){

				// Dans le cas d'une exception, on remet un entier invalide. 
				entier =  String.valueOf(min-1) ;
			}

		}while(entier != null && 
				(Integer.parseInt(entier) < min ||
						Integer.parseInt(entier) > max));

		return (entier== null)?min-1:Integer.parseInt(entier) ;
	}

	/**
	 * Saisit et valide un r�el entre min et max.  
	 * La fonction retourne Double.NaN si l'utilisateur annule.
	 * 
	 * 
	 * @param msgSollic Le message affich�.
	 * @param min La plus petite valeur permise.
	 * @param max La plus grande valeur permise.
	 * 
	 * @return L'entier saisit ou min-1 si l'utilisateur annule.
	 */
	public static double reelValide(String msgSollic) {

		String reel = null;

		reel = JOptionPane.showInputDialog(msgSollic);

		// V�rifier si c'est convertissable en r�el.
		try{

			if(reel != null){
				// Tentative de conversion.
				double x = Double.parseDouble(reel);
			}
		}
		catch(Exception e){

			// Dans le cas d'une exception, reel == null. 
			reel =  null;
		}
		return (reel== null)?Double.NaN:Double.parseDouble(reel) ;
	}

}