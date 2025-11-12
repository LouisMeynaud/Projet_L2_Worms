package com.lllmtb.ui;
import com.lllmtb.engine.GameSettings;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.Reader;

public class TerminalUI extends UI {
    Terminal terminal;
    LineReader reader;
    public TerminalUI(){
        super();
        try {
            // Crée un terminal JLine avec support des couleurs et de la saisie
            this.terminal = TerminalBuilder.builder()
                    .system(true)
                    .jna(true)
                    .jansi(true)
                    .encoding("UTF-8")
                    .build();

            // Initialise un LineReader pour les entrées utilisateur interactives
            this.reader = LineReaderBuilder.builder()
                    .terminal(terminal)
                    .appName("WORMS Game")
                    .build();

            // Efface l’écran au démarrage
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.writer().println("=== Welcome to WORMS \uD83E\uDEB1===");
            terminal.writer().println("Terminal initialized");
            terminal.writer().println();
            terminal.writer().flush();

        } catch (IOException e) {
            throw new RuntimeException("Erreur d'initialisation du terminal JLine", e);
        }
    }

    @Override
    GameSettings promptSettings() {
        String[] option = {"2"};
        int index = MCQMenu.askInteractiveMCQ(terminal, "Number of Teams", option);
        int nbTeams = Integer.parseInt(option[index]);

        String[] teamNames =  new String[nbTeams];
        for (int i = 0; i < nbTeams; i++) {
            teamNames[i] = reader.readLine("Team Name n°" + (i + 1));
        }
        option = new String[]{"2", "3", "4", "5", "6"};
        index = MCQMenu.askInteractiveMCQ(terminal, "Number of Worms", option);
        int nbWorms = Integer.parseInt(option[index]);
        boolean generateMap = false;
        String mapFilePath = reader.readLine("The path to the map file");
        boolean generateIslands = false;
        boolean isWind = false;
        if (!generateMap){mapFilePath = null;}
        terminal.writer().println(nbWorms);
        terminal.writer().println(mapFilePath);
        terminal.writer().println(isWind);
        terminal.writer().println(nbTeams);
        for (int i = 0; i < nbTeams; i++) {
            terminal.writer().println(teamNames[i]);
        }

        return new GameSettings(nbTeams,teamNames, nbWorms, isWind,mapFilePath, generateIslands);

    }

    @Override
    boolean readyToPlay() {
        return false;
    }


}
