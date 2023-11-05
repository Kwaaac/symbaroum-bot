package fr.cl;

import discord4j.common.JacksonResources;
import discord4j.discordjson.json.ApplicationCommandData;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.RestClient;
import discord4j.rest.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GlobalCommandRegistrar {
    private final RestClient restClient;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    public GlobalCommandRegistrar(RestClient restClient) {
        Objects.requireNonNull(restClient);
        this.restClient = restClient;
    }

    //Since this will only run once on startup, blocking is okay.
    protected void registerCommands() throws IOException {
        //Create an ObjectMapper that supports Discord4J classes
        final JacksonResources d4jMapper = JacksonResources.create();

        // Convenience variables for the sake of easier to read code below
        final ApplicationService applicationService = restClient.getApplicationService();
        final long applicationId = restClient.getApplicationId().block();
        //Get our commands json from resources as command data
        List<ApplicationCommandRequest> commands = new ArrayList<>();

        for (String json : getCommandsJson()) {
            System.out.println("json = " + json);
            ApplicationCommandRequest request = d4jMapper.getObjectMapper().readValue(json, ApplicationCommandRequest.class);
            System.out.println("request = " + request);
            commands.add(request); //Add to our array list
        }

        /* Bulk overwrite commands. This is now idempotent, so it is safe to use this even when only 1 command
        is changed/added/removed
        */
        var cl = 833806692604051456L;
        System.out.println("commands = " + commands);

        // Delete it
        //applicationService.deleteGuildApplicationCommand(applicationId, cl, commandId).subscribe();

        applicationService.bulkOverwriteGuildApplicationCommand(applicationId, cl, commands)
                .doOnEach(signal -> LOGGER.info("AHHHHHHHHHHH {}", signal))
                .subscribe();
    }

    /* The two below methods are boilerplate that can be completely removed when using Spring Boot */

    private static List<String> getCommandsJson() throws IOException {
        // Confirm that the commands folder exists
        var commands = Paths.get("src", "main", "resources", "commands");
        //Get all the files inside this folder and return the contents of the files as a list of strings
        try (Stream<Path> file = Files.list(commands)) {
            System.out.println("file = " + file);
            return file.map(path -> {
                try {
                    System.out.println("path = " + path.toAbsolutePath());
                    return Objects.requireNonNull(getResourceFileAsString(path));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }).toList();
        } catch (UncheckedIOException e) {
            throw new IOException(e);
        }
    }

    /**
     * Gets a specific resource file as String
     *
     * @param fileName The file path omitting "resources/"
     * @return The contents of the file as a String, otherwise throws an exception
     */
    private static String getResourceFileAsString(Path fileName) throws IOException {
        List<String> strings = Files.readAllLines(fileName);
        System.out.println("strings = " + strings);
        return strings.stream().collect(Collectors.joining(System.lineSeparator()));
        /*
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream resourceAsStream = classLoader.getResourceAsStream(fileName)) {
            if (resourceAsStream == null) return null;
            try (InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream);
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
        */
    }
}