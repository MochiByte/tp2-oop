package disjoncteurs;
import java.io.Serializable;

import utile.Coord;
import utile.UtilitaireMath;

/**
 * Module qui permet la gestion d'une boîte électrique
 * avec disjoncteurs.
 *
 * La boite doit d'abord être initialisée au nombre d'ampères voulus 
 * ainsi que son nombre de disjoncteurs maximum possibles.
 *
 * Implémente l'interface Serializable pour la sauvegarde
 * dans un fichier binaire. 
 * 
 * #author Pierre Bélisle
 * @version Copyright H2023
 */
public class Boite implements Serializable{
	
	/**
	 * Enlève un "warning". On ne gère pas les versions.
	 */
	private static final long serialVersionUID = 1L;
	
	/*********************************
	 *  LES CONSTANTES DE LA BOITE
	 *********************************/
	// La modification a un effet direct sur l'affichage.
	public static final int MAX_DISJONCTEURS  = 60;
	public static final int NB_COLONNES  = 2;
	
	public static final int NB_LIGNES_MAX  = 
			MAX_DISJONCTEURS/NB_COLONNES;
	
	// Pour le remplissage de départ.
    public static final double POURC_REMPLI = 0.6;
	public static final double POURC_TENSION_ENTREE = .3;
	
	public static final int AMPERAGE_MIN= 100;
	public static final int AMPERAGE_MAX = 400;
	
	/*********************************
	 *  LES ATTRIBUTS DE LA BOITE
	 *********************************/
	private int maxAmperes;
	
	// Le tableau est 2D mais il est à l'envers de la réalité (ligne-colonne).
	// Toutes les méthodes qui nécessitent la position, reçoivent (colonne-ligne).  
	private Disjoncteur[][] tabDisjoncteurs;	
	private int nbDisjoncteurs;
	
	// On déduit les disjoncteurs TENSION_ENTREE par
	// nbDisjoncteurs - nbDisjoncteursPhase  
	private int nbDisjoncteursPhase;
	
	

	/**
	 * Constructeur par défaut qui crée une boîte vide de max_ampères ampères. 
	 * @param max_amperes
	 */
	public Boite(int max_amperes){
		
		// Retient le max ampères et initialise le tableau de disjoncteurs.
	  this.maxAmperes = max_amperes;
	  initTabDisjoncteurs();
		    
	}
	
	/**
	 * Fonction locale qui crée et  initialise le tableau de disjoncteurs.
	 * (Bonne pratique).
	 */
	private void initTabDisjoncteurs(){

		tabDisjoncteurs = 
				new  Disjoncteur[NB_LIGNES_MAX][NB_COLONNES];

		// Tous les emplacement des disjoncteurs sont vides.
		for(int i = 0; i < NB_LIGNES_MAX; i++){
			
			for (int j = 0; j < NB_COLONNES; j++){
				
				tabDisjoncteurs[i][j] = null;
			}
		}
		
		nbDisjoncteurs = 0;
		nbDisjoncteursPhase = 0;
		

	}
	// La tension doit être TENSION ENTREE ou TENSION_PHASE.
	// L'ampérage entre MIN_AMPERAGE et MAX_AMPERAGE.
	// Aucune validation n'est effectuée ici.
	public boolean ajouterDisjoncteur(int colonne, int ligne,
	                          	      Disjoncteur disjoncteur){

	    boolean valide = tabDisjoncteurs[ligne][colonne] == null;

	    if(valide){

	        tabDisjoncteurs[ligne][colonne] = disjoncteur;
	        
	        tabDisjoncteurs[ligne][colonne]
	        		.setEtat(	Disjoncteur.ALLUME);
	        
		    nbDisjoncteurs++;
		    
		    if(disjoncteur.getTension() == Disjoncteur.TENSION_PHASE){
		    	
		    	nbDisjoncteursPhase++;		    
		    }
	    }
	   
	    return valide;
	    
	}
	
	
	/**
	 * @return Le maximum d'amperage de la boîte.
	 */
	public int getMaxAmperes() {
		
		return maxAmperes;
	}

	
	/**
	 * @param max_amperes Le maximum d'ampère à modifier.
	 */
	public void setMaxAmperes(int max_amperes) {
		
		this.maxAmperes = max_amperes;
	}
	
