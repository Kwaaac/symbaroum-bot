package fr.cl.characters;

import discord4j.core.spec.EmbedCreateFields;
import discord4j.core.spec.EmbedCreateSpec;
import discord4j.rest.util.Color;

import java.util.ArrayList;
import java.util.List;
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

    private Character(String player, String name, String race, Stat stat, Corruption corruption, Endurance endurance) {
        Objects.requireNonNull(player);
        Objects.requireNonNull(name);
        Objects.requireNonNull(race);
        Objects.requireNonNull(stat);
        Objects.requireNonNull(corruption);
        Objects.requireNonNull(endurance);
        this.corruption = corruption;
        this.endurance = endurance;
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

        return "# Personnage de " + player + "\n\n" +
                "## Données" +
                infoPersonnage() +
                "\n## Statistiques de base\n" +
                "\n" + endurance +
                "\n## Corruption\n" +
                "\n" + corruption +
                "\n## Statistiques générales\n" +
                "\n" + stat;
    }

    private String infoPersonnage() {
        return "\n- Nom: " + name +
                "\n- Race: " + race +
                "\n- Archetype: " + (archetype == null ? "" : archetype) +
                "\n- Carriere: " + (carriere == null ? "" : carriere) +
                "\n- Ombre: " + (ombre == null ? "" : ombre) +
                "\n- Phrase type: " + (phraseType == null ? "" : phraseType);
    }

    private EmbedCreateFields.Field[] infoFieldPersonnage() {
        List<EmbedCreateFields.Field> lst = new ArrayList<>();

        lst.add(EmbedCreateFields.Field.of("Nom", name, true));
        lst.add(EmbedCreateFields.Field.of("Race", race, true));
        if (archetype != null)
            lst.add(EmbedCreateFields.Field.of("Archetype", archetype, true));
        if (carriere != null)
            lst.add(EmbedCreateFields.Field.of("Carrière", carriere, true));
        if (phraseType != null)
            lst.add(EmbedCreateFields.Field.of("Phrase Type", phraseType, true));
        if (ombre != null)
            lst.add(EmbedCreateFields.Field.of("Ombre", ombre, true));

        return lst.toArray(EmbedCreateFields.Field[]::new);
    }

    public EmbedCreateSpec embed(String imageURL) {
        EmbedCreateSpec embed = EmbedCreateSpec.builder()
                .author("Fiche de personnage de " + player, null, null)
                .thumbnail(imageURL)
                .color(Color.RED)
                .title(name)
                .description("\u200B")
                .addField("# Info du personnage", "\u200B", false)
                .addFields(infoFieldPersonnage())
                .addField("\u200B", "\u200B", false)
                .addField("Statistiques générales", "\u200B", false)
                .addField("Endurance", endurance.toString(), true)
                .addField("Corruption", corruption.toString(), true)
                .addField("\u200B", "\u200B", false)
                .addField("Statistiques des compétences", stat.toString(), false)
                .footer(phraseType + "- " + name, null)
                .build();

        return embed;
    }

    public static class CharacterBuilder {
        private String player;
        private String name;
        private String race;
        private Stat stat;
        private Corruption corruption;
        private Endurance endurance;

        private String archetype;
        private String carriere;
        private String ombre;
        private String phraseType;

        private CharacterBuilder() {
            // nothing
        }

        public static CharacterBuilder newBuilder() {
            return new CharacterBuilder();
        }

        public CharacterBuilder player(String player) {
            Objects.requireNonNull(player);
            this.player = player;
            return this;
        }

        public CharacterBuilder name(String name) {
            Objects.requireNonNull(name);
            this.name = name;
            return this;
        }

        public CharacterBuilder race(String race) {
            Objects.requireNonNull(race);
            this.race = race;
            return this;
        }

        /**
         * Construit un personnage avec un tableau de statistiques, dans l'ordre de la fiche de personnage:
         * AGILITE, ASTUCE, DISCRETION, FORCE, PERSUASION, PRECISION, VIGILANCE, VOLONTE
         *
         * @param stats tableau des valeurs des compétences
         * @return le builder
         */
        public CharacterBuilder stat(int... stats) {
            Objects.requireNonNull(stats);
            var skills = Stat.Skill.values();

            if (stats.length != skills.length) {
                throw new IllegalArgumentException("Le nombre de compétences données est érroné. Attendu: " + skills.length + ", Obtenu: " + stats.length);
            }
            var stat = new Stat();

            for (int i = 0; i < skills.length; i++) {
                var skill = skills[i];
                var value = stats[i];

                stat.updateStat(skill, value);
            }

            this.stat = stat;

            return this;
        }

        public CharacterBuilder stat(Stat stat) {
            Objects.requireNonNull(stat);
            this.stat = stat;
            return this;
        }

        public CharacterBuilder corruption(int corruption) {
            this.corruption = new Corruption(corruption);
            return this;
        }

        public CharacterBuilder endurance(int endurance, int douleur) {
            this.endurance = new Endurance(endurance, douleur);
            return this;
        }

        public CharacterBuilder archetype(String archetype) {
            Objects.requireNonNull(archetype);
            this.archetype = archetype;
            return this;
        }

        public CharacterBuilder carriere(String carriere) {
            Objects.requireNonNull(carriere);
            this.carriere = carriere;
            return this;
        }

        public CharacterBuilder ombre(String ombre) {
            Objects.requireNonNull(ombre);
            this.ombre = ombre;
            return this;
        }

        public CharacterBuilder phraseType(String phraseType) {
            Objects.requireNonNull(phraseType);
            this.phraseType = phraseType;
            return this;
        }

        public Character build() throws IllegalStateException {
            if (player == null || name == null || race == null || stat == null || corruption == null || endurance == null) {
                throw new IllegalStateException("Le personnage n'est pas complet");
            }

            var character = new Character(player, name, race, stat, corruption, endurance);

            if (archetype != null) {
                character.setArchetype(archetype);
            }
            if (ombre != null) {
                character.setOmbre(ombre);
            }
            if (carriere != null) {
                character.setCarriere(carriere);
            }
            if (phraseType != null) {
                character.setPhraseType(phraseType);
            }

            return character;
        }


    }
}
