package com.lllmtb.ui;

import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;


/**
 * Utility class for displaying interactive multiple-choice questions (MCQs) in the terminal.
 * <p>
 * This class uses JLine to create a navigable menu where the user can select options using
 * arrow keys or Enter. The currently selected option is highlighted.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>{@code
 * String[] options = {"Option 1", "Option 2", "Option 3"};
 * int selected = MCQMenu.askInteractiveMCQ(terminal, "Choose an option:", options);
 * }</pre>
 * </p>
 */
public class MCQMenu {

    /**
     * Displays an interactive multiple-choice question in the given terminal.
     * <p>
     * The user can navigate the options using the up and down arrow keys.
     * The selection is confirmed by pressing the right arrow key or Enter.
     * The selected option index is then returned.
     * </p>
     *
     * @param terminal the JLine {@link Terminal} to use for displaying the menu and reading input
     * @param question the question to display above the options
     * @param options  an array of option strings to display
     * @return the index of the selected option in the {@code options} array
     */
    public static int askInteractiveMCQ(Terminal terminal, String question, String[] options, BindingReader bindingReader ) {
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
            terminal.flush();
            key = bindingReader.readBinding(keys);
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
