package com.lllmtb.ui;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public class MCQMenu {

    public static int askInteractiveMCQ(Terminal terminal, String question, String[] options) {
        BindingReader bindingReader = new BindingReader(terminal.reader());
        KeyMap<String> keys = new KeyMap<>();

        keys.bind("up", "\033[A");     // flèche haut
        keys.bind("down", "\033[B");   // flèche bas
        keys.bind("right", "\033[C");
        keys.bind("enter", "\n");// entrée

        int selected = 0;
        String key;

        while (true) {
            terminal.puts(InfoCmp.Capability.clear_screen);
            terminal.writer().println(question);
            terminal.writer().println();

            for (int i = 0; i < options.length; i++) {
                if (i == selected)
                    terminal.writer().printf("> \u001B[33m%s\u001B[0m%n", options[i]); // jaune sélectionné
                else
                    terminal.writer().printf("  %s%n", options[i]);
            }
            terminal.writer().println("boucle1");
            terminal.flush();
            key = bindingReader.readBinding(keys);
            terminal.writer().println("boucle2");
            if ("up".equals(key)) {
                selected = (selected - 1 + options.length) % options.length;
            } else if ("down".equals(key)) {
                selected = (selected + 1) % options.length;
            } else if ("right".equals(key)) {
                if ("enter".equals(bindingReader.readBinding(keys))) {
                    return selected;
                }
            }
        }
    }
}
