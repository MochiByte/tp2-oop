package disjoncteurs;


import java.io.Serializable;

import hierarchieAppareils.AppareilAbstrait;
import liste.Liste;

/**
* Classe qui regroupe tout qui concerne un
* disjoncteur dans le projet.
*
* On y retrouve les constantes et les  sous-programmes
* lié à un disjoncteur.
* 
* Implémente l'interface Serializable pour la sauvegarde
* dans un fichier binaire. 
*
* @author Pierre Bélisle
* @version Copyright H2023
*/
public class Disjoncteur implements Serializable{

	/**
	 * Enlève un "warning". On ne gère pas les versions.
	 */
	private static final long serialVersionUID = 1L;
	

	
    // État possible d'un disjoncteur.
	public static final int ALLUME = 1;
	public static final int ETEINT = 0;
	
	// Choix d'ampérages possibles.
	private static final int MIN_AMPERAGE = 15;
	private static final int MAX_AMPERAGE = 60;
	

	// Tous les ampérages permis dans un tableau.  
	public static final int AMPERAGES_PERMIS[] =
		                         {MIN_AMPERAGE, 20, 40, 50, MAX_AMPERAGE};

	// Construction d'une chaîne avec les ampérages permis. Sert à valider.
	public static final  String CHAINE_AMPERAGE_PERMIS = 
			"15/20/40/50/60";
	
	// Les tensions possibles.
	public static final int TENSION_ENTREE = 240;
	public static final int TENSION_PHASE = 120;

	// Construction d'une chaîne avec les tensions permises. Sert à valider.
	public static final  String CHAINE_TENSION_PERMISE = 
			"120/240";
	
	// Total de la boîte.
	public static final double MAX = .8;
	
	/******************************
	 * * Les attributs d'un disjoncteur
	 ********************************/
	
	private double ampere;
    private double tension;

	// Une liste d'appareils sur le circuit.
	private Liste listeAppareils;
	
	// ALLUME ou ETEINT.	
    private int etat;
    
    
    /*
     * Stratégie : On utilise la classe Listepour conserver les appareil et
     * on maintient à jour l'attribut demandeDuCircuit à chaque ajout et 
     * retrait d'appareil.
     * 
     */

	/**
	 * Constructeur qui reçoit la capacité maximum de puissance et
	 * la tension du disjoncteur.
	 * 
	 * @param ampere
	 * @param tension
	 */
	public Disjoncteur(int ampere, int tension){

	    this.ampere  = ampere;
        this.listeAppareils = new Liste();
	    this.etat = ETEINT;
	    this.tension = tension;

	}    


	/**
	 * Ajoute un appareil sur le circuit d'un disjoncteur.
	 * 
	 * Aucune validation, l'appareil est ajouté début de liste.
	 * 
	 * @param appareil
	 */
	public void ajouterAppareil(AppareilAbstrait appareil){
		

		listeAppareils.inserer(appareil, 0);

		// S'il y a dépassement, on ETEINT.
		if(getPuissanceEnWatt() > puissanceMax()){

			setEtat(Disjoncteur.ETEINT);
		}	
	}

	/**
	 * Retire le ième appareil de la liste.
	 * 
	 * Rien n'est retiré si ieme n'existe pas dans la liste.
	 * 
	 * @param ieme La position de l'appareil à retirer. 0 == premier.
	 */
	public void retirerAppareil(int ieme){
		
		int nb = listeAppareils.getNbElements();
		
		// Si la liste n'est pas vide et que ieme est valide
		if(!listeAppareils.estVide() && ieme > -1 && ieme < nb){
			
				listeAppareils.supprimer(ieme);
		}
		
	}
	
	/**
	 * Retourne tous les appareils contenu dans le disjoncteur ou null.
	 * 
	 * @return null ou un tableau avec les appareils du disjoncteur.
	 */
	public AppareilAbstrait[] getTabAppareils(){
		
		// Tabelau à retourner
		AppareilAbstrait[] tabAppareils = null;

		int nb = listeAppareils.getNbElements();

		if(nb != 0){

			tabAppareils = new AppareilAbstrait[nb];

			for(int i = 0; i < nb; i++){
				tabAppareils[i] = 
						(AppareilAbstrait)listeAppareils.getElement(i);
			}
		}
		
		return tabAppareils;
	}

	/*
	 * @return La puissance maximum possible pour ce disjoncteur.
	 */
	double puissanceMax(){

	    return ampere * tension * MAX;
	}


	/**
	 * @return l'amperage du disjoncteur.
	 */
	public double getAmpere() {
		
		return ampere;
	}


	/**
	 * @param ampere Le nouvel amperage.
	 */
	public void setAmpere(double ampere) {
		
		this.ampere = ampere;
	}
	

	/**
	 * @return La puissance en Watts.
	 */
	public double getPuissanceEnWatt(){		
		
		/*
		 * On doit parcourir tous les appareils pour obtenir la 
		 * puissance réelle selon l'état ou le variateur.
		 */
		double demandeDuCircuit = 0;
		
		int nb = listeAppareils.getNbElements();
		
		// Évite l'exception.
		for(int i =0; i < nb; i++){

			AppareilAbstrait appareil =
					(AppareilAbstrait) listeAppareils.getElement(i);

			demandeDuCircuit += appareil.getPuissance();
		}


		return demandeDuCircuit;
	}

	
	/**
	 * @return L'état du disjoncteur ALLUME ou ETEINT.
	 */
	public int getEtat() {
		
		return etat;
	}

	/**
	 * @param etat Modifie l'état par l'état reçu ALLUME ou ETEINT.
	 */
	public void setEtat(int etat) {
		
		this.etat = etat;
	}

	/**
	 * @return La tension du disjoncteur.
	 */
	public double getTension() {
		
		return tension;
	}
	
}