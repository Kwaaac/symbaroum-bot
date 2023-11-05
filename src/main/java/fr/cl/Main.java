package fr.cl;

import discord4j.core.DiscordClient;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.interaction.ChatInputInteractionEvent;
import discord4j.core.event.domain.lifecycle.ReadyEvent;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        var token = args[0];
        var client = DiscordClient.create(token);

        try {
            new GlobalCommandRegistrar(client).registerCommands();
        } catch (Exception e) {
            //Handle exception
        }

        var handler = new ChatInputHandler();
        Mono<Void> login = client.withGateway((GatewayDiscordClient gateway) -> {
            Mono<Void> printOnLogin = gateway.on(ReadyEvent.class, event -> {

                var user = event.getSelf();

                System.out.println(user);

                return Mono.empty();
            }).then();

            Mono<Void> pong = gateway.on(ChatInputInteractionEvent.class, handler::handleEvent).then();

            return printOnLogin.and(pong);
        });

        login.block();

    }

    private static void extracted() {
        var cmd = "d6";
        var cmd2 = "1d6";
        var cmd3 = "2d6";
        var cmd4 = "d6+1";
        var cmd5 = "2d6+2";
        var cmd6 = "";
        var cmd7 = "1f34";


        var reg = "^(\\d*)?[d|D](4|6|12|20|100)\\+?(\\d*)?";

        var lst = new ArrayList<String>();
        lst.add(cmd);
        lst.add(cmd2);
        lst.add(cmd3);
        lst.add(cmd4);
        lst.add(cmd5);
        lst.add(cmd6);
        lst.add(cmd7);

        var pattern = Pattern.compile(reg);


    }
}