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
    // public static void main(String[] args) throws IOException, InterruptedException {
    //     TerminalUI ui = new TerminalUI();
    //     Thread.sleep(5000);
    //     ui.promptSettings(); 
    // }

    // @Test
    // public void testConstructor() {
    //     TerminalUI ui = new TerminalUI();
    //     assertNull(ui.world);
    //     assertInstanceOf(Terminal.class, ui.terminal);
    //     assertInstanceOf(LineReader.class, ui.reader);
    // }
    // @Test
    // public void testConstructor2() throws IOException {
    //     Terminal terminal = TerminalBuilder.builder().system(true).jna(true).jansi(true).encoding("UTF-8").build();
    //     LineReader reader = LineReaderBuilder.builder().terminal(terminal).appName("WORMS Game").build();
    //     TerminalUI ui = new TerminalUI(terminal, reader);
    //     assertNull(ui.world);

    // }
    // @Test
    // public void testConstructor3Catch1() throws IOException {
    //     TerminalBuilder terminalBuilder = mock(TerminalBuilder.class);
    //     when(terminalBuilder.system(true)).thenReturn(terminalBuilder);
    //     when(terminalBuilder.jna(true)).thenReturn(terminalBuilder);
    //     when(terminalBuilder.jansi(true)).thenReturn(terminalBuilder);
    //     when(terminalBuilder.encoding("UTF-8")).thenReturn(terminalBuilder);
    //     when(terminalBuilder.build()).thenThrow(new IOException("This Test is gooooooooooooood"));
    //     try (MockedStatic<TerminalBuilder> mocked = mockStatic(TerminalBuilder.class)) {
    //         mocked.when(TerminalBuilder::builder).thenReturn(terminalBuilder);
    //         assertThrows(RuntimeException.class, () -> new TerminalUI());
    //     }
    // }

    // @Test
    // public void testPromptSettings() {
    //     Terminal terminal = mock(Terminal.class);
    //     LineReader reader = mock(LineReader.class);
    //     TerminalUI ui = new TerminalUI(terminal, reader);
    //     String[] option = {"2","2"};
    //     try(MockedStatic<MCQMenu> menu = Mockito.mockStatic(MCQMenu.class)) {
    //         menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Teams ",option,new BindingReader(terminal.reader()) )).thenReturn(0);
    //         menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Worms ",option ,new BindingReader(terminal.reader()))).thenReturn(1);
    //         when(ui.reader.readLine("Team Name n°1 ")).thenReturn("Thomas");
    //         when(ui.reader.readLine("Team Name n°2 ")).thenReturn("Louis");
    //         when(ui.reader.readLine("Do you want to load an existing map from a file? [y/N] ")).thenReturn("N");
    //         when(ui.reader.readLine("The path to the map file ")).thenReturn("File");
    //         when(ui.reader.readLine("Do you want the map to be a floating island? [y/N] ")).thenReturn("y");
    //         when(ui.reader.readLine("Do you want to enable the wind? [y/N] ")).thenReturn("y");
    //         when(ui.reader.readLine("Press Enter to start the game...")).thenReturn("x");
    //         GameSettings settings = ui.promptSettings();
    //         assertEquals(2, settings.nbTeams);
    //         assertEquals(2, settings.nbWorms);
    //         assertTrue(settings.floatingIsland);
    //         assertTrue(settings.hasWind);
    //         assertNull(settings.mapFileName);
    //         assertEquals("Thomas", settings.teamNames[0]);
    //         assertEquals("Louis", settings.teamNames[1]);
    //         assertTrue(ui.readyToPlay());
    //     }
    // }

    // @Test
    // public void testPromptSettings2() {
    //     Terminal terminal = mock(Terminal.class);
    //     LineReader reader = mock(LineReader.class);
    //     TerminalUI ui = new TerminalUI(terminal, reader);
    //     String[] option = {"2","2"};
    //     try(MockedStatic<MCQMenu> menu = Mockito.mockStatic(MCQMenu.class)) {
    //         menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Teams ",option,new BindingReader(terminal.reader()) )).thenReturn(0);
    //         menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Worms ",option ,new BindingReader(terminal.reader()))).thenReturn(1);
    //         when(ui.reader.readLine("Team Name n°1 ")).thenReturn("Thomas");
    //         when(ui.reader.readLine("Team Name n°2 ")).thenReturn("Louis");
    //         when(ui.reader.readLine("Do you want to load an existing map from a file? [y/N] ")).thenReturn("N");
    //         when(ui.reader.readLine("Do you want the map to be a floating island? [y/N] ")).thenReturn("y");
    //         when(ui.reader.readLine("Do you want to enable the wind? [y/N] ")).thenReturn("y");
    //         when(ui.reader.readLine("Press Enter to start the game...")).thenReturn(null);
    //         GameSettings settings = ui.promptSettings();
    //         assertEquals(2, settings.nbTeams);
    //         assertEquals(2, settings.nbWorms);
    //         assertTrue(settings.floatingIsland);
    //         assertTrue(settings.hasWind);
    //         assertEquals("Thomas", settings.teamNames[0]);
    //         assertEquals("Louis", settings.teamNames[1]);
    //         assertFalse(ui.readyToPlay());
    //     }
    // }

    // @Test
    // public void testPromptSettings_loadMap() {
    //     Terminal terminal = mock(Terminal.class);
    //     LineReader reader = mock(LineReader.class);
    //     TerminalUI ui = new TerminalUI(terminal, reader);
    //     String[] option = {"2","2"};
    //     try(MockedStatic<MCQMenu> menu = Mockito.mockStatic(MCQMenu.class)) {
    //         menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Teams ",option,new BindingReader(terminal.reader()) )).thenReturn(0);
    //         menu.when(() ->MCQMenu.askInteractiveMCQ(ui.terminal,"Number of Worms ",option ,new BindingReader(terminal.reader()))).thenReturn(1);
    //         when(ui.reader.readLine("Team Name n°1 ")).thenReturn("Thomas");
    //         when(ui.reader.readLine("Team Name n°2 ")).thenReturn("Louis");
    //         when(ui.reader.readLine("Do you want to load an existing map from a file? [y/N] ")).thenReturn("y");
    //         when(ui.reader.readLine("The path to the map file ")).thenReturn("mapTest");
    //         when(ui.reader.readLine("Do you want to enable the wind? [y/N] ")).thenReturn("zsdefrgh");
    //         when(ui.reader.readLine("Press Enter to start the game...")).thenReturn("");
    //         GameSettings settings = ui.promptSettings();
    //         assertEquals(2, settings.nbTeams);
    //         assertEquals(2, settings.nbWorms);
    //         assertEquals("mapTest", settings.mapFileName);
    //         assertFalse(settings.hasWind);
    //         assertEquals("Thomas", settings.teamNames[0]);
    //         assertEquals("Louis", settings.teamNames[1]);
    //         assertTrue(ui.readyToPlay());
    //     }
    // }

    // @Test
    // public void testCloseUI() throws IOException {
    //     Terminal terminal = mock(Terminal.class);
    //     LineReader reader = mock(LineReader.class);
    //     TerminalUI ui = new TerminalUI(terminal, reader);
    //     assertDoesNotThrow(ui::close);
    //     verify(terminal, times(1)).close();
    // }
}
