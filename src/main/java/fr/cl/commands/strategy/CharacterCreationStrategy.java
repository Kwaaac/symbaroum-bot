package fr.cl.commands.strategy;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.command.ApplicationCommandInteractionOption;
import discord4j.core.object.command.ApplicationCommandInteractionOptionValue;
import fr.cl.characters.Character;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;

public class CharacterCreationStrategy implements CommandEventStrategy {

    private boolean onError;

    private static ApplicationCommandInteractionOptionValue getValue(ChatInputInteractionEvent event, String option) throws NoSuchElementException {
        return event.getOption(option).orElseThrow().getValue().orElseThrow();
    }

    public Optional<Character> createCharacter(ChatInputInteractionEvent event) {
        try {
            var builder = Character.CharacterBuilder.newBuilder();
            // options required
            var enduranceMax = Integer.parseInt(getValue(event, "endurance").getRaw());
            var douleur = Integer.parseInt(getValue(event, "douleur").getRaw());

            builder
                    .player(event.getInteraction().getMember().orElseThrow().getUsername())
                    .name(getValue(event, "nom").asString())
                    .race(getValue(event, "race").asString())
                    .corruption(Integer.parseInt(getValue(event, "corruption").getRaw()))
                    .endurance(enduranceMax, douleur);

            // options non required
            event.getOption("archetype").flatMap(ApplicationCommandInteractionOption::getValue).ifPresent(value -> builder.archetype(value.asString()));
            event.getOption("ombre").flatMap(ApplicationCommandInteractionOption::getValue).ifPresent(value -> builder.ombre(value.asString()));
            event.getOption("phrase").flatMap(ApplicationCommandInteractionOption::getValue).ifPresent(value -> builder.phraseType(value.asString()));
            event.getOption("carriere").flatMap(ApplicationCommandInteractionOption::getValue).ifPresent(value -> builder.carriere(value.asString()));

            // stats
            var array = event.getOptions().stream().filter(option -> option.getName().startsWith("cp-")).mapToInt(option -> Integer.parseInt(option.getValue().orElseThrow().getRaw())).toArray();

            builder.stat(array);

            return Optional.of(builder.build());
        } catch (NoSuchElementException | NumberFormatException | IllegalStateException e) {
            onError = true;
            return Optional.empty();
        }
    }

    @Override
    public Mono<Void> apply(ChatInputInteractionEvent event) {
        if (onError) {
            return event.reply("Votre personnage n'a pas pu être créé").withEphemeral(true);
        }
        return event.reply("Votre personnage à été créé").withEphemeral(true);
    }
}
