package com.andrejanesic.cads.homework1.args;

import com.andrejanesic.cads.homework1.args.commons.ArgsCommons;
import com.andrejanesic.cads.homework1.constants.IConstants;
import com.andrejanesic.cads.homework1.core.exceptions.ArgsException;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class CommonsSteps {

    String configPath = "string.path";
    String[] args;
    ArgsCommons argsCommons;

    @Before
    public void setUp() {
        argsCommons = new ArgsCommons();
    }

    @Given("no arguments passed")
    public void no_arguments_passed() {
        args = new String[]{};
    }

    @Given("invalid arguments passed")
    public void invalid_arguments_passed() {
        args = new String[]{
                "java",
                "SampleClass.class",
                "-c",
        };
    }

    @Given("valid arguments passed")
    public void valid_arguments_passed() {
        args = new String[]{
                "java",
                "SampleClass.class",
                "-c",
                configPath,
        };
    }

    @Then("throw args exception")
    public void throw_args_exception() {
        assertThrows(ArgsException.class, () -> argsCommons.parse(args));
    }

    @Then("default arguments match")
    public void default_arguments_match() {
        assertDoesNotThrow(() -> argsCommons.parse(args));
        assertEquals(IConstants.DEFAULT_FILEPATH_APP_PROPERTIES, argsCommons.configSource());
    }

    @Then("parsed arguments match")
    public void parsed_arguments_match() {
        assertDoesNotThrow(() -> argsCommons.parse(args));
        assertEquals(configPath, argsCommons.configSource());
    }

}
