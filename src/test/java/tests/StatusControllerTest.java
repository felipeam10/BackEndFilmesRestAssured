package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.enums.Endpoint;
import support.utils.RestAssuredConfig;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusControllerTest {

    @BeforeAll
    public static void setup() {
        String environment = System.getProperty("env");
        if (environment == null || environment.isEmpty()) {
            throw new RuntimeException("Environment not specified. Please provide the 'env' system property (e.g., -Denv=dev or -Denv=homolog).");
        }
        RestAssuredConfig.setup(environment, "base.url");
    }

    @Test
    public void testStatusApi() {

        Response response =
        given()
            .contentType(ContentType.JSON)
        .when()
            .get(Endpoint.STATUS.getPath());

        assertEquals(200, response.getStatusCode(), "Expected HTTP 200 OK");
        String actualContentType = response.getContentType();
        assertEquals("text/plain", actualContentType.split(";")[0], "Content type mismatch");
        assertEquals("A aplicação está de pé", response.getBody().asString(), "Response body mismatch");
    }
}

