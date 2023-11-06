package fr.cl.commands.strategy;

import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.object.entity.channel.GuildMessageChannel;
import fr.cl.characters.Character;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

public class FicheStrategy implements CommandEventStrategy {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private final Character character;
    private final String username;

    public FicheStrategy(Character character, String username) {
        this.character = Objects.requireNonNull(character);
        this.username = Objects.requireNonNull(username);
    }

    @Override
    public Mono<Void> apply(ChatInputInteractionEvent event) throws NoSuchElementException {

        var channelId = event.getInteraction().getChannelId();
        var image = event.getInteraction().getMember().orElseThrow().getAvatarUrl();

        event.getClient()
                .getChannelById(channelId)
                .ofType(GuildMessageChannel.class)
                .flatMap(channel -> channel.createMessage(character.embed(image)))
                .doOnNext(signal -> LOGGER.info("piou: {}", signal))
                .doOnError(signal -> LOGGER.error(signal.getMessage()))
                .subscribe();

        System.out.println("\"coucou\" = " + "coucou");
        return Mono.empty();
    }
}
