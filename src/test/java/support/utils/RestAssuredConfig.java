package support.utils;

import io.restassured.RestAssured;

public class RestAssuredConfig {
    public static void setup(String environment, String baseUrlKey) {
        // Load the base URL dynamically based on the key
        String baseURI = BaseUrlSetup.setupBaseUrl(environment, baseUrlKey);
        if (baseURI != null) {
            RestAssured.baseURI = baseURI;
        } else {
            throw new RuntimeException("Base URI not found for key: " + baseUrlKey);
        }

        // Set default configurations
        RestAssured.config = RestAssured.config()
                .encoderConfig(RestAssured.config().getEncoderConfig().defaultContentCharset("UTF-8"))
                .decoderConfig(RestAssured.config().getDecoderConfig().defaultContentCharset("UTF-8"));
    }
}
