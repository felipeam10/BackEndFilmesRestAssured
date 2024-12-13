package config;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class TestConfig {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8082"; // Replace with the actual base URL of the external project
    }

    public static void setBasePath(String basePath) {
        RestAssured.basePath = basePath;
    }

    public static void resetBasePath() {
        RestAssured.basePath = "";
    }
}
