package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import support.utils.RestAssuredConfig;

import static org.hamcrest.Matchers.equalTo;

public class WiremockControllerTest {

    @BeforeAll
    public static void setup() {
        // Use RestAssuredConfig to dynamically set the base URI from homolog.properties
        RestAssuredConfig.setup("homolog.properties", "wiremock.base.url");
    }

    @Test
    public void testConsultarIdFilmeExterno() {
        int codigo = 1;

        RestAssured.given()
            .pathParam("codigo", codigo)
        .when()
            .get("/idfilmeExterno/{codigo}")
        .then()
            .statusCode(200)
            .body(equalTo("{\"id\":\"12345\"}")); // Match the plain text response
    }

    @Test
    public void testValidarHeaderWithValidHeader() {
        String validHeader = "12345678";

        RestAssured.given()
            .header("optional-header", validHeader)
        .when()
            .get("/validarHeader")
        .then()
                .statusCode(200)
                .body(equalTo("{\"mensagem\":\"Header válido\"}"));// Replace with the actual expected response body
    }

    @Test
    public void testValidarHeaderWithInvalidHeader() {
        String invalidHeader = "invalidHeader";

        RestAssured.given()
                .header("optional-header", invalidHeader)
                .when()
                .get("/validarHeader")
                .then()
                .statusCode(404);
    }
}
