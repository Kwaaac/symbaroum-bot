package fr.cl.commands;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import fr.cl.characters.Character;
import fr.cl.commands.strategy.CharacterCreationStrategy;
import fr.cl.commands.strategy.CommandEventStrategy;
import fr.cl.commands.strategy.ThrowDiceStategy;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.NoSuchElementException;

public class ChatInputHandler {

    private final HashMap<String, Character> characters = new HashMap<>();


    public Mono<Void> handleEvent(ChatInputInteractionEvent event) {
        var username = event.getInteraction().getMember().orElseThrow().getUsername();

        try {
            CommandEventStrategy cmd = switch (event.getCommandName()) {
                case "throw" -> new ThrowDiceStategy(false);
                case "throwhidden" -> new ThrowDiceStategy(true);
                case "fiche" -> {
                    var user = username;
                    var option = event.getOption("joueur");
                    if (option.isPresent()) {
                        user = option.get().getValue().orElseThrow().asString();
                    }

                    var character = characters.get(username);
                    if (character != null) {
                        yield ev -> ev.reply(character.toString()).withEphemeral(true);
                    }
                    String content = "Il n'y as pas de personnage associé à " + user;
                    yield ev -> ev.reply(content).withEphemeral(true);
                }
                case "personnage-creation" -> {
                    var strategy = new CharacterCreationStrategy();
                    strategy.createCharacter(event).ifPresent(c -> characters.put(username, c));
                    yield strategy;
                }

                default -> ev -> ev.reply("Cette commande n'existe pas").withEphemeral(true);
            };

            return cmd.apply(event);
        } catch (NoSuchElementException e) {
            return CommandEventStrategy.defaultReponse.apply(event);
        }

    }
}
