package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;
import org.jline.consoleui.elements.ConfirmChoice;
import org.jline.consoleui.prompt.ConsolePrompt;
import org.jline.consoleui.prompt.PromptResultItemIF;
import org.jline.consoleui.prompt.builder.PromptBuilder;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

// TODO - make prompt methods content-agnostic to remove duplicated code
//  eg: promptBool(String key, String message, ConfirmChoice.ConfirmationValue defaultValue)
//  and promptString(...)

// TODO - add better styling (colours...)

// TODO - as we add more game options, a multi-select menu would be better UX (cf. createCheckboxPrompt)

// TODO - when prompting for a map file to load, display a list of available files in a select menu

public class ConsoleUI extends UI {
    private Terminal terminal;

    public ConsoleUI() {
        try {
            this.terminal = TerminalBuilder.builder().system(true).build();
            terminal.puts(InfoCmp.Capability.clear_screen);
            // styling does not seem to work - needs fix
            AttributedString title = new AttributedString("      Welcome to WORMS \uD83E\uDEB1      ", AttributedStyle.BOLD.foreground(AttributedStyle.GREEN));
            terminal.writer().println(title);
            terminal.writer().println();
            terminal.writer().flush();
        } catch (IOException e) {
            System.err.println("Error while initialising JLine Terminal");
        }
    }

    @Override
    public GameSettings promptSettings() {
        try {
            terminal.writer().println("=== GAME SETTINGS ===");
            int nbTeams = promptNbTeams();

            String[] teamNames = promptTeamNames(nbTeams);

            int nbWorms = promptNbWorms();

            boolean loadMap = promptLoadMap();
            String mapFileName = null;
            boolean floatingIsland = false;

            if (loadMap) mapFileName = promptMapFileName();
            else floatingIsland = promptFloatingIsland();

            boolean hasWind = promptHasWind();

            // Print a recap of the configured settings
            terminal.writer().println();
            terminal.writer().println();
            terminal.writer().println(" RECAP: ");
            terminal.writer().println("   " + nbTeams + " teams " + Arrays.toString(teamNames) + " of " + nbWorms + " worm(s) each");
            terminal.writer().println(loadMap
                    ? "   Load map from: " + mapFileName
                    : "   Generate a new map " + (floatingIsland ? "(floating island mode)" : "(normal mode)")
            );
            terminal.writer().println("   Wind: " + (hasWind ? "ON" : "OFF"));
            terminal.writer().flush();
            return new GameSettings(nbTeams, teamNames, nbWorms, false, null, false);
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
                    .message("Enter the name of Team n°" + (i+1))
                    .addPrompt();
        }
        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        for (int i = 0; i < nbTeams; i++) teamNames[i] = result.get("nameTeam" + i).getResult();
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

    private String promptMapFileName() throws IOException {
        ConsolePrompt prompt = new ConsolePrompt(terminal);
        PromptBuilder builder = prompt.getPromptBuilder();

        builder.createInputPrompt()
                .name("mapFileName")
                .message("  Enter the file name:")
                .addPrompt();

        Map<String, PromptResultItemIF> result = prompt.prompt(builder.build());
        return result.get("mapFileName").getResult();
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

    @Override
    public boolean readyToPlay() {
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
        return reader.readLine("Press Enter to start the game...") != null;
    }

    @Override
    public void close() {
        try {
            terminal.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

