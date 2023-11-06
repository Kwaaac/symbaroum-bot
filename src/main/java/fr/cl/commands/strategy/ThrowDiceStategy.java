package fr.cl.commands.strategy;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.cl.Dice;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;

public class ThrowDiceStategy implements CommandEventStrategy {

    private final boolean ephemeral;

    public ThrowDiceStategy(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    @Override
    public Mono<Void> apply(ChatInputInteractionEvent event) throws NoSuchElementException {
        // Une seule option dans ce cas
        var dice = event.getOptions().getFirst().getValue().orElseThrow().asString();
        var mod = 0;

        var optModificateur = event.getOption("modificateur");

        if (optModificateur.isPresent()) {
            mod = Integer.parseInt(optModificateur.get().getValue().orElseThrow().getRaw()) * -1;
        }

        Matcher matcher = Dice.pattern.matcher(dice);
        if (!matcher.find()) return event.reply("Le lancer ne respecte pas la convention").withEphemeral(true);
        var die = Dice.createFromMatcher(matcher);

        return event.reply("> " + die + " avec un modificateur de +" + mod + "\n" + die.throwDice(mod)).withEphemeral(ephemeral);
    }
}
