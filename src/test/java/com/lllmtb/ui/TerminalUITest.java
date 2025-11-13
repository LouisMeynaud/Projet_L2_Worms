package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TerminalUITest {
   /* public static void main(String[] args) throws IOException {
       Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).encoding("UTF-8").build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
        TerminalUI ui = new TerminalUI(terminal, reader);
        ui.promptSettings();
    }*/

    @Test
    public void testConstructor() {
        TerminalUI ui = new TerminalUI();
        assertEquals(ui.world,null);
        assertInstanceOf(Terminal.class, ui.terminal);
        assertInstanceOf(LineReader.class, ui.reader);
    }
    @Test
    public void testConstructor2() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).encoding("UTF-8").build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
        TerminalUI ui = new TerminalUI(terminal, reader);
        assertEquals(ui.world,null);

    }

    @Test
    public void testPromptSettings() {
        Terminal terminal = Mockito.mock(Terminal.class);
        LineReader reader = Mockito.mock(LineReader.class);
        TerminalUI ui = new TerminalUI(terminal, reader);
        String[] option = {"2","2"};
        try(MockedStatic<MCQMenu> menu = Mockito.mockStatic(MCQMenu.class)) {
            menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Teams ",option )).thenReturn(0);
            menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Worms ",option )).thenReturn(1);
            when(ui.reader.readLine("Team Name n°1 ")).thenReturn("Thomas");
            when(ui.reader.readLine("Team Name n°2 ")).thenReturn("Louis");
            when(ui.reader.readLine("The path to the map file ")).thenReturn("File");
            GameSettings settings = ui.promptSettings();
            assertEquals(settings.nbTeams, 2);
            assertEquals(settings.nbWorms, 2);
            assertEquals(settings.generateIslands, false);
            assertEquals(settings.isWind, false);
            assertEquals(settings.mapFilePath, null);
            assertEquals(settings.teamNames[0], "Thomas");
            assertEquals(settings.teamNames[1], "Louis");
            assertTrue(ui.readyToPlay());
        }

    }
}
