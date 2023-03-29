package com.andrejanesic.cads.homework1.args.commons;

import com.andrejanesic.cads.homework1.Main;
import com.andrejanesic.cads.homework1.constants.IConstants;
import com.andrejanesic.cads.homework1.core.exceptions.ArgsException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsCommonsCLITest {

    @BeforeAll
    static void setUp() {
        Main.main(new String[]{});
    }

    @Test
    void givenCorrectArgs_whenConfigSourcePassed_thenParseArgs() {
        String configPath = "string.path";
        String[] args = new String[]{
                "java",
                "SampleClass.class",
                "-c",
                configPath,
        };

        ArgsCommonsCLI argsCommonsCLI = new ArgsCommonsCLI();
        assertDoesNotThrow(() -> argsCommonsCLI.parse(args));
        assertEquals(configPath, argsCommonsCLI.configSource());
    }

    @Test
    void givenCorrectArgs_whenConfigSourceNotPassed_thenParseArgs() {
        String[] args = new String[]{
                "java",
                "SampleClass.class",
        };

        ArgsCommonsCLI argsCommonsCLI = new ArgsCommonsCLI();
        assertDoesNotThrow(() -> argsCommonsCLI.parse(args));
        assertEquals(IConstants.DEFAULT_FILEPATH_APP_PROPERTIES, argsCommonsCLI.configSource());
    }

    @Test
    void givenBadArgs_thenThrowException() {
        String[] args = new String[]{
                "java",
                "SampleClass.class",
                "-c",
        };

        ArgsCommonsCLI argsCommonsCLI = new ArgsCommonsCLI();
        assertThrows(ArgsException.class, () -> argsCommonsCLI.parse(args));
    }
}
