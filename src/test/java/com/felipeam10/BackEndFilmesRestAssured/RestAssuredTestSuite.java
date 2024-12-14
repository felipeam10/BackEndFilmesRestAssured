package com.felipeam10.BackEndFilmesRestAssured;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import tests.FilmeControllerTest;
import tests.StatusControllerTest;
import tests.WiremockControllerTest;

@Suite
@SelectClasses({
        FilmeControllerTest.class,
        StatusControllerTest.class,
        WiremockControllerTest.class
})
public class RestAssuredTestSuite {
    // This class remains empty. It is used only as a holder for the above annotations.
}
