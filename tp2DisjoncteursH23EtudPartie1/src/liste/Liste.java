package liste;

import java.io.Serializable;

/**
 * Classe qui implémente une liste
 * avec une position numérique donnée à l'appel des méthodes.  
 *
 * On ne lève aucune exception externe pour cet exemple.  Seul les exceptions 
 * systèmes seront levées s'il y a un problème.
 *
 * @author  Pierre Bélisle
 * @version Copyright H2023
 */
public class Liste implements Serializable{

	/*
	 * STRATÉGIE : Pour toutes les fonctions, on reçoit un indice de tableau et
	 * on y effectue l'opéraions.  L'indice doit être valide.
	 */

	// Nombre d'éléments possibles au maximum par défaut.
	public static final int MAX_ELEMENTS = 100;

	// La liste avec les objets.
	private Object [] liste;

	// Maintenu à jour après une insertion ou une suppresion.
	private int nbElements;


	/**
	 * Crée une liste vide de MAX_ELEMENTS au maximum.
	 */
	public Liste(){

		/**
		 * STRATÉGIE : On utilise le constructeur suivant (bonne pratique)
		 */

		this(MAX_ELEMENTS);

	}

	/**
	 * Crée une liste vide de la taille fournie au maximum.
	 */
	public Liste(int taille){

		/**
		 * STRATÉGIE : On initialise explicitement les valeurs plutôt 
		 * que d'utiliser l'initialisation automatique des attributs de 
		 * l'environnement Eclipse.
		 */

		liste = new Object[taille];
		nbElements = 0;

	}



	/**
	 * Retourne si la liste est vide.
	 *
	 * Antécédent : Aucun.
	 *
	 * Conséquent : Aucun.
	 *
	 * @return Si true la liste est vide et false autrement
	 */
	public boolean estVide(){

		/*
		 * STRATÉGIE : On retourne simplement l'évaluation booléenne de la
		 *  comparaison du nombre d'éléments avec 0.
		 */
		return nbElements == 0;
	}

	/**
	 * Décale les données d'un tableau d'une case vers la droite pour les cases
	 * de début à fin.  Début et fin sont considérés comme valides.
	 */
	private void decalerDroite(Object[] tab, int debut, int fin){

		for(int i = fin; i >= debut;i--){

			tab[i+1] = tab[i];
		}

	}


	/**
	 * Décale les données d'un tableau d'une case vers la gauche pour les cases
	 * de début à fin.  Début et fin sont considérés comme valides.
	 */
	private void decalerGauche(Object[] tab, int debut, int fin){

		for(int i = debut; i <= fin;i++){

			tab[i-1] = tab[i];
		}

	}


	/**
	 * Insère l'élément reçu à la position reçue après avoir déplacé
	 * tous les éléments vers la droite d'une case.
	 * 
	 * Si la position voulue dépasse le nombre d'éléments -1 dans la liste, 
	 * l'élément est inséré après le dernier sans décalage.
	 *
	 * Antédécent : Aucun.
	 *
	 * Conséquent : nbElement = nbElement + 1 &&
	 *              liste.getElement(positionCourante) == element.
	 *
	 * @param element L'élément à insérer à la position de l'insertion.
	 * @param position Position de l'insertion.
	 *
	 */
	public void inserer(Object element, int position){

		/*
		 * STRATÉGIE : On utilise le nombre d'éléments pour tester s'il reste
		 * de  la place. Si c'est le cas, on décale les données à l'aide de 
		 * la procédure locale et on met l'élément à la position courante reçue. 
		 */

		//Si le tableau n'est pas plein
		if(nbElements < liste.length){
			
			// Si on insère dans le tableau, il faut décaler.
			if(position < nbElements) {

				decalerDroite(liste, position, nbElements-1);

				liste[position] = element;
			}
			else {
				
				liste[nbElements] = element;
			}
			
			// Un élément de plus.
			nbElements++;
		}

	}

	/**
	 * Supprime l'élément à la position fournie
	 * 
	 * La position doit être entre 0 et getNbElements() - 1.
	 */
	public void supprimer(int position){

		decalerGauche(liste, position+1, --nbElements);
	}

	//LES AUTRES MÉTHODES
	/**
	 * Retourne l'élément à la position voulue.
	 *
	 * Antécédent : La liste ne doit pas être vide et la position valide.
	 *
	 * @return L'élément à la position courante
	 */
	public Object getElement(int position){

		/*STRATÉGIE : Retourne simplement l'élément à la
		 * position courante.
		 */
		return liste[position];
	}

	/**
	 * Retourne le nombre d'éléments actuellement dans la liste.
	 *
	 * Antécédent : aucun.
	 * Conséquent : aucun.
	 *
	 * @return Le nombre d'éléments de la liste.
	 */
	public int getNbElements(){

		return nbElements;
	}
}