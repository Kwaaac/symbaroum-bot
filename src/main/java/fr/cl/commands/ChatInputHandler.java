package fr.cl.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.cl.Dice;
import fr.cl.commands.strategy.CommandStrategy;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;

public class ChatInputHandler {

    public Mono<Void> handleEvent(ChatInputInteractionEvent event) {


        try {
            return switch (event.getCommandName()) {
                case "throw" -> throwDice(event);
                case "throwhidden" -> throwDiceHidden(event);
                default -> CommandStrategy.defaultReponse.apply(event);
            };
        } catch (NoSuchElementException e) {
            return CommandStrategy.defaultReponse.apply(event);
        }
    }

    private Mono<Void> greet(ChatInputInteractionEvent event) {
        var name = event.getOptions().get(0);
        return event.reply("Hey salut " + name.getValue().get().getRaw() + "!");
    }

    private Mono<Void> throwDice(ChatInputInteractionEvent event) throws NoSuchElementException {
        var dice = event.getOptions().get(0).getValue().orElseThrow().getRaw();
        var mod = 0L;

        var optModificateur = event.getOption("modificateur");

        if (optModificateur.isPresent()) {
            mod = optModificateur.get().getValue().orElseThrow().asLong();
        }

        Matcher matcher = Dice.pattern.matcher(dice);
        if (!matcher.find()) return event.reply("Le lancer ne respecte pas la convention").withEphemeral(true);
        var die = Dice.createFromMatcher(matcher);

        return event.reply("> " + die + "\n" + die.throwDice());
    }

    private Mono<Void> throwDiceHidden(ChatInputInteractionEvent event) throws NoSuchElementException {
        var dice = event.getOptions().get(0).getValue().orElseThrow().getRaw();
        var mod = 0L;

        var optModificateur = event.getOption("modificateur");

        if (optModificateur.isPresent()) {
            mod = optModificateur.get().getValue().orElseThrow().asLong();
        }

        Matcher matcher = Dice.pattern.matcher(dice);
        if (!matcher.find()) return event.reply("Le lancer ne respecte pas la convention").withEphemeral(true);
        var die = Dice.createFromMatcher(matcher);

        return event.reply("> " + die + "\n" + die.throwDice()).withEphemeral(true);
    }
}
