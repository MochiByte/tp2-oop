package utile;
/**
 * Cet enregistrement représente les coordonnées 
 * possible dans différents jeux de grille (ligne-colonne).
 * 
 * Les attributs sont utilisables à l'aide des fonctions ou directement (public).
 * (même principe que java.awt.Dimension).
 * 
 *@author Pierre Bélisle
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
	 * Constructeur par défaut de (0,0).
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
	 * @param ligne La ligne à modifier.
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
	 * @param colonne La colonne à modifier.
	 */
	public void setColonne(int colonne) {
		
		this.colonne = colonne;
	}

	/**
	 * @return Une chaîne contenant les infos de la coordonnée.
	 */
	public String toString(){
		
		return "(" + colonne + "-" + ligne + ")";
	}
}
