package graphiqueGui;
/**
 * Fenêtre de dialogue pour obtenir les données d'un appareil.
 * Sert essentiellement à sauver du temps pour la saisie.  Les valeurs
 * ne sont pas exhaustive mais suffiront.
 * 
 * Il suffit d'appelerFenetreSaisieAppareil.getAppareil() pour obtenir
 * les valeurs saisies ou null si l'utilisateur clique sur le x en haut à droite.
 * 
 * Code fourni dans le cadre du cours inf111 tp3 A16.
 * 
 * @author Pierre Bélisle 
 * @version Copyright  H2023
 */
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import disjoncteurs.Disjoncteur;
import hierarchieAppareils.AppareilAbstrait;
import hierarchieAppareils.AppareilInterrupteur;
import hierarchieAppareils.AppareilVariateur;

public class FenetreSaisieAppareil {
	
	/*
	 * Stratégie : On utilise JDialog parce que cette fenêtre peut être mise en
	 * mode modal.  Une procédure crée la fenêtre de dialogue et la met visible.
	 * Lorsque le bouton ok est cliqué, l'appareil est instancié avec les 
	 * valeurs et le dialog est disposé.
	 * 
	 * Lorsque la fenetre se ferme, l'appareil est retourné.
	 * 
	 * Certaines bonnes pratiques n'ont pas été utilisées compte tenu 
	 * du manque de temps.
	 */
	
	private static final double BASE = 10.0;
	
	// Appareil à retourner.
	private static AppareilAbstrait appareil;
	
	// La fenetre de dialogue qui sera modal.
	private static JDialog dialog;
	
	// Paneau de contenu de la fenêtre.
	private static JPanel panneau;
	
	 // Panneau du haut.
	private static JPanel panHaut = new JPanel();
	
	// Panneau en bas avec le bouton.
	private static JPanel panBas = new JPanel();
	
	// Les options possibles des comboBox
	// Les choix des différents attributs.
	private static String [] tabCategories ={"Réfrigérateur", 
			                                "Cuisinière", 
			                                "Grille-pain", 
			                                "Sèche-cheveux",
			                                "Sèche-ligne",
			                                "Presse-jus",
			                                "Chauffage",
			                                "Autres"};
	
	private static String [] tabTensions ={
			String.valueOf(Disjoncteur.TENSION_PHASE),
			String.valueOf(Disjoncteur.TENSION_ENTREE)};

	private static String [] tabEmplacements ={"Cuisine", 
                                                    "Salon",
                                                    "Chambre principale",
                                                    "Chambre 1",
                                                    "Chambre 2",
                                                    "Chambre 3",
                                                    "Salle familiale",
                                                    "Garage",
                                                    "Autres"};
	

	// Nécessaire d'être global pour l'utilisation dans un écouteur de 
	// classe interne. Évite le passage de paramètre au constructeur.
	private static JRadioButton btnVariateur;
	
	private static JSlider variateur;
	
	private static JSlider ampereSlider;
	
