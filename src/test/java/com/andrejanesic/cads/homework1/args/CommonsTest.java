package com.andrejanesic.cads.homework1.args;

import io.cucumber.core.options.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/args/commons.feature")
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "com.andrejanesic.cads.homework1.args"
)
public class CommonsTest {
}
