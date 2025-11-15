package com.lllmtb.ui;

import org.jline.keymap.BindingReader;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.io.PrintWriter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MCQMenuTest {

    private int i = -1;

    public String mockInput(){
        i++;
        String[] solution = {"up", "down","up","down","right","up","down","enter","right","enter"};
        return solution[i];
    }

    @Test
    public void testAskInteractiveMCQ() {
        PrintWriter printWriter = Mockito.mock(PrintWriter.class);
        Terminal terminal = Mockito.mock(Terminal.class);
        String[] option = {"One", "Two"};
        BindingReader bindingReader = Mockito.mock(BindingReader.class);
        when(bindingReader.readBinding(any())).thenAnswer(i -> mockInput());
        when(terminal.writer()).thenReturn(printWriter);
        int index = MCQMenu.askInteractiveMCQ(terminal,"NoQuestion", option,bindingReader);
        Assertions.assertEquals(option[index],"Two");
    }
}
