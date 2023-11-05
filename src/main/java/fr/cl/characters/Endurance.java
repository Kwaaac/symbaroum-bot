package fr.cl.characters;

/**
 * Classe qui représente l'endurance d'un personnage, plus vulgairement ses points de vie.
 */
public class Endurance {

    /**
     * Maximum de point de vie
     */
    private int maximum;

    /**
     * Points de vie courant
     */
    private int current;

    /**
     * Seuil de douleur
     */
    private int douleur;

    public Endurance(int maximum) {
        if (maximum < 1) {
            throw new IllegalArgumentException("La vie ne peut pas être à 0 ou moins");
        }

        this.maximum = maximum;
        this.current = maximum;
    }

    /**
     * Méthode qui applique des dommages à la vie.
     * Renvoie <code>true</code> si les points de vie atteignent 0 <code>false</code> sinon
     *
     * @param damage nombre de dommage à infliger aux points de vies
     * @return <code>true</code> si les points de vie atteignent 0 <code>false</code> sinon
     */
    public boolean damage(int damage) {
        current = Math.max(0, current - damage);

        return current == 0;
    }

    /**
     * Soigne les points de vie à hauteur de heal
     *
     * @param heal nombre de points de vie à restaurer
     */
    public void heal(int heal) {
        var healed = current + heal;
        current = Math.min(healed, maximum);
    }

    /**
     * Met à jour les points de vie maximum
     *
     * @param maximum points de vie
     */
    public void updateEndurance(int maximum) {
        if (maximum < 1) {
            throw new IllegalArgumentException("La vie ne peut pas être à 0 ou moins");
        }
        this.maximum = maximum;
    }

    /**
     * Renvoi si les dégats sont plus important que le seuil de douleur ou non
     *
     * @param damage dégats infligés
     * @return <code>true</code> si les dommages sont plus important que le seuil de douleur,
     * <code>false</code> si égal ou inférieur
     */
    public boolean douleur(int damage) {
        return douleur > damage;
    }


}