	/**
	 * @return Le nombre de disjoncteurs.
	 */
	public int getNbDisjoncteurs() {
		
		return nbDisjoncteurs;
	}


	/**
	 * @return Le nombre de disjoncteurs TENSION_PHASE.
	 */
	public int getNbDisjoncteursPhase() {
		
		return nbDisjoncteursPhase;
	}

	/**
	 * @return Le nombre de disjoncteurs TENSION_ENTREE.
	 */
	public int getNbDisjoncteursEntree() {
		
		return nbDisjoncteurs - nbDisjoncteursPhase;
	}



	/**
	 * Accesseur d'un disjonctuur dans la boîte.
	 * 
	 * Aucune validation de (colonne-ligne) n'est faite ici.  Ils doivent être
	 * valides.
	 * 
	 * @param colonne
	 * @param ligne
	 * @return Le disjoncteur à la position colonne-ligne.
	 */
	public Disjoncteur getDisjoncteur(int colonne, int ligne){
		
		// Rappel : La boîte est à l'envers du tableau (colonne-ligne). 
		return tabDisjoncteurs[ligne][colonne];
	}
	
	/*
	* Remplit la boite avec des disjoncteurs sélectionnés au hasard
	* à partir des constantes POURC_REMPLI et 
	* POURC_TENSION_ENTREE.
	*/
	public void  remplirAlea(){

	   // En partant, le POURC_REMPLI de la boîte est en 
	   // TENSION_PHASE et le reste en TENSION_ENTREE.  
		// Tous sont allumés.
	   int colonne;
	   int ligne;

	   // Compte le nombre de disjoncteurs installé.
	   int compteur = 0;

	   // Sert à obtenir les différents ampérages générés.
	   int ampere;
	   
	   // Sert à récupérer ceux à installer.
	   Disjoncteur  disjoncteur;

	   // Remplit selon la valeur du POURC_REMPLI du nombre total de disjoncteurs.
	   while(compteur < (int) MAX_DISJONCTEURS * POURC_REMPLI){

	          // Un ampérage au hasard parmis les AMPERAGES_PERMIS.
	          ampere = 
	        		  UtilitaireMath.entierAlea(0, 
	        				   Disjoncteur.AMPERAGES_PERMIS.length-1);
	          
	          
	          colonne = UtilitaireMath.entierAlea(0,  NB_COLONNES-1);
	          ligne = UtilitaireMath.entierAlea (0,  NB_LIGNES_MAX-1);

	          // Le pourcentage en volt TENSION_ENTREE
	          if(UtilitaireMath.entierAlea(0, 1) < POURC_TENSION_ENTREE){

	              // Obtenir un disjoncteur de la bonne tension.
	              disjoncteur = 
	                  new Disjoncteur(Disjoncteur.AMPERAGES_PERMIS[ampere],
	                		  Disjoncteur.TENSION_ENTREE);

	          }
	          // Le reste en TENSION_PHASE
	          else{

	              disjoncteur = 
	                    new Disjoncteur(Disjoncteur.AMPERAGES_PERMIS[ampere], 
	                    		Disjoncteur.TENSION_PHASE);
	          }

	          ajouterDisjoncteur(colonne, ligne, disjoncteur);

	          compteur++;
	         
	    }
	}



	/**
	 * Ajoute la demande au circuit du disjoncteur de la boite.
	 * Si la puissance totale est trop élevée, le disjoncteur est éteint. 
	 * Si la puissance est négative et que la demande ne dépasse pas la 
	 * capacité totale le disjoncteur est allumé.
	 *  
	 * @param colonne
	 * @param ligne
	 * @param demande
	 */
	public void ajouterDemande(int colonne,
	                                                   int ligne,
	                                                   double demande){
	   
		// On ajoute rien sur un disjoncteur vide.
	    if(tabDisjoncteurs[ligne][colonne] != null){

	        tabDisjoncteurs[ligne][colonne]
	        	 .ajouterDemande(demande);	    
	    }
    }
	
