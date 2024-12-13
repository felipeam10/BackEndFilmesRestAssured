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

    @Test
    public void testCriarFilme_Success() {
        // Test for successfully creating a movie
        setBasePath("/salvar");

        String filmeJson = """
            {
                "codigo": 10,
                "faixaEtaria": "12",
                "genero": "Ação",
                "nome": "Filme de Ação",
                "sinopse": "Um filme emocionante de ação."
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post()
        .then()
            .statusCode(201) // Expect HTTP 201 Created
            .contentType(ContentType.JSON) // Expect JSON response
            .body("codigo", equalTo(10)) // Validate the returned movie's code
            .body("nome", equalTo("Filme de Ação")) // Validate the movie's name
            .body("genero", equalTo("Ação")) // Validate the movie's genre
            .body("faixaEtaria", equalTo("12")) // Validate the movie's age rating
            .body("sinopse", equalTo("Um filme emocionante de ação.")); // Validate the movie's synopsis
    }

    @Test
    public void testCriarFilme_Conflict() {
        // Test for conflict when the movie already exists
        setBasePath("/salvar");

        String filmeJson = """
            {
                "codigo": 10,
                "faixaEtaria": "12",
                "genero": "Ação",
                "nome": "Filme de Ação",
                "sinopse": "Um filme emocionante de ação."
            }
        """;

        // Assuming the movie with codigo 1 already exists
        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post()
        .then()
            .statusCode(409) // Expect HTTP 409 Conflict
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Filme já cadastrado!")); // Validate the conflict message
    }

    @Test
    public void testCriarFilme_MissingFaixaEtaria() {
        // Test for missing "faixaEtaria"
        setBasePath("/salvar");

        String filmeJson = """
            {
                "codigo": 12,
                "faixaEtaria": "",
                "genero": "Ação",
                "nome": "Filme de Ação",
                "sinopse": "Um filme emocionante de ação."
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post()
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Faixa etária é obrigatória!")); // Validate the error message
    }

    @Test
    public void testCriarFilme_MissingGenero() {
        // Test for missing "genero"
        setBasePath("/salvar");

        String filmeJson = """
            {
                "codigo": 13,
                "faixaEtaria": "1",
                "genero": "",
                "nome": "Filme de Ação",
                "sinopse": "Um filme emocionante de ação."
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post()
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Genêro é obrigatório!")); // Validate the error message
    }

    @Test
    public void testCriarFilme_MissingNome() {
        // Test for missing "nome"
        setBasePath("/salvar");

        String filmeJson = """
            {
                "codigo": 14,
                "faixaEtaria": "12",
                "genero": "Ação",
                "nome": "",
                "sinopse": "Um filme emocionante de ação."
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post()
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Nome é obrigatório!")); // Validate the error message
    }

    @Test
    public void testCriarFilme_MissingSinopse() {
        // Test for missing "sinopse"
        setBasePath("/salvar");

        String filmeJson = """
            {
                "codigo": 15,
                "faixaEtaria": "12",
                "genero": "Ação",
                "nome": "Filme de Ação",
                "sinopse": ""
            }
        """;

        given()
            .contentType(ContentType.JSON)
            .body(filmeJson)
        .when()
            .post()
        .then()
            .statusCode(400) // Expect HTTP 400 Bad Request
            .contentType(ContentType.JSON) // Expect JSON response
            .body("message", equalTo("Sinopse é obrigatório!")); // Validate the error message
    }
}
