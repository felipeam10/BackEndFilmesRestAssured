package tests;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.enums.Endpoint;
import support.utils.RestAssuredConfig;
import support.utils.RestAssuredHelper;

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

        Response response = RestAssuredHelper.sendGetRequest(Endpoint.STATUS.getPath());
        RestAssuredHelper.assertResponse(response, 200, "text/plain");
        assertEquals("A aplicação está de pé", response.getBody().asString(), "Response body mismatch");
    }
}

