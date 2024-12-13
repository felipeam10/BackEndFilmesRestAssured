package tests;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FilmeApiTest extends TestConfig {

    @Test
    public void testConsultarFilmePorId_Success() {
        int codigoFilme = 1; // Replace with a valid movie ID

        given()
            .pathParam("codigo", codigoFilme)
        .when()
            .get("/{codigo}")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("codigo", equalTo(codigoFilme))
            .body("nome", notNullValue())
            .body("sinopse", notNullValue())
            .body("faixaEtaria", notNullValue())
            .body("genero", notNullValue());
    }

    @Test
    public void testConsultarFilmePorId_NotFound() {
        int codigoFilmeInvalido = 9999; // Replace with an invalid movie ID

        given()
            .pathParam("codigo", codigoFilmeInvalido)
        .when()
            .get("/{codigo}")
        .then()
            .statusCode(404);
    }


}