	/**
	 * Seule fonction public qui démarre une fenêtre de saisie et retourne un 
	 * appareil selon les données saisies ou null si l'utilisateur annule.
	 * 
	 * @return null ou un appareil créé par l'utilisateur.
	 */
	public static AppareilAbstrait getAppareil(){
		
		initDialog();
		
		dialog.setVisible(true);

		return appareil;	
	}

	
	/*
	 * Crée les instances des composants, les ajoute et met la fenêtre visible.
	 */
	private static void initDialog(){
		
		// Instanciation à chaque appel.
		initFenetre();
						
		// Tout ce qui concerne le type d'appareil
		ajouterTypeAppareil();
		
		// Les comboBox sont ajoutés au panneau dans la fonction qui le crée.
		JComboBox<String> categorie = 
				ajouterCombo("Catégorie", tabCategories);
		
		JComboBox<String> tension = 
				ajouterCombo("Tension", tabTensions);
		
		JComboBox<String> emplacement = 
				ajouterCombo("Emplacement", tabEmplacements);
						
		// Tout ce qui touche le nombre d'ampères.
	    ajouterAmpere();
				
		//Le bouton est laissé ici pour permettre à l'écouteur
		// d'utiliser directement les valeurs.
		JButton btn = new JButton("Ok");
		
		btn.setPreferredSize(new Dimension(100,30));
		
		// Un écouteur de bouton anonyme qui instancie l'appareil 
		// selon les données
		btn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
								
				// L'instanciation se fait selon la sorte d'appareil
				// sélectionnée.
				if(btnVariateur.isSelected()){

					appareil = 
					  new AppareilVariateur((String)categorie.getSelectedItem(), 
						   ampereSlider.getValue() / BASE, 
				   		   Integer.parseInt((String)tension.getSelectedItem()),
				    	   (String)emplacement.getSelectedItem(),
						   variateur.getValue() / BASE);
									
				}
				else{
					
					appareil = new AppareilInterrupteur(
							(String)categorie.getSelectedItem(), 
					  	    ampereSlider.getValue() / BASE, 
					        Integer.parseInt((String)tension.getSelectedItem()),
							(String)emplacement.getSelectedItem());
				}
				
				dialog.dispose();
				dialog  = null;
								
			}
		
		});
		
		panBas.add(btn);
		
	}

	/*
	 * Fonction locale qui monte un "slider" et la valeur actuellement 
	 * sélectionné.
	 */
	private static void ajouterAmpere(){
		
		JLabel etiqAmpere= new JLabel("Ampère ");
		
		ampereSlider = new JSlider(1, 1000);
		JPanel panSliderAmpere = getPanSlider(ampereSlider);
		
		JPanel panAmpere = new JPanel();
		panAmpere.add(etiqAmpere);
		panAmpere.add(panSliderAmpere);		
		
		panHaut.add(panAmpere);		
	}
	
	/**
	 * Initialise les attributs de la fenêtre de dialogue. 
	 */
	private static void initFenetre(){
		
		dialog =  new JDialog ();
		appareil = null;
		panHaut.removeAll();
		panBas.removeAll();
		
		dialog.setTitle("Ajout d'appareils");
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

		dialog.setSize(dim);
		
		dialog.setResizable(false);
		
		dialog.setModal(true);

		// On contrôle le layout de la fenêtre et attribut est utilisé 
		// globalement.
		panneau = (JPanel) dialog.getContentPane();	
		

		panneau.add(panHaut, BorderLayout.PAGE_START);
		panneau.add(panBas,  BorderLayout.PAGE_END);

	}
	
	/*
	 * Procédure locale pour la lisibilité du code.  Crée un panneau avec
	 * un slider et lui ajoute un écouteur pour voir sa valeur lors des 
	 * changements.
	 * 
	 * Le slider est en réel.
	 */
	private static JPanel getPanSlider(JSlider slider){
		
		JPanel panSlider = new JPanel();
		
		panSlider.add(slider);
		slider.setValue(1);
		slider.setOrientation(JSlider.VERTICAL);
		
		// La plus petit valeur au départ.
		JLabel etiqSlider = 
				new JLabel(String.valueOf(slider.getValue() / BASE));
		
		panSlider.add(etiqSlider);
			
		// Permet de montrer la valeur du slider en nombre réel.
		slider.addChangeListener(new ChangeListener(){
			 
			@Override
			public void stateChanged(ChangeEvent e) {
				
				etiqSlider.setText(String.valueOf(slider.getValue() / BASE)); 
				
			}
			
		});
		
		return panSlider;
	}
	
	/*
	 * Procédure locale pour créer les deux radios button et le silder.
	 * 
	 * Ajoute les écouteur pour mettre activer oiu désactiver le slider.
	 */
	private static void ajouterTypeAppareil(){
		
		// Tout ce qui touche la position du variateur.
		variateur = new JSlider(1, 1000);
		
		JPanel panSliderVariateur = getPanSlider(variateur);
		variateur.setMaximumSize(new Dimension(100,50));
		
		// Le type d'appareil et leur écouteur.
		JRadioButton btnInterrupteur = new JRadioButton("Interrupteur");
				
		btnInterrupteur.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {

				variateur.setEnabled(false);							
			}
			
		});
		
		btnVariateur = new JRadioButton("Variateur ");
		btnVariateur.setSelected(true);
		btnVariateur.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				variateur.setEnabled(true);						
			}
			
		});

		// Rend les boutons mutullement exclusifs.	
		ButtonGroup group = new 	ButtonGroup();
		group.add(btnInterrupteur);
		group.add(btnVariateur);
		
		// Un petit panneau 
		JPanel panBtnTypeAppareil = new JPanel();
		panBtnTypeAppareil.add(btnVariateur);
		panBtnTypeAppareil.add(panSliderVariateur);
		panBtnTypeAppareil.add(btnInterrupteur);
						
		panHaut.add(panBtnTypeAppareil);		

		
	}
	
	/**
	 * Procédure local pour créer un panneau avec un combobox et une étiquette.
	 * Sert essentiellement a la réutilisation.
	 * 
	 * @param etiqStr  Le texte de l'étiquette.
	 * @param tabOptions Les options du comboBox
	 * 
	 * @return Le comboBox créé.
	 */
	private static JComboBox<String>  ajouterCombo(String etiqStr, 
	                                                                                      String[] tabOptions){
		
		// On utilise des JCombox génériques, évite les "warnings.
		JLabel etiq = new JLabel(etiqStr);
		JComboBox<String> combo = 
				new JComboBox<String>(tabOptions);
		
		// Petit panneau pour disposer les composants en FlowLayout.
		JPanel pan = new JPanel();
		pan.add(etiq);
		pan.add(combo);
		
		// Le panneau du comboBox dans le panneau de la fenêtre.
		panHaut.add(pan);
		
		return combo;
	
	}
	

}
