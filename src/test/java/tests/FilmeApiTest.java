package tests;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class FilmeApiTest extends TestConfig {

    @AfterEach
    public void tearDown() {
        // Reset the basePath after each test to avoid conflicts
        resetBasePath();
    }

    @Test
    public void testConsultarFilmePorId_Success() {
        setBasePath("/filme");

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
        setBasePath("/filme");

        int codigoFilmeInvalido = 9999; // Replace with an invalid movie ID

        given()
            .pathParam("codigo", codigoFilmeInvalido)
        .when()
            .get("/{codigo}")
        .then()
            .statusCode(404);
    }

    @Test
    public void testConsultarTodosOsFilmes_Success() {
        setBasePath("/filmes");

        given()
        .when()
            .get()
        .then()
            .statusCode(200) // Expect HTTP 200 OK
            .contentType(ContentType.JSON) // Expect JSON response
            .body("$", not(empty())) // Ensure the response is not empty
            .body("size()", greaterThan(0)) // Ensure at least one movie is returned
            .body("[0].codigo", notNullValue()) // Validate the first movie has an ID
            .body("[0].nome", notNullValue()) // Validate the first movie has a name
            .body("[0].sinopse", notNullValue()) // Validate the first movie has a description
            .body("[0].faixaEtaria", notNullValue()) // Validate the first movie has a description
            .body("[0].genero", notNullValue()) // Validate the first movie has a description
        ;
    }

    @Test
    public void testConsultarTodosOsFilmes_InvalidEndpoint() {
        setBasePath("/filmes-invalid");

        // Test for an invalid endpoint
        given()
        .when()
            .get() // Invalid endpoint
        .then()
            .statusCode(404); // Expect HTTP 404 Not Found
    }
}
