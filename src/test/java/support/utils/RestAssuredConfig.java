package support.utils;

import io.restassured.RestAssured;

public class RestAssuredConfig {
    public static void setup() {
        BaseUrlSetup.setupBaseUrl("homolog.properties");
    }
}