	/**
	 * Retire une demande au disjoncteur.  Si la demande n'existe pas,
	 * rien n'esrt retiré.
	 *  
	 * @param colonne La demande à retirer.
	 * @param i 
	 * @param demande 
	 */	
	public void retirerPuissance(int colonne, int ligne, double demande){
		
		if(tabDisjoncteurs[ligne][colonne]!=null){
			tabDisjoncteurs[ligne][colonne].retirerPuissance(Math.abs(demande));
			
	        // S'il y a dépassement, on ETEINT.
	        if(tabDisjoncteurs[ligne][colonne].getPuissanceEnWatt() <= 
	        		tabDisjoncteurs[ligne][colonne].puissanceMax()){
	        	
	        	tabDisjoncteurs[ligne][colonne].setEtat(Disjoncteur.ALLUME);
	        }


		}
		
	}

	/**
	 * Cherche et retourne le premier emplacement vide dans l'une ou l'autre
	 * des colonnes.  S'i n'y en a pas, l'attribut colonne est PLEINE.
	 * 
	 * @return La coordonnée d'un emplacement disponible.
	 */
	public Coord getEmplacementDisponible(){
	    
		Coord c = new Coord();

	    /*
	     * Stratégie : On déplace le champ ligne tant qu'il n'y a pas de place.
	     * S'il devient égal à NB_LIGNES_MAX, on déplace la colonne.
	     */
	    // Au départ la colonne est 0.
	    do{
	    	
	    	c.setLigne(0);
	    	
	    	// On cherche dans une colonne
	    	while(c.getLigne() < tabDisjoncteurs.length && 
	    			tabDisjoncteurs[c.getLigne()][c.getColonne()]!= null){

	    		c.setLigne(c.getLigne() + 1);	    	
	    	}
	    	
	    	// si on en a pas trouvé, on change de colonne
	    	if(c.getLigne() == NB_LIGNES_MAX){	
	    		
	    		c.setColonne(c.getColonne() + 1);
	    	}
	    	
	    	
	    }while (c.getColonne() < NB_COLONNES && 
	    		     c.getLigne() == NB_LIGNES_MAX);
	    
	   
	    return c;
	}

	/**
	 * @return S'il reste encore des emplacement disponible.
	 */
	public boolean emplacementEncoreDisponible(){		
		
		// Stratégie : On utilise la taille de la grille.
		return nbDisjoncteurs <
					tabDisjoncteurs[0].length * tabDisjoncteurs.length;
		
	
	}
	/**
	 * @return Si le disjoncteurs à la position colonne-ligne dans la boîte 
	 * est null.
	 */
	public boolean emplacementEstVide(int colonne, int ligne){
	    
	    return tabDisjoncteurs[ligne][colonne] == null;
	}

	/**
	 * @return La consommation totale en Watts de la boîte.
	 */
	public double getConsommationTotalEnWatt(){

	    double  total = 0;
	    
	    for(int i = 0; i < NB_LIGNES_MAX; i++){

	        for(int j = 0; j < NB_COLONNES; j++){

	            if(tabDisjoncteurs[i][j] != null){

	            	// Calcul en Watts.
	                total+=tabDisjoncteurs[i][j].getPuissanceEnWatt();
	            }
	        }
	    }

	    return total;

	}

	/**
	 * @return la puissance totale consommée sur les disjoncteurs. 
	 */
	public double puissance_total_boite(){
		
	    double total = 0;

	    for(int i = 0; i < NB_LIGNES_MAX; i++){

	        for(int j = 0; j < NB_COLONNES; j++){

	            if(tabDisjoncteurs[i][j] != null){

	                total+= tabDisjoncteurs[i][j].puissanceMax();
	            }
	        }
	    }

	    return total;

	}

	/*
	 * 
	 * @return  Le temps de support de la charge.
	 */
	public double temps_ups(){

	    return maxAmperes /
	                (puissance_total_boite()/Disjoncteur.TENSION_ENTREE);
	}
}
