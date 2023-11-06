package fr.cl.commands.strategy;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@FunctionalInterface
public interface CommandEventStrategy {
    CommandEventStrategy defaultReponse = ev -> ev
            .reply("Oulah, y'a eu une bille, va crier fort dans un channel, peut-être que quelqu'un t'entendra")
            .withEphemeral(true);

    /**
     * Applique la lambda sur un evenement et renvoi un Consumer de void (Mono => 1 élement ou rien, ici c'est rien)
     * Généralement pour répondre à la commande
     *
     * @param event
     * @return Mono<Void> qui fait une action sur l'event
     * @throws NoSuchElementException Si une tentative de récuperer une option se solde par un échec
     */
    Mono<Void> apply(ChatInputInteractionEvent event) throws NoSuchElementException;
}
