package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.enums.Endpoint;
import support.utils.RestAssuredConfig;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class WiremockControllerTest {

    @BeforeAll
    public static void setup() {
        String environment = System.getProperty("env");
        if (environment == null || environment.isEmpty()) {
            throw new RuntimeException("Environment not specified. Please provide the 'env' system property (e.g., -Denv=dev or -Denv=homolog).");
        }
        RestAssuredConfig.setup(environment, "wiremock.base.url");
    }

    @Test
    public void testConsultarIdFilmeExterno() {

        Response response = given()
                .pathParam("codigo", 1)
                .when()
                .get(Endpoint.FILME_EXTERNO.getPath());

        assertEquals(200, response.getStatusCode(), "Expected HTTP 200 OK");
        assertEquals("{\"id\":\"12345\"}", response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testValidarHeaderWithValidHeader() {

        Response response =
       given()
            .header("optional-header", "12345678")
        .when()
            .get(Endpoint.VALIDAR_HEADER.getPath());

        assertEquals(200, response.getStatusCode(), "Expected HTTP 200 OK");
        assertEquals("{\"mensagem\":\"Header válido\"}", response.getBody().asString(), "Response body mismatch");
    }

    @Test
    public void testValidarHeaderWithInvalidHeader() {

        Response response =
        given()
            .header("optional-header", "invalidHeader")
        .when()
            .get(Endpoint.VALIDAR_HEADER.getPath());

        assertEquals(404, response.getStatusCode(), "Expected HTTP 404 Not Found");
    }
}
