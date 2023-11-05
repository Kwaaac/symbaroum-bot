package fr.cl.characters;

import java.io.Serializable;

/**
 * Classe qui représente la corruption d'un personnage.
 * Lorsque la corruption courante atteint 8, un point de corruption permanente est ajouté.
 */
public class Corruption implements Serializable {

    private int current;
    private int permanent;


    /**
     * Incrémente la corruption courante, s'il elle atteint 8, la corruption permanente augmente de 1
     * et renvoie <code>true</code>.
     *
     * @return <code>true</code> si la corruption permanente augmente, <code>false</code> sinon
     */
    public boolean increment() {
        current++;
        if (current == 8) {
            current = 0;
            permanent++;
            return true;
        }

        return false;
    }

    /**
     * Décrémente la corruption courante de 1
     */
    public void decrement() {
        current = Math.max(0, current - 1);
    }

    /**
     * Réinitialise la corruption courante à 0
     */
    public void reset() {
        current = 0;
    }

    /**
     * Décrémente la corruption permanente de 1
     */
    public void decrementPermanent() {
        current = 0;
        permanent = Math.max(0, permanent - 1);
    }

    /**
     * Incrémente la corruption permanente de 1
     */
    public void incrementPermanent() {
        current = 0;
        permanent++;
    }

}
