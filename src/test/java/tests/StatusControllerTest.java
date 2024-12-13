package tests;

import config.TestConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StatusControllerTest extends TestConfig {
    @AfterEach
    public void tearDown() {
        // Reset the basePath after each test to avoid conflicts
        resetBasePath();
    }

    @Test
    public void testStatusApi() {
        setBasePath("/status");

        given()
            .contentType(ContentType.JSON)
        .when()
            .get()
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.TEXT) // Validate the response content type
            .body(equalTo("A aplicação está de pé")); // Validate the response body
    }
}

