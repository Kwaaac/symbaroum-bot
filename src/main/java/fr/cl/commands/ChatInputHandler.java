package fr.cl.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.cl.commands.strategy.CommandStrategy;
import fr.cl.commands.strategy.ThrowDiceStategy;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

public class ChatInputHandler {

    public Mono<Void> handleEvent(ChatInputInteractionEvent event) {

        try {
            CommandStrategy cmd = switch (event.getCommandName()) {
                case "throw" -> new ThrowDiceStategy(false);
                case "throwhidden" -> new ThrowDiceStategy(true);
                default -> CommandStrategy.defaultReponse;
            };

            return cmd.apply(event);
        } catch (NoSuchElementException e) {
            return CommandStrategy.defaultReponse.apply(event);
        }

    }

    private Mono<Void> greet(ChatInputInteractionEvent event) {
        var name = event.getOptions().get(0);
        return event.reply("Hey salut " + name.getValue().get().getRaw() + "!");
    }
}
