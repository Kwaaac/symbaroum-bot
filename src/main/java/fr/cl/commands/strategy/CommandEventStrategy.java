package fr.cl.commands.strategy;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CommandEventStrategy {
    CommandEventStrategy defaultReponse = ev -> ev
            .reply("Oulah, y'a eu une bille, va crier fort dans un channel, peut-être que quelqu'un t'entendra")
            .withEphemeral(true);

    Mono<Void> apply(ChatInputInteractionEvent event);
}
