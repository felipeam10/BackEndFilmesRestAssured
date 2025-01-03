package tests;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.enums.Endpoint;
import support.utils.RestAssuredConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class StatusControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssuredConfig.setup("homolog.properties", "base.url");
    }

    @Test
    public void testStatusApi() {

        given()
            .contentType(ContentType.JSON)
        .when()
            .get(Endpoint.STATUS.getPath())
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.TEXT) // Validate the response content type
            .body(equalTo("A aplicação está de pé")); // Validate the response body
    }
}

