package liste;

import java.io.Serializable;

/**
 * Classe qui impl�mente une liste
 * avec une position num�rique donn�e � l'appel des m�thodes.  
 *
 * On ne l�ve aucune exception externe pour cet exemple.  Seul les exceptions 
 * syst�mes seront lev�es s'il y a un probl�me.
 *
 * @author  Pierre B�lisle
 * @version Copyright H2023
 */
public class Liste implements Serializable{

	/*
	 * STRAT�GIE : Pour toutes les fonctions, on re�oit un indice de tableau et
	 * on y effectue l'op�raions.  L'indice doit �tre valide.
	 */

	// Nombre d'�l�ments possibles au maximum par d�faut.
	public static final int MAX_ELEMENTS = 100;

	// La liste avec les objets.
	private Object [] liste;

	// Maintenu � jour apr�s une insertion ou une suppresion.
	private int nbElements;


	/**
	 * Cr�e une liste vide de MAX_ELEMENTS au maximum.
	 */
	public Liste(){

		/**
		 * STRAT�GIE : On utilise le constructeur suivant (bonne pratique)
		 */

		this(MAX_ELEMENTS);

	}

	/**
	 * Cr�e une liste vide de la taille fournie au maximum.
	 */
	public Liste(int taille){

		/**
		 * STRAT�GIE : On initialise explicitement les valeurs plut�t 
		 * que d'utiliser l'initialisation automatique des attributs de 
		 * l'environnement Eclipse.
		 */

		liste = new Object[taille];
		nbElements = 0;

	}



	/**
	 * Retourne si la liste est vide.
	 *
	 * Ant�c�dent : Aucun.
	 *
	 * Cons�quent : Aucun.
	 *
	 * @return Si true la liste est vide et false autrement
	 */
	public boolean estVide(){

		/*
		 * STRAT�GIE : On retourne simplement l'�valuation bool�enne de la
		 *  comparaison du nombre d'�l�ments avec 0.
		 */
		return nbElements == 0;
	}

	/**
	 * D�cale les donn�es d'un tableau d'une case vers la droite pour les cases
	 * de d�but � fin.  D�but et fin sont consid�r�s comme valides.
	 */
	private void decalerDroite(Object[] tab, int debut, int fin){

		for(int i = fin; i >= debut;i--){

			tab[i+1] = tab[i];
		}

	}


	/**
	 * D�cale les donn�es d'un tableau d'une case vers la gauche pour les cases
	 * de d�but � fin.  D�but et fin sont consid�r�s comme valides.
	 */
	private void decalerGauche(Object[] tab, int debut, int fin){

		for(int i = debut; i <= fin;i++){

			tab[i-1] = tab[i];
		}

	}


	/**
	 * Ins�re l'�l�ment re�u � la position re�ue apr�s avoir d�plac�
	 * tous les �l�ments vers la droite d'une case.
	 * 
	 * Si la position voulue d�passe le nombre d'�l�ments -1 dans la liste, 
	 * l'�l�ment est ins�r� apr�s le dernier sans d�calage.
	 *
	 * Ant�d�cent : Aucun.
	 *
	 * Cons�quent : nbElement = nbElement + 1 &&
	 *              liste.getElement(positionCourante) == element.
	 *
	 * @param element L'�l�ment � ins�rer � la position de l'insertion.
	 * @param position Position de l'insertion.
	 *
	 */
	public void inserer(Object element, int position){

		/*
		 * STRAT�GIE : On utilise le nombre d'�l�ments pour tester s'il reste
		 * de  la place. Si c'est le cas, on d�cale les donn�es � l'aide de 
		 * la proc�dure locale et on met l'�l�ment � la position courante re�ue. 
		 */

		//Si le tableau n'est pas plein
		if(nbElements < liste.length){
			
			// Si on ins�re dans le tableau, il faut d�caler.
			if(position < nbElements) {

				decalerDroite(liste, position, nbElements-1);

				liste[position] = element;
			}
			else {
				
				liste[nbElements] = element;
			}
			
			// Un �l�ment de plus.
			nbElements++;
		}

	}

	/**
	 * Supprime l'�l�ment � la position fournie
	 * 
	 * La position doit �tre entre 0 et getNbElements() - 1.
	 */
	public void supprimer(int position){

		decalerGauche(liste, position+1, --nbElements);
	}

	//LES AUTRES M�THODES
	/**
	 * Retourne l'�l�ment � la position voulue.
	 *
	 * Ant�c�dent : La liste ne doit pas �tre vide et la position valide.
	 *
	 * @return L'�l�ment � la position courante
	 */
	public Object getElement(int position){

		/*STRAT�GIE : Retourne simplement l'�l�ment � la
		 * position courante.
		 */
		return liste[position];
	}

	/**
	 * Retourne le nombre d'�l�ments actuellement dans la liste.
	 *
	 * Ant�c�dent : aucun.
	 * Cons�quent : aucun.
	 *
	 * @return Le nombre d'�l�ments de la liste.
	 */
	public int getNbElements(){

		return nbElements;
	}
}