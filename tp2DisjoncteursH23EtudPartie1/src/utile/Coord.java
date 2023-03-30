package utile;
/**
 * Cet enregistrement repr�sente les coordonn�es 
 * possible dans diff�rents jeux de grille (ligne-colonne).
 * 
 * Les attributs sont utilisables � l'aide des fonctions ou directement (public).
 * (m�me principe que java.awt.Dimension).
 * 
 *@author Pierre B�lisle
 *@version H 2009
 *@revisite H 2023
 */
public class Coord {


	/*
	 * Les items choisis par l'utilisateur. 
	 */
	int ligne;
	int colonne;

	/*
	 * Constructeur par d�faut de (0,0).
	 */
	public Coord() {
		ligne = 0;
		colonne = 0;
	}


	/**
	 * @return La ligne
	 */
	public int getLigne() {
		
		return ligne;
	}

	/**
	 * @param ligne La ligne � modifier.
	 */
	public void setLigne(int ligne) {
		
		this.ligne = ligne;
	}

	/**
	 * @return La colonne.
	 */
	public int getColonne() {
		
		return colonne;
	}

	/**
	 * @param colonne La colonne � modifier.
	 */
	public void setColonne(int colonne) {
		
		this.colonne = colonne;
	}

	/**
	 * @return Une cha�ne contenant les infos de la coordonn�e.
	 */
	public String toString(){
		
		return "(" + colonne + "-" + ligne + ")";
	}
}
