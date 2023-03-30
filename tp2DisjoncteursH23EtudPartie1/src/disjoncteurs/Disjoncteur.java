package disjoncteurs;
import java.io.Serializable;

import liste.Liste;

/*
* Classe qui regroupe tout qui concerne un
* disjoncteur dans le projet.
*
* On y retrouve les constantes et les  sous-programmes
* li� � un disjoncteur.
* 
* Impl�mente l'interface Serializable pour la sauvegarde
* dans un fichier binaire.
* 
*  @author Pierre B�lisle
*  @Copyright H2023
*/
public class Disjoncteur implements Serializable{

	/**
	 * Enl�ve un "warning". On ne g�re pas les versions.
	 */
	private static final long serialVersionUID = 1L;
	

	
    // �tat possible d'un disjoncteur.
	public static final int ALLUME = 1;
	public static final int ETEINT = 0;
	
	// Choix d'amp�rages possibles.
	private static final int MIN_AMPERAGE = 15;
	private static final int MAX_AMPERAGE = 60;
	

	// Tous les amp�rages permis dans un tableau.  
	public static final int AMPERAGES_PERMIS[] =
		                         {MIN_AMPERAGE, 20, 40, 50, MAX_AMPERAGE};

	// Construction d'une cha�ne avec les amp�rages permis. Sert � valider.
	public static final  String CHAINE_AMPERAGE_PERMIS = 
			"15/20/40/50/60";
	
	// Les tensions possibles.
	public static final int TENSION_ENTREE = 240;
	public static final int TENSION_PHASE = 120;

	// Construction d'une cha�ne avec les tensions permises. Sert � valider.
	public static final  String CHAINE_TENSION_PERMISE = 
			"120/240";
	
	
	
	/******************************
	 * * Les attributs d'un disjoncteur
	 ********************************/
	
	private double ampere;
    private double tension;

	// Une liste qui contient les demandes (charge) sur le circuit.
	private Liste demandeDuCircuit;
	
	// ALLUME ou ETEINT.	
    private int etat;
    
    

	/**
	 * Constructeur qui re�oit la capacit� maximum de puissance et
	 * la tension du disjoncteur.
	 * 
	 * @param ampere
	 * @param tension
	 */
	public Disjoncteur(int ampere, int tension){

	    this.ampere  = ampere;
        this.demandeDuCircuit = new Liste();
	    this.etat = ETEINT;
	    this.tension = tension;

	}    


	/*
	 * @return La puissance maximum possible pour ce disjoncteur.
	 */
	double puissanceMax(){

	    return ampere * tension * .8;
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
		
		return getPuissanceEnAmpere() *getTension();
	}

	/**
	 * @throws ListeVideException 
	 * @returnLa demande du circuit en Watts.
	 */
	public double getPuissanceEnAmpere(){
		
		double demande = 0;
		
		// �vite pls appels � l'accesseur.
		int nb =  demandeDuCircuit.getNbElements();		
		
		for(int i = 0; i < nb; i++){
			
			demande += (double) demandeDuCircuit.getElement(i);
		}
		
		return demande;
	}


	/**
	 * Si la puissance max est atteonte, le disjoncteur pass � ETEINT.
	 * 
	 * @param demande Ajoute cette demande au disjoncteur.
	 */
	public void ajouterDemande(double demande) {
		
		int nb = this.demandeDuCircuit.getNbElements();
		
		// Ins�rer � la fin.
		this.demandeDuCircuit.inserer(demande, nb);				
		
        // S'il y a d�passement, on ETEINT.
        if(getPuissanceEnWatt() > puissanceMax()){
        	
            setEtat(Disjoncteur.ETEINT);
        }

	}

	/**
	 * @return L'�tat du disjoncteur ALLUME ou ETEINT.
	 */
	public int getEtat() {
		
		return etat;
	}

	/**
	 * @param etat Modifie l'�tat par l'�tat re�u ALLUME ou ETEINT.
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


	/**
	 * Retire la puissance sur le disjoncteur s'il existe.
	 * Si le disjoncteur est �TEINT et que la puissance max n'est plus 
	 * atteinte apr�s le retrait, le disjoncteur est ALLUME.
	 * 
	 * @param demande
	 */
	public void retirerPuissance(double demande) {

		boolean trouve = false;
		double puissance = 0;

		int i = 0;
		int nb = demandeDuCircuit.getNbElements();

		// La boucle arr�te si on a trouv� (fouille lin�aire).
		while(!trouve && i < nb){

			// Utilise le "wrapper" pour le transtypage.
			puissance = (double) demandeDuCircuit.getElement(i);

			trouve = puissance == demande;

			i++;
		}
		
		// On supprime, juste si on l'a trouv�.
		if(trouve){		
			
			demandeDuCircuit.supprimer(i);
		}
	}
}
