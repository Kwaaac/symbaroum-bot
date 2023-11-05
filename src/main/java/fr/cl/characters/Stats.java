package fr.cl.characters;

import java.util.HashMap;
import java.util.Objects;

/**
 * Représente les statistiques de compétences d'un {@link Character}
 */
public class Stats {

    private final HashMap<Skill, Integer> stats = new HashMap<>();

    /**
     * Récupère la valeur de la compétence donnée en paramètre
     *
     * @param skill compétence choisie
     * @return la valeur de la compétence
     */
    public int getStat(Skill skill) {
        var result = stats.get(skill);

        if (result == null) {
            throw new IllegalStateException();
        }

        return result;
    }

    /**
     * Fait appel à la méthode {@link #getStat(Skill)}, si la compétence donnée en paramètre
     * n'existe pas, une @{@link IllegalArgumentException} est levée
     *
     * @param skill compétence choisie
     * @return la valeur de la compétence
     * @throws IllegalArgumentException si la compétence n'existe pas
     */
    public int getStat(String skill) {
        Objects.requireNonNull(skill);
        return getStat(Skill.valueOf(skill));
    }

    /**
     * Met à jour la valeur de la compétence
     *
     * @param skill compétence à modifier
     * @param value nouvelle valeur
     */
    public void updateStat(Skill skill, int value) {
        stats.put(skill, value);
    }

    /**
     * Voir {@link #updateStat(Skill, int)}
     *
     * @throws IllegalArgumentException Si la compétence n'existe pas
     */
    public void updateStat(String skill, int value) {
        stats.put(Skill.valueOf(skill), value);
    }

    /**
     * Enumaration des compétences de Symbaroum
     */
    public enum Skill {
        AGILITE, ASTUCE, DISCRETION, FORCE, PERSUASION, PRECISION, VIGILANCE, VOLONTE
    }
}
