package com.lllmtb.ui;

import com.lllmtb.engine.GameSettings;
import org.jline.keymap.BindingReader;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TerminalUITest {
    /*public static void main(String[] args) throws IOException {
       Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).encoding("UTF-8").build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
        TerminalUI ui = new TerminalUI(terminal, reader);
        ui.promptSettings();
    }*/

    @Test
    public void testConstructor() {
        TerminalUI ui = new TerminalUI();
        assertNull(ui.world);
        assertInstanceOf(Terminal.class, ui.terminal);
        assertInstanceOf(LineReader.class, ui.reader);
    }
    @Test
    public void testConstructor2() throws IOException {
        Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).encoding("UTF-8").build();
        LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
        TerminalUI ui = new TerminalUI(terminal, reader);
        assertNull(ui.world);

    }
    @Test
    public void testConstructor3Catch() throws IOException {
        TerminalBuilder terminalBuilder = mock(TerminalBuilder.class);
        when(terminalBuilder.system(true)).thenReturn(terminalBuilder);
        when(terminalBuilder.jna(true)).thenReturn(terminalBuilder);
        when(terminalBuilder.jansi(true)).thenReturn(terminalBuilder);
        when(terminalBuilder.encoding("UTF-8")).thenReturn(terminalBuilder);
        when(terminalBuilder.build()).thenThrow(new IOException("This Test is good"));
        try (MockedStatic<TerminalBuilder> mocked = mockStatic(TerminalBuilder.class)) {
            mocked.when(TerminalBuilder::builder).thenReturn(terminalBuilder);
            assertThrows(RuntimeException.class, () -> new TerminalUI());
        }
    }

    @Test
    public void testPromptSettings() {
        Terminal terminal = mock(Terminal.class);
        LineReader reader = mock(LineReader.class);
        TerminalUI ui = new TerminalUI(terminal, reader);
        String[] option = {"2","2"};
        try(MockedStatic<MCQMenu> menu = Mockito.mockStatic(MCQMenu.class)) {
            menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Teams ",option,new BindingReader(terminal.reader()) )).thenReturn(0);
            menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Worms ",option ,new BindingReader(terminal.reader()))).thenReturn(1);
            when(ui.reader.readLine("Team Name n°1 ")).thenReturn("Thomas");
            when(ui.reader.readLine("Team Name n°2 ")).thenReturn("Louis");
            when(ui.reader.readLine("The path to the map file ")).thenReturn("File");
            GameSettings settings = ui.promptSettings();
            assertEquals(2, settings.nbTeams);
            assertEquals(2, settings.nbWorms);
            assertFalse(settings.generateIslands);
            assertFalse(settings.isWind);
            assertNull(settings.mapFilePath);
            assertEquals("Thomas", settings.teamNames[0]);
            assertEquals("Louis", settings.teamNames[1]);
            assertTrue(ui.readyToPlay());
        }
    }
}
