package com.lllmtb.ui.console;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.lllmtb.ui.UI;
import org.jline.consoleui.elements.ConfirmChoice;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.PromptResultItemIF;
import org.jline.consoleui.prompt.builder.ListPromptBuilder;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;

import com.lllmtb.engine.GameSettings;
import com.lllmtb.engine.logger.Logger;
import com.lllmtb.events.EventBus;
import com.lllmtb.events.game.StartGameEvent;
import com.lllmtb.physics.GameMode;
import com.lllmtb.terrain.generation.LandScape;
import com.lllmtb.terrain.map.MapLoader;

// TODO - make prompt methods content-agnostic to remove duplicated code
//  eg: promptBool(String key, String message, ConfirmChoice.ConfirmationValue defaultValue)
//  and promptString(...)

// TODO - add better styling (colours...)

// TODO - as we add more game options, a multi-select menu would be better UX (cf. createCheckboxPrompt)

public class ConsoleUI implements UI {
    private Terminal terminal;
    private final EventBus eventBus;
    private GameWorldScreen gameScreen;
    private Thread gameScreenThread;


    public ConsoleUI(EventBus bus) {
        this.eventBus = bus;
        try {
            this.terminal = TerminalBuilder.builder().system(true).build();
            terminal.puts(InfoCmp.Capability.clear_screen);
            // styling does not seem to work - needs fix
            AttributedString title = new AttributedString("      Welcome to WORMS \uD83E\uDEB1      ", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN));
            terminal.writer().println(title);
            terminal.writer().println();
            terminal.writer().flush();
        } catch (IOException e) {
            Logger.logger.error("Error while initialising JLine Terminal");
        }
    }


    public GameSettings promptSettings() {

        try {
            terminal.writer().println("=== GAME SETTINGS ===");
            int nbTeams = promptNbTeams();

            String[] teamNames = promptTeamNames(nbTeams);

            int nbWorms = promptNbWorms();

            int maxHealth = promptMaxHealth();

            int turnDuration = promptTurnDuration();

            boolean loadMap = promptLoadMap();
            String mapFileName = null;
            int size = 0;
            LandScape landscape = LandScape.UNKNOWN;
            boolean floatingIsland = false;

            if (loadMap) {
                mapFileName = promptMapName();
            } else {
                size = promptMapSize();
                landscape = promptLandscape();
                floatingIsland = promptFloatingIsland();
            }

            boolean hasWind = promptHasWind();

            boolean friendlyfire = promptFriendlyFire();

            // Print a recap of the configured settings
            terminal.writer().println();
            terminal.writer().println();
            terminal.writer().println(" RECAP: ");
            terminal.writer().println("   " + nbTeams + " teams " + Arrays.toString(teamNames) + " of " + nbWorms + " worm(s) each");
            if (loadMap) {
                terminal.writer().println("   Load map from: " + mapFileName);
            } else {
                terminal.writer()
                        .println("   Generate a new " + size + "x map with " + landscape.name() + " landscape " + (floatingIsland ? "(floating island mode)" : "(normal mode)"));
            }
            terminal.writer().println("   Wind: " + (hasWind ? "ON" : "OFF"));
            terminal.writer().flush();
            return new GameSettings("blabla", nbTeams, teamNames, nbWorms, maxHealth, turnDuration, hasWind, mapFileName,
                    floatingIsland, size, landscape, 2, GameMode.NORMAL, friendlyfire);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int promptNbTeams() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("nbTeams")
                .message("Number of Teams")
                .newItem("2").text("2").add()
                .newItem("3").text("3").add()
                .newItem("4").text("4").add()
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return Integer.parseInt(result.get("nbTeams").getResult());
    }

    private String[] promptTeamNames(int nbTeams) throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        String[] teamNames = new String[nbTeams];
        for (int i = 0; i < nbTeams; i++) {
            builder.createInputPrompt()
                    .name("nameTeam" + i)
                    .message("Enter the name of Team n°" + (i + 1))
                    .addPrompt();
        }
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        for (int i = 0; i < nbTeams; i++) {
            teamNames[i] = result.get("nameTeam" + i).getResult();
        }
        return teamNames;
    }

    private int promptNbWorms() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("nbWorms")
                .message("Number of Worms per Team")
                .newItem("1").text("1").add()
                .newItem("2").text("2").add()
                .newItem("3").text("3").add()
                .newItem("4").text("4").add()
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return Integer.parseInt(result.get("nbWorms").getResult());
    }

    private int promptMaxHealth() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("maxHealth")
                .message("Max health of Worm")
                .newItem("50").text("50").add()
                .newItem("100").text("100").add()
                .newItem("200").text("200").add()
                .newItem("300").text("300").add()
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return Integer.parseInt(result.get("maxHealth").getResult());
    }

    private int promptTurnDuration() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("turnDuration")
                .message("  The duration in seconds of a single turn")
                .newItem("10").text("10").add()
                .newItem("20").text("20").add()
                .newItem("30").text("30").add()
                .newItem("40").text("40").add()
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return Integer.parseInt(result.get("turnDuration").getResult());
    }

    private boolean promptLoadMap() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createConfirmPromp()
                .name("loadMap")
                .message("Do you want to load an existing map from a save file?")
                .defaultValue(ConfirmChoice.ConfirmationValue.NO)
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return result.get("loadMap").getResult().equals(ConfirmChoice.ConfirmationValue.YES.toString());
    }

    private String promptMapName() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();

        File[] dossiers = MapLoader.getListeDossiersMaps();

        ListPromptBuilder b = builder.createListPrompt()
                .name("mapFileName")
                .message("  Select a map to load");

        for (File d : dossiers) {
            b.newItem(d.getName()).text(d.getName()).add();
        }
        b.addPrompt();

        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return result.get("mapFileName").getResult();
    }

    private int promptMapSize() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("size")
                .message("  The size (width) of the map")
                .newItem("100").text("100").add()
                .newItem("200").text("200").add()
                .newItem("300").text("300").add()
                .newItem("400").text("400").add()
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return Integer.parseInt(result.get("size").getResult());
    }

    private LandScape promptLandscape() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createListPrompt()
                .name("size")
                .message("  The type of landscape to use")
                .newItem("FLAT").text("Flat").add()
                .newItem("HILLY").text("Hilly").add()
                .newItem("JAGGED").text("Jagged").add()
                .newItem("CHAOTIC").text("Chaotic").add()
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return LandScape.valueOf(result.get("size").getResult());
    }

    private boolean promptFloatingIsland() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createConfirmPromp()
                .name("floatingIsland")
                .message("Do you want the map to be a floating island?")
                .defaultValue(ConfirmChoice.ConfirmationValue.NO)
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return result.get("floatingIsland").getResult().equals(ConfirmChoice.ConfirmationValue.YES.toString());
    }

    private boolean promptHasWind() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createConfirmPromp()
                .name("hasWind")
                .message("Do you want to turn on the wind?")
                .defaultValue(ConfirmChoice.ConfirmationValue.NO)
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return result.get("hasWind").getResult().equals(ConfirmChoice.ConfirmationValue.YES.toString());
    }

    private boolean promptFriendlyFire() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();
        builder.createConfirmPromp()
                .name("friendlyFire")
                .message("Do you want to turn the friendly fire?")
                .defaultValue(ConfirmChoice.ConfirmationValue.YES)
                .addPrompt();
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return result.get("friendlyFire").getResult().equals(ConfirmChoice.ConfirmationValue.YES.toString());
    }

    public boolean readyToPlay() {

        LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
        return reader.readLine("Press Enter to start the game...") != null;
    }


    @Override
    public void start() {
        try {
            GameSettings settings = this.promptSettings();
            boolean ready = this.readyToPlay();

            if (ready) {
                terminal.writer().println("Starting the game...");
                terminal.writer().flush();

                gameScreen = new GameWorldScreen(terminal, eventBus);
                gameScreenThread = new Thread(gameScreen, "console-game-screen");
                gameScreenThread.start();

                // Create the world
                this.eventBus.postQueued(new StartGameEvent(settings));

                // Wait for the game screen to finish
                try {
                    gameScreenThread.join();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (Exception e) {
            Logger.logger.error("An error occurred during configuration :");
            Logger.logger.error("", e);
            close();
        }
    }

    public void close() {
        try {
            if (gameScreen != null) gameScreen.stop();
            if (gameScreenThread != null) gameScreenThread.interrupt();
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}