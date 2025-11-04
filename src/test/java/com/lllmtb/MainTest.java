package com.lllmtb;

import org.junit.jupiter.api.Test;

public class MainTest {

    @Test
    void main_null() {
        Main.main(null);
    }

    @Test
    void main_emptyTab() {
        Main.main(new String[]{});
    }

}
