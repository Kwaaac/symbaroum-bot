package fr.cl.commands.strategy;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

public class CharacterCreationStrategy implements CommandEventStrategy {
    @Override
    public Mono<Void> apply(ChatInputInteractionEvent event) {

        // options required
        var nom = event.getOption("nom").orElseThrow().;

        // options non required

        // stats


        return null;
    }
}
