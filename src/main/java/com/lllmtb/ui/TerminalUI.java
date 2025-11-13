package com.lllmtb.ui;
import com.lllmtb.engine.GameSettings;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.Reader;

/**
 * Terminal-based user interface for the WORMS game.
 * <p>
 * This class provides interactive prompts for the user to configure the game settings
 * using a JLine terminal. It supports colored output and interactive menus (via {@link MCQMenu}).
 * The class manages a {@link Terminal} and a {@link LineReader} for reading user input.
 * </p>
 * <p>
 * Example usage:
 * <pre>{@code
 * TerminalUI ui = new TerminalUI();
 * GameSettings settings = ui.promptSettings();
 * }</pre>
 * </p>
 */

public class TerminalUI extends UI {
     /** The terminal used for displaying output and reading input. */
    Terminal terminal;

     /** The line reader used for interactive input from the user. */
    LineReader reader;

    /**
     * Constructs a new {@code TerminalUI}, initializing the terminal and line reader.
     * <p>
     * The constructor initializes a JLine terminal with UTF-8 encoding, ANSI/JNA support,
     * clears the screen, and prints a welcome message.
     * </p>
     *
     * @throws RuntimeException if the terminal cannot be initialized
     */
    public TerminalUI(){
        super();
        try(Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).encoding("UTF-8").build()) {
            this.terminal = terminal;
            this.reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.writer().println("=== Welcome to WORMS \uD83E\uDEB1===");
            terminal.writer().println("Terminal initialized");
            terminal.writer().println();
            terminal.writer().flush();

        } catch (IOException e) {
            throw new RuntimeException("Erreur d'initialisation du terminal JLine", e);
        }
    }


    /**
     * Prompts the user for the game settings.
     * <p>
     * This method will ask the user interactively:
     * <ul>
     *     <li>The number of teams</li>
     *     <li>The name of each team</li>
     *     <li>The number of worms per team</li>
     *     <li>The path to the map file or to generate a new map</li>
     * </ul>
     *
     * @return a {@link GameSettings} object containing the user's selections
     * @throws RuntimeException if reading input fails
     */
    @Override
    GameSettings promptSettings() {
        String[] option = {"2","2"};
        int index = MCQMenu.askInteractiveMCQ(terminal, "Number of Teams ", option);
        int nbTeams = Integer.parseInt(option[index]);

        String[] teamNames =  new String[nbTeams];
        for (int i = 0; i < nbTeams; i++) {
            teamNames[i] = reader.readLine("Team Name n°" + (i + 1) + " ");
        }
        option = new String[]{"2", "3", "4", "5", "6"};
        index = MCQMenu.askInteractiveMCQ(terminal, "Number of Worms ", option);
        int nbWorms = Integer.parseInt(option[index]);
        boolean generateMap = false;
        String mapFilePath = reader.readLine("The path to the map file ");
        boolean generateIslands = false;
        boolean isWind = false;
        if (!generateMap){mapFilePath = null;}
        return new GameSettings(nbTeams,teamNames, nbWorms, isWind,mapFilePath, generateIslands);
    }

    /**
     * Checks if the user is ready to start the game.
     *
     * @return {@code true} by default; override to provide actual logic
     */
    @Override
    boolean readyToPlay() {
        return true;
    }


}
