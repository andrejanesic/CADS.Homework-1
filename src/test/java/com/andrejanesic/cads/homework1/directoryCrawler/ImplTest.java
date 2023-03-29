package com.andrejanesic.cads.homework1.directoryCrawler;

import io.cucumber.core.options.Constants;
import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features/directoryCrawler/impl.feature")
@ConfigurationParameter(
        key = Constants.GLUE_PROPERTY_NAME,
        value = "com.andrejanesic.cads.homework1.directoryCrawler"
)
public class ImplTest {
}
