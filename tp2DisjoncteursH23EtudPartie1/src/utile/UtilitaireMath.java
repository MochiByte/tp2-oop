package utile;
/**
 * Classe qui offre des fonctions mathématiques utilitaires qui n'existent pas 
 * intégralement en Java.
 * 
 * @Author Pierre Bélisle
 * @version H2023
 */
public class UtilitaireMath {

    /**
     * Retourne un nombre réel aléatoire dans un intervalle réel donné.
     * 
     * @param min La plus petite valeur possible
     * @param max La plus grande valeur possible
     * @return Un nombre réel entre min et max (inclusivement)
     */
    public static double reelAlea(double min, double max) {
    	
        /*
         * Stratégie, on utilise le générateur de Java qui retourne une valeur 
         * réelle entre 0 et 1  ensuite, on le ramène dans l'intervalle min..max
         * 
         * La division par / 1.000001 est pour s'assurer d'une répartition 
         * plus uniforme des valeurs. 
    	 */
        return Math.random() / 1.000001 * (max - min + 1) + min;
    }

    /**
     * Retourne un nombre entier aléatoire dans un intervalle entier donné.
     * 
     * @param min La plus petite valeur possible
     * @param max La plus grande valeur possible
     * @return Un nombre entier entre min et max (inclusivement)
     */
    public static int entierAlea(int min, int max) {
    	
        /*
         * Stratégie, on utilise le générateur de Java qui retourne une valeur 
         * réelle entre 0 et 1  ensuite, on le ramène dans l'intervalle min..max 
         * et on le transtype en entier.
         * 
         * La division par / 1.000001 est pour s'assurer d'une répartition 
         * plus uniforme des valeurs. 
         * 
    	 */
        return (int) (Math.random() / 1.000001 * (max - min + 1) + min);
    }



}
