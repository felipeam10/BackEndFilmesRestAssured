package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

public class WiremockControllerTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8091"; // Update to match your service's base URI
        RestAssured.config = RestAssured.config()
                .encoderConfig(RestAssured.config().getEncoderConfig().defaultContentCharset("UTF-8"))
                .decoderConfig(RestAssured.config().getDecoderConfig().defaultContentCharset("UTF-8"));
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
