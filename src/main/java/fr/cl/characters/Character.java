package fr.cl.characters;

import java.util.Objects;

/**
 * Représente un personnage de Symbaroum
 */
public class Character {

    /**
     * id du joueur sur Discord
     */
    private final String player;

    private final String name;

    private final String race;
    private final Stat stat;

    private final Corruption corruption;

    private final Endurance endurance;
    private String archetype;
    private String carriere;
    private String ombre;
    private String phraseType;

    public Character(String player, String name, String race, Stat stat, int corruption, int endurance) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(name);
        Objects.requireNonNull(race);
        Objects.requireNonNull(stat);
        this.corruption = new Corruption(corruption);
        this.endurance = new Endurance(endurance);
        this.player = player;
        this.name = name;
        this.race = race;
        this.stat = stat;
    }

    public void setArchetype(String archetype) {
        this.archetype = archetype;
    }

    public void setCarriere(String carriere) {
        this.carriere = carriere;
    }

    public void setOmbre(String ombre) {
        this.ombre = ombre;
    }

    public void setPhraseType(String phraseType) {
        this.phraseType = phraseType;
    }

    @Override
    public String toString() {
        return "# Personnage de " + player + "\n" +
                "## Données" +
                "\n- name: " + name +
                "\n- race: " + race +
                "\n- archetype: " + archetype +
                "\n- carriere: " + carriere +
                "\n- ombre: " + ombre +
                "\n- phrase type='" + phraseType + '\'' +
                "\n" + endurance +
                "\n" + corruption +
                "\n" + stat;
    }
}
